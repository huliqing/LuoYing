/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core;

import com.jme3.network.serializing.Serializer;
import com.jme3.network.serializing.serializers.MapSerializer;
import java.util.LinkedHashMap;
import name.huliqing.core.game.mess.MessClient;
import name.huliqing.core.game.mess.MessPlayGetServerState;
import name.huliqing.core.game.mess.MessPlayInitGame;
import name.huliqing.core.game.mess.MessSCClientList;
import name.huliqing.core.game.mess.MessSCGameData;
import name.huliqing.core.game.mess.MessSCServerState;
import name.huliqing.core.game.mess.MessSCInitGameOK;
import name.huliqing.core.game.mess.MessPlayActorSelect;
import name.huliqing.core.game.mess.MessPlayActorLoaded;
import name.huliqing.core.game.mess.MessPlayActorSelectResult;
import name.huliqing.core.game.mess.MessActionRun;
import name.huliqing.core.game.mess.MessActorAddSkill;
import name.huliqing.core.game.mess.MessActorApplyXp;
import name.huliqing.core.game.mess.MessActorFollow;
import name.huliqing.core.game.mess.MessActorKill;
import name.huliqing.core.game.mess.MessActorPhysics;
import name.huliqing.core.game.mess.MessActorSetGroup;
import name.huliqing.core.game.mess.MessActorSetLevel;
import name.huliqing.core.game.mess.MessActorSetTarget;
import name.huliqing.core.game.mess.MessActorSpeak;
import name.huliqing.core.game.mess.MessActorTeam;
import name.huliqing.core.game.mess.MessActorViewDir;
import name.huliqing.core.game.mess.MessAttributeSync;
import name.huliqing.core.game.mess.MessAutoAttack;
import name.huliqing.core.game.mess.MessChatSell;
import name.huliqing.core.game.mess.MessChatSend;
import name.huliqing.core.game.mess.MessPlayGetClients;
import name.huliqing.core.game.mess.MessPlayGetGameData;
import name.huliqing.core.game.mess.MessChatShop;
import name.huliqing.core.game.mess.MessProtoAdd;
import name.huliqing.core.game.mess.MessProtoRemove;
import name.huliqing.core.game.mess.MessProtoSync;
import name.huliqing.core.game.mess.MessProtoUse;
import name.huliqing.core.game.mess.MessMessage;
import name.huliqing.core.game.mess.MessSCActorRemove;
import name.huliqing.core.game.mess.MessSkill;
import name.huliqing.core.game.mess.MessSkillAbstract;
import name.huliqing.core.game.mess.MessSkillWalk;
import name.huliqing.core.game.mess.MessStateAdd;
import name.huliqing.core.game.mess.MessActorTransform;
import name.huliqing.core.game.mess.MessActorTransformDirect;
import name.huliqing.core.game.mess.MessPing;
import name.huliqing.core.game.mess.MessSkillFaceTo;
import name.huliqing.core.game.mess.MessSkinWeaponTakeOn;
import name.huliqing.core.game.mess.MessStateRemove;
import name.huliqing.core.game.mess.MessSyncObject;
import name.huliqing.core.game.mess.MessTalentAdd;
import name.huliqing.core.game.mess.MessTalentAddPoint;
import name.huliqing.core.game.mess.MessTaskAdd;
import name.huliqing.core.game.mess.MessTaskApplyItem;
import name.huliqing.core.game.mess.MessTaskComplete;
import name.huliqing.core.game.mess.MessViewAdd;
import name.huliqing.core.game.mess.MessViewRemove;
import name.huliqing.core.object.SyncData;
import name.huliqing.core.game.mess.MessPlayLoadSavedActor;
import name.huliqing.core.game.mess.MessPlayLoadSavedActorResult;
import name.huliqing.core.game.mess.MessPlayClientExit;
import name.huliqing.core.game.mess.MessPlayChangeGameState;
import name.huliqing.core.data.ConnData;
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
        Serializer.registerClass(MessSCClientList.class);
        Serializer.registerClass(MessSCGameData.class);
        Serializer.registerClass(MessSCInitGameOK.class);
        Serializer.registerClass(MessSCServerState.class);
        
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
