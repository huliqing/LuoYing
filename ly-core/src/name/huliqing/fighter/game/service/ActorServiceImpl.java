/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.collision.CollisionResult;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.utils.NpcNameUtils;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.actor.ActorControl;
import name.huliqing.fighter.constants.IdConstants;
import name.huliqing.fighter.constants.ResConstants;
import name.huliqing.fighter.data.ActorData;
import name.huliqing.fighter.data.AttributeData;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.data.SkinData;
import name.huliqing.fighter.enums.HurtFace;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.enums.DataType;
import name.huliqing.fighter.enums.Sex;
import name.huliqing.fighter.game.dao.ItemDao;
import name.huliqing.fighter.manager.talk.Talk;
import name.huliqing.fighter.loader.ActorLoader;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.object.DataLoaderFactory;
import name.huliqing.fighter.enums.SkillType;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.manager.talk.SpeakManager;
import name.huliqing.fighter.manager.talk.TalkManager;
import name.huliqing.fighter.object.actor.ActorListener;
import name.huliqing.fighter.object.actor.SkillListener;
import name.huliqing.fighter.object.channel.Channel;
import name.huliqing.fighter.object.channel.ChannelProcessor;
import name.huliqing.fighter.object.effect.Effect;
import name.huliqing.fighter.object.el.LevelEl;
import name.huliqing.fighter.object.el.XpDropEl;
import name.huliqing.fighter.utils.GeometryUtils;
import name.huliqing.fighter.utils.RayUtils;
import name.huliqing.fighter.utils.Temp;

/**
 *
 * @author huliqing
 */
public class ActorServiceImpl implements ActorService {

    private SkillService skillService;
    private StateService stateService;
    private LogicService logicService;
    private PlayService playService;
    private SkinService skinService;
    private ElService elService;
    private AttributeService attributeService;
    private TalentService talentService;
    private EffectService effectService;
    private ConfigService configService;
    private ItemDao itemDao;
    
    @Override
    public void inject() {
        skillService = Factory.get(SkillService.class);
        stateService = Factory.get(StateService.class);
        logicService = Factory.get(LogicService.class);
        itemDao = Factory.get(ItemDao.class);
        playService = Factory.get(PlayService.class);
        skinService = Factory.get(SkinService.class);
        elService = Factory.get(ElService.class);
        attributeService = Factory.get(AttributeService.class);
        configService = Factory.get(ConfigService.class);
        talentService = Factory.get(TalentService.class);
        effectService = Factory.get(EffectService.class);
    }

    @Override
    public Actor loadActor(String actorId) {
        ActorData actorData = DataLoaderFactory.createActorData(actorId);
        return loadActor(actorData);
    }

    @Override
    public Actor loadActor(ActorData actorData) {
        // 在载入角色之前需要把所有属性清除，这些属性会根据等级及状态、装备、天赋
        // 等进行重新计算，以避免叠加BUG。
        for (AttributeData ad : actorData.getAttributes().values()) {
            ad.setDynamicValue(0);
            ad.setLevelValue(0);
            ad.setStaticValue(0);
        }
        
        // ==1.基本角色
        Actor actor = Loader.loadActor(actorData);
        
        // ==2.穿上装备
        List<ProtoData> items = itemDao.getItems(actor, null);
        boolean hasOutfit = false;
        for (ProtoData item : items) {
            if (item.getDataType() == DataType.skin) {
                SkinData sd = (SkinData) item;
                if (sd.isUsing()) {
                    hasOutfit = true;
                    skinService.attachSkin(actor, sd);
                }
            }
        }
        
        // 如果角色没有指定装备，则需要补上基本皮肤（如果存在基本皮肤）
        if (!hasOutfit) {
            if (actorData.getSkinBase() != null && !actorData.getSkinBase().isEmpty()) {
                // 这里使用一个基本皮肤就可以，会自动补上其它皮肤
                skinService.attachSkin(actor, actorData.getSkinBase().get(0));
            }
        }
        
        // ==3.更新属性的等级值,根据等级计算公式为角色设置相应属性的等级值
        updateLevel(actor.getData());
        
        // ==4.更新天赋值（已经在ActorLoader中处理）
        // ignore
        
        return actor;
    }
    
