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
import name.huliqing.editor.edit.controls.ControlTile;
import name.huliqing.editor.toolbar.EntityBrushToolbar;
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
    
    private final Toolbar extTerrainToolbar = new TerrainToolbar(this);
    private final Toolbar extEntityBrushToolbar = new EntityBrushToolbar(this);

    private Game game;
    private Scene scene;
    private boolean sceneLoaded;
    
    // 保存当前实体数据列表与ControlTile的匹配关系
    private final Map<EntityData, EntityControlTile> controlTileMap = new LinkedHashMap<EntityData, EntityControlTile>();
    
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
        tbs.add(extTerrainToolbar);
        tbs.add(extEntityBrushToolbar);
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
                Entity source = (Entity) selectObj.getTarget();
                source.updateDatas();
                
                EntityData cloneData = source.getData().cloneData();
                cloneData.setUniqueId(DataFactory.generateUniqueId()); // 需要生成一个新的唯一ID
                
                EntityControlTile ectCloned = ControlTileManager.createEntityControlTile(cloneData);
                ectCloned.setTarget(Loader.load(cloneData));
                
                EntityAddedUndoRedo eaur = new EntityAddedUndoRedo(ectCloned);
                eaur.redo();
                addUndoRedo(eaur);
                
                setSelected(ectCloned);
                
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
            controlTileMap.clear();
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
                EntityControlTile eso = ControlTileManager.createEntityControlTile(t.getData());
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
        EntityControlTile eso = controlTileMap.get(entityData);
        if (eso != null) {
            setSelected(eso);
        }
    }
    
    /**
     * 重装载入指定的Entity
     * @param entityData 
     */
    public void reloadEntity(EntityData entityData) {
        EntityControlTile<Entity> ect = controlTileMap.get(entityData);
        if (ect != null) {
            ect.reloadEntity(scene);
        }
    }

    @Override
    public void addControlTile(ControlTile ct) {
        if (ct instanceof EntityControlTile) {
            EntityControlTile ect = (EntityControlTile) ct;
            scene.addEntity(ect.getTarget());
            controlTileMap.put(ect.getTarget().getData(), ect);
            setModified(true);
        }
        super.addControlTile(ct); 
    }

    @Override
    public boolean removeControlTile(ControlTile ct) {
        if (ct instanceof EntityControlTile) {
            EntityControlTile ect = (EntityControlTile) ct;
            Entity entity = ect.getTarget();
            entity.updateDatas();
            controlTileMap.remove(entity.getData());
            scene.removeEntity(entity);
            setModified(true);
        }
        return super.removeControlTile(ct);
    }
    
    /**
     * 向场景中添加实体，这个方法会记录历史操作, 注：如果指定的EntityData已经存在于场景中，则该方法将什么也不做。
     * @param ed 
     */
    public void addEntityUseUndoRedo(EntityData ed) {
        if (controlTileMap.containsKey(ed)) {
            return;
        }
        Entity entity = Loader.load(ed);
        EntityControlTile<Entity> ect = ControlTileManager.createEntityControlTile(ed);
        ect.setTarget(entity);
        EntityAddedUndoRedo eaur = new EntityAddedUndoRedo(ect);
        eaur.redo();
        addUndoRedo(eaur);
    }
    
    /**
     * 添加物体到场景，cursorPos指定了GUI上的屏幕位置，这个位置会自动转换到3D场景中的位置，
     * 物体即添加到这个3D位置中。转换方式是通过使用射线与场景物体的最近的交叉点计算的，
     * 如果射线不与场景中任何物体产生交叉，则将物体放置在原点处。
     * @param ed
     * @param cursorPos 
     */
    public void addEntityOnCursorUseUndoRedo(EntityData ed, Vector2f cursorPos) {
        Vector3f loc = PickManager.pickPoint(editor.getCamera(), cursorPos, editRoot);
        if (loc != null) 
            ed.setLocation(loc);
        addEntityUseUndoRedo(ed);
    }
    
    /**
     * 从场景中移除指定EntityData所关联的实体,该方法同时会记录历史记录操作。
     * 如果关联的实体不存在或者删除失败，则该方法会返回false.
     * @param ed
     * @return 
     */
    public boolean removeEntityUseUndoRedo(EntityData ed) {
        EntityControlTile<Entity> ect = controlTileMap.get(ed);
        if (ect == null) {
            return false;
        }
        EntityRemovedUndoRedo erur = new EntityRemovedUndoRedo(ect);
        erur.redo();
        addUndoRedo(erur);
        return true;
    }
    
    /**
     * 通过EntityData来获取场景中的控制物体，如果不存在则返回null.
     * @param ed
     * @return 
     */
    public EntityControlTile getEntityControlTile(EntityData ed) {
        return controlTileMap.get(ed);
    }

    private class EntityAddedUndoRedo implements UndoRedo {
        private final EntityControlTile<Entity> ectAdded;
        public EntityAddedUndoRedo(EntityControlTile ectAdded) {
            this.ectAdded = ectAdded;
        }
        
        @Override
        public void undo() {
            removeControlTile(ectAdded);
        }

        @Override
        public void redo() {
            addControlTile(ectAdded);
        }
    }
    
    private class EntityRemovedUndoRedo implements UndoRedo {

        private final EntityControlTile<Entity> ectRemoved;
        
        public EntityRemovedUndoRedo(EntityControlTile ect) {
            this.ectRemoved = ect;
        }
        
        @Override
        public void undo() {
            addControlTile(ectRemoved);
        }

        @Override
        public void redo() {
            removeControlTile(ectRemoved);
        }
        
    }
}
