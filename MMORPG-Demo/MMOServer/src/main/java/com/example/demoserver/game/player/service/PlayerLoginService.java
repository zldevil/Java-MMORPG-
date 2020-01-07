package com.example.demoserver.game.player.service;


import com.example.demoserver.game.player.dao.UserEntityMapper;
import com.example.demoserver.game.player.manager.PlayerCacheMgr;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.model.UserEntity;
import com.example.demoserver.game.user.manager.UserCacheManger;
import com.example.demoserver.game.user.model.User;
import com.example.demoserver.game.user.service.UserService;
import com.example.demoserver.server.net.utils.IDUtil;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Description: 角色登陆服务
 */
@Service
@Slf4j
public class PlayerLoginService {


    @Autowired
    private PlayerCacheMgr playerCacheMgr;

    @Autowired
    private PlayerDataService playerDataService;

    @Autowired
    private UserService userService;

    @Autowired
    private PlayerQuitService playerQuitService;

    @Autowired
    private UserEntityMapper userEntityMapper;

    /**
     *  角色登陆
     */
    public Player login(int playerId, ChannelHandlerContext ctx) {

        Player playerCache  = playerCacheMgr.getPlayerByCtx(ctx);

        // 清理当前通道的角色
        playerQuitService.logoutScene(ctx);

        // 如果角色缓存为空 或者 缓存中的角色不是要加载的角色，那就从数据库查询
        if (playerCache == null || !(playerCache.getId()==playerId)) {
            UserEntity userEntity=  userEntityMapper.selectUserEntityById(playerId);
            Player player = new Player();
            BeanUtils.copyProperties(userEntity,player);

            // 玩家初始化
            playerDataService.initPlayer(player);

            //调用缓存，在登录时将上下文ctx与player加载到缓存中
            playerCacheMgr.putCtxPlayer(ctx,player);

            // 保存playerId跟ChannelHandlerContext之间的关系
            //明明是palyerId与ctx之间的映射关系，channelId是什么鬼

            //在登录时将上下文id与ctx加载到缓存中
            playerCacheMgr.savePlayerCtx(playerId,ctx);

            player.setCtx(ctx);

            return player;

        } else {


            playerCacheMgr.putCtxPlayer(ctx,playerCache);

            // 保存playerId跟ChannelHandlerContext之间的关系
            playerCacheMgr.savePlayerCtx(playerId,ctx);
            // 玩家初始化
            playerDataService.initPlayer(playerCache);

            return playerCache;

        }
    }

    /**
     *  判断用户是否拥有这个角色
     * @param ctx   上下文
     * @param playerId  要判定的角色id
     * @return 用户是否拥有此角色
     */

    public boolean hasPlayerRole(ChannelHandlerContext ctx, int playerId) {
        User user = UserCacheManger.getUserByCtx(ctx);
        List<UserEntity>  userEntityList = userService.findPlayers(user.getId());


        for(UserEntity userEntity:userEntityList){
            if(userEntity.getId().equals(playerId)){
                return true;
            }

        }

        return  false;
    }

    /**
     *  创建新角色
     * @param roleName  角色名字，数据路唯一
     * @param role  职业
     * @param userId    用户id
     */
    public void roleCreate(ChannelHandlerContext ctx, String roleName, Integer role, int userId) {

        UserEntity player = new UserEntity();

        player.setId(IDUtil.getid());
        player.setName(roleName);
        player.setTypeId(role);
        player.setUserId(userId);
        //设置初始状态
        player.setState(1);
        //设置初始时的场景ID
        player.setSenceId(1);

        try {
            userEntityMapper.insertUserEntity(player);
        } catch( DuplicateKeyException e) {
            Notify.notifyByCtx(ctx,"角色名已经存在");
            return;
        }

        UserEntity userEntity=userEntityMapper.selectPlayerByName(roleName);

        if(!(userEntity ==null)){
            Notify.notifyByCtx(ctx, "创建角色成功");
        }else {
            Notify.notifyByCtx(ctx,"角色创建失败");
        }

    }
}
