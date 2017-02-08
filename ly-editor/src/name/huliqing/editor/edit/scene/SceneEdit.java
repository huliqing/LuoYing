/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.scene;

import com.jme3.app.Application;
import com.jme3.asset.ModelKey;
import com.jme3.input.KeyInput;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import name.huliqing.editor.Editor;
import name.huliqing.editor.constants.UserDataConstants;
import name.huliqing.editor.edit.SaveAction;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.manager.ControlTileManager;
import name.huliqing.editor.tools.base.MoveTool;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.toolbar.TerrainToolbar;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.utils.TerrainUtils;
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
                toolbar.setEnabled(moveTool, true);
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
        EntityControlTile<Entity> ect = objMap.get(entityData);
        if (ect != null) {
            
            // 在清理之前先把spatial取出,这样不会在entity清理的时候被一同清理掉
            Spatial terrainSpatial = ect.getTarget().getSpatial();
            String terrainFilePathInAssets = ect.getTarget().getData().getAsString("file");
            
            
            // 对Entity进行清理，需要优先执行，这样可以清理掉各Module给Spatial添加的Control,这样不会在保存地形的时候
            // 把Module所添加的各种Control也保存进去。因为这些Control在Module初始化的时候会重装添加。
            ect.getTarget().cleanup();
            
            // 一些控制物体在清理并重新载入之前需要进行保存.
            // 如, 地形文件(Terrain)，因为地形文件可能编辑后还没有进行过保存, 因为地形文件的修改数据不是保存在EntityData中的，
            // 则需要在重装载入之前进行保存j3o文件，然后重新载入的时候才不会丢失地形数据及alpha等修改数据
            // see -> SimpleModelEntityControlTile.java
            doSaveTerrain(terrainSpatial, terrainFilePathInAssets);
            
            // 重新载入实体
            ect.getTarget().setData(entityData);
            ect.getTarget().initialize();
            ect.getTarget().onInitScene(scene);
            ect.updateState();
        }
    }
    
    /**
     * 保存地形修改
     * @param ect 
     */
    private void doSaveTerrain(Spatial terrainSpatial, String filePathInAssets) {
        if (!(terrainSpatial instanceof Terrain)) 
            return;
        
        // 以下是针对地形(Terrain)实体的特别保存操作，这个方法需要在EntityControl重载入的时候进行保存。
        String assetFolder = Manager.getConfigManager().getMainAssetDir();

        // 重新把terrainSpatial更新到缓存(或者删除也可以)，必须的，否则地形的材质不会更新,特别是贴图图层没有更新，
        // 因为缓存中存的仍是旧的,Entity在重新载入的时候会去缓存中获取。
        editor.getAssetManager().addToCache(new ModelKey(filePathInAssets), terrainSpatial);

        // 保存贴图修改
        Boolean terrainAlphaModified = terrainSpatial.getUserData(UserDataConstants.EDIT_TERRAIN_MODIFIED_ALPHA);
        if (terrainAlphaModified != null && terrainAlphaModified) {
            terrainSpatial.setUserData(UserDataConstants.EDIT_TERRAIN_MODIFIED_ALPHA, null); // clear
            TerrainUtils.doSaveAlphaImages((Terrain) terrainSpatial, assetFolder);
        }

        // 保存地形修改
        Boolean terrainModified = terrainSpatial.getUserData(UserDataConstants.EDIT_TERRAIN_MODIFIED);
        if (terrainModified != null && terrainModified) {
            terrainSpatial.setUserData(UserDataConstants.EDIT_TERRAIN_MODIFIED, null); // clear
            TerrainUtils.saveTerrain(terrainSpatial, new File(assetFolder, filePathInAssets));
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