    // remove 0221
//    @Override
//    public boolean rewardItem(Actor actor, String objectId, int count) {
//        boolean result = itemDao.addItem(actor, objectId, count);
//        if (result) {
//            ListenerManager.broadcastItemUpdate(actor);
//            
//            // 如果角色是当前场景中的玩家，则提示信息
//            if (actor == playService.getPlayer()) {
//                // 提示获得物品
//                playService.addMessage(ResourceManager.get(ResConstants.COMMON_REWARD_ITEM
//                        , new Object[] {ResourceManager.getObjectName(objectId), count > 1 ? "(" + count + ")" : ""})
//                        , MessageType.item);
//
//                // 播放获得物品时的声效
//                SoundManager.getInstance().playGetItemSound(objectId, actor.getModel().getWorldTranslation());
//            }
//        }
//        return result;
//    }
    
    @Override
    public String createRandomName(Sex sex) {
        return NpcNameUtils.createRandomName(sex);
    }
    
    // remove20160504
//    /**
//     * use {@link #hasObstacleActor(name.huliqing.fighter.actor.Actor, java.util.List) }
//     * 代替
//     * @param actor
//     * @return 
//     */
//    @Override
//    public boolean hasObstacle(Actor actor) {
//        TempVars tv = TempVars.get();
//        Vector3f rayOrigin = GeometryUtils.getModelBoundCenter(actor.getModel(), tv.vect3);
//        Vector3f rayDirection = tv.vect2.set(actor.getViewDirection()).normalizeLocal();
//        float checkDistance = GeometryUtils.getBoundingVolumeZExtent(actor.getModel()) * 2f;
//        tv.release();
//        
//        // debug
////        DebugDynamicUtils.debugArrow(actor.getModel().getName() + actor.toString(), rayOrigin, rayDirection, checkDistance);
//        
//        // 2.查找障碍
//        Spatial root = GeometryUtils.findRootNode(actor.getModel());
//        Temp temp = Temp.get();
//        temp.results.clear();
//        RayUtils.collideWith(rayOrigin, rayDirection, root, temp.results);
//        boolean obstacle = false;
//        for (CollisionResult r : temp.results) {
//            // 只查找在指定距离内的障碍物,大于该距离的都不视为障碍物。
//            if (r.getDistance() > checkDistance) {
//                break;
//            }
//            
//            // 排除自身
//            if (!isObstacle(r.getGeometry(), actor.getModel())) {
//                continue;
//            }
//
////            Logger.get(GeometryUtils.class).log(Level.INFO, "Found obstacle infront! distance={0}, name={1}"
////                    , new Object[] {r.getDistance(), r.getGeometry().getName()});
//            // 障碍物找到
//            obstacle = true;
//            break;
//        }
//        temp.release();
//        return obstacle;
//    }
    
    // remove20160504
//    // TODO 该方法后续需要优化
//    // 基本上只查找含有physicsControl的对象就可以，但要排除terrain及sky
//    private boolean isObstacle(Spatial spatial, Spatial except) {
//        // except为需要排除的对象
//        if (spatial == except) {
//            return false;
//        }
//        ProtoData pd = spatial.getUserData(ProtoData.USER_DATA);
//        if (pd != null) {
//            DataType pt = pd.getProto().getDataType();
//            if (pt == DataType.sky || pt == DataType.terrain) {
//                return false;
//            }
//            if (spatial.getControl(PhysicsControl.class) != null) {
//                return true;
//            }
//        } 
//        if (spatial.getParent() != null) {
//            return isObstacle(spatial.getParent(), except);
//        }
//        return false;
//    }
    
