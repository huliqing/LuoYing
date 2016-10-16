/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.logic.scene;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.GameLogicData;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.gamelogic.AbstractGameLogic;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.luoying.utils.ThreadHelper;

/**
 * 场景角色生成器,用于为场景角色刷新功能使用,该刷新器可指定要刷新多少个角色。
 * 当这些角色死亡并被移除出场景之后，角色会重新刷新。
 * 1.可指定刷新哪些角色（随机）,通过ID列表指定可用于刷新的角色ID
 * 2.刷新的角色坐标位置随机，通过坐标列表指定。
 * 3.可指定最多刷新多少个角色,当角色达到指定数量时，刷新器将暂停刷新
 * @author huliqing
 * @param <T>
 */
public class ActorBuildLogic<T extends GameLogicData> extends AbstractGameLogic<T> {
    
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    
    /**
     * 实现模型的载入功能。
     */
    public interface ModelLoader {
        
        /**
         * 实现模型的载入功能,该方法只要实现模型的载入并设置位置即可，注：
         * 该方法在多线程中运行,所以不能在该方法中将model添加到场景。
         * 只要负责载入模型即可。
         * @param actorId
         * @return 
         */
        Entity load(String actorId);
   
    }
    
    public interface Callback {
        
        /**
         * 在角色被添加到场景之前执行，如果返回null,赠什么也不做。否则将角色
         * 添加到场景
         * @param actor
         * @return 
         */
        Entity onAddBefore(Entity actor);
    }
    
    // 模型装载器
    private ModelLoader modelLoader; 
    // 回调，在模型载入完成后，加入场景之前执行
    private Callback callback;
    
    // 可用于生成角色的地点，每次刷新从这些地点中随机选取
    private List<Vector3f> positions;
    
    // 让物体在指定的坐标点的随机半径范围内产生。
    private float radius = 10;
    
    // 可用于生成的物体ID列表。
    private List<String> ids;
    
    // 可刷新的物体的最高数量
    private int total = 10;
    
    // 已经生成的角色的列表
    private final SafeArrayList<Entity> models = new SafeArrayList<Entity>(Entity.class);
    
    // 用于从其它线程载入角色
    private Future<Entity> future;
    
    public ActorBuildLogic() {
        this.interval = 3;
    }

    /**
     * 设置一个自定义的模型载入器，用于拦截处理模型的载入
     * @param modelLoader 
     */
    public void setModelLoader(ModelLoader modelLoader) {
        this.modelLoader = modelLoader;
    }

    /**
     * 设置一个回调对象，用于处理拦截角色的参数设置
     * @param callback 
     */
    public void setCallback(Callback callback) {
        this.callback = callback;
    }
    
    /**
     * 添加可以刷新怪物的地点。
     * @param position 
     */
    public void addPosition(Vector3f... position) {
        if (position == null) {
            return;
        }
        if (positions == null) {
            positions = new ArrayList<Vector3f>(position.length);
        }
        positions.addAll(Arrays.asList(position));
    }
    
    public void addId(String... id) {
        if (id == null) {
            return;
        }
        if (ids == null) {
            ids = new ArrayList<String>(id.length);
        }
        ids.addAll(Arrays.asList(id));
    }
    
    /**
     * 设置半径，设置这个值将允许物体在指定的坐标点随机半径范围内产生。
     * 如果设置为0，则物体将精确的在指定的坐标点产生。
     * @param radius 
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    /**
     * 设置最多可同时存在的角色数量
     * @param total 
     */
    public void setTotal(int total) {
        this.total = total;
    }
    
    @Override
    protected void doLogic(float tpf) {
        if (!enabled) {
            return;
        }
        
        if (future != null && future.isDone()) {
            try {
                // 将模型添加到场景和当前列表。
                Entity actor = future.get();
                actorService.setLocation(actor, getRandomPosition());
                if (callback != null) {
                    actor = callback.onAddBefore(actor);
                }
                // 注：如果callback存在，并onAddBefore返回null,则不载入场景
                if (actor != null) {
                    playNetwork.addEntity(actor);
                    models.add(actor);
                }
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
            future = null;
        }
        
//        Log.get(getClass()).log(Level.INFO, "ActorBuildLogic, actor size={0}", models.size());
        // 1.角色已经不存在战场，则清理出列表。
        if (models.size() >= total) {
            for (Entity s : models.getArray()) {
                if (s.getScene() == null) {
                    models.remove(s);
                }
            }
        }
        
//        // 2.检测并载入模型
//        Logger.get(getClass()).log(Level.INFO, "future={0}, models.size={1}, totalLimit={2} timeUsed={3}"
//                , new Object[] {future, models.size(), total, timeUsed});
        
        if (future == null && models.size() < total) {
            String id = getRandomId();
            future = loadModel(id);
        }
    }
    
    /**
     * 获得一个随机id以用于模型的载入
     * @return 
     */
    private String getRandomId() {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("No ids set!");
        }
        return ids.get(FastMath.nextRandomInt(0, ids.size() - 1));
    }
    
    /**
     * 获得一个坐标位置，以用于生成后的模型的位置
     * @return 
     */
    public Vector3f getRandomPosition() {
        if (positions == null || positions.isEmpty()) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "No positions set! Now use Vector3f.ZERO instead.");
            return Vector3f.ZERO.clone();
        }
        
        Vector3f center = positions.get(FastMath.nextRandomInt(0, positions.size() - 1));
        if (radius <= 0) {
            return center.clone();
        }
        // 生成半径范围内随机点
        return MathUtils.getRandomPosition(center, radius, null);

    }
    
    /**
     * 在多线程中载入模型，一般不需要覆盖该方法。
     * @param id
     * @param position
     * @return 
     */
    private Future loadModel(final String id) {
        Future tempFuture = ThreadHelper.submit(new Callable<Entity>() {
            @Override
            public Entity call() throws Exception {
                if (modelLoader != null) {
                    // 如果指定了特殊的模型载入器，则使用指定的
                    return modelLoader.load(id);
                } else {
                    return Loader.load(id);
                }
            }
        });
        return tempFuture;
    }
    
}
