/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter;

import com.jme3.network.serializing.Serializer;
import com.jme3.network.serializing.serializers.MapSerializer;
import java.util.LinkedHashMap;
import name.huliqing.fighter.data.ActionData;
import name.huliqing.fighter.data.ActorAnimData;
import name.huliqing.fighter.data.ActorData;
import name.huliqing.fighter.data.AnimData;
import name.huliqing.fighter.data.AttributeApply;
import name.huliqing.fighter.data.AttributeData;
import name.huliqing.fighter.data.BulletData;
import name.huliqing.fighter.data.DropData;
import name.huliqing.fighter.data.DropItem;
import name.huliqing.fighter.data.EffectData;
import name.huliqing.fighter.data.EmitterData;
import name.huliqing.fighter.data.PositionData;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.data.HandlerData;
import name.huliqing.fighter.data.HitCheckerData;
import name.huliqing.fighter.data.ElData;
import name.huliqing.fighter.data.LogicData;
import name.huliqing.fighter.data.ItemData;
import name.huliqing.fighter.data.PkgItemData;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.data.ResistData;
import name.huliqing.fighter.data.SceneData;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.data.SkinData;
import name.huliqing.fighter.data.SoundData;
import name.huliqing.fighter.data.StateData;
import name.huliqing.fighter.data.TalentData;
import name.huliqing.fighter.data.AttributeUse;
import name.huliqing.fighter.data.DataAttribute;
import name.huliqing.fighter.data.MagicData;
import name.huliqing.fighter.data.ShapeData;
import name.huliqing.fighter.data.TaskData;
import name.huliqing.fighter.data.ViewData;
import name.huliqing.fighter.game.mess.MessClient;
import name.huliqing.fighter.game.mess.MessPlayGetServerState;
import name.huliqing.fighter.game.mess.MessPlayInitGame;
//import name.huliqing.fighter.game.mess.MessPlayClientData;
import name.huliqing.fighter.game.mess.MessSCClientList;
import name.huliqing.fighter.game.mess.MessSCGameData;
import name.huliqing.fighter.game.mess.MessSCServerState;
import name.huliqing.fighter.game.mess.MessSCInitGameOK;
import name.huliqing.fighter.game.mess.MessPlayActorSelect;
import name.huliqing.fighter.game.mess.MessPlayActorLoaded;
import name.huliqing.fighter.game.mess.MessPlayActorSelectResult;
import name.huliqing.fighter.game.mess.MessActionRun;
import name.huliqing.fighter.game.mess.MessActorAddSkill;
import name.huliqing.fighter.game.mess.MessActorApplyXp;
import name.huliqing.fighter.game.mess.MessActorFollow;
import name.huliqing.fighter.game.mess.MessActorKill;
import name.huliqing.fighter.game.mess.MessActorPhysics;
import name.huliqing.fighter.game.mess.MessActorSetGroup;
import name.huliqing.fighter.game.mess.MessActorSetLevel;
import name.huliqing.fighter.game.mess.MessActorSetTarget;
import name.huliqing.fighter.game.mess.MessActorSpeak;
import name.huliqing.fighter.game.mess.MessActorTeam;
import name.huliqing.fighter.game.mess.MessActorViewDir;
import name.huliqing.fighter.game.mess.MessAttributeSync;
import name.huliqing.fighter.game.mess.MessAutoAttack;
import name.huliqing.fighter.game.mess.MessChatSell;
import name.huliqing.fighter.game.mess.MessChatSend;
import name.huliqing.fighter.game.mess.MessPlayGetClients;
import name.huliqing.fighter.game.mess.MessPlayGetGameData;
import name.huliqing.fighter.game.mess.MessChatShop;
import name.huliqing.fighter.game.mess.MessProtoAdd;
import name.huliqing.fighter.game.mess.MessProtoRemove;
import name.huliqing.fighter.game.mess.MessProtoSync;
import name.huliqing.fighter.game.mess.MessProtoUse;
import name.huliqing.fighter.game.mess.MessMessage;
import name.huliqing.fighter.game.mess.MessSCActorRemove;
import name.huliqing.fighter.game.mess.MessSkill;
import name.huliqing.fighter.game.mess.MessSkillAbstract;
import name.huliqing.fighter.game.mess.MessSkillWalk;
import name.huliqing.fighter.game.mess.MessStateAdd;
import name.huliqing.fighter.game.mess.MessActorTransform;
import name.huliqing.fighter.game.mess.MessActorTransformDirect;
import name.huliqing.fighter.game.mess.MessPing;
import name.huliqing.fighter.game.mess.MessSkillFaceTo;
import name.huliqing.fighter.game.mess.MessSkinWeaponTakeOn;
import name.huliqing.fighter.game.mess.MessStateRemove;
import name.huliqing.fighter.game.mess.MessSyncObject;
import name.huliqing.fighter.game.mess.MessTalentAdd;
import name.huliqing.fighter.game.mess.MessTalentAddPoint;
import name.huliqing.fighter.game.mess.MessTaskAdd;
import name.huliqing.fighter.game.mess.MessTaskApplyItem;
import name.huliqing.fighter.game.mess.MessTaskComplete;
import name.huliqing.fighter.game.mess.MessViewAdd;
import name.huliqing.fighter.game.mess.MessViewRemove;
import name.huliqing.fighter.object.SyncData;
import name.huliqing.fighter.object.actor.ItemStore;
import name.huliqing.fighter.object.actor.SkillStore;
import name.huliqing.fighter.game.mess.MessPlayLoadSavedActor;
import name.huliqing.fighter.game.mess.MessPlayLoadSavedActorResult;
import name.huliqing.fighter.game.mess.MessPlayClientExit;
import static com.jme3.network.serializing.Serializer.registerClass;
import name.huliqing.fighter.game.mess.MessPlayChangeGameState;
import name.huliqing.fighter.game.state.ConnData;
import static com.jme3.network.serializing.Serializer.registerClass;
import static com.jme3.network.serializing.Serializer.registerClass;
import static com.jme3.network.serializing.Serializer.registerClass;
import static com.jme3.network.serializing.Serializer.registerClass;
import static com.jme3.network.serializing.Serializer.registerClass;
import static com.jme3.network.serializing.Serializer.registerClass;
import static com.jme3.network.serializing.Serializer.registerClass;
import static com.jme3.network.serializing.Serializer.registerClass;
import static com.jme3.network.serializing.Serializer.registerClass;
import static com.jme3.network.serializing.Serializer.registerClass;
import static com.jme3.network.serializing.Serializer.registerClass;
import static com.jme3.network.serializing.Serializer.registerClass;
import static com.jme3.network.serializing.Serializer.registerClass;
import static com.jme3.network.serializing.Serializer.registerClass;
import static com.jme3.network.serializing.Serializer.registerClass;

