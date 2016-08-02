/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skill;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.ActorControl;
import name.huliqing.core.data.ProtoData;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.enums.DataType;
import name.huliqing.core.game.network.ActorNetwork;
import name.huliqing.core.game.network.PlayNetwork;
import name.huliqing.core.game.service.ActorService;
import name.huliqing.core.game.service.PlayService;
import name.huliqing.core.game.service.StateService;
import name.huliqing.core.network.Network;
import name.huliqing.core.game.service.ConfigService;
import name.huliqing.core.game.service.EffectService;
import name.huliqing.core.object.DataFactory;
import name.huliqing.core.object.anim.Anim;
import name.huliqing.core.object.anim.Listener;
import name.huliqing.core.object.anim.MoveAnim;
import name.huliqing.core.object.skill.AbstractSkill;
import name.huliqing.core.utils.GeometryUtils;
import name.huliqing.core.utils.ThreadHelper;

/**
 * 召唤技
 * @author huliqing
 * @param <T>
 */
public class SummonSkill<T extends SkillData> extends AbstractSkill<T> {
//    private final static Logger logger = Logger.getLogger(SummonSkill.class.getName());
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final ConfigService configService = Factory.get(ConfigService.class);
    private final EffectService effectService = Factory.get(EffectService.class);
    
    // 要召唤的角色的ID
    private String summonId;
    
    // 召唤的时间插值点，如果为0，则立即召唤。如果为0.5f则表示动作执行到一半时召唤 
    // 该值定义了发起效果的时间
    private float summonPoint = 0.3f;
    // summon的偏移位置该位置是以角色自身坐标为依据
    private Vector3f summonOffset = new Vector3f(0,0,0);
    // 调唤出角色的用时,单位秒
    private float summonTime = 4.0f;
    
    // ---- 内部
    private List<SummonOper> cache = new ArrayList<SummonOper>(1);
    private SummonOper currentSummon;
    
    @Override
    public void setData(T data) {
        super.setData(data); 
        this.summonId = data.getAttribute("summonActorId", summonId);
        this.summonPoint = data.getAsFloat("summonPoint", summonPoint);
        this.summonOffset = data.getAsVector3f("summonOffset", summonOffset);
        this.summonTime = data.getAsFloat("summonTime", summonTime);
    }
    
