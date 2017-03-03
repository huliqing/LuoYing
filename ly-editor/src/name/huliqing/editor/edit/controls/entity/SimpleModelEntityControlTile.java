/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.controls.entity;

import com.jme3.app.Application;
import com.jme3.asset.ModelKey;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
import java.io.File;
import name.huliqing.editor.constants.UserDataConstants;
import name.huliqing.editor.edit.SaveAction;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.utils.TerrainUtils;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.TerrainEntity;
import name.huliqing.luoying.object.module.ActorModule;
import name.huliqing.luoying.object.scene.Scene;

/**
 * 普通模型的操作物体，直接操作实体本身的模型节点, 使用这类操作物体时必须确保实体存在网格模型，
 * 否则物体将无法通过鼠标来选择。
 * @author huliqing
 */
public class SimpleModelEntityControlTile extends EntityControlTile<Entity> implements SaveAction {

//    private static final Logger LOG = Logger.getLogger(SimpleModelEntityControlTile.class.getName());

    private SimpleJmeEdit edit;
    
    @Override
    public Spatial getControlSpatial() {
        return target.getSpatial();
    }

    @Override
    public void initialize(SimpleJmeEdit edit) {
        super.initialize(edit); 
        updateState();
        this.edit = edit;
        this.edit.addSaveAction(this);
    }

    @Override
    public void cleanup() {
        edit.removeSaveAction(this);
        super.cleanup(); 
    }

    @Override
    public void reloadEntity(Scene scene) {
        checkToSaveTerrain();
        super.reloadEntity(scene);
    }
    
     /**
     * 如果是地形，则需要判断地形是否经过修改，如果已经修改则需要在实体重新载入之前或编辑器保存之前进行保存。
     * @param ect 
     */
    private void checkToSaveTerrain() {
        if (!(target instanceof TerrainEntity)) {
            return;
        }
        if (!(target.getSpatial() instanceof Terrain)) {
            return;
        }
        // 在清理之前先把spatial取出,这样不会在entity清理的时候被一同清理掉
        Spatial terrainSpatial = target.getSpatial();
         // 判断地形贴图是否有修改
        Boolean terrainAlphaModified = terrainSpatial.getUserData(UserDataConstants.EDIT_TERRAIN_MODIFIED_ALPHA);
        // 判断地形是否有修改
        Boolean terrainModified = terrainSpatial.getUserData(UserDataConstants.EDIT_TERRAIN_MODIFIED);
        // 没有任何修改
        if ((terrainAlphaModified == null || !terrainAlphaModified) && (terrainModified == null || !terrainModified)) {
            return;
        }
        
        String terrainFilePathInAssets = target.getData().getAsString("file");
        
        // 对Entity进行清理，需要优先执行，这样可以清理掉各Module给Spatial添加的Control,这样不会在保存地形的时候
        // 把Module所添加的各种Control也保存进去。因为这些Control在Module初始化的时候会重新添加。
        Scene scene = target.getScene();
        EntityData data = target.getData();
        boolean isCleanup = false;
        if (target.isInitialized()) {
            target.cleanup();
            isCleanup = true;
        }

        // 以下是针对地形(Terrain)实体的特别保存操作，这个方法需要在EntityControl重载入的时候进行保存。
        String assetFolder = Manager.getConfigManager().getMainAssetDir();

        // 重新把terrainSpatial更新到缓存(或者删除也可以)，必须的，否则地形的材质不会更新,特别是贴图图层没有更新，
        // 因为缓存中存的仍是旧的,Entity在重新载入的时候会去缓存中获取
        // 注：直接更新缓存比删除性能要好，因删除后再重新载入会导致一次轻微的视觉闪烁。
        // 注：这里必须强制更新一下变换，否则缩放后的地形没有实时更新，导致点选的时候无法正确选择到。
//        terrainSpatial.updateGeometricState();
//        edit.getEditor().getAssetManager().addToCache(new ModelKey(terrainFilePathInAssets), terrainSpatial); 

        edit.getEditor().getAssetManager().deleteFromCache(new ModelKey(terrainFilePathInAssets));

        // 保存贴图修改，然后清除标记,这样下次就不会再需要处理
        if (terrainAlphaModified != null && terrainAlphaModified) {
            terrainSpatial.setUserData(UserDataConstants.EDIT_TERRAIN_MODIFIED_ALPHA, null); 
            TerrainUtils.doSaveAlphaImages((Terrain) terrainSpatial, assetFolder);
        }

        // 保存地形修改，然后清除标记,这样下次就不会再需要处理
        if (terrainModified != null && terrainModified) {
            terrainSpatial.setUserData(UserDataConstants.EDIT_TERRAIN_MODIFIED, null);
            TerrainUtils.saveTerrain(terrainSpatial, new File(assetFolder, terrainFilePathInAssets));
        }
        
        // 如果已经清理，则需要重新载入实体。
        if (isCleanup) {
            target.setData(data);
            target.initialize();
            target.onInitScene(scene);
        }
        
    }

    @Override
    public void updateState() {
        super.updateState(); 
        // 当选择的是地形的时候，,注意：地形在载入的时候需要重新设置材质，使用地形中的所有分块指定同一个材质实例，
        // 否则指定刷到特定的材质上。
        if (target.getSpatial() instanceof Terrain) {
            Terrain terrain = (Terrain) target.getSpatial();
            target.getSpatial().setMaterial(terrain.getMaterial());
        }
    }

    @Override
    protected void onLocationUpdated(Vector3f location) {
        RigidBodyControl control = target.getSpatial().getControl(RigidBodyControl.class);
        if (control != null) {
            control.setPhysicsLocation(location);
        }
        BetterCharacterControl character = target.getSpatial().getControl(BetterCharacterControl.class);
        if (character != null) {
            character.warp(location);
        }
        target.getSpatial().setLocalTranslation(location);
        target.updateDatas();
        notifyPropertyChanged("location", location);
    }

    @Override
    protected void onRotationUpdated(Quaternion rotation) {
        RigidBodyControl control = target.getSpatial().getControl(RigidBodyControl.class);
        if (control != null) {
            control.setPhysicsRotation(rotation);
        }
        // 角色类型使用的不是普通的RigidBodyControl来控制旋转。
        // 需要通过ActorModule来旋转
        ActorModule actorModule = target.getModuleManager().getModule(ActorModule.class);
        if (actorModule != null) {
            actorModule.setRotation(rotation);
        }
        target.getSpatial().setLocalRotation(rotation);
        target.updateDatas();
        notifyPropertyChanged("rotation", rotation);
    }

    @Override
    protected void onScaleUpdated(Vector3f scale) {
        target.getSpatial().setLocalScale(scale);
        target.updateDatas();
        notifyPropertyChanged("scale", target.getSpatial().getLocalScale());
    }

    @Override
    public void doSave(Application application) {
        checkToSaveTerrain();
    }
    
}