    // remove20160204,这个方法有bug,在客户端可能发生角色碰撞弹跳到空中的现象，可能是因为WorldBound距离
    // 太近的原因。
//    @Override
//    public boolean hasObstacleActor(Actor self, List<Actor> actors) {
//        boolean obstacle = false;
//        for (Actor a : actors) {
//            if (a == self) {
//                continue;
//            }
//            
//            // 使用ray也可以，但是使用WorldBound可能性能更好一些。
//            if (a.getModel().getWorldBound().intersects(self.getModel().getWorldBound())) {
//                obstacle = true;
//                break;
//            }
//        }
//        return obstacle;
//    }
    
    
    @Override
    public boolean hasObstacleActor(Actor self, List<Actor> actors) {
        TempVars tv = TempVars.get();
        Temp temp = Temp.get();
        
        Vector3f origin = tv.vect1.set(self.getModel().getWorldBound().getCenter());
        Vector3f dir = tv.vect2.set(self.getViewDirection()).normalizeLocal();
        float zExtent = GeometryUtils.getBoundingVolumeZExtent(self.getModel());
        origin.addLocal(dir.mult(zExtent, tv.vect3));
        
//        DebugDynamicUtils.debugArrow(self.toString(), origin, dir, zExtent);
        
        // 检查碰撞
//        float checkDistance = zExtent;
        float checkDistance = zExtent * 1.5f; // modify20160504, * 1.5f,稍微加大了一点距离
        float checkDistanceSquare = checkDistance * checkDistance;
        Vector3f targetOrigin = tv.vect4;
        
        boolean obstacle = false;
        Ray ray = temp.ray;
        ray.setOrigin(origin);
        ray.setDirection(dir);
        ray.setLimit(checkDistance);
        for (Actor a : actors) {
            if (a == self) {
                continue;
            }
            
            // 如果距离跳过checkDistance,则不视为障碍（该判断用于优化性能）
            // 减少ray检测
            targetOrigin.set(a.getModel().getWorldBound().getCenter());
            if (targetOrigin.distanceSquared(origin) > checkDistanceSquare) {
                continue;
            }
            
            // 使用ray也可以，但是使用WorldBound可能性能更好一些。
            if (a.getModel().getWorldBound().intersects(ray)) {
                obstacle = true;
                break;
            }
        }
        
        // 释放缓存
        tv.release();
        temp.release();
        return obstacle;
    }

    /**
     * @deprecated use ItemService instead
     * @param actor
     * @param objectId
     * @return 
     */
    @Override
    public ProtoData getItem(Actor actor, String objectId) {
        return itemDao.getItemExceptSkill(actor, objectId);
    }

    /**
     * @deprecated use ItemService instead
     * @param actor
     * @param store
     * @return 
     */
    @Override
    public List<ProtoData> getItems(Actor actor, List<ProtoData> store) {
        return itemDao.getItems(actor, store);
    }
    
    @Override
    public HurtFace checkFace(Spatial self, Actor target) {
        if (target.getViewAngle(self.getWorldTranslation()) < 90) {
            return HurtFace.front;
        } else {
            return HurtFace.back;
        }
    }

    @Override
    public Actor getActor(Spatial spatial) {
        ProtoData pd = spatial.getUserData(ProtoData.USER_DATA);
        if (pd.getDataType() == DataType.actor) {
            return spatial.getControl(ActorControl.class);
        }
        return null;
    }

    @Override
    public Actor findNearestEnemyExcept(Actor actor, float maxDistance, Actor except) {
        List<Actor> actors = Common.getPlayState().getActors();
        float minDistanceSquared = maxDistance * maxDistance;
        float distanceSquared;
        Actor enemy = null;
        for (Actor target : actors) {
            if (target == except) { // 被排除的角色（同一实例）
                continue;
            }
            // 负值的派系不作为敌人搜寻
            if (getGroup(target) < 0) {
                continue;
            }
            // 角色已经死亡或同一派别
            if (target.isDead() || !target.isEnemy(actor)) {
                continue;
            }
            // 判断可视范围内的敌人.
            distanceSquared = target.getModel().getWorldTranslation()
                    .distanceSquared(actor.getModel().getWorldTranslation());
            if (distanceSquared < minDistanceSquared) { // 找出最近的敌人
                minDistanceSquared = distanceSquared;
                enemy = target;
            }
        }
        return enemy;
    }
    
