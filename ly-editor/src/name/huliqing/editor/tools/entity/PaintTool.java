/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.entity;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;
import name.huliqing.editor.edit.scene.SceneEdit;
import name.huliqing.editor.manager.ControlTileManager;
import name.huliqing.editor.toolbar.EntityBrushToolbar;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.TerrainEntity;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.luoying.xml.DataFactory;

/**
 * 实体笔刷工具，这个工具用于将实体源克隆并刷到场景中
 * @author huliqing
 */
public class PaintTool extends AbstractAdjustEntityBrushTool {
    
    // 保存每次操作过程中添加的EntityControlTile列表，以便在操作结束的时候添加到历史记录。
    private final List<EntityControlTile> ectTemps = new ArrayList();
     
    public PaintTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    public void initialize(SimpleJmeEdit edit, EntityBrushToolbar toolbar) {
        super.initialize(edit, toolbar); 
        controlObj.getMaterial().setColor("Color", ColorRGBA.Blue);
    }
    
    @Override
    protected void doAction() {
        float radius = controlObj.getWorldScale().x;
        int density = densityTool.getValue().intValue();
        if (radius <= 0 || density <= 0) {
            return;
        }
        TerrainEntity terrain = getSelectTerrain();
        if (terrain == null) 
            return;
        
        List<EntityData> sourceList = toolbar.getSourceTool().getSources();
        if (sourceList == null || sourceList.isEmpty())
            return;
        
        if (!(edit instanceof SceneEdit)) 
            return;
        
        Vector3f paintWorldLoc = controlObj.getWorldTranslation();
        float minHeight = toolbar.getMinHeight().getValue().floatValue();
        Vector3f locationOffset = toolbar.getLocationOffset().getValue();
        Vector3f scaleAdjustMin = toolbar.getScaleMinAdjust().getValue();
        Vector3f scaleAdjustMax = toolbar.getScaleMaxAdjust().getValue();
        Vector3f rotationAdjustMin = toolbar.getRotationMinAdjust().getValue();
        Vector3f rotationAdjustMax = toolbar.getRotationMaxAdjust().getValue();
        boolean useNormal = toolbar.getUseNormal().getValue();
        
        paint((SceneEdit) edit, terrain, sourceList
                , paintWorldLoc, radius, density, minHeight, useNormal
                , locationOffset, scaleAdjustMin, scaleAdjustMax, rotationAdjustMin, rotationAdjustMax);
    }
    
    @Override
    protected void doEndAction() {
        if (ectTemps.isEmpty())
            return;
        
        PaintUndoRedo pur = new PaintUndoRedo(ectTemps);
        edit.addUndoRedo(pur);
        
        ectTemps.clear(); 
    }
    
    private void paint(SceneEdit sceneEdit, TerrainEntity terrain, List<EntityData> sourceList
            , Vector3f paintWorldLoc, float radius, int density, float minHeight, boolean useNormal
            , Vector3f locationOffset, Vector3f scaleAdjustMin, Vector3f scaleAdjustMax, Vector3f rotationAdjustMin, Vector3f rotationAdjustMax) {
        int sourceSize = sourceList.size();
        TempVars tv = TempVars.get();
        Vector3f scaleAdjust = tv.vect2;
        Vector3f rotAdjustInAngle = tv.vect3;
        Quaternion rotAdjust = tv.quat1;
        Quaternion normalRotAdjust = tv.quat2;
        
        Vector3f loc = tv.vect1;
        for (int i = 0; i < density; i++) {
            // 在笔刷半径范围内随机取一点
            MathUtils.getRandomPositionInXZPlane(radius, loc);
            loc.addLocal(paintWorldLoc);
            
            CollisionResults crs =terrain.getHeightPoint(loc.x, loc.z);
            if (crs.size() <= 0)
                continue;
            
            // 把这一点投射到地面上,如果这一点不在地面范围内或者在限制的minHeight下，则略过
            CollisionResult collisionResult = crs.getClosestCollision();
            Vector3f locOnTerrain = collisionResult.getContactPoint();
            if (locOnTerrain.y < minHeight) {
                continue;
            }
            
            EntityData source = sourceList.get(FastMath.nextRandomInt(0, sourceSize - 1));
            EntityData cloneData = source.clone();
            cloneData.setUniqueId(DataFactory.generateUniqueId());
            
            // location
            locOnTerrain.addLocal(locationOffset);
            cloneData.setLocation(locOnTerrain);
            
            // rotation
            Quaternion rotation = cloneData.getRotation();
            if (rotation == null) {
                rotation = new Quaternion();
            }
            if (useNormal) {
                Vector3f locNormal = collisionResult.getContactNormal();
                normalRotAdjust.lookAt(new Vector3f(0, 0, 1), locNormal);
                rotation.multLocal(normalRotAdjust);
            }
            rotAdjustInAngle.set(rotationAdjustMin).interpolateLocal(rotationAdjustMax, FastMath.nextRandomFloat());
            rotAdjust.fromAngles(rotAdjustInAngle.x, rotAdjustInAngle.y, rotAdjustInAngle.z);
            rotation.multLocal(rotAdjust);
            cloneData.setRotation(rotation);
            
            // scale
            Vector3f scale = cloneData.getScale();
            if (scale == null) {
                scale = new Vector3f(1,1,1);
            }
            scaleAdjust.set(scaleAdjustMin).interpolateLocal(scaleAdjustMax, FastMath.nextRandomFloat());
            scale.multLocal(scaleAdjust);
            cloneData.setScale(scale);
            
            // 转化为EntityControlTile, 然后添加到场景，并记录在临时列表ectTemps中，在当次操作完后记录到历史记录中。
            EntityControlTile ect = ControlTileManager.createEntityControlTile(cloneData);
            ect.setTarget(Loader.load(cloneData));
            sceneEdit.addControlTile(ect);
            ectTemps.add(ect);
        }
        tv.release();
    }
    
    private class PaintUndoRedo implements UndoRedo {
        
        private final List<EntityControlTile> ectAddeds = new ArrayList();

        public PaintUndoRedo(List<EntityControlTile> ectAddeds) {
            this.ectAddeds.addAll(ectAddeds);
        }
        
        @Override
        public void undo() {
            SceneEdit sEdit = (SceneEdit) edit;
            for (int i = ectAddeds.size() - 1; i >= 0; i--) {
                sEdit.removeControlTile(ectAddeds.get(i));
            }
        }

        @Override
        public void redo() {
            SceneEdit sEdit = (SceneEdit) edit;
            for (int i = 0; i < ectAddeds.size(); i++) {
                sEdit.addControlTile(ectAddeds.get(i));
            }
        }
    }
}