    private SummonOper getOperFromCache() {
//        logger.log(Level.INFO, "SummonOper.size={0}", cache.size());
        SummonOper result = null;
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).isEnd()) {
                result = cache.get(i);
                break;
            }
        }
        if (result == null) { // create new one and cache.
            result = new SummonOper();
            cache.add(result);
        }
        return result;
    }

    @Override
    public void init() {
        super.init();
        
        if (summonId == null) {
//            logger.log(Level.INFO, "No summonActorId set!");
            cleanup();
            return;
        }
        
        // 提前一点点时间载入角色
        currentSummon = getOperFromCache();
        currentSummon.summonObjectId = summonId;
        currentSummon.preload();
        
        playService.addObject(currentSummon);
    }

    @Override
    protected void doUpdateLogic(float tpf) {
        if (summonId == null) {
            cleanup();
            return;
        }
        
        if (!currentSummon.started) {
            if ((time / trueUseTime) >= summonPoint) {
                currentSummon.start();
            }
        }
    }
    
    @Override
    protected void playEffect(String effectId) {
        // 计算召唤点，注：召唤点的计算和效果的执行放在一起，以避免效果显示位置和
        // 召唤点不一致的问题，因为召唤角色的时间和播放效果的时间点时不一致的。
        // 当效果播放后角色可能转向，角色可能发生转向，如果后续再计算召唤点就会出
        // 现召唤位置和效果位置不一致的奇怪现象
        actorService.getLocalToWorld(actor, summonOffset, currentSummon.summonPos);
        currentSummon.summonPos.setY(playService.getTerrainHeight(currentSummon.summonPos.x, currentSummon.summonPos.z));
        currentSummon.summonPos.setY(currentSummon.summonPos.y + summonOffset.y);
        currentSummon.setLocalTranslation(currentSummon.summonPos);
        
        // remove20160516,不再需要这样执行效果
//        // 执行效果
//        Effect effect = effectService.loadEffect(effectId);
//        effect.setTraceObject(actor.getModel());
//        AbstractEffect ae = (AbstractEffect) effect;
//        Vector3f loc = ae.getLocalTranslation();
//        loc.setY(playService.getTerrainHeight(loc.x, loc.z));
//        if (ae.getTracePositionOffset() != null) {
//            loc.addLocal(0, ae.getTracePositionOffset().y, 0);
//        }
//        ae.setLocalTranslation(loc);
//        playService.addEffect(effect);
        
        super.playEffect(effectId);
    }
    
    @Override
    public void cleanup() {
        // 召唤完成之后要清除掉summonId,否则下次点击该技能时又可以召唤
        // 会造成只要召唤一次后就可以无限召唤的BUG。
        summonId = null;
        super.cleanup();
    }
    
    // ----
    
    /**
     * @see #setSummonActorId(java.lang.String) 
     * @return 
     */
    public String getSummonActorId() {
        return summonId;
    }
    
    /**
     * 设置要召唤的角色的ID
     * @param summonActorId 
     */
    public void setSummonActorId(String summonActorId) {
        this.summonId = summonActorId;
    }

    /**
     * @see #setSummonPoint(float) 
     * @return 
     */
    public float getSummonPoint() {
        return summonPoint;
    }

    /**
     * 召唤的时间插值点，如果为0，则立即召唤。如果为0.5f则表示动作执行到一半时召唤 
     * 该值定义了发起效果的时间
     * @param summonPoint 
     */
    public void setSummonPoint(float summonPoint) {
        this.summonPoint = summonPoint;
    }
    
    // 负责召唤的工具类，Node
    private class SummonOper extends Node implements Listener {
        // 召唤的物品ID:May actor or obj.
        String summonObjectId;
        Vector3f summonPos = new Vector3f();
        HelpLoader loader = new HelpLoader();
        Future<Actor> future;
        
        // -- state
        boolean started;
        boolean init;
        float time;// 当前用时
        
        // 是否已经在载入角色
        boolean loadStarted;
        // 载入的角色，可能是actor或obj
        Actor summonActor;
        // 是否正在召出角色
        boolean showing;
        // 用于显示角色的动画处理器
        MoveAnim showAnim = new MoveAnim();
        
        SummonOper() {
            // remove20160504,不要设置boundFactor，这会让角色往下掉
//            showAnim.setBoundFactor(0.2f);
            showAnim.addListener(this);
        }
        
        void start() {
            this.started = true;
        }
        
        /**
         * 提前载入目标,提高性能
         */
        void preload() {
            if (!loadStarted && summonObjectId != null) {
                ProtoData data = DataFactory.createData(summonObjectId);
                loader.loadId = summonObjectId;
                loader.type = data.getDataType();
                future = ThreadHelper.submit(loader);
                loadStarted = true;
            }
        }
        
        void doInit() {
            this.showAnim.setUseTime(summonTime);
        }

        @Override
        public void updateLogicalState(float tpf) {
            super.updateLogicalState(tpf);
            
            if (!started) {
                return;
            }
            if (!init) {
                doInit();
                init = true;
            }
            
            if (!loadStarted) {
                preload();
            }
            
            time += tpf;
            
            if (future != null && future.isDone()) {
                try {
                    summonActor = future.get();
                    future = null;
                } catch (Exception ex) {
                    Logger.getLogger(SummonSkill.class.getName()).log(Level.SEVERE
                            , "Unload object, summonObjectId=" + summonObjectId, ex);
                    cleanup();
                    return;
                }
            }
            
//            logger.log(Level.INFO, "summon time={0}, showTime={1}, showing={2}", new Object[] {time, showTime, showing});
            // 开始展示角色。
            if (!showing && summonActor != null) {
                TempVars tv = TempVars.get();
                
                // 设置同伴等级
                actorService.setLevel(summonActor, (int) (actorService.getLevel(actor) * configService.getSummonLevelFactor()));
                actorService.setPhysics(summonActor, false);
                actorService.setPartner(actor, summonActor);
                actorService.setColor(summonActor, new ColorRGBA(1f, 1f, 2f, 1));
                playNetwork.addActor(summonActor);
                
                // 动画展示召唤角色，上升动画,end位置要向上加大一些，以避免召唤后角色在
                // 开启物理特性后掉到地下。
                Vector3f start = tv.vect1.set(summonPos).subtractLocal(
                        0, GeometryUtils.getModelHeight(summonActor.getModel()), 0);
                Vector3f end = tv.vect2.set(summonPos).addLocal(0, 0.5f, 0);
                summonActor.setLocation(start);
                summonActor.faceTo(tv.vect3.set(actor.getModel().getWorldTranslation()).setY(start.getY()));
                summonActor.getModel().addControl(showAnim);
                showAnim.setStartPos(start);
                showAnim.setEndPos(end);
                showAnim.start();
                
//                if (Config.debug) {
//                    logger.log(Level.INFO, "summon start to showing!");
//                }
                
                showing = true;
                tv.release();
            }
            
            if (summonActor != null && !showAnim.isEnd()) {
                Network.getInstance().syncTransformDirect(summonActor);
            }
        }
        
        void cleanup() {
            this.started = false;
            this.init = false;
            this.summonActor = null;
            this.summonObjectId = null;
            this.showing = false;
            this.loadStarted = false;
            this.time = 0;
            if (future != null) {
                future.cancel(true);
            }
            this.future = null;
            this.removeFromParent();
        }
        
        boolean isEnd() {
            return !started && showAnim.isEnd();
        }

        @Override
        public void onDone(Anim anim) {
            MoveAnim ma = (MoveAnim) anim;
            Spatial summonModel = ma.getTarget();
            if (summonModel != null) {
                // 让召唤到的目标获得物理碰撞
                ActorControl ac = summonModel.getControl(ActorControl.class);
                ac.setLocation(summonModel.getLocalTranslation());
                actorService.setAutoAi(ac, true);
                actorNetwork.setPhysics(ac, true);// 物理开需要同步
                
                // 释放showAnim以便重用
                summonModel.removeControl(ma);
            }
            cleanup();
        }
    }
    
    // help to call
    private class HelpLoader implements Callable<Actor> {

        DataType type;
        String loadId;
        /**
         * 是否正在载入中...
         */
        boolean calling;
        
        @Override
        public Actor call() throws Exception {
            calling = true;
            Actor result = actorService.loadActor(loadId);
            calling = false;
            return result;
        }
    }
}