    @Override
    public List<Actor> findNearestEnemies(Actor actor, float maxDistance, List<Actor> store) {
        if (store == null) {
            store = new ArrayList<Actor>();
        }
        List<Actor> actors = Common.getPlayState().getActors();
        float maxDistanceSquared = maxDistance * maxDistance;
        float distanceSquared;
        for (Actor target : actors) {
            // 负值的派系不作为敌人搜寻
            if (getGroup(target) < 0) {
                continue;
            }
            // 角色已经死亡或同一派别
            if (target.isDead() || !target.isEnemy(actor)) {
                continue;
            }
            // 找出范围内的敌人
            distanceSquared = target.getModel().getWorldTranslation()
                    .distanceSquared(actor.getModel().getWorldTranslation());
            if (distanceSquared <= maxDistanceSquared) { 
                store.add(target);
            }
        }
        return store;
    }
    
    @Override
    public List<Actor> findNearestFriendly(Actor actor, float maxDistance, List<Actor> store) {
        if (store == null) {
            store = new ArrayList<Actor>();
        }
        List<Actor> actors = Common.getPlayState().getActors();
        for (Actor a : actors) {
            if (a.isDead() || a.isEnemy(actor) || a.getDistance(actor) > maxDistance) {
                continue;
            }
            if (getGroup(a) == getGroup(actor)) {
                store.add(a);
            }
        }
        return store;
    }

    @Override
    public List<Actor> findNearestActors(Actor actor, float maxDistance, List<Actor> store) {
        // remove20160413
//        if (store == null) {
//            store = new ArrayList<Actor>();
//        }
//        float maxDistanceSquared = FastMath.pow(maxDistance, 2);
//        List<Actor> actors = Common.getPlayState().getActors();
//        for (Actor a : actors) {
//            if (a == actor || a.getDistanceSquared(actor) > maxDistanceSquared) 
//                continue;
//            store.add(a);
//        }
//        return store;
        
        return findNearestActors(actor, maxDistance, 360, store);
    }

    @Override
    public List<Actor> findNearestActors(Actor actor, float maxDistance, float angle, List<Actor> store) {
        if (angle <= 0)
            return store;
        if (store == null) {
            store = new ArrayList<Actor>();
        }
        float maxDistanceSquared = FastMath.pow(maxDistance, 2);
        List<Actor> actors = Common.getPlayState().getActors();
        float halfAngle = angle * 0.5f;
        for (Actor a : actors) {
            
            if (a == actor || a.getDistanceSquared(actor) > maxDistanceSquared) 
                continue;
            
            if (angle >= 360 || actor.getViewAngle(a.getLocation()) < halfAngle) {
                store.add(a);
            }
        }
        return store;
    }

    @Override
    public boolean isSkillUpdated(Actor actor, long timePoint) {
        return actor.getData().getSkillStore().getLastModifyTime() > timePoint;
    }

    @Override
    public float getHeight(Actor actor) {
        BoundingBox bb = (BoundingBox) actor.getModel().getWorldBound();
        return bb.getXExtent() * 2;
    }

    @Override
    public void setPartner(Actor owner, Actor partner) {
        // 设置为同一派别,及所有者
        setGroup(partner, getGroup(owner));
        setOwner(partner, owner.getData().getUniqueId());
        setFollow(partner, owner.getData().getUniqueId());
    }

    @Override
    public void speak(Actor actor, String mess, float useTime) {
        SpeakManager.getInstance().doSpeak(actor, mess, useTime);
    }

