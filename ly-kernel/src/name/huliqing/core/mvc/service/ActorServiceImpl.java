/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import name.huliqing.core.LY;
import name.huliqing.core.Factory;
import name.huliqing.core.utils.NpcNameUtils;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.data.ActorData;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.enums.HurtFace;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.enums.Sex;
import name.huliqing.core.mvc.dao.ItemDao;
import name.huliqing.core.view.talk.Talk;
import name.huliqing.core.object.actor.ActorModelLoader;
import name.huliqing.core.object.Loader;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.view.talk.SpeakManager;
import name.huliqing.core.view.talk.TalkManager;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.object.actor.ActorListener;
import name.huliqing.core.object.channel.Channel;
import name.huliqing.core.object.module.PhysicsModule;
import name.huliqing.core.object.effect.Effect;
import name.huliqing.core.object.el.LevelEl;
import name.huliqing.core.object.el.XpDropEl;
import name.huliqing.core.utils.GeometryUtils;
import name.huliqing.core.utils.Temp;
import name.huliqing.core.object.module.BaseModule;
import name.huliqing.core.object.module.ChannelModule;

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
        ActorData data = DataFactory.createData(actorId);
        return loadActor(data);
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
        List<ObjectData> items = itemDao.getItems(actor, null);
        boolean hasOutfit = false;
        for (ObjectData item : items) {
            if (item instanceof SkinData) {
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
    
    @Override
    public String createRandomName(Sex sex) {
        return NpcNameUtils.createRandomName(sex);
    }
    
    @Override
    public boolean hasObstacleActor(Actor self, List<Actor> actors) {
        TempVars tv = TempVars.get();
        Temp temp = Temp.get();
        
        Vector3f origin = tv.vect1.set(self.getSpatial().getWorldBound().getCenter());
        Vector3f dir = tv.vect2.set(getViewDirection(self)).normalizeLocal();
        float zExtent = GeometryUtils.getBoundingVolumeZExtent(self.getSpatial());
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
            targetOrigin.set(a.getSpatial().getWorldBound().getCenter());
            if (targetOrigin.distanceSquared(origin) > checkDistanceSquare) {
                continue;
            }
            
            // 使用ray也可以，但是使用WorldBound可能性能更好一些。
            if (a.getSpatial().getWorldBound().intersects(ray)) {
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
    public ObjectData getItem(Actor actor, String objectId) {
        return itemDao.getItemExceptSkill(actor, objectId);
    }

    /**
     * @deprecated use ItemService instead
     * @param actor
     * @param store
     * @return 
     */
    @Override
    public List<ObjectData> getItems(Actor actor, List<ObjectData> store) {
        return itemDao.getItems(actor, store);
    }
    
    @Override
    public HurtFace checkFace(Spatial self, Actor target) {
        if (getViewAngle(target, self.getWorldTranslation()) < 90) {
            return HurtFace.front;
        } else {
            return HurtFace.back;
        }
    }
    
    // remove20160815
//    /**
//     * @deprecated 
//     * @param spatial
//     * @return 
//     */
//    @Override
//    public Actor getActor(Spatial spatial) {
//        ObjectData pd = spatial.getUserData(ObjectData.USER_DATA);
//        if (pd instanceof ActorData) {
//            return spatial.getControl(ActorControl.class);
//        }
//        return null;
//    }

    @Override
    public Actor findNearestEnemyExcept(Actor actor, float maxDistance, Actor except) {
        List<Actor> actors = LY.getPlayState().getActors();
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
            if (isDead(target) || !isEnemy(target, actor)) {
                continue;
            }
            // 判断可视范围内的敌人.
            distanceSquared = target.getSpatial().getWorldTranslation()
                    .distanceSquared(actor.getSpatial().getWorldTranslation());
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
        List<Actor> actors = LY.getPlayState().getActors();
        float maxDistanceSquared = maxDistance * maxDistance;
        float distanceSquared;
        for (Actor target : actors) {
            // 负值的派系不作为敌人搜寻
            if (getGroup(target) < 0) {
                continue;
            }
            // 角色已经死亡或同一派别
            if (isDead(target) || !isEnemy(target, actor)) {
                continue;
            }
            // 找出范围内的敌人
            distanceSquared = target.getSpatial().getWorldTranslation()
                    .distanceSquared(actor.getSpatial().getWorldTranslation());
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
        List<Actor> actors = LY.getPlayState().getActors();
        for (Actor a : actors) {
            if (isDead(a) || isEnemy(a, actor) 
                    || a.getSpatial().getWorldTranslation().distance(actor.getSpatial().getWorldTranslation()) > maxDistance) {
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
        List<Actor> actors = LY.getPlayState().getActors();
        float halfAngle = angle * 0.5f;
        for (Actor a : actors) {
            
            if (a == actor || a.getSpatial().getWorldTranslation().distanceSquared(actor.getSpatial().getWorldTranslation()) > maxDistanceSquared) 
                continue;
            
            if (angle >= 360 || getViewAngle(actor, a.getSpatial().getWorldTranslation()) < halfAngle) {
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
        BoundingBox bb = (BoundingBox) actor.getSpatial().getWorldBound();
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
        actor.getSpatial().getWorldRotation().mult(localPos, store);
        store.addLocal(actor.getSpatial().getWorldTranslation());
        return store;
    }

    @Override
    public boolean checkAndLoadAnim(Actor actor, String animName) {
        if (animName == null) {
            return false;
        }
        AnimControl ac = actor.getSpatial().getControl(AnimControl.class);
        if (ac.getAnim(animName) != null) {
            return true;
        } else {
            return ActorModelLoader.loadExtAnim(actor, animName);
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
                List<ActorListener> als = getActorBaseControl(oldTarget).getActorListeners(); 
                if (als != null && als.isEmpty()) {
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
        List<ActorListener> als = getActorBaseControl(target).getActorListeners();
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
        List<Actor> actors = LY.getPlayState().getActors();
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

    // remove20160813
//    @Override
//    public void setPhysics(Actor actor, boolean enabled) {
//        actor.setEnabled(enabled);
//    }
//
//    @Override
//    public void setViewDirection(Actor actor, Vector3f viewDirection) {
//        actor.setViewDirection(viewDirection);
//    }

    @Override
    public void setColor(Actor actor, ColorRGBA color) {
        actor.getData().setColor(color);
        GeometryUtils.setColor(actor.getSpatial(), color);
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
        List<ActorListener> sourceListeners = getActorBaseControl(target).getActorListeners();
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
            List<ActorListener> attackerListeners = getActorBaseControl(source).getActorListeners();
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
                levelEl = (LevelEl) elService.getEl(attrData.getEl());
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
        LevelEl levelEl = (LevelEl) elService.getEl(actorData.getLevelUpEl());
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
            effect.setTraceObject(actor.getSpatial());
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
            LevelEl el = (LevelEl) elService.getEl(actor.getData().getLevelUpEl());
            return (int) el.getValue(actor.getData().getLevel() + 1);
        }
        return 0;
    }

    @Override
    public boolean isMoveable(Actor actor) {
        return getMass(actor) > 0;
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
        List<ActorListener> als = getActorBaseControl(actor).getActorListeners();
        if (!als.contains(actorListener)) {
            als.add(actorListener);
        }
    }

    @Override
    public boolean removeActorListener(Actor actor, ActorListener actorListener) {
        return getActorBaseControl(actor).removeActorListener(actorListener);
    }

    // remove20160815
//    @Override
//    public void addSkillListener(Actor actor, SkillListener skillListener) {
//        if (skillListener == null)
//            return;
//        List<SkillListener> sls = actor.getSkillListeners();
//        if (!sls.contains(skillListener)) {
//            sls.add(skillListener);
//        }
//    }
//
//    @Override
//    public boolean removeSkillListener(Actor actor, SkillListener skillListener) {
//        List<SkillListener> sls = actor.getSkillListeners();
//        return sls.remove(skillListener);
//    }
    
    @Override
    public void setName(Actor actor, String name) {
        actor.getData().setName(name);
        actor.getSpatial().setName(name);
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
        LY.getPlayState().getTeamView().checkAddOrRemove(actor);
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
    
//    @Override
//    public int getTalentPoints(Actor actor) {
//        return actor.getData().getTalentPoints();
//    }

    @Override
    public void syncTransform(Actor actor, Vector3f location, Vector3f viewDirection) {
        if (location != null)
            setLocation(actor, location);
        
        if(viewDirection != null)
            setViewDirection(actor, viewDirection);
    }

    @Override
    public void syncAnimation(Actor actor, String[] channelIds, String[] animNames, byte[] loopModes, float[] speeds, float[] times) {
        if (channelIds == null) 
            return;
        
        ChannelModule cp = actor.getModule(ChannelModule.class);
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

    @Override
    public void setLocation(Actor actor, Vector3f location) {
        PhysicsModule module = actor.getModule(PhysicsModule.class);
        if (module != null) {
            module.setLocation(location);
        } else {
            actor.getSpatial().setLocalTranslation(location);
        }
    }

    @Override
    public Vector3f getLocation(Actor actor) {
        return actor.getSpatial().getWorldTranslation();
    }

    @Override
    public void setPhysicsEnabled(Actor actor, boolean enabled) {
        PhysicsModule module = actor.getModule(PhysicsModule.class);
        if (module != null) {
            module.setEnabled(enabled);
        }
    }
    
    @Override
    public boolean isPhysicsEnabled(Actor actor) {
        PhysicsModule module = actor.getModule(PhysicsModule.class);
        return module != null && module.isEnabled();
    }
    
    @Override
    public void setViewDirection(Actor actor, Vector3f viewDirection) {
        PhysicsModule module = actor.getModule(PhysicsModule.class);
        if (module != null) {
            module.setViewDirection(viewDirection);
        }
    }

    @Override
    public Vector3f getViewDirection(Actor actor) {
        PhysicsModule module = actor.getModule(PhysicsModule.class);
        if (module != null) {
            module.getViewDirection();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLookAt(Actor actor, Vector3f position) {
        PhysicsModule module = actor.getModule(PhysicsModule.class);
        if (module != null) {
            module.setLookAt(position);
        }
    }

    @Override
    public void setWalkDirection(Actor actor, Vector3f walkDirection) {
        PhysicsModule module = actor.getModule(PhysicsModule.class);
        if (module != null) {
            module.setWalkDirection(walkDirection);
        }
    }

    @Override
    public Vector3f getWalkDirection(Actor actor) {
        PhysicsModule module = actor.getModule(PhysicsModule.class);
        if (module != null) {
            return module.getWalkDirection();
        }
        throw new UnsupportedOperationException("No ActorPhysicsControl set!");
    }
    
    @Override
    public void playAnim(Actor actor, String animName, LoopMode loop, float useTime, float startTime, String... channelIds) {
        ChannelModule module = actor.getModule(ChannelModule.class);
        if (module != null) {
            module.playAnim(animName, loop, useTime, startTime, channelIds);
        }
        throw new UnsupportedOperationException("");
    }
    
    @Override
    public void setChannelLock(Actor actor, boolean locked, String... channelIds) {
        ChannelModule module = actor.getModule(ChannelModule.class);
        if (module != null) {
            module.setChannelLock(locked, channelIds);
        }
        throw new UnsupportedOperationException("");
    }

    @Override
    public void restoreAnimation(Actor actor, String animName, LoopMode loop, float useTime, float startTime, String... channelIds) {
        ChannelModule module = actor.getModule(ChannelModule.class);
        if (module != null) {
            module.restoreAnimation(animName, loop, useTime, startTime, channelIds);
        }
        throw new UnsupportedOperationException("");
    }

    @Override
    public boolean reset(Actor actor) {
        ChannelModule module = actor.getModule(ChannelModule.class);
        if (module != null) {
            module.reset();
            return true;
        }
        return false;
    }

    @Override
    public void resetToAnimationTime(Actor actor, String animation, float timePoint) {
        ChannelModule module = actor.getModule(ChannelModule.class);
        if (module != null) {
            module.resetToAnimationTime(animation, timePoint);
        }
    }
    
    @Override
    public boolean isPlayer(Actor actor) {
        return actor.getData().isPlayer();
    }

    @Override
    public void setPlayer(Actor actor, boolean player) {
        actor.getData().setPlayer(player);
    }
    
    @Override
    public float getViewAngle(Actor actor, Vector3f position) {
        // 优化性能
        TempVars tv = TempVars.get();
        Vector3f view = tv.vect1.set(getViewDirection(actor)).normalizeLocal();
        Vector3f dir = tv.vect2.set(position).subtractLocal(actor.getSpatial().getWorldTranslation()).normalizeLocal();
        float dot = dir.dot(view);
        float angle = 90;
        if (dot > 0) {
            angle = (1.0f - dot) * 90;
        } else if (dot < 0) {
            angle = -dot * 90 + 90;
        } else {
//            angle = 90;
        }
        tv.release();
        return angle;
    }

    @Override
    public float getMass(Actor actor) {
        PhysicsModule module = actor.getModule(PhysicsModule.class);
        return module != null ? module.getMass() : 0;
    }

    @Override
    public boolean isKinematic(Actor actor) {
        PhysicsModule module = actor.getModule(PhysicsModule.class);
        return module != null ? module.isKinematic() : false;
    }

    @Override
    public void setKinematic(Actor actor, boolean kinematic) {
        PhysicsModule module = actor.getModule(PhysicsModule.class);
        if (module != null) {
            module.setKinematic(kinematic);
        }
    }
    
    private BaseModule getActorBaseControl(Actor actor) {
        return actor.getModule(BaseModule.class);
    }

    @Override
    public float distance(Actor actor, Actor target) {
        return actor.getSpatial().getWorldTranslation().distance(target.getSpatial().getWorldTranslation());
    }

    @Override
    public float distanceSquared(Actor actor, Actor target) {
        return actor.getSpatial().getWorldTranslation().distanceSquared(target.getSpatial().getWorldTranslation());
    }

    @Override
    public float distance(Actor actor, Vector3f position) {
        return actor.getSpatial().getWorldTranslation().distance(position);
    }

}
