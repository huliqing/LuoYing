/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.edit.controls.ControlTile;
import name.huliqing.editor.edit.select.AdvanceWaterEntitySelectObj;
import name.huliqing.editor.edit.select.DirectionalLightEntitySelectObj;
import name.huliqing.editor.edit.select.EmptyEntitySelectObj;
import name.huliqing.editor.edit.select.EntitySelectObj;
import name.huliqing.editor.edit.select.SimpleModelEntitySelectObj;

/**
 *
 * @author huliqing
 */
public class SelectObjManager {

    private static final Logger LOG = Logger.getLogger(SelectObjManager.class.getName());
    
    private final static Map<String, Class<? extends EntitySelectObj>> MAPPING = new HashMap();
    
    static {
        MAPPING.put("entitySimpleModel", SimpleModelEntitySelectObj.class);
        MAPPING.put("entitySimpleWater", SimpleModelEntitySelectObj.class);
        MAPPING.put("entitySimpleTerrain", SimpleModelEntitySelectObj.class);
        MAPPING.put("entityTree", SimpleModelEntitySelectObj.class);
        MAPPING.put("entityGrass", SimpleModelEntitySelectObj.class);
        MAPPING.put("entityDirectionalLight", DirectionalLightEntitySelectObj.class);
        MAPPING.put("entityAdvanceWater", AdvanceWaterEntitySelectObj.class);
        
        MAPPING.put("entitySkyBox", EmptyEntitySelectObj.class);
        MAPPING.put("entityAmbientLight", EmptyEntitySelectObj.class);
        MAPPING.put("entityDirectionalLightFilterShadow", EmptyEntitySelectObj.class);
        MAPPING.put("entityChaseCamera", EmptyEntitySelectObj.class);
        MAPPING.put("entityPhysics", EmptyEntitySelectObj.class);
    }
    
    public final static <T extends EntitySelectObj> T createEntityControlTile(String tagName) {
        Class<? extends ControlTile> clazz = MAPPING.get(tagName);
        if (clazz == null) {
            LOG.log(Level.WARNING, "Could not create select obj, unknow tagName={0}", tagName);
            return (T) new EmptyEntitySelectObj();
        }
         
        ControlTile obj;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            obj = new EmptyEntitySelectObj();
            Logger.getLogger(SelectObjManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (T) obj;
    }
}