    @Override
    public void talk(Talk talk) {
        // 不要在这里设置setNetwork(false),这会覆盖掉actorNetwork.talk的设置
        // 因为actorNetwork.talk是直接调用actorService.talk的方法
//        talk.setNetwork(false);
        
        TalkManager.getInstance().startTalk(talk);
    }

    @Override
    public Vector3f getLocalToWorld(Actor actor, Vector3f localPos, Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        actor.getModel().getWorldRotation().mult(localPos, store);
        store.addLocal(actor.getModel().getWorldTranslation());
        return store;
    }

    @Override
    public boolean checkAndLoadAnim(Actor actor, String animName) {
        if (animName == null) {
            return false;
        }
        AnimControl ac = actor.getModel().getControl(AnimControl.class);
        if (ac.getAnim(animName) != null) {
            return true;
        } else {
            return ActorLoader.loadExtAnim(actor, animName);
        }
    }

    @Override
    public void kill(Actor actor) {
        // 血量降低
        AttributeData life = actor.getData().getLifeAttributeData();
        if (life != null) {
            life.setDynamicValue(0);
        }
        
        // 执行死亡动画
        skillService.playSkill(actor, skillService.getSkill(actor, SkillType.dead).getId(), false);
    }
    
    @Override
    public void reborn(Actor actor) {
        // 重生的时候属性值全满
        Map<String, AttributeData> attrMaps = actor.getData().getAttributes();
        if (attrMaps != null) {
            for (AttributeData ad : attrMaps.values()) {
                ad.setDynamicValue(ad.getMaxValue());
            }
        }
        
        skillService.playSkill(actor, skillService.getSkill(actor, SkillType.wait).getId(), false);
    }

    @Override
    public void setTarget(Actor actor, Actor target) {
        ActorData data = actor.getData();
        
        // 同一个目标不需要重新设置
        if (target != null && target.getData().getUniqueId() == data.getTarget()) {
            return;
        }
        
        // 释放旧目标的listener
        long oldTargetId = data.getTarget();
        if (oldTargetId != -1) {
            Actor oldTarget = playService.findActor(oldTargetId);
            if (oldTarget != null) {
                List<ActorListener> als = oldTarget.getActorListeners();
                if (als != null) {
                    for (ActorListener al : als) {
                        al.onActorReleased(oldTarget, actor);
                    }
                }
            }
        }
        
        if (target == null) {
            actor.getData().setTarget(-1);
            return ;
        }

        // 锁定新目标的listener.
        actor.getData().setTarget(target.getData().getUniqueId());
        List<ActorListener> als = target.getActorListeners();
        if (als != null) {
            for (ActorListener al : als) {
                al.onActorLocked(target, actor);
            }
        }
    }

    @Override
    public Actor getTarget(Actor actor) {
        long targetId = actor.getData().getTarget();
        if (targetId == -1)
            return null;
        List<Actor> actors = Common.getPlayState().getActors();
        for (Actor a : actors) {
            if (a.getData().getUniqueId() == targetId) {
                return a;
            }
        }
        return null;
    }

    @Override
    public boolean isAutoAi(Actor actor) {
        return actor.getData().isAutoAi();
    }

    @Override
    public void setAutoAi(Actor actor, boolean autoAi) {
        actor.getData().setAutoAi(autoAi);
    }

    @Override
    public boolean isAutoDetect(Actor actor) {
        return actor.getData().isAutoDetect();
    }

    @Override
    public void setAutoDetect(Actor actor, boolean autoDetect) {
        actor.getData().setAutoDetect(autoDetect);
    }

    @Override
    public boolean isDead(Actor actor) {
        AttributeData life = actor.getData().getLifeAttributeData();
        if (life != null) {
            return life.getDynamicValue() <= 0;
        }
        return false;
    }

    @Override
    public boolean isEnemy(Actor actor, Actor target) {
        // 如果组别小于0则视为非敌人。
        if (target.getData().getGroup() < 0) {
            return false;
        }
        return actor.getData().getGroup() != target.getData().getGroup();
    }

