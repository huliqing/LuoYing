/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.editor.edit.controls.entity;

import com.jme3.app.Application;
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
import name.huliqing.luoying.UncacheAssetEventListener;
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
        // 地形文件的路径，在assets中
        String terrainFilePathInAssets = target.getData().getAsString("file");
         // 判断地形贴图是否有修改
        Boolean terrainAlphaModified = terrainSpatial.getUserData(UserDataConstants.EDIT_TERRAIN_MODIFIED_ALPHA);
        // 判断地形是否有修改
        Boolean terrainModified = terrainSpatial.getUserData(UserDataConstants.EDIT_TERRAIN_MODIFIED);
        // 没有任何修改
        if ((terrainAlphaModified == null || !terrainAlphaModified) && (terrainModified == null || !terrainModified)) {
            UncacheAssetEventListener.getInstance().removeUncache(terrainFilePathInAssets); // 从缓存过滤中移除，以便可以进行缓存
            return;
        }
        
        // 地形在有修改的时候不能被缓存
        UncacheAssetEventListener.getInstance().addUncache(terrainFilePathInAssets);
        
        // 对Entity进行清理，需要优先执行，这样可以清理掉各Module给Spatial添加的Control,这样不会在保存地形的时候
        // 把Module所添加的各种Control也保存进去。因为这些Control在Module初始化的时候会重新添加。
        Scene scene = target.getScene();
        EntityData data = target.getData();
        boolean isCleanup = false;
        if (target.isInitialized()) {
            target.cleanup();
            isCleanup = true;
        }

        String assetFolder = Manager.getConfigManager().getMainAssetDir();
        
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
            // 重新激活物理特性，当物体不再运动时物理引擎会让它进入睡眠，以节省性能。
            // 这里要确保重新激活，以便在编辑的时候可以重新看到物理效果的影响
            control.activate();
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
            control.activate();
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
        // 当保存的时候要确保地形编辑后的文件有进行保存
        checkToSaveTerrain();
        
        // 更新一次状态，重要：这确保如果是地形模型的话，当地形在重新载入的时候需要重新设置地形的材质，使它们
        // 重新指向一个材质实例，否则在编辑地形，刷地形的时候会有问题。
        // 因为地形在编辑、保存后(地形编辑时不缓存)再重新读取时，地形中的材质不会指向同一个实例。
        updateState();
    }
    
}
