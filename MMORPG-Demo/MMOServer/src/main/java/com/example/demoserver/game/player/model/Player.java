package com.example.demoserver.game.player.model;



import com.example.demoserver.common.commons.Character;
import com.example.demoserver.common.commons.Constant;
import com.example.demoserver.event.dispatch.EventManager;
import com.example.demoserver.event.events.LevelEvent;
import com.example.demoserver.event.events.MoneyListenerEvent;
import com.example.demoserver.game.ScenceEntity.model.Characters;
import com.example.demoserver.game.bag.model.Bag;
import com.example.demoserver.game.bag.model.Item;
import com.example.demoserver.game.buff.model.Buff;
import com.example.demoserver.game.friend.model.Friend;
import com.example.demoserver.game.roleproperty.model.RoleProperty;
import com.example.demoserver.game.scence.model.GameScene;
import com.example.demoserver.game.skills.model.Skill;
import com.example.demoserver.game.task.model.Task;
import com.example.demoserver.game.task.model.TaskProgress;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**

 * Description: 玩家
 */

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(exclude = {"ctx","target","currentScene"})
@Slf4j
public class Player extends UserEntity implements Character {


    /** 当前通道上下文 **/
    private ChannelHandlerContext ctx;

    /** 当前目标 */
    private Character target;

    /**  受职业配置表和装备影响 */
    private Long hp;

    private Long mp;

    private Integer level;

    /**
     * 玩家现在拥有的buff
     */
    List<Buff> buffList=new ArrayList<>();

    /** 构建背包 **/
    Bag bag=new Bag(this.getId(),16);

    /** 装备栏 **/
    private Map<String, Item> equipmentBar = new ConcurrentHashMap<>();

    /** 场景目前 **/
    private GameScene currentScene;

    /**
     * 使用过的技能
     */
    private Map<Integer , Skill> skillMap =new ConcurrentHashMap<>();

    /**
     * 角色属性，key为每一个属性的id，value为属性对象
     */
    private Map<Integer, RoleProperty> rolePropertyMap = new ConcurrentHashMap<>();

    /**
     * 定义一个Map，存放接过的任务
     */
    private  Map<Integer, TaskProgress> taskProgressMap =new ConcurrentHashMap<>();


    /**
     * 定义玩家所拥有的好友
     */
    private Map<Integer, Friend> friendMap = new HashMap<>();




    /**
     *  金币变化
     * @param money 当前要变化的金币数量，正数为增加，负数减少
     */
    public void moneyChange(Integer money) {
        this.setMoney(this.getMoney()+money);
        EventManager.publish(new MoneyListenerEvent(this,money));
    }


    /**
     *
     * @param exp
     */
    public synchronized void addExp(Integer exp) {
        this.setExp(this.getExp() + exp);

        int newLevel = this.getExp() / Constant.LEVEL_DIVISOR;

        // 如果等级发生变化，进行提示
        if (newLevel != this.getLevel()) {
            EventManager.publish(new LevelEvent(this, newLevel));
        }
    }


    public String displayData() {
        return MessageFormat.format("id:{0}  name:{1}  hp:{2}  mp:{3}"
                ,this.getId(),this.getName(), this.getHp(), this.getMp());
    }


    @Override
    public Map<Integer, Skill> getHasUseSkillMap() {
        return null;
    }

    @Override
    public void setHasUseSkillMap(Map<Integer, Skill> skillMap) {

    }

    @Override
    public List<Buff> getBufferList() {
        return buffList;
    }

    @Override
    public void setBufferList(List<Buff> buffList) {

        this.buffList=buffList;
    }

    @Override
    public void setTarget(Character target) {
        this.target=target;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

}
