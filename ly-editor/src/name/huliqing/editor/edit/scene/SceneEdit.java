/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.scene;

import com.jme3.input.KeyInput;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import name.huliqing.editor.Editor;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.select.EntityControlTile;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.manager.ControlTileManager;
import name.huliqing.editor.tools.base.MoveTool;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.editor.toolbar.TerrainToolbar;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.manager.PickManager;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.game.Game;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.scene.SceneListener;
import name.huliqing.luoying.xml.DataFactory;

/**
 *
 * @author huliqing
 */
public class SceneEdit extends SimpleJmeEdit implements SceneListener {

//    private static final Logger LOG = Logger.getLogger(SceneEdit.class.getName());
    private final PlayService playService = Factory.get(PlayService.class);
    private final JfxSceneEdit jfxEdit;
    
    private final Toolbar extTerrainTools = new TerrainToolbar(this);

    private Game game;
    private Scene scene;
    private boolean sceneLoaded;
    
    private final Map<EntityData, EntityControlTile> objMap = new LinkedHashMap<EntityData, EntityControlTile>();
    
    private final JmeEvent delEvent = new JmeEvent("delete");
    private final JmeEvent duplicateEvent = new JmeEvent("duplicate");
    
    public SceneEdit(JfxSceneEdit jfxEdit) {
        this.jfxEdit = jfxEdit;
    }

    @Override
    protected List<Toolbar> createExtToolbars() {
        List<Toolbar> tbs = new ArrayList();
        tbs.add(extTerrainTools);
        return tbs;
    }
    
    @Override
    public void initialize(Editor editor) {
        super.initialize(editor);
        
        // 删除操作事件
        delEvent.bindKey(KeyInput.KEY_X, true);
        delEvent.addListener((Event e) -> {
            if (e.isMatch() && (selectObj instanceof EntityControlTile)) {
                Vector2f cursorPos = editor.getInputManager().getCursorPosition();
                Jfx.runOnJfx(() -> {
                    jfxEdit.showDeleteConfirm(cursorPos.x, editor.getCamera().getHeight() - cursorPos.y, (EntityControlTile) selectObj);
                });
            }
        });
        
        // 复制操作
        duplicateEvent.bindKey(KeyInput.KEY_D, true).bindKey(KeyInput.KEY_LSHIFT, true);
        duplicateEvent.addListener((Event e) -> {
            if (e.isMatch() && (selectObj instanceof EntityControlTile)) {
                Entity entity = (Entity) selectObj.getTarget();
                entity.updateDatas();
                EntityData cloneData = entity.getData().clone();
                cloneData.setUniqueId(DataFactory.generateUniqueId()); // 需要生成一个新的唯一ID
                addEntity(cloneData);
                setSelected(cloneData);
                // 转到移动工具激活并进行自由移动
                MoveTool moveTool = (MoveTool) toolbar.getTool(MoveTool.class);
                toolbar.setActivated(moveTool, true);
                moveTool.startFreeMove();
            }
        });
        
        delEvent.initialize();
        duplicateEvent.initialize();
    }
    
    @Override
    public void cleanup() {
        delEvent.cleanup();
        duplicateEvent.cleanup();
        if (game != null && game.isInitialized()) {
            game.cleanup();
            game = null;
        }
        sceneLoaded = false;
        super.cleanup(); 
    }
    
    public Scene getScene() {
        return scene;
    }
    
    public void setScene(Scene newScene) {
        if (game != null) {
            game.cleanup();
            game = null;
            objMap.clear();
        }
        sceneLoaded = false;
        scene = newScene;
        scene.addSceneListener(this);
        
        game = Loader.load(IdConstants.SYS_GAME);
        playService.registerGame(game); // game.initialize必须先调用
        
        game.setScene(scene);
        game.initialize(editor);
        getEditRoot().attachChild(game.getScene().getRoot());
    }

    // remove20170202
//    @Override
//    public EntitySelectObj doPick(Ray ray) {
//        if (scene == null)
//            return null;
//        
//        EntitySelectObj result = null;
//        float minDistance = Float.MAX_VALUE;
//        Float temp;
//        for (EntitySelectObj seo : objMap.values()) {
//            temp = seo.distanceOfPick(ray);
//            if (temp != null && temp < minDistance) {
//                minDistance = temp;
//                result = seo;
//            }
//        }
////        if (result != null) {
////            LOG.log(Level.INFO, "doPick, selectObj={0}", result.getObject().getData().getId());
////        }
//        return result;
//    }