/**
 *
 * @author huliqing
 */
public class MessRegister {
    
    public static void register() {
        // Serializer.registerClass(Enum.class, new MyEnumSerializer());
        // 注：EnumSerializer在处理Collection和Set、Map等包含Enum类的字段时会报错
        // 所以一般字段不要使用如： Set<Enum>,Map<Enum,xxx>,List<Enum>作为字段。
        // throw new SerializerException("Class has no enum constants:" + c);
        
        
        // 对LinkedHashMap有序MAP的支持
        registerClass(LinkedHashMap.class, new MapSerializer());
        
        
        // Lan 
        Serializer.registerClass(MessClient.class);
        Serializer.registerClass(MessPlayGetClients.class);
        Serializer.registerClass(MessPlayGetGameData.class);
        Serializer.registerClass(MessPlayGetServerState.class);
        Serializer.registerClass(MessPlayInitGame.class);
        Serializer.registerClass(MessPlayLoadSavedActor.class);
        Serializer.registerClass(MessPlayLoadSavedActorResult.class);
        Serializer.registerClass(MessPlayClientExit.class);
//        Serializer.registerClass(MessPlayClientData.class);
        Serializer.registerClass(MessSCClientList.class);
        Serializer.registerClass(MessSCGameData.class);
        Serializer.registerClass(MessSCInitGameOK.class);
        Serializer.registerClass(MessSCServerState.class);
        
        // ---- Proto Data
        Serializer.registerClass(ItemStore.class);
        Serializer.registerClass(SkillStore.class);
        Serializer.registerClass(ActionData.class);
        Serializer.registerClass(ActorAnimData.class);
        Serializer.registerClass(ActorData.class);
        Serializer.registerClass(AnimData.class);
        Serializer.registerClass(AttributeApply.class);
        Serializer.registerClass(AttributeData.class);
        Serializer.registerClass(AttributeUse.class);
        Serializer.registerClass(BulletData.class);
        Serializer.registerClass(DataAttribute.class);
        Serializer.registerClass(DropData.class);
        Serializer.registerClass(DropItem.class);
        Serializer.registerClass(EffectData.class);
        Serializer.registerClass(EmitterData.class);
        Serializer.registerClass(PositionData.class);
        Serializer.registerClass(EnvData.class);
        Serializer.registerClass(GameData.class);
        Serializer.registerClass(HandlerData.class);
        Serializer.registerClass(HitCheckerData.class);
        Serializer.registerClass(ElData.class);
        Serializer.registerClass(LogicData.class);
        Serializer.registerClass(MagicData.class);
        Serializer.registerClass(ItemData.class);
        Serializer.registerClass(PkgItemData.class);
        Serializer.registerClass(Proto.class);
        Serializer.registerClass(ProtoData.class);
        Serializer.registerClass(ResistData.class);
        
        Serializer.registerClass(SceneData.class);
        
        Serializer.registerClass(ShapeData.class);
        Serializer.registerClass(SkillData.class);
        Serializer.registerClass(SkinData.class);
        Serializer.registerClass(SoundData.class);
        Serializer.registerClass(StateData.class);
        Serializer.registerClass(TalentData.class);
        Serializer.registerClass(TaskData.class);
        Serializer.registerClass(ViewData.class);
        
        // ---- Lan play
        Serializer.registerClass(ConnData.class);
        Serializer.registerClass(MessPing.class);
        Serializer.registerClass(MessPlayActorSelect.class);
        
        Serializer.registerClass(MessProtoAdd.class);
        Serializer.registerClass(MessProtoRemove.class);
        Serializer.registerClass(MessProtoSync.class);
        Serializer.registerClass(MessProtoUse.class);
        Serializer.registerClass(MessMessage.class);
        Serializer.registerClass(MessPlayActorLoaded.class);
        Serializer.registerClass(MessSCActorRemove.class);
        Serializer.registerClass(MessPlayActorSelectResult.class);
        Serializer.registerClass(MessPlayChangeGameState.class);
        
        // ---- Game play
        Serializer.registerClass(MessAutoAttack.class);
        Serializer.registerClass(MessActionRun.class);
        Serializer.registerClass(MessActorAddSkill.class);
        Serializer.registerClass(MessActorApplyXp.class);
        Serializer.registerClass(MessActorFollow.class);
        Serializer.registerClass(MessActorKill.class);
        Serializer.registerClass(MessActorPhysics.class);
        Serializer.registerClass(MessActorSetGroup.class);
        Serializer.registerClass(MessActorSetLevel.class);
        Serializer.registerClass(MessActorSetTarget.class);
        Serializer.registerClass(MessActorSpeak.class);
        Serializer.registerClass(MessActorTeam.class);
        Serializer.registerClass(MessActorViewDir.class);
        
        // Attribute
        Serializer.registerClass(MessAttributeSync.class);
        
        // Skill
        Serializer.registerClass(MessSkill.class);
        Serializer.registerClass(MessSkillAbstract.class);
        Serializer.registerClass(MessSkillFaceTo.class);
        Serializer.registerClass(MessSkillWalk.class);
        
        // Skin
        Serializer.registerClass(MessSkinWeaponTakeOn.class);
        
        // State
        Serializer.registerClass(MessStateAdd.class);
        Serializer.registerClass(MessStateRemove.class);
        
        // Sync object
        Serializer.registerClass(MessSyncObject.class);
        Serializer.registerClass(SyncData.class);
        
        // Talents
        Serializer.registerClass(MessTalentAdd.class);
        Serializer.registerClass(MessTalentAddPoint.class);
        
        // Task
        Serializer.registerClass(MessTaskAdd.class);
        Serializer.registerClass(MessTaskApplyItem.class);
        Serializer.registerClass(MessTaskComplete.class);
        
        // View
        Serializer.registerClass(MessViewAdd.class);
        Serializer.registerClass(MessViewRemove.class);
        
        // Sync
        Serializer.registerClass(MessActorTransform.class);
        Serializer.registerClass(MessActorTransformDirect.class);
        
        // Chat
        Serializer.registerClass(MessChatSell.class); 
        Serializer.registerClass(MessChatSend.class); 
        Serializer.registerClass(MessChatShop.class); 
        
    }
}
