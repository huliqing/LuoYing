/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import com.jme3.animation.AnimControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.data.ActionData;
import name.huliqing.fighter.data.ActorAnimData;
import name.huliqing.fighter.data.ActorData;
import name.huliqing.fighter.data.AnimData;
import name.huliqing.fighter.data.BulletData;
import name.huliqing.fighter.data.ChatData;
import name.huliqing.fighter.data.EffectData;
import name.huliqing.fighter.data.EmitterData;
import name.huliqing.fighter.data.PositionData;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.data.HandlerData;
import name.huliqing.fighter.data.HitCheckerData;
import name.huliqing.fighter.data.LogicData;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.data.SkinData;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.data.ResistData;
import name.huliqing.fighter.data.SceneData;
import name.huliqing.fighter.data.StateData;
import name.huliqing.fighter.data.MagicData;
import name.huliqing.fighter.data.ShapeData;
import name.huliqing.fighter.data.TalentData;
import name.huliqing.fighter.data.TaskData;
import name.huliqing.fighter.data.ViewData;
import name.huliqing.fighter.object.DataFactory;
import name.huliqing.fighter.object.action.Action;
import name.huliqing.fighter.object.actoranim.ActorAnim;
import name.huliqing.fighter.object.logic.ActorLogic;
import name.huliqing.fighter.object.anim.Anim;
import name.huliqing.fighter.object.bullet.Bullet;
import name.huliqing.fighter.object.channel.Channel;
import name.huliqing.fighter.object.chat.Chat;
import name.huliqing.fighter.object.effect.Effect;
import name.huliqing.fighter.object.env.Env;
import name.huliqing.fighter.object.game.Game;
import name.huliqing.fighter.object.handler.Handler;
import name.huliqing.fighter.object.hitchecker.HitChecker;
import name.huliqing.fighter.object.el.El;
import name.huliqing.fighter.object.magic.Magic;
import name.huliqing.fighter.object.position.Position;
import name.huliqing.fighter.object.resist.Resist;
import name.huliqing.fighter.object.scene.Scene;
import name.huliqing.fighter.object.shape.Shape;
import name.huliqing.fighter.object.skill.Skill;
import name.huliqing.fighter.object.skin.Skin;
import name.huliqing.fighter.object.state.State;
import name.huliqing.fighter.object.talent.Talent;
import name.huliqing.fighter.object.task.Task;
import name.huliqing.fighter.object.view.View;


public class Loader {
    
    public static Spatial loadModel(String file) {
        return AssetLoader.loadModel(file);
    }
    
    public static Material loadMaterial(String j3mFile) {
        return MatLoader.loadMaterial(j3mFile);
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
        return ActorAnimLoader.load(data);
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
        return AnimLoader.load(data);
    }
        
    public static Bullet loadBullet(String id) {
        BulletData data = DataFactory.createData(id);
        return loadBullet(data);
    }
    
    public static Bullet loadBullet(BulletData data) {
        return BulletLoader.load(data); 
    }
        
    public static Channel loadChannel(String channelId, AnimControl ac) {
        return ChannelLoader.load(channelId, ac);
    }
    
    public static Chat loadChat(String chatId) {
        ChatData data = DataFactory.createData(chatId);
        return ChatLoader.load(data);
    }
    
    public static Effect loadEffect(String id) {
        EffectData ed = DataFactory.createData(id);
        Effect effect = DataFactory.createProcessor(ed);
        Common.preloadScene(effect.getDisplay());
        return effect;
    }

    public static El loadEl(String levelId) {
        return ElLoader.load(levelId); 
    }

    public static ParticleEmitter loadEmitter(EmitterData data, ParticleEmitter store) {
        if (store == null) {
            store = new ParticleEmitter(data.getProto().getName(), ParticleMesh.Type.Triangle, 0);
        }
        // 设置userData
        EmitterLoader.loadEmitter(data, store);
        store.setUserData(ProtoData.USER_DATA, data);
        return store;
    }
    
    public static ParticleEmitter loadEmitter(String id) {
        EmitterData data = DataFactory.createData(id);
        return loadEmitter(data, null);
    }
    
    public static ParticleEmitter loadEmitter(String id, ParticleEmitter store) {
        EmitterData data = DataFactory.createData(id);
        return loadEmitter(data, store);
    }
        
    public static Position loadPosition(PositionData esd) {
        return PositionLoader.load(esd);
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
        return HitCheckerLoader.load(data); 
    }
       
    public static ActorLogic loadLogic(LogicData logicData) {
        return LogicLoader.load(logicData);
    }
    
    public static ActorLogic loadLogic(String logicId) {
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
        return ResistLoader.load(data);
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
        return ShapeLoader.load(data);
    }
    
    public static Skill loadSkill(SkillData data) {
        return SkillLoader.loadSkill(data);
    }
    
    public static Skill loadSkill(String id) {
        SkillData sd = DataFactory.createData(id);
        return loadSkill(sd);
    }
        
    public static Skin loadSkin(String skinId) {
        SkinData skinData = DataFactory.createData(skinId);
        return loadSkin(skinData);
    }
    
    public static Skin loadSkin(SkinData skinData) {
        Skin skin = SkinLoader.load(skinData);
        return skin;
    }
    
    public static State loadState(String id) {
        StateData data = DataFactory.createData(id);
        return loadState(data);
    }
    
    public static State loadState(StateData data) {
        return DataFactory.createProcessor(data);
    }
        
    public static Talent loadTalent(String talentId) {
        return loadTalent((TalentData) DataFactory.createData(talentId));
    }
    
    public static Talent loadTalent(TalentData talentData) {
        return TalentLoader.load(talentData);
    }
    
    public static Task loadTask(String taskId) {
        return loadTask((TaskData) DataFactory.createData(taskId));
    }
    
    public static Task loadTask(TaskData data) {
        return TaskLoader.load(data);
    }
     
    public static View loadView(String viewId) {
        return loadView((ViewData)DataFactory.createData(viewId));
    }
    
    public static View loadView(ViewData viewData) {
        return ViewLoader.load(viewData);
    }
}
