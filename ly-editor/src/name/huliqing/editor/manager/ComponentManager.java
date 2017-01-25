/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import name.huliqing.luoying.constants.IdConstants;

/**
 *
 * @author huliqing
 */
public class ComponentManager {
    
    private final static Map<String, List<Component>> COMPONENTS = new HashMap<>();
    
    public static void loadComponents() {
        
        List<Component> sceneEntities = new ArrayList<Component>();
        sceneEntities.add(new Component(IdConstants.SYS_ENTITY_AMBIENT_LIGHT, "Ambient Light"));
        sceneEntities.add(new Component(IdConstants.SYS_ENTITY_DIRECTIONAL_LIGHT, "Directional Light"));
        sceneEntities.add(new Component(IdConstants.SYS_ENTITY_BOUNDARY, "Boundary"));
        sceneEntities.add(new Component(IdConstants.SYS_ENTITY_CHASE_CAMERA, "Chase Camera"));
        sceneEntities.add(new Component(IdConstants.SYS_ENTITY_PHYSICS, "Physics Space"));
        sceneEntities.add(new Component(IdConstants.SYS_ENTITY_SHADOW, "Shadow"));
        sceneEntities.add(new Component(IdConstants.SYS_ENTITY_SIMPLE_WATER, "Simple Water"));
        sceneEntities.add(new Component(IdConstants.SYS_ENTITY_ADVANCE_WATER, "Advance Water"));
        sceneEntities.add(new Component(IdConstants.SYS_ENTITY_SKY, "Sky"));
        sceneEntities.add(new Component(IdConstants.SYS_ENTITY_TERRAIN, "SimpleTerrain"));
        sceneEntities.add(new Component(IdConstants.SYS_ENTITY_TREE, "Tree"));
        sceneEntities.add(new Component(IdConstants.SYS_ENTITY_AUDIO, "Audio"));
        sceneEntities.add(new Component("AdvanceTerrain", "AdvanceTerrain"));
        
        COMPONENTS.put("SceneEntity", sceneEntities);
    }
    
    public static void clearComponents() {
        COMPONENTS.clear();
    }
    
    public static List<Component> getComponents(String componentType) {
        return COMPONENTS.get(componentType);
    }
    
    public static class Component implements Serializable {
        public String id;
        public String name; // displayName
        public String icon;
        public Component(String id, String name) {
            this.id = id;
            this.name = name;
        }
        public Component(String id, String name, String icon) {
            this.id = id;
            this.name = name;
            this.icon = icon;
        }
    }
}