    @Override
    public void onSceneLoaded(Scene scene) {
        // 先预生成SelectObjs
        List<Entity> entities = scene.getEntities();
        if (entities != null) {
            entities.stream().filter(t -> t != null).forEach(t -> {
                EntityControlTile eso = ControlTileManager.createEntityControlTile(t.getData().getTagName());
                objMap.put(t.getData(), eso);
                eso.setTarget(t);
                addControlTile(eso);
            });
        }
        sceneLoaded = true;
    }

    @Override
    public void onSceneEntityAdded(Scene scene, Entity entityAdded) {}
    
    @Override
    public void onSceneEntityRemoved(Scene scene, Entity entityRemoved) {}
    
    public void setSelected(EntityData entityData) {
        EntityControlTile eso = objMap.get(entityData);
        if (eso != null) {
            setSelected(eso);
        }
    }
    
    /**
     * 重装载入指定的Entity
     * @param entityData 
     */
    public void reloadEntity(EntityData entityData) {
        if (!sceneLoaded)
            return;
        EntityControlTile<Entity> eso = objMap.get(entityData);
        if (eso != null) {
            eso.getTarget().cleanup();
            eso.getTarget().setData(entityData);
            eso.getTarget().initialize();
            eso.getTarget().onInitScene(scene);
            eso.updateState();
        }
    }
    
    public void addEntity(EntityData ed) {
        if (!sceneLoaded)
            return;
        if (objMap.containsKey(ed)) {
            return;
        }
        Entity entity = Loader.load(ed);
        scene.addEntity(entity);
        EntityControlTile<Entity> eso = ControlTileManager.createEntityControlTile(ed.getTagName());
        objMap.put(ed, eso);
        eso.setTarget(entity);
        addControlTile(eso);
        addUndoRedo(new EntityAddedUndoRedo(eso));
    }
    
    /**
     * 添加物体到场景，cursorPos指定了GUI上的屏幕位置，这个位置会自动转换到3D场景中的位置，
     * 物体即添加到这个3D位置中。转换方式是通过使用射线与场景物体的最近的交叉点计算的，
     * 如果射线不与场景中任何物体产生交叉，则将物体放置在原点处。
     * @param ed
     * @param cursorPos 
     */
    public void addEntityOnCursor(EntityData ed, Vector2f cursorPos) {
        if (!sceneLoaded)
            return;
        if (objMap.containsKey(ed)) {
            return;
        }
        Vector3f loc = PickManager.pick(editor.getCamera(), cursorPos, editRoot);
        if (loc != null) {
            ed.setLocation(loc);
        }
        Entity entity = Loader.load(ed);
        scene.addEntity(entity);
        EntityControlTile<Entity> eso = ControlTileManager.createEntityControlTile(ed.getTagName());
        objMap.put(ed, eso);
        eso.setTarget(entity);
        addControlTile(eso);
        addUndoRedo(new EntityAddedUndoRedo(eso));
    }
    
    public boolean removeEntity(EntityData ed) {
        if (!sceneLoaded) 
            return false;
        EntityControlTile<Entity> eso = objMap.get(ed);
        if (eso == null) {
            return false;
        }
        eso.getTarget().updateDatas();
        scene.removeEntity(eso.getTarget());
        objMap.remove(ed);
        removeControlTile(eso);
        addUndoRedo(new EntityRemovedUndoRedo(eso));
        return true;
    }

    private class EntityAddedUndoRedo implements UndoRedo {
        private final EntityControlTile<Entity> eso;
        public EntityAddedUndoRedo(EntityControlTile eso) {
            this.eso = eso;
        }
        
        @Override
        public void undo() {
            eso.getTarget().updateDatas();
            objMap.remove(eso.getTarget().getData());
            scene.removeEntity(eso.getTarget());
            removeControlTile(eso);
        }

        @Override
        public void redo() {
            eso.getTarget().setData(eso.getTarget().getData()); // 重新更新一次数据，优先
            scene.addEntity(eso.getTarget());
            objMap.put(eso.getTarget().getData(), eso);
            addControlTile(eso);
        }
        
    }
    
    private class EntityRemovedUndoRedo implements UndoRedo {

        private final EntityControlTile<Entity> eso;
        
        public EntityRemovedUndoRedo(EntityControlTile eso) {
            this.eso = eso;
        }
        
        @Override
        public void undo() {
            eso.getTarget().setData(eso.getTarget().getData()); // 重新更新一次数据，优先
            scene.addEntity(eso.getTarget());
            addControlTile(eso);
            objMap.put(eso.getTarget().getData(), eso);
        }

        @Override
        public void redo() {
            eso.getTarget().updateDatas();
            objMap.remove(eso.getTarget().getData());
            scene.removeEntity(eso.getTarget());
            removeControlTile(eso);
        }
        
    }
}
