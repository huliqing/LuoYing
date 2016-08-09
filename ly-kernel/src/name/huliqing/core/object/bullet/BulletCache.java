/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.bullet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Config;
import name.huliqing.core.state.GameState.PlayListener;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.object.IntervalLogic;

/**
 *
 * @author huliqing
 */
public class BulletCache extends IntervalLogic implements PlayListener {
    private static final Logger LOG = Logger.getLogger(BulletCache.class.getName());
    private final static BulletCache ins = new BulletCache();
    
    // 空闲列表
    private List<Bullet> freeStore = new LinkedList<Bullet>();
    // 忙碌的列表,正在运行的子弹
    private List<Bullet> busyStore = new LinkedList<Bullet>();
    
    private BulletCache() {
        super(10); // 频率不用太高
    }
    
    public static BulletCache getInstance() {
        return ins;
    }
    
    public Bullet getBullet(String bulletId) {
        // 先偿试从缓存中取
        if (!freeStore.isEmpty()) {
            for (Iterator<Bullet> it = freeStore.iterator(); it.hasNext(); ) {
                Bullet bullet = it.next();
                if (bullet.getData().getId().equals(bulletId)) {
                    it.remove();
                    return bullet;
                }
            }
        }
        // 载入一个新的
        return Loader.loadBullet(bulletId);
    }
    
    @Override
    protected void doLogic(float tpf) {
        // 检测busy列表中是否存在已经结束的子弹，
        // 如果已经有结束的则添加到空闲列表中(子弹移出场景的动作由子弹内部处理)
        for (Iterator<Bullet> it = busyStore.iterator(); it.hasNext(); ) {
            Bullet bullet = it.next();
            
            
//            if (bullet()) {
//                it.remove();
//                freeStore.add(bullet);
//            }

            throw new UnsupportedOperationException("");
        }
        
        if (Config.debug) {
            LOG.log(Level.INFO, "BulletCache, busy bullets={0}, free bullets={1}"
                    , new Object[] {busyStore.size(), freeStore.size()});
        }
    }

    @Override
    public void onObjectAdded(Object object) {
        if (!(object instanceof Bullet))
            return;
        // 如果场景中添加了bullet，则把它存入busy列表。
        Bullet bb = (Bullet) object;
        if (!busyStore.contains(bb)) {
            busyStore.add(bb);
        }
    }

    @Override
    public void onObjectRemoved(Object spatial) {
        // 子弹移除时不判断，因为子弹可能会自行脱离场景。即不经过playState的removeObject.
    }

    @Override
    public void onExit() {
        freeStore.clear();
        busyStore.clear();
    }
    
}