    @Override
    public void setPhysics(Actor actor, boolean enabled) {
        actor.setEnabled(enabled);
    }

    @Override
    public void setViewDirection(Actor actor, Vector3f viewDirection) {
        actor.setViewDirection(viewDirection);
    }

    @Override
    public void setColor(Actor actor, ColorRGBA color) {
        actor.getData().setColor(color);
        GeometryUtils.setColor(actor.getModel(), color);
    }

    @Override
    public void hitAttribute(Actor target, Actor source, String hitAttribute, float hitValue) {
        if (!attributeService.existsAttribute(target, hitAttribute)) {
            return;
        }
        boolean deadBefore = isDead(target);
        attributeService.applyDynamicValue(target, hitAttribute, hitValue);
        attributeService.clampDynamicValue(target, hitAttribute);
        boolean killed = !deadBefore && isDead(target);
        
        // 触发"被攻击者(source)"的角色侦听器
        // "被伤害","被杀死"侦听
        List<ActorListener> sourceListeners = target.getActorListeners();
        if (sourceListeners != null) {
            for (ActorListener l : sourceListeners) {
                l.onActorHit(target, source, hitAttribute, hitValue);
                if (killed) {
                    l.onActorKilled(target, source);
                }
            }
        }
        
        // 触发"攻击者(attacker)"的角色侦听器
        // 当攻击者“杀死”目标时，要让“攻击者”知道.但有时候攻击者不是一个角色
        // 或者不是任何一个"存在",所以source可能为null.
        if (killed && source != null) {
            List<ActorListener> attackerListeners = source.getActorListeners();
            if (attackerListeners != null) {
                for (ActorListener al : attackerListeners) {
                    al.onActorKill(source, target);
                }
            }
        }
    }

    @Override
    public int getLevel(Actor actor) {
        return actor.getData().getLevel();
    }

    @Override
    public void setLevel(Actor actor, int level) {
        actor.getData().setLevel(level);
        updateLevel(actor.getData());
    }
    
    // 更新角色的属性等级
    private void updateLevel(ActorData actorData) {
        // 根据等级计算公式为角色设置相应属性的等级值
        Map<String, AttributeData> attributeMap = actorData.getAttributes();
        if (attributeMap != null) {
            LevelEl levelEl;
            for (AttributeData attrData : attributeMap.values()) {
                levelEl = (LevelEl) Loader.loadEl(attrData.getEl());
                attrData.setLevelValue((float)levelEl.getValue(actorData.getLevel()));
                attrData.setDynamicValue(attrData.getMaxValue());
//                if (Config.debug) {
//                    Logger.getLogger(ElServiceImpl.class.getName())
//                            .log(Level.INFO
//                            , "setLevel, id={0}, levelValue={1}, staticValue={2}, DynamicValue={3}"
//                            , new Object[] {attrData.getId(), attrData.getLevelValue(), attrData.getStaticValue(), attrData.getDynamicValue()});
//                }
            }
        }
    }

    @Override
    public int getXpReward(Actor attacker, Actor dead) {
        String xpDropEl = dead.getData().getXpDropEl();
        if (xpDropEl != null) {
            XpDropEl xde = elService.getXpDropEl(xpDropEl);
            return xde.getValue(dead.getData().getLevel(), attacker.getData().getLevel());
        }
        return 0;
    }
    
