/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.scene;

import com.jme3.export.binary.BinaryExporter;
import com.jme3.input.KeyInput;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.Editor;
import name.huliqing.editor.edit.SaveAction;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;
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
    private static final Logger LOG = Logger.getLogger(SceneEdit.class.getName());
    private final PlayService playService = Factory.get(PlayService.class);
    private final JfxSceneEdit jfxEdit;
    
    private final Toolbar extTerrainTools = new TerrainToolbar(this);

    private Game game;
    private Scene scene;
    private boolean sceneLoaded;
    
    // 保存当前实体数据列表与ControlTile的匹配关系
    private final Map<EntityData, EntityControlTile> objMap = new LinkedHashMap<EntityData, EntityControlTile>();
    
    private final JmeEvent delEvent = new JmeEvent("delete");
    private final JmeEvent duplicateEvent = new JmeEvent("duplicate");
    
    // 保存路径，绝对路径,包含文件名和后缀，如：c:\....\xxxScene.ying
    private String savePath;
    
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

    @Override
    protected void doSaveEdit() {
        if (savePath == null) {
            throw new NullPointerException("Need to set savePath before save edit!");
        }
        try {
            // 先保存一些特殊的”保存行为“, 这些保存操作需要在地形保存之前优先进行保存操作.
            // 如地形在修改后的特殊的保存行为
            if (!saveActions.isEmpty()) {
                for (SaveAction sa : saveActions) {
                    sa.doSave(editor);
                }
            }
            // 场景保存
            scene.updateDatas();
            BinaryExporter.getInstance().save(scene.getData(), new File(savePath));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 设置保存路径，绝对路径，包含完整的文件名及后缀名，如: c:\xxxx\xxxScene.ying
     * @param savePath 
     */
    public void setSavePath(String savePath) {
        this.savePath = savePath;
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
            ect.reloadEntity(scene);
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
        setModified(true);
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
        setModified(true);
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
        setModified(true);
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

        private final EntityControlTile<Entity> ect;
        
        public EntityRemovedUndoRedo(EntityControlTile ect) {
            this.ect = ect;
        }
        
        @Override
        public void undo() {
            ect.getTarget().setData(ect.getTarget().getData()); // 重新更新一次数据，优先
            objMap.put(ect.getTarget().getData(), ect);
            scene.addEntity(ect.getTarget());
            addControlTile(ect);
        }

        @Override
        public void redo() {
            ect.getTarget().updateDatas();
            objMap.remove(ect.getTarget().getData());
            scene.removeEntity(ect.getTarget());
            removeControlTile(ect);
        }
        
    }
}
