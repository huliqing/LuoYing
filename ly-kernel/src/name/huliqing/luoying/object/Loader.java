/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.xml.DataProcessor;
import name.huliqing.luoying.xml.ProtoData;

public class Loader {
    
    public static <T extends ProtoData> T loadData(String id) {
        return DataFactory.createData(id);
    }
    
    public static <T extends DataProcessor> T load(String id) {
        return load(DataFactory.createData(id));
    }
    
    public static <T extends DataProcessor> T load(ProtoData data) {
        return DataFactory.createProcessor(data);
    }
    
    public static Spatial loadModel(String file) {
        return AssetLoader.loadModel(file);
    }
    
    public static Material loadMaterial(String j3mFile) {
        AssetManager am = LuoYing.getAssetManager();
        return am.loadMaterial(j3mFile);
    }
    
//    /**
//     * @deprecated 
//     * @param actionId
//     * @return 
//     */
//    public static Action loadAction(String actionId) {
//        ActionData ad = DataFactory.createData(actionId);
//        return loadAction(ad);
//    }
//      
//    /**
//     * @deprecated 
//     * @param data
//     * @return 
//     */
//    public static Action loadAction(ActionData data) {
//        return DataFactory.createProcessor(data);
//    }
//    
//    /**
//     * @deprecated 
//     * @param data
//     * @return 
//     */
//    public static Entity loadActor(ActorData data) {
//        Entity actor = DataFactory.createProcessor(data);
//        return actor;
//    }
//    
//    /**
//     * @deprecated 
//     * @return 
//     */
//    public static Bullet loadBullet(String id) {
//        BulletData data = DataFactory.createData(id);
//        return loadBullet(data);
//    }
//    
//    /** @deprecated */
//    public static Bullet loadBullet(BulletData data) {
//        return DataFactory.createProcessor(data);
//    }
//    
//    /** @deprecated */
//    public static Effect loadEffect(String id) {
//        return DataFactory.createProcessor(DataFactory.createData(id));
//    }
//
//    /** @deprecated */
//    public static El loadEl(String levelId) {
//        ElData data = DataFactory.createData(levelId);
//        return DataFactory.createProcessor(data);
//    }
//
//    /** @deprecated */
//    public static Emitter loadEmitter(EmitterData data) {
//        Emitter emitter = DataFactory.createProcessor(data);
//        return emitter;
//    }
//    
//    /** @deprecated */
//    public static Emitter loadEmitter(String id) {
//        EmitterData data = DataFactory.createData(id);
//        return loadEmitter(data);
//    }
//    
//    /** @deprecated */
//    public static Position loadPosition(PositionData esd) {
//        return DataFactory.createProcessor(esd);
//    }
//    
//    /** @deprecated */
//    public static Position loadPosition(String id) {
//        PositionData esd = DataFactory.createData(id);
//        return loadPosition(esd);
//    }
//        
//    /** @deprecated */
//    public static Game loadGame(String id) {
//        GameData data = DataFactory.createData(id);
//        return loadGame(data);
//    }
//    
//    /** @deprecated */
//    public static Game loadGame(GameData data) {
//        Game game = DataFactory.createProcessor(data);
//        return game;
//    }
//    
//    /** @deprecated */
//    public static HitChecker loadHitChecker(String id) {
//        HitCheckerData data = DataFactory.createData(id);
//        return loadHitChecker(data);
//    }
//    
//    /** @deprecated */
//    public static HitChecker loadHitChecker(HitCheckerData data) {
//        return DataFactory.createProcessor(data);
//    }
//       
//    /** @deprecated */
//    public static Logic loadLogic(LogicData data) {
//        return DataFactory.createProcessor(data);
//    }
//    
//    /** @deprecated */
//    public static Logic loadLogic(String logicId) {
//        return loadLogic((LogicData) DataFactory.createData(logicId));
//    }
//    
//    /** @deprecated */
//    public static Magic loadMagic(MagicData data) {
//        return DataFactory.createProcessor(data);
//    }
//    
//    /** @deprecated */
//    public static Magic loadMagic(String magicId) {
//        MagicData data = DataFactory.createData(magicId);
//        return loadMagic(data);
//    }
//    
//    /** @deprecated */
//    public static Resist loadResist(String id) {
//        ResistData data = DataFactory.createData(id);
//        return loadResist(data);
//    }
//    
//    /** @deprecated */
//    public static Resist loadResist(ResistData data) {
//        return DataFactory.createProcessor(data);
//    }
//        
//    /** @deprecated */
//    public static Scene loadScene(String sceneId) {
//        SceneData data = DataFactory.createData(sceneId);
//        return loadScene(data);
//    }
//    
//    /** @deprecated */
//    public static Scene loadScene(SceneData data) {
//        return DataFactory.createProcessor(data);
//    }
//    
//    /** @deprecated */
//    public static Shape loadShape(String shapeId) {
//        ShapeData data = DataFactory.createData(shapeId);
//        return loadShape(data);
//    }
//    
//    /** @deprecated */
//    public static Shape loadShape(ShapeData data) {
//        return DataFactory.createProcessor(data);
//    }
//    
//    /** @deprecated */
//    public static Skill loadSkill(SkillData data) {
//        return DataFactory.createProcessor(data);
//    }
//    
//    /** @deprecated */
//    public static Skill loadSkill(String id) {
//        SkillData sd = DataFactory.createData(id);
//        return loadSkill(sd);
//    }
//        
//    /** @deprecated */
//    public static Skin loadSkin(String skinId) {
//        SkinData skinData = DataFactory.createData(skinId);
//        return loadSkin(skinData);
//    }
//    
//    /** @deprecated */
//    public static Skin loadSkin(SkinData data) {
//        Skin skin = DataFactory.createProcessor(data);
//        return skin;
//    }
//    
//    /** @deprecated */
//    public static AbstractState loadState(String id) {
//        StateData data = DataFactory.createData(id);
//        return loadState(data);
//    }
//    
//    /** @deprecated */
//    public static AbstractState loadState(StateData data) {
//        return DataFactory.createProcessor(data);
//    }
//    
//    /** @deprecated */
//    public static Task loadTask(String taskId) {
//        return loadTask((TaskData) DataFactory.createData(taskId));
//    }
//    
//    /** @deprecated */
//    public static Task loadTask(TaskData data) {
//        return DataFactory.createProcessor(data);
//    }
    
}
