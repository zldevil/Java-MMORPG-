package com.example.demoserver.game.user.service;


import com.example.demoserver.game.player.dao.UserEntityMapper;
import com.example.demoserver.game.player.model.UserEntity;
import com.example.demoserver.game.user.dao.UserMapper;
import com.example.demoserver.game.user.manager.PlayerListCacheMgr;
import com.example.demoserver.game.user.manager.UserCacheManger;
import com.example.demoserver.game.user.model.User;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@Service
public class UserService {


    @Autowired
    private UserMapper userMapper;


    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private PlayerListCacheMgr playerListCacheMgr;

    @Autowired
    private Notify notify;



    /**
     * 判断用户id和密码是否正确
     */

    public void IsCorrectIdAndPassword(int userId, String password, ChannelHandlerContext ctx) {

        User userCache = UserCacheManger.getUserByUserId(userId);

        User user = Optional.ofNullable(userCache)
                .orElseGet(
                        () -> {
                            // 缓存中不存在用户,或者当前连接在线的不是现在要登陆的账号，如果缓存中不存在用户返回null
                            //System.out.println("Sql开始执行");
                            User tmpUser = userMapper.selectByPrimaryKey(userId);
                            System.out.println("sql执行结束");

                            return tmpUser;
                        }
                );
        if(Objects.isNull(user)) {
            Notify.notifyByCtx(ctx,"玩家id不存在");
            return ;
        }
        // 用户名或密码错误，或没注册
        if ((!(user.getId()==userId))||(!user.getPassword().equals(password))) {
            Notify.notifyByCtx(ctx,"密码或用户名错误");
            return ;
        }


        //  替换channelId和用户缓存的联系,
        UserCacheManger.putCtxUser(ctx,user);
        // 替换channel上下文和用户缓存的联系
        UserCacheManger.putUserIdCtx(user.getId(),ctx);

        Notify.notifyByCtx(ctx, "登录成功"
                );
    }

     /**
     *  根据用户id产找游戏角色列表
     */
    public List<UserEntity> findPlayers(int userId) {
        return Optional.ofNullable(playerListCacheMgr.get(userId))
                .orElseGet(() -> {

                    List<UserEntity> entityList =userEntityMapper.selectUserEntityByUserID(userId);

                    // 将角色列表加入缓存
                    playerListCacheMgr.put(userId, entityList);
                    return entityList;
                });
    }


    /**
     *      用户注销
     * @param userId    用户id
     */
    public void logoutUser(long userId) {

        ChannelHandlerContext ctx = UserCacheManger.getCtxByUserId(userId);
        if (ctx != null) {
            //用户注销就是缓存中删除user
            UserCacheManger.removeUserByChannelId(ctx.channel().id().asLongText());
            // 关闭连接
            ctx.close();
        }

    }

    public boolean isUserOnline(ChannelHandlerContext ctx) {
        return UserCacheManger.getUserByCtx(ctx) != null;
    }


    public void register(ChannelHandlerContext ctx,Integer id, String password) {
        User user = new User();
        user.setId(id);
        user.setPassword(password);

        try {
            //将这个新建的对象插入到数据库中
            userMapper.insertUser(user);
            //tUserMapper.insertSelective(tUser);

        } catch(DuplicateKeyException e) {
            Notify.notifyByCtx(ctx,"用户名已经存在");
            return;
        }


        /**TUserExample tUserExample = new TUserExample();

         tUserExample.or().andNameEqualTo(userName);

         List<User> newUserList = tUserMapper.selectByExample(tUserExample);
         */

        User newUser =userMapper.selectByPrimaryKey(id);
        if(newUser!=null){
            Notify.notifyByCtx(ctx,"账号注册成功");
        }else {
            Notify.notifyByCtx(ctx,"没有注册成功");
        }

    }

    /**
     *  通过连接上下文找到用户
     */

    public User getUserByCxt(ChannelHandlerContext ctx) {
        return UserCacheManger.getUserByCtx(ctx);
    }
}