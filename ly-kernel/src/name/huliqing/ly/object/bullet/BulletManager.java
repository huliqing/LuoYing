/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.bullet;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author huliqing
 */
public class BulletManager extends AbstractAppState {
    
    private final static BulletManager INSTANCE = new BulletManager();
    
    private final List<Bullet> initializing = new ArrayList<Bullet>();
    
    private Node bulletRoot;
    private final SafeArrayList<Bullet> runningBullets = new SafeArrayList<Bullet>(Bullet.class);
    
    /**
     * Listener用来监听子弹被添加或移除出场景。
     */
    public interface Listener {
        
        /**
         * 当某个子弹被添加到场景后该方法被调用，每一个被BulletManager管理的子弹在被添加到场景后都会调用一次。
         * @param bullet
         */
        void onBulletAdded(Bullet bullet);
        
        /**
         * 当某个子弹被移出场景后该方法被调用,每一个被BulletManager管理的子弹在被移出场景后该方法都会调用一次。
         * @param bullet 
         */
        void onBulletRemoved(Bullet bullet);
    }
    
    private List<Listener> listeners;
    
    private BulletManager() {}
    
    public static BulletManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        super.initialize(stateManager, app);
        if (bulletRoot == null) {
            if (app instanceof SimpleApplication) {
                bulletRoot = new Node("bulletRoot");
                ((SimpleApplication) app).getRootNode().attachChild(bulletRoot);
            } else {
                throw new IllegalStateException("BulletManager need a parent node to attach bulletRoot node, but this"
                        + " application type could not support! or esle you can specify a custom bulletRoot before the "
                        + "BulletManager initialize. Current application=" + app.getClass());
            }
        }
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        synchronized (initializing) {
            if (!initializing.isEmpty()) {
                for (int i = 0; i < initializing.size(); i++) {
                    Bullet bAdded = initializing.get(i);
                    runningBullets.add(bAdded);
                    bulletRoot.attachChild(bAdded);
                    if (!bAdded.isInitialized()) {
                        bAdded.initialize();
                    }
                    notifyAddedListeners(bAdded);
                }
                initializing.clear();
            }
        }
        
        // 检查并移除已经执行完的子弹
        if (!runningBullets.isEmpty()) {
            for (Bullet bRemoved : runningBullets.getArray()) {
                if (bRemoved.isConsumed()) {
                    removeBullet(bRemoved);
                    notifyRemovedListeners(bRemoved);
                }
            }
        }
    }
    
    private void notifyAddedListeners(Bullet bullet) {
        if (listeners != null) {
            for (Listener l : listeners) {
                l.onBulletAdded(bullet);
            }
        }
    }
    
    private void notifyRemovedListeners(Bullet bullet) {
        if (listeners != null) {
            for (Listener l : listeners) {
                l.onBulletRemoved(bullet);
            }
        }
    }

    @Override
    public void cleanup() {
        synchronized (initializing) {
            for (Bullet e : initializing) {
                if (e.isInitialized()) {
                    e.cleanup();
                }
            }
            initializing.clear();
        }
        
        // 清理bulletRoot下的物效。
        for (Bullet e : runningBullets.getArray()) {
            e.cleanup();
        }
        runningBullets.clear();
        
        // 注：如果bulletRoot下添加了其它类型的节点，则会一起被清理掉。
        bulletRoot.detachAllChildren();
    }

    public Node getBulletRoot() {
        return bulletRoot;
    }

    public void setBulletRoot(Node bulletRoot) {
        this.bulletRoot = bulletRoot;
    }

    public boolean addBullet(Bullet bullet) {
        synchronized (initializing) {
            if (!initializing.contains(bullet) && bullet.getParent() != bulletRoot) {
                initializing.add(bullet);
                return true;
            }
            return false;
        }
    }
    
    /**
     * 将指定的子弹移除出场景，指定的子弹必须是被BulletManager所管理的（通过addBullet(Bullet)添加到场景中的）。
     * 如果子弹不是被BulletManager所管理，则该方法什么也不做，并且返回false.
     * @param bullet
     * @return 
     */
    public boolean removeBullet(Bullet bullet) {
        // 从运行时
        if (bullet.getParent() == bulletRoot) {
            bullet.cleanup();
            bullet.removeFromParent();
            runningBullets.remove(bullet);
            return true;
        }
        
        synchronized (initializing) {
            if (initializing.remove(bullet)) {
                bullet.cleanup();
                bullet.removeFromParent();
                return true;
            }
        }
        
        return false;
    }
    
    public void addListener(Listener listener) {
        if (listeners == null) {
            listeners = new ArrayList<Listener>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public boolean removeListener(Listener listener) {
        return listeners != null && listeners.remove(listener);
    }
}
