/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;
import name.huliqing.ly.Ly;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.data.ActionData;
import name.huliqing.ly.data.ActorData;
import name.huliqing.ly.data.AnimData;
import name.huliqing.ly.data.BulletData;
import name.huliqing.ly.data.ChatData;
import name.huliqing.ly.data.ElData;
import name.huliqing.ly.data.EmitterData;
import name.huliqing.ly.data.PositionData;
import name.huliqing.ly.data.env.EnvData;
import name.huliqing.ly.data.GameData;
import name.huliqing.ly.data.HitCheckerData;
import name.huliqing.ly.data.LogicData;
import name.huliqing.ly.data.SkinData;
import name.huliqing.ly.data.SkillData;
import name.huliqing.ly.data.ResistData;
import name.huliqing.ly.data.SceneData;
import name.huliqing.ly.data.StateData;
import name.huliqing.ly.data.MagicData;
import name.huliqing.ly.data.ShapeData;
import name.huliqing.ly.data.TaskData;
import name.huliqing.ly.data.ViewData;
import name.huliqing.ly.xml.DataFactory;
import name.huliqing.ly.object.action.Action;
import name.huliqing.ly.object.logic.Logic;
import name.huliqing.ly.object.anim.Anim;
import name.huliqing.ly.object.bullet.Bullet;
import name.huliqing.ly.object.chat.Chat;
import name.huliqing.ly.object.effect.Effect;
import name.huliqing.ly.object.env.Env;
import name.huliqing.ly.object.game.Game;
import name.huliqing.ly.object.hitchecker.HitChecker;
import name.huliqing.ly.object.el.El;
import name.huliqing.ly.object.emitter.Emitter;
import name.huliqing.ly.object.magic.Magic;
import name.huliqing.ly.object.position.Position;
import name.huliqing.ly.object.resist.Resist;
import name.huliqing.ly.object.scene.Scene;
import name.huliqing.ly.object.shape.Shape;
import name.huliqing.ly.object.skill.Skill;
import name.huliqing.ly.object.skin.Skin;
import name.huliqing.ly.object.state.AbstractState;
import name.huliqing.ly.object.task.Task;
import name.huliqing.ly.object.view.View;
import name.huliqing.ly.xml.DataProcessor;
import name.huliqing.ly.xml.ProtoData;


public class Loader {
    
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
//        return MatLoader.loadMaterial(j3mFile);
        AssetManager am = Ly.getAssetManager();
        return am.loadMaterial(j3mFile);
    }
    
    public static Action loadAction(String actionId) {
        ActionData ad = DataFactory.createData(actionId);
        return loadAction(ad);
    }
      
    public static Action loadAction(ActionData data) {
        return DataFactory.createProcessor(data);
    }
    
    public static Actor loadActor(ActorData data) {
        Actor actor = DataFactory.createProcessor(data);
        return actor;
    }
        
    public static Anim loadAnimation(String animationId) {
        AnimData data = DataFactory.createData(animationId);
        return loadAnimation(data);
    }
    
    public static Anim loadAnimation(AnimData data) {
        return DataFactory.createProcessor(data);
    }
        
    public static Bullet loadBullet(String id) {
        BulletData data = DataFactory.createData(id);
        return loadBullet(data);
    }
    
    public static Bullet loadBullet(BulletData data) {
        return DataFactory.createProcessor(data);
    }
    
    public static Chat loadChat(String chatId) {
        ChatData data = DataFactory.createData(chatId);
        return DataFactory.createProcessor(data);
    }
    
    public static Effect loadEffect(String id) {
        return DataFactory.createProcessor(DataFactory.createData(id));
    }

    public static El loadEl(String levelId) {
        ElData data = DataFactory.createData(levelId);
        return DataFactory.createProcessor(data);
    }

    public static Emitter loadEmitter(EmitterData data) {
        Emitter emitter = DataFactory.createProcessor(data);
        return emitter;
    }
    
    public static Emitter loadEmitter(String id) {
        EmitterData data = DataFactory.createData(id);
        return loadEmitter(data);
    }
    
    public static Position loadPosition(PositionData esd) {
        return DataFactory.createProcessor(esd);
    }
    
    public static Position loadPosition(String id) {
        PositionData esd = DataFactory.createData(id);
        return loadPosition(esd);
    }
    
    public static Env loadEnv(String envId) {
        EnvData data = DataFactory.createData(envId);
        return loadEnv(data);
    }
    
    public static Env loadEnv(EnvData data) {
        return DataFactory.createProcessor(data);
    }
        
    public static Game loadGame(String id) {
        GameData data = DataFactory.createData(id);
        return loadGame(data);
    }
    
    public static Game loadGame(GameData data) {
        Game game = DataFactory.createProcessor(data);
        return game;
    }
    
    public static HitChecker loadHitChecker(String id) {
        HitCheckerData data = DataFactory.createData(id);
        return loadHitChecker(data);
    }
    
    public static HitChecker loadHitChecker(HitCheckerData data) {
        return DataFactory.createProcessor(data);
    }
       
    public static Logic loadLogic(LogicData data) {
        return DataFactory.createProcessor(data);
    }
    
    public static Logic loadLogic(String logicId) {
        return loadLogic((LogicData) DataFactory.createData(logicId));
    }
    
    public static Magic loadMagic(MagicData data) {
        return DataFactory.createProcessor(data);
    }
    
    public static Magic loadMagic(String magicId) {
        MagicData data = DataFactory.createData(magicId);
        return loadMagic(data);
    }
    
    public static Resist loadResist(String id) {
        ResistData data = DataFactory.createData(id);
        return loadResist(data);
    }
    
    public static Resist loadResist(ResistData data) {
        return DataFactory.createProcessor(data);
    }
        
    public static Scene loadScene(String sceneId) {
        SceneData data = DataFactory.createData(sceneId);
        return loadScene(data);
    }
    
    public static Scene loadScene(SceneData data) {
        return DataFactory.createProcessor(data);
    }
    
    public static Shape loadShape(String shapeId) {
        ShapeData data = DataFactory.createData(shapeId);
        return loadShape(data);
    }
    
    public static Shape loadShape(ShapeData data) {
        return DataFactory.createProcessor(data);
    }
    
    public static Skill loadSkill(SkillData data) {
        return DataFactory.createProcessor(data);
    }
    
    public static Skill loadSkill(String id) {
        SkillData sd = DataFactory.createData(id);
        return loadSkill(sd);
    }
        
    public static Skin loadSkin(String skinId) {
        SkinData skinData = DataFactory.createData(skinId);
        return loadSkin(skinData);
    }
    
    public static Skin loadSkin(SkinData data) {
        Skin skin = DataFactory.createProcessor(data);
        return skin;
    }
    
    public static AbstractState loadState(String id) {
        StateData data = DataFactory.createData(id);
        return loadState(data);
    }
    
    public static AbstractState loadState(StateData data) {
        return DataFactory.createProcessor(data);
    }
    
    public static Task loadTask(String taskId) {
        return loadTask((TaskData) DataFactory.createData(taskId));
    }
    
    public static Task loadTask(TaskData data) {
        return DataFactory.createProcessor(data);
    }
     
    public static View loadView(String viewId) {
        return loadView((ViewData)DataFactory.createData(viewId));
    }
    
    public static View loadView(ViewData data) {
        return DataFactory.createProcessor(data);
    }
    
}