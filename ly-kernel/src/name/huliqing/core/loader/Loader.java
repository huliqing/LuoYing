/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;
import name.huliqing.core.LY;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.ActionData;
import name.huliqing.core.data.ActorAnimData;
import name.huliqing.core.data.ActorData;
import name.huliqing.core.data.AnimData;
import name.huliqing.core.data.BulletData;
import name.huliqing.core.data.ChatData;
import name.huliqing.core.data.ElData;
import name.huliqing.core.data.EmitterData;
import name.huliqing.core.data.PositionData;
import name.huliqing.core.data.EnvData;
import name.huliqing.core.data.GameData;
import name.huliqing.core.data.HandlerData;
import name.huliqing.core.data.HitCheckerData;
import name.huliqing.core.data.ActorLogicData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.data.ResistData;
import name.huliqing.core.data.SceneData;
import name.huliqing.core.data.StateData;
import name.huliqing.core.data.MagicData;
import name.huliqing.core.data.ShapeData;
import name.huliqing.core.data.TalentData;
import name.huliqing.core.data.TaskData;
import name.huliqing.core.data.ViewData;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.object.action.Action;
import name.huliqing.core.object.actoranim.ActorAnim;
import name.huliqing.core.object.actorlogic.ActorLogic;
import name.huliqing.core.object.anim.Anim;
import name.huliqing.core.object.bullet.Bullet;
import name.huliqing.core.object.chat.Chat;
import name.huliqing.core.object.effect.Effect;
import name.huliqing.core.object.env.Env;
import name.huliqing.core.object.game.Game;
import name.huliqing.core.object.handler.Handler;
import name.huliqing.core.object.hitchecker.HitChecker;
import name.huliqing.core.object.el.El;
import name.huliqing.core.object.emitter.Emitter;
import name.huliqing.core.object.magic.Magic;
import name.huliqing.core.object.position.Position;
import name.huliqing.core.object.resist.Resist;
import name.huliqing.core.object.scene.Scene;
import name.huliqing.core.object.shape.Shape;
import name.huliqing.core.object.skill.Skill;
import name.huliqing.core.object.skin.Skin;
import name.huliqing.core.object.state.State;
import name.huliqing.core.object.talent.Talent;
import name.huliqing.core.object.task.Task;
import name.huliqing.core.object.view.View;
import name.huliqing.core.xml.DataProcessor;
import name.huliqing.core.xml.ProtoData;


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
        AssetManager am = LY.getAssetManager();
        return am.loadMaterial(j3mFile);
    }
    
    public static Action loadAction(String actionId) {
        ActionData ad = DataFactory.createData(actionId);
        return loadAction(ad);
    }
      
    public static Action loadAction(ActionData data) {
        return DataFactory.createProcessor(data);
    }
    
    public static ActorAnim loadActorAnim(String id) {
        ActorAnimData data = DataFactory.createData(id);
        return loadActorAnim(data);
    }
    
    public static ActorAnim loadActorAnim(ActorAnimData data) {
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
    
    public static Handler loadHandler(HandlerData data) {
        return DataFactory.createProcessor(data);
    }
    
    public static Handler loadHandler(String handlerId) {
        if (handlerId == null) {
            return null;
        }
        HandlerData ad = DataFactory.createData(handlerId);
        return loadHandler(ad);
    }
    
    public static HitChecker loadHitChecker(String id) {
        HitCheckerData data = DataFactory.createData(id);
        return loadHitChecker(data);
    }
    
    public static HitChecker loadHitChecker(HitCheckerData data) {
        return DataFactory.createProcessor(data);
    }
       
    public static ActorLogic loadLogic(ActorLogicData data) {
        return DataFactory.createProcessor(data);
    }
    
    public static ActorLogic loadLogic(String logicId) {
        return loadLogic((ActorLogicData) DataFactory.createData(logicId));
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
    
    public static State loadState(String id) {
        StateData data = DataFactory.createData(id);
        return loadState(data);
    }
    
    public static State loadState(StateData data) {
        return DataFactory.createProcessor(data);
    }
        
//    public static Talent loadTalent(String talentId) {
//        return loadTalent((TalentData) DataFactory.createData(talentId));
//    }
    
//    public static Talent loadTalent(TalentData data) {
//        return DataFactory.createProcessor(data);
//    }
    
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