    @Override
    public int applyXp(Actor actor, int xp) {
        // 如果没有配置升级公式则不作处理
        String levelUpEl = actor.getData().getLevelUpEl();
        if (levelUpEl == null) {
            return 0;
        }
        
        // 增加经验
        ActorData data = actor.getData();
        data.setXp(data.getXp() + xp);
        
        // 对当前场景角色进行提示，注：不需要对其它玩家角色提示
        if (actor == playService.getPlayer()) {
            playService.addMessage(ResourceManager.get(ResConstants.COMMON_GET_XP, new Object[]{xp}), MessageType.info);
        }
        
        // 检查升级
        Temp tv = Temp.get();
        tv.array2[0] = 0;   // upCount 可以升多少级
        tv.array2[1] = 0;   // needXp 需要多少XP
        ActorData actorData = actor.getData();
        LevelEl levelEl = (LevelEl) Loader.loadEl(actorData.getLevelUpEl());
        checkLevelUp(levelEl, actorData.getLevel(), actorData.getXp(), tv.array2, configService.getMaxLevel());
        int upCount = tv.array2[0];
        int needXp = tv.array2[1];
        tv.release();
        
        // 升级角色，如果可以升级
        if (upCount > 0) {
            
            // 1.奖励天赋点数
            String tpel = data.getTalentPointsLevelEl();
            int rewardTP = 0; // item talentPoints;
            if (tpel != null) {
                for (int i = 1; i <= upCount; i++) {
                    rewardTP += (int) elService.getLevelEl(tpel, data.getLevel() + i);
                }
                data.setTalentPoints(data.getTalentPoints() + rewardTP);
            }
            
            // 2.升级等级
            data.setLevel(data.getLevel() + upCount);
            actorData.setXp(actorData.getXp() - needXp);
            updateLevel(actorData);
            
            // 3.提示升级(效果）
            Effect effect = effectService.loadEffect(IdConstants.EFFECT_LEVEL_UP);
            effect.setTraceObject(actor.getModel());
            playService.addEffect(effect);
            
            // 4.提示升级(mess)
            if (actor == playService.getPlayer()) {
                playService.addMessage(ResourceManager.get(ResConstants.COMMON_LEVEL_UP) 
                            + "(" + actor.getData().getLevel() + ")"
                        , MessageType.levelUp);
                
            }
            
            // 4.提示获得天赋点数
            if (actor == playService.getPlayer() && rewardTP > 0) {
                playService.addMessage(ResourceManager.get(ResConstants.COMMON_GET_TALENT
                            , new Object[] {rewardTP})
                        , MessageType.levelUp);
            }
        }
        
        return upCount;
    }

    @Override
    public int getNextLevelXp(Actor actor) {
        String levelUpEl = actor.getData().getLevelUpEl();
        if (levelUpEl != null) {
            LevelEl el = (LevelEl) Loader.loadEl(actor.getData().getLevelUpEl());
            return (int) el.getValue(actor.getData().getLevel() + 1);
        }
        return 0;
    }

    @Override
    public boolean isMoveable(Actor actor) {
        return actor.getMass() > 0;
    }

    @Override
    public float getViewDistance(Actor actor) {
        AttributeData viewAttr = actor.getData().getViewAttributeData();
        if (viewAttr != null) {
            return viewAttr.getDynamicValue();
        }
        return 0;
    }

    @Override
    public void addActorListener(Actor actor, ActorListener actorListener) {
        if (actorListener == null) 
            return;
        List<ActorListener> als = actor.getActorListeners();
        if (!als.contains(actorListener)) {
            als.add(actorListener);
        }
    }

    @Override
    public boolean removeActorListener(Actor actor, ActorListener actorListener) {
        return actor.getActorListeners().remove(actorListener);
    }

    @Override
    public void addSkillListener(Actor actor, SkillListener skillListener) {
        if (skillListener == null)
            return;
        List<SkillListener> sls = actor.getSkillListeners();
        if (!sls.contains(skillListener)) {
            sls.add(skillListener);
        }
    }

    @Override
    public boolean removeSkillListener(Actor actor, SkillListener skillListener) {
        List<SkillListener> sls = actor.getSkillListeners();
        return sls.remove(skillListener);
    }
    
    @Override
    public void setName(Actor actor, String name) {
        actor.getData().setName(name);
        actor.getModel().setName(name);
    }

    @Override
    public String getName(Actor actor) {
        return actor.getData().getName();
    }

    @Override
    public int getLife(Actor actor) {
        AttributeData life = actor.getData().getLifeAttributeData();
        return (int) life.getDynamicValue();
    }

    @Override
    public int getGroup(Actor actor) {
        return actor.getData().getGroup();
    }

    @Override
    public void setGroup(Actor actor, int group) {
        actor.getData().setGroup(group);
    }

    @Override
    public int getTeam(Actor actor) {
        return actor.getData().getTeam();
    }

    @Override
    public void setTeam(Actor actor, int team) {
        actor.getData().setTeam(team);
        Common.getPlayState().getTeamView().checkAddOrRemove(actor);
    }

    @Override
    public Sex getSex(Actor actor) {
        return actor.getData().getSex();
    }

    @Override
    public boolean isEssential(Actor actor) {
        return actor.getData().isEssential();
    }

    @Override
    public void setEssential(Actor actor, boolean essential) {
        actor.getData().setEssential(essential);
    }

    @Override
    public String getRace(Actor actor) {
        return actor.getData().getRace();
    }

    @Override
    public void setOwner(Actor actor, long ownerId) {
        actor.getData().setOwnerId(ownerId);
    }

    @Override
    public long getOwner(Actor actor) {
        return actor.getData().getOwnerId();
    }

    @Override
    public void setFollow(Actor actor, long targetId) {
        actor.getData().setFollowTarget(targetId);
    }

    @Override
    public long getFollow(Actor actor) {
        return actor.getData().getFollowTarget();
    }
    
    @Override
    public int getTalentPoints(Actor actor) {
        return actor.getData().getTalentPoints();
    }

    @Override
    public void syncTransform(Actor actor, Vector3f location, Vector3f viewDirection) {
        if (location != null)
            actor.setLocation(location);
        if(viewDirection != null)
            actor.setViewDirection(viewDirection);
    }

    @Override
    public void syncAnimation(Actor actor, String[] channelIds, String[] animNames, byte[] loopModes, float[] speeds, float[] times) {
        if (channelIds == null) 
            return;
        
        ChannelProcessor cp = actor.getChannelProcessor();
        if (cp == null)
            return;
        
        for (int i = 0; i < channelIds.length; i++) {
            Channel ch = cp.getChannel(channelIds[i]);
            if (ch == null)
                continue;
            if (animNames[i] == null) 
                continue;
            
            // 同步动画通道时先解锁，因为可能存在一些正处于锁定状态的通道。
            // 比如正在抽取武器的过程，手部通道可能会锁定。同步完动画之后再把状态设置回去。
            boolean oldLocked = ch.isLocked();
            ch.setLock(false);
            byte loopByte = loopModes[i];
            LoopMode lm = LoopMode.DontLoop;
            if (loopByte == 1) {
                lm = LoopMode.Loop;
            } else if (loopByte == 2) {
                lm = LoopMode.Cycle;
            }
            // 检查是否存在动画，如果没有则载入。
            checkAndLoadAnim(actor, animNames[i]);
            // 同步动画
            ch.playAnim(animNames[i], 0, lm, speeds[i], times[i]);
            ch.setLock(oldLocked);
        }
    }
    
    /**
     * 检查可以升多少级和需要多少经验
     * @param currentLevel 当前的等级
     * @param currentXp 当前的经验值
     * @param store 存放结果的数组，store[2] {upCount, needXp} upCount表示可以
     * 升多少级，needXp表示需要多少xp.
     */
    private void checkLevelUp(LevelEl el, int currentLevel, int currentXp, int[] store, int maxLevel) {
        if (currentLevel >= maxLevel) {
            return;
        }
        long nextXp = (long) el.getValue(currentLevel + 1);
        if (currentXp >= nextXp) {
            currentLevel++;
            currentXp -= nextXp;
            store[0] += 1;
            store[1] += (int) nextXp;
            checkLevelUp(el, currentLevel, currentXp, store, maxLevel);
        }
    }
}
