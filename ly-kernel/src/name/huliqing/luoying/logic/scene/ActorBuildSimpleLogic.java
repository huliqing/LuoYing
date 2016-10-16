/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.logic.scene;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.IntervalLogic;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.utils.ThreadHelper;

/**
 * 简单的刷怪功能,可指定要刷新的角色id,位置及间隔刷新时间。
 * 当目标角色死亡并被移除出场景后，在达到指定间隔时间之后该角色会重新在指定
 * 地点刷新。
 * @author huliqing
 */
public class ActorBuildSimpleLogic extends IntervalLogic {
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    
    public interface Callback {
        /**
         * 当角色载入完成时调用,该方法的调用在角色已经载入内存，并准备放入场
         * 景之前。
         * @param actor 
         */
        void onload(Entity actor);
    }
    
    private List<ActorBuilder> datas = new ArrayList<ActorBuilder>();
    private boolean enabled = true;
    
    public ActorBuildSimpleLogic() {
        super(3);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @param position
     * @param actorIds
     * @param interval
     * @see #addBuilder(com.jme3.math.Vector3f, java.lang.String[]
     * , float, name.huliqing.fighter.logic.scene.ActorBuildSimpleLogic.Callback) 
     */
    public void addBuilder(Vector3f position, String[] actorIds, float interval) {
        datas.add(new ActorBuilder(position, actorIds, interval, null));
    }
    
    /**
     * 添加一个角色刷新地点。
     * @param position 要刷新角色的地点
     * @param actorIds 可用于刷新的id列表，刷新时将会从这个列表中随机获取ID进行载入
     * @param interval 刷新的间隔，单位秒，是指角色死亡并被移出场景后一定时间。
     * @param callback 回调函数，该回调方法会在角色载入内存，并在添加到场景之前调用。
     */
    public void addBuilder(Vector3f position, String[] actorIds, float interval, Callback callback) {
        datas.add(new ActorBuilder(position, actorIds, interval, callback));
    }

    @Override
    protected void doLogic(float tpf) {
        if (!enabled) {
            return;
        }
        
        for (ActorBuilder data : datas) {
            if (data.actor != null) {
                if (data.actor.getScene() != null) {
                    // 角色还在场景中，不管是否死亡都不应该考虑刷新
                    continue;
                } else if (data.deadTime <= 0) {
                    // 如果不在场景中，并且未标记过角色死亡，则标记死亡时间
                    data.deadTime = LuoYing.getGameTime();
                }
            }
            
            // 如果正在载入角色(刷新),则检查并处理
            if (data.isbuilding()) {
                
                data.waitAndCheckBuilding();
            
            // 以下两种情况需要刷新载入角色
            // 1.角色未载入过;
            // 2.死亡时间已经超过刷新时间
            } else if (data.actor == null 
                    || (data.deadTime > 0 && LuoYing.getGameTime() - data.deadTime >= data.interval * 1000)){
                
                data.rebuild();
                
            }
        }
    }
        
    // --
    private class ActorBuilder {

        // 刷新地点
        public Vector3f position;
        
        // 可用于刷新的角色id列且
        public String[] actorIds;
        
        // 生成角色的时间间隔,单位秒
        public float interval;
        
        // 刷新的角色
        public Entity actor;
        
        // 最近角色的死亡时间,0表示未死亡
        public long deadTime;
        
        private Future<Entity> future;
        
        private final Callback callback;
        
        public ActorBuilder(Vector3f position, String[] actorIds, float interval, Callback callback) {
            this.position = position;
            this.actorIds = actorIds;
            this.interval = interval;
            this.callback = callback;
        }
        
        public void rebuild() {
            future = ThreadHelper.submit(new Callable<Entity>() {
                @Override
                public Entity call() throws Exception {
                    return Loader.load(actorIds[FastMath.nextRandomInt(0, actorIds.length - 1)]);
                }
            });
        }
        
        /**
         * 是否正在载入角色中
         * @return 
         */
        public boolean isbuilding() {
            return future != null;
        }
        
        /**
         * 检查并等待角色载入.
         */
        public void waitAndCheckBuilding() {
            if (future.isDone()) {
                try {
                    actor = future.get();
                    
                    // remove0813
//                    actor.setLocation(position);
                    actorService.setLocation(actor, position);

                    if (callback != null) {
                        callback.onload(actor);
                    }
                    playNetwork.addEntity(actor);
                    deadTime = 0;
                    future = null;
                } catch (Exception e) {
                    // 重要：这个异常必须处理。
                    if (future != null) {
                        future.cancel(true);
                    }
                    future = null;
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
                }
            } else if (future.isCancelled()) {
                future = null;
            }
        }
        
    }

}
