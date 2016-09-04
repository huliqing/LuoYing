/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.input.InputManager;
import com.jme3.math.Vector2f;
import com.jme3.network.serializing.Serializer;
import com.jme3.network.serializing.serializers.MapSerializer;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import name.huliqing.core.data.ActionData;
import name.huliqing.core.data.ActorAnimData;
import name.huliqing.core.data.ActorData;
import name.huliqing.core.data.ActorLogicData;
import name.huliqing.core.data.AnimData;
import name.huliqing.core.data.AttributeApply;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.data.AttributeUse;
import name.huliqing.core.data.BulletData;
import name.huliqing.core.data.ChannelData;
import name.huliqing.core.data.ChatData;
import name.huliqing.core.data.ConfigData;
import name.huliqing.core.data.ConnData;
import name.huliqing.core.data.module.ModuleData;
import name.huliqing.core.data.DropData;
import name.huliqing.core.data.DropItem;
import name.huliqing.core.data.EffectData;
import name.huliqing.core.data.ElData;
import name.huliqing.core.data.EmitterData;
import name.huliqing.core.data.env.EnvData;
import name.huliqing.core.data.GameData;
import name.huliqing.core.data.GameLogicData;
import name.huliqing.core.data.HandlerData;
import name.huliqing.core.data.HitCheckerData;
import name.huliqing.core.data.ItemData;
import name.huliqing.core.data.MagicData;
import name.huliqing.core.data.PositionData;
import name.huliqing.core.data.ResistData;
import name.huliqing.core.data.SceneData;
import name.huliqing.core.data.ShapeData;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.data.SlotData;
import name.huliqing.core.data.SoundData;
import name.huliqing.core.data.StateData;
import name.huliqing.core.data.TalentData;
import name.huliqing.core.data.TaskData;
import name.huliqing.core.data.ViewData;
import name.huliqing.core.mess.MessActionRun;
import name.huliqing.core.mess.MessActorAddSkill;
import name.huliqing.core.mess.MessActorFollow;
import name.huliqing.core.mess.MessActorKill;
import name.huliqing.core.mess.MessActorPhysics;
import name.huliqing.core.mess.MessActorSetGroup;
import name.huliqing.core.mess.MessActorSetLevel;
import name.huliqing.core.mess.MessActorSetTarget;
import name.huliqing.core.mess.MessActorSpeak;
import name.huliqing.core.mess.MessActorTeam;
import name.huliqing.core.mess.MessActorTransform;
import name.huliqing.core.mess.MessActorTransformDirect;
import name.huliqing.core.mess.MessActorViewDir;
import name.huliqing.core.mess.MessAutoAttack;
import name.huliqing.core.mess.MessChatSell;
import name.huliqing.core.mess.MessChatSend;
import name.huliqing.core.mess.MessChatShop;
import name.huliqing.core.mess.MessClient;
import name.huliqing.core.mess.MessMessage;
import name.huliqing.core.mess.MessPing;
import name.huliqing.core.mess.MessPlayActorLoaded;
import name.huliqing.core.mess.MessPlayActorSelect;
import name.huliqing.core.mess.MessPlayActorSelectResult;
import name.huliqing.core.mess.MessPlayChangeGameState;
import name.huliqing.core.mess.MessPlayClientExit;
import name.huliqing.core.mess.MessPlayGetClients;
import name.huliqing.core.mess.MessPlayGetGameData;
import name.huliqing.core.mess.MessPlayGetServerState;
import name.huliqing.core.mess.MessPlayInitGame;
import name.huliqing.core.mess.MessPlayLoadSavedActor;
import name.huliqing.core.mess.MessPlayLoadSavedActorResult;
import name.huliqing.core.mess.MessProtoAdd;
import name.huliqing.core.mess.MessProtoRemove;
import name.huliqing.core.mess.MessProtoUse;
import name.huliqing.core.mess.MessSCActorRemove;
import name.huliqing.core.mess.MessSCClientList;
import name.huliqing.core.mess.MessSCGameData;
import name.huliqing.core.mess.MessSCInitGameOK;
import name.huliqing.core.mess.MessSCServerState;
import name.huliqing.core.mess.MessSkill;
import name.huliqing.core.mess.MessSkillAbstract;
import name.huliqing.core.mess.MessActorLookAt;
import name.huliqing.core.mess.MessSkillWalk;
import name.huliqing.core.mess.MessSkinWeaponTakeOn;
import name.huliqing.core.mess.MessStateAdd;
import name.huliqing.core.mess.MessStateRemove;
import name.huliqing.core.mess.MessSyncObject;
import name.huliqing.core.mess.MessTalentAdd;
import name.huliqing.core.mess.MessTalentAddPoint;
import name.huliqing.core.mess.MessTaskAdd;
import name.huliqing.core.mess.MessTaskApplyItem;
import name.huliqing.core.mess.MessTaskComplete;
import name.huliqing.core.mess.MessViewAdd;
import name.huliqing.core.mess.MessViewRemove;
import name.huliqing.core.object.SyncData;
import name.huliqing.core.loader.ActionDataLoader;
import name.huliqing.core.object.action.FightDynamicAction;
import name.huliqing.core.object.action.FollowPathAction;
import name.huliqing.core.object.action.IdleDynamicAction;
import name.huliqing.core.object.action.IdlePatrolAction;
import name.huliqing.core.object.action.IdleStaticAction;
import name.huliqing.core.object.action.RunPathAction;
import name.huliqing.core.object.action.RunSimpleAction;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.loader.ActorDataLoader;
import name.huliqing.core.loader.ActorAnimDataLoader;
import name.huliqing.core.object.actoranim.ActorCurveMove;
import name.huliqing.core.loader.ActorLogicDataLoader;
import name.huliqing.core.object.actorlogic.AttributeChangeActorLogic;
import name.huliqing.core.object.actorlogic.DefendActorLogic;
import name.huliqing.core.object.actorlogic.FightActorLogic;
import name.huliqing.core.object.actorlogic.FollowActorLogic;
import name.huliqing.core.object.actorlogic.IdleActorLogic;
import name.huliqing.core.object.actorlogic.NotifyActorLogic;
import name.huliqing.core.object.actorlogic.PlayerActorLogic;
import name.huliqing.core.object.actorlogic.PositionActorLogic;
import name.huliqing.core.object.actorlogic.SearchEnemyActorLogic;
import name.huliqing.core.object.actorlogic.ShopActorLogic;
import name.huliqing.core.loader.AnimDataLoader;
import name.huliqing.core.object.anim.ColorAnim;
import name.huliqing.core.object.anim.CurveMoveAnim;
import name.huliqing.core.object.anim.MoveAnim;
import name.huliqing.core.object.anim.RandomRotationAnim;
import name.huliqing.core.object.anim.RotationAnim;
import name.huliqing.core.object.anim.ScaleAnim;
import name.huliqing.core.loader.AttributeDataLoader;
import name.huliqing.core.loader.BulletDataLoader;
import name.huliqing.core.object.bullet.CurveBullet;
import name.huliqing.core.object.bullet.CurveTrailBullet;
import name.huliqing.core.object.bullet.SimpleBullet;
import name.huliqing.core.object.bullet.StraightBullet;
import name.huliqing.core.object.module.ChannelModule;
import name.huliqing.core.loader.ChannelDataLoader;
import name.huliqing.core.object.channel.SimpleChannel;
import name.huliqing.core.loader.ChatDataLoader;
import name.huliqing.core.object.chat.GroupChat;
import name.huliqing.core.object.chat.SellChat;
import name.huliqing.core.object.chat.SendChat;
import name.huliqing.core.object.chat.ShopChat;
import name.huliqing.core.object.chat.TaskChat;
import name.huliqing.core.loader.ConfigDataLoader;
import name.huliqing.core.object.module.ActionModule;
import name.huliqing.core.object.module.AttributeModule;
import name.huliqing.core.object.module.ActorModule;
import name.huliqing.core.object.module.ChatModule;
import name.huliqing.core.object.module.ItemModule;
import name.huliqing.core.object.module.LogicModule;
import name.huliqing.core.object.module.SkinModule;
import name.huliqing.core.object.module.ResistModule;
import name.huliqing.core.object.module.SkillModule;
import name.huliqing.core.object.module.StateModule;
import name.huliqing.core.object.module.TalentModule;
import name.huliqing.core.object.module.TaskModule;
import name.huliqing.core.loader.DropDataLoader;
import name.huliqing.core.loader.EffectDataLoader;
import name.huliqing.core.object.effect.EncircleHaloEffect;
import name.huliqing.core.object.effect.GroupEffect;
import name.huliqing.core.object.effect.HaloEffect;
import name.huliqing.core.object.effect.ModelEffect;
import name.huliqing.core.object.effect.ParticleEffect;
import name.huliqing.core.object.effect.ProjectionEffect;
import name.huliqing.core.object.effect.SlideColorEffect;
import name.huliqing.core.object.effect.SlideColorIOSplineEffect;
import name.huliqing.core.object.effect.SlideColorSplineEffect;
import name.huliqing.core.object.effect.TextureCylinderEffect;
import name.huliqing.core.object.effect.TextureEffect;
import name.huliqing.core.loader.ElDataLoader;
import name.huliqing.core.object.el.HitEl;
import name.huliqing.core.object.el.LevelEl;
import name.huliqing.core.object.el.AttributeEl;
import name.huliqing.core.object.emitter.Emitter;
import name.huliqing.core.loader.EmitterDataLoader;
import name.huliqing.core.object.env.AudioEnv;
import name.huliqing.core.object.env.BoundaryBoxEnv;
import name.huliqing.core.object.env.CameraChaseEnv;
import name.huliqing.core.loader.env.EnvDataLoader;
import name.huliqing.core.object.env.LightAmbientEnv;
import name.huliqing.core.object.env.LightDirectionalEnv;
import name.huliqing.core.object.env.ModelEnv;
import name.huliqing.core.data.env.ModelEnvData;
import name.huliqing.core.loader.env.ModelEnvLoader;
import name.huliqing.core.object.env.PhysicsEnv;
import name.huliqing.core.object.env.PlantEnv;
import name.huliqing.core.loader.env.PlantEnvLoader;
import name.huliqing.core.object.env.ProxyPlatformEnv;
import name.huliqing.core.object.env.ShadowEnv;
import name.huliqing.core.object.env.SkyEnv;
import name.huliqing.core.object.env.TerrainEnv;
import name.huliqing.core.object.env.TreeEnv;
import name.huliqing.core.object.env.WaterAdvanceEnv;
import name.huliqing.core.object.env.WaterSimpleEnv;
import name.huliqing.core.loader.GameDataLoader;
import name.huliqing.core.object.game.RpgGame;
import name.huliqing.core.object.game.impl.StoryGbGame;
import name.huliqing.core.object.game.impl.StoryGuardGame;
import name.huliqing.core.object.game.impl.StoryTreasureGame;
import name.huliqing.core.object.game.impl.SurvivalGame;
import name.huliqing.core.object.gamelogic.ActorCleanGameLogic;
import name.huliqing.core.object.gamelogic.AttributeChangeGameLogic;
import name.huliqing.core.loader.GameLogicDataLoader;
import name.huliqing.core.object.gamelogic.PlayerDeadCheckerGameLogic;
import name.huliqing.core.object.handler.AttributeHandler;
import name.huliqing.core.loader.HandlerDataLoader;
import name.huliqing.core.object.handler.ItemSkillHandler;
import name.huliqing.core.object.handler.MapHandler;
import name.huliqing.core.object.handler.OutfitHandler;
import name.huliqing.core.object.handler.SkillBookHandler;
import name.huliqing.core.object.handler.SkillHandler;
import name.huliqing.core.object.handler.StateGainHandler;
import name.huliqing.core.object.handler.StateRemoveHandler;
import name.huliqing.core.object.handler.SummonHandler;
import name.huliqing.core.object.handler.SummonSkillHandler;
import name.huliqing.core.object.handler.TestHandler;
import name.huliqing.core.object.handler.WeaponHandler;
import name.huliqing.core.loader.HitCheckerDataLoader;
import name.huliqing.core.object.hitchecker.SimpleHitChecker;
import name.huliqing.core.loader.ItemDataLoader;
import name.huliqing.core.object.magic.AttributeHitMagic;
import name.huliqing.core.loader.MagicDataLoader;
import name.huliqing.core.object.magic.StateMagic;
import name.huliqing.core.data.module.ActionModuleData;
import name.huliqing.core.object.position.FixedPosition;
import name.huliqing.core.loader.PositionDataLoader;
import name.huliqing.core.object.position.RandomBoxPosition;
import name.huliqing.core.object.position.RandomCirclePosition;
import name.huliqing.core.object.position.RandomSpherePosition;
import name.huliqing.core.object.position.ViewPosition;
import name.huliqing.core.object.resist.AllResist;
import name.huliqing.core.loader.ResistDataLoader;
import name.huliqing.core.object.resist.SimpleResist;
import name.huliqing.core.object.scene.RandomSceneLoader;
import name.huliqing.core.object.scene.Scene;
import name.huliqing.core.loader.SceneDataLoader;
import name.huliqing.core.object.shape.BoxShape;
import name.huliqing.core.loader.ShapeDataLoader;
import name.huliqing.core.object.skill.AttackSkill;
import name.huliqing.core.object.skill.BackSkill;
//import name.huliqing.core.object.skill.DeadRagdollSkill;
import name.huliqing.core.object.skill.DeadSkill;
import name.huliqing.core.object.skill.DefendSkill;
import name.huliqing.core.object.skill.DuckSkill;
import name.huliqing.core.object.skill.HurtSkill;
import name.huliqing.core.object.skill.IdleSkill;
import name.huliqing.core.object.skill.ReadySkill;
import name.huliqing.core.object.skill.ResetSkill;
import name.huliqing.core.object.skill.RunSkill;
import name.huliqing.core.object.skill.ShotBowSkill;
import name.huliqing.core.object.skill.ShotSkill;
import name.huliqing.core.loader.SkillDataLoader;
import name.huliqing.core.object.skill.SkinSkill;
import name.huliqing.core.object.skill.SummonSkill;
import name.huliqing.core.object.skill.WaitSkill;
import name.huliqing.core.object.skill.WalkSkill;
import name.huliqing.core.object.skin.OutfitSkin;
import name.huliqing.core.loader.SkinDataLoader;
import name.huliqing.core.object.skin.WeaponSkin;
import name.huliqing.core.loader.SlotDataLoader;
import name.huliqing.core.object.sound.Sound;
import name.huliqing.core.loader.SoundDataLoader;
import name.huliqing.core.object.state.AttributeDynamicState;
import name.huliqing.core.object.state.AttributeState;
import name.huliqing.core.object.state.CleanState;
import name.huliqing.core.object.state.EssentialState;
import name.huliqing.core.object.state.MoveSpeedState;
import name.huliqing.core.object.state.SkillLockedState;
import name.huliqing.core.object.state.SkillState;
import name.huliqing.core.loader.StateDataLoader;
import name.huliqing.core.object.talent.AttributeTalent;
import name.huliqing.core.loader.TalentDataLoader;
import name.huliqing.core.object.task.CollectTask;
import name.huliqing.core.loader.TaskDataLoader;
import name.huliqing.core.object.view.TextPanelView;
import name.huliqing.core.object.view.TextView;
import name.huliqing.core.object.view.TimerView;
import name.huliqing.core.loader.ViewDataLoader;
import name.huliqing.core.loader.ModuleDataLoader;
import name.huliqing.core.mess.MessAttributeNumberAddValue;
import name.huliqing.core.mess.MessAttributeNumberHit;
import name.huliqing.core.object.attribute.BooleanAttribute;
import name.huliqing.core.object.attribute.FloatAttribute;
import name.huliqing.core.object.attribute.GroupAttribute;
import name.huliqing.core.object.attribute.IntegerAttribute;
import name.huliqing.core.object.attribute.LevelFloatAttribute;
import name.huliqing.core.object.attribute.LevelIntegerAttribute;
import name.huliqing.core.object.attribute.LimitIntegerAttribute;
import name.huliqing.core.object.attribute.LongAttribute;
import name.huliqing.core.object.attribute.StringAttribute;
import name.huliqing.core.object.attribute.StringListAttribute;
import name.huliqing.core.object.drop.AttributeDrop;
import name.huliqing.core.object.drop.GroupDrop;
import name.huliqing.core.object.drop.ItemDrop;
import name.huliqing.core.object.drop.SkinDrop;
import name.huliqing.core.object.module.DropModule;
import name.huliqing.core.object.module.LevelModule;
import name.huliqing.core.object.state.GroupState;
import name.huliqing.core.state.PlayState;
import name.huliqing.core.xml.Data;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.xml.Proto;
import org.xml.sax.SAXException;

/**
 *
 * @author huliqing
 */
public class LY {
    
    private static Application app;
    private static AppSettings settings; 
    private static BitmapFont font;
    
    /**
     * 初始化环境
     * @param app
     * @param settings 
     */
    public static void initialize(Application app, AppSettings settings) {
        LY.app = app;
        LY.settings = settings;
        
        // 注册需要序列化的数据，对于网络版进行序列化时需要用到。
        registerSerializer();
        
        // 注册数据处理器
        registerProcessor();
        
        // 注册messages,用于network通信
        registerMessage();
        
        // 载入数据
        loadData();
    }

    private static void registerSerializer() {
        Serializer.registerClass(Proto.class);
        Serializer.registerClass(AttributeApply.class);
        Serializer.registerClass(AttributeUse.class);
        Serializer.registerClass(Data.class);
        Serializer.registerClass(DropItem.class);
        
        Serializer.registerClass(ActionData.class);
        Serializer.registerClass(ActorAnimData.class);
        Serializer.registerClass(ActorData.class);
        Serializer.registerClass(ActorLogicData.class);
        Serializer.registerClass(AnimData.class);
        Serializer.registerClass(AttributeData.class);
        Serializer.registerClass(BulletData.class);
        Serializer.registerClass(ChannelData.class);
        Serializer.registerClass(ChatData.class);
        Serializer.registerClass(ConfigData.class);
        Serializer.registerClass(ModuleData.class);
        Serializer.registerClass(DropData.class);
        Serializer.registerClass(EffectData.class);
        Serializer.registerClass(ElData.class);
        Serializer.registerClass(EmitterData.class);
        Serializer.registerClass(EnvData.class);
        Serializer.registerClass(GameData.class);
        Serializer.registerClass(GameLogicData.class);
        Serializer.registerClass(HandlerData.class);
        Serializer.registerClass(HitCheckerData.class);
        Serializer.registerClass(ItemData.class);
        Serializer.registerClass(MagicData.class);
        Serializer.registerClass(PositionData.class);
        Serializer.registerClass(ResistData.class);
        Serializer.registerClass(SceneData.class);
        Serializer.registerClass(ShapeData.class);
        Serializer.registerClass(SkillData.class);
        Serializer.registerClass(SkinData.class);
        Serializer.registerClass(SlotData.class);
        Serializer.registerClass(SoundData.class);
        Serializer.registerClass(StateData.class);
        Serializer.registerClass(TalentData.class);
        Serializer.registerClass(TaskData.class);
        Serializer.registerClass(ViewData.class);
        
        Serializer.registerClass(ModelEnvData.class);
    }
    
        
    private static void registerProcessor() {
        
        // Action
        DataFactory.register("actionIdleStatic", ActionData.class, ActionDataLoader.class, IdleStaticAction.class);
        DataFactory.register("actionIdleDynamic", ActionData.class, ActionDataLoader.class,  IdleDynamicAction.class);
        DataFactory.register("actionIdlePatrol", ActionData.class, ActionDataLoader.class,  IdlePatrolAction.class);
        DataFactory.register("actionRun", ActionData.class, ActionDataLoader.class,  RunPathAction.class);
        DataFactory.register("actionRunSimple", ActionData.class, ActionDataLoader.class, RunSimpleAction.class);
        DataFactory.register("actionFollow", ActionData.class, ActionDataLoader.class,  FollowPathAction.class);
        DataFactory.register("actionFight", ActionData.class, ActionDataLoader.class,  FightDynamicAction.class);
        
        // Actor
        DataFactory.register("actor",  ActorData.class, ActorDataLoader.class, Actor.class);
        
         //ActorAnim
        DataFactory.register("actorAnimCurveMove",  ActorAnimData.class, ActorAnimDataLoader.class, ActorCurveMove.class);
        
        // ActorLogic
        DataFactory.register("actorLogicFight",  ActorLogicData.class, ActorLogicDataLoader.class, FightActorLogic.class);
        DataFactory.register("actorLogicFollow",  ActorLogicData.class, ActorLogicDataLoader.class,  FollowActorLogic.class);
        DataFactory.register("actorLogicNotify",  ActorLogicData.class, ActorLogicDataLoader.class,  NotifyActorLogic.class);
        DataFactory.register("actorLogicPlayer",  ActorLogicData.class, ActorLogicDataLoader.class,  PlayerActorLogic.class);
        DataFactory.register("actorLogicPosition",  ActorLogicData.class, ActorLogicDataLoader.class,  PositionActorLogic.class);
        DataFactory.register("actorLogicSearchEnemy",  ActorLogicData.class, ActorLogicDataLoader.class,  SearchEnemyActorLogic.class);
        DataFactory.register("actorLogicAttributeChange",  ActorLogicData.class, ActorLogicDataLoader.class,  AttributeChangeActorLogic.class);
        DataFactory.register("actorLogicDefend",  ActorLogicData.class, ActorLogicDataLoader.class,  DefendActorLogic.class);
        DataFactory.register("actorLogicIdle",  ActorLogicData.class, ActorLogicDataLoader.class,  IdleActorLogic.class);
        DataFactory.register("actorLogicShop",  ActorLogicData.class, ActorLogicDataLoader.class,  ShopActorLogic.class);
        
        // Anim
        DataFactory.register("animMove",  AnimData.class, AnimDataLoader.class, MoveAnim.class); 
        DataFactory.register("animCurveMove",  AnimData.class, AnimDataLoader.class, CurveMoveAnim.class);
        DataFactory.register("animRotation",  AnimData.class, AnimDataLoader.class, RotationAnim.class);
        DataFactory.register("animRandomRotation",  AnimData.class, AnimDataLoader.class, RandomRotationAnim.class);
        DataFactory.register("animScale", AnimData.class, AnimDataLoader.class,  ScaleAnim.class);
        DataFactory.register("animColor",  AnimData.class, AnimDataLoader.class, ColorAnim.class);
        
        // Attribute
        DataFactory.register("attributeBoolean",  AttributeData.class, AttributeDataLoader.class, BooleanAttribute.class);
        DataFactory.register("attributeFloat",  AttributeData.class, AttributeDataLoader.class, FloatAttribute.class);
        DataFactory.register("attributeGroup",  AttributeData.class, AttributeDataLoader.class, GroupAttribute.class);
        DataFactory.register("attributeInteger",  AttributeData.class, AttributeDataLoader.class, IntegerAttribute.class);
        DataFactory.register("attributeLevelFloat",  AttributeData.class, AttributeDataLoader.class, LevelFloatAttribute.class);
        DataFactory.register("attributeLevelInteger",  AttributeData.class, AttributeDataLoader.class, LevelIntegerAttribute.class);
        DataFactory.register("attributeLimitInteger",  AttributeData.class, AttributeDataLoader.class, LimitIntegerAttribute.class);
        DataFactory.register("attributeLong",  AttributeData.class, AttributeDataLoader.class, LongAttribute.class);
        DataFactory.register("attributeString",  AttributeData.class, AttributeDataLoader.class, StringAttribute.class);
        DataFactory.register("attributeStringList",  AttributeData.class, AttributeDataLoader.class, StringListAttribute.class);
        
        // Bullet
        DataFactory.register("bulletSimple",  BulletData.class, BulletDataLoader.class, SimpleBullet.class);
        DataFactory.register("bulletStraight",  BulletData.class, BulletDataLoader.class, StraightBullet.class);
        DataFactory.register("bulletCurve",  BulletData.class, BulletDataLoader.class, CurveBullet.class);
        DataFactory.register("bulletCurveTrail",  BulletData.class, BulletDataLoader.class, CurveTrailBullet.class);
        
        // Channel
        DataFactory.register("channel",  ChannelData.class, ChannelDataLoader.class, SimpleChannel.class);
        
        // Chat
        DataFactory.register("chatGroup",  ChatData.class, ChatDataLoader.class, GroupChat.class);
        DataFactory.register("chatSend",  ChatData.class, ChatDataLoader.class, SendChat.class);
        DataFactory.register("chatShop",  ChatData.class, ChatDataLoader.class, ShopChat.class);
        DataFactory.register("chatSell",  ChatData.class, ChatDataLoader.class, SellChat.class);
        DataFactory.register("chatTask",  ChatData.class, ChatDataLoader.class, TaskChat.class);
        
        // Config
        DataFactory.register("config",  ConfigData.class, ConfigDataLoader.class, null);
        
        // Drop
        DataFactory.register("dropAttribute",  DropData.class, DropDataLoader.class, AttributeDrop.class);
        DataFactory.register("dropGroup",  DropData.class, DropDataLoader.class, GroupDrop.class);
        DataFactory.register("dropItem",  DropData.class, DropDataLoader.class, ItemDrop.class);
        DataFactory.register("dropSkin",  DropData.class, DropDataLoader.class, SkinDrop.class);
        
        // Effect
        DataFactory.register("effectHalo",  EffectData.class, EffectDataLoader.class, HaloEffect.class);
        DataFactory.register("effectParticle", EffectData.class, EffectDataLoader.class, ParticleEffect.class);
        DataFactory.register("effectGroup", EffectData.class, EffectDataLoader.class, GroupEffect.class);
        DataFactory.register("effectEncircleHalo", EffectData.class, EffectDataLoader.class, EncircleHaloEffect.class);
        DataFactory.register("effectTexture", EffectData.class, EffectDataLoader.class, TextureEffect.class);
        DataFactory.register("effectTextureCylinder", EffectData.class, EffectDataLoader.class, TextureCylinderEffect.class);
        DataFactory.register("effectModel", EffectData.class, EffectDataLoader.class, ModelEffect.class);
        DataFactory.register("effectSlideColor", EffectData.class, EffectDataLoader.class, SlideColorEffect.class);
        DataFactory.register("effectSlideColorSpline", EffectData.class, EffectDataLoader.class, SlideColorSplineEffect.class);
        DataFactory.register("effectSlideColorIOSpline", EffectData.class, EffectDataLoader.class, SlideColorIOSplineEffect.class);
        DataFactory.register("effectProjection", EffectData.class, EffectDataLoader.class, ProjectionEffect.class);
        
        // El
        DataFactory.register("elLevel",  ElData.class, ElDataLoader.class, LevelEl.class);
        DataFactory.register("elAttribute",  ElData.class, ElDataLoader.class, AttributeEl.class);
        DataFactory.register("elHit",  ElData.class, ElDataLoader.class, HitEl.class);
        
        // Emitter
        DataFactory.register("emitter",  EmitterData.class, EmitterDataLoader.class, Emitter.class);
        
        // Env
        DataFactory.register("envSky", EnvData.class, EnvDataLoader.class, SkyEnv.class);
        DataFactory.register("envWaterSimple", EnvData.class, EnvDataLoader.class, WaterSimpleEnv.class);
        DataFactory.register("envWaterAdvance", EnvData.class, EnvDataLoader.class, WaterAdvanceEnv.class);
        DataFactory.register("envBoundaryBox", EnvData.class, EnvDataLoader.class, BoundaryBoxEnv.class);
        DataFactory.register("envAudio", EnvData.class, EnvDataLoader.class, AudioEnv.class);
        DataFactory.register("envLightDirectional", EnvData.class, EnvDataLoader.class, LightDirectionalEnv.class);
        DataFactory.register("envLightAmbient", EnvData.class, EnvDataLoader.class, LightAmbientEnv.class);
        DataFactory.register("envShadow", EnvData.class, EnvDataLoader.class, ShadowEnv.class);
        DataFactory.register("envProxyPlatform", EnvData.class, EnvDataLoader.class, ProxyPlatformEnv.class);
        DataFactory.register("envPhysics", EnvData.class, EnvDataLoader.class, PhysicsEnv.class);
        DataFactory.register("envCameraChase", EnvData.class, EnvDataLoader.class, CameraChaseEnv.class);
        DataFactory.register("envModel", ModelEnvData.class, ModelEnvLoader.class, ModelEnv.class);
        DataFactory.register("envTerrain", ModelEnvData.class, ModelEnvLoader.class, TerrainEnv.class);
        DataFactory.register("envTree", ModelEnvData.class, PlantEnvLoader.class, TreeEnv.class);
        DataFactory.register("envGrass", ModelEnvData.class, PlantEnvLoader.class, PlantEnv.class);
        
        // Game
        DataFactory.register("gameRpg", GameData.class, GameDataLoader.class, RpgGame.class);
        DataFactory.register("gameStoryTreasure", GameData.class, GameDataLoader.class, StoryTreasureGame.class);
        DataFactory.register("gameStoryGb", GameData.class, GameDataLoader.class, StoryGbGame.class);
        DataFactory.register("gameStoryGuard", GameData.class, GameDataLoader.class, StoryGuardGame.class);
        DataFactory.register("gameSurvival", GameData.class, GameDataLoader.class, SurvivalGame.class);
        
        // GameLogic
        DataFactory.register("gameLogicPlayerDeadChecker", GameLogicData.class, GameLogicDataLoader.class, PlayerDeadCheckerGameLogic.class);
        DataFactory.register("gameLogicActorClean", GameLogicData.class, GameLogicDataLoader.class, ActorCleanGameLogic.class);
        DataFactory.register("gameLogicAttributeChange", GameLogicData.class, GameLogicDataLoader.class, AttributeChangeGameLogic.class);
        
        // Handler
        DataFactory.register("handlerTest", HandlerData.class, HandlerDataLoader.class, TestHandler.class);
        DataFactory.register("handlerSummon", HandlerData.class, HandlerDataLoader.class, SummonHandler.class);
        DataFactory.register("handlerAttribute", HandlerData.class, HandlerDataLoader.class, AttributeHandler.class);
        DataFactory.register("handlerOutfit", HandlerData.class, HandlerDataLoader.class, OutfitHandler.class);
        DataFactory.register("handlerWeapon", HandlerData.class, HandlerDataLoader.class, WeaponHandler.class);
        DataFactory.register("handlerSkill", HandlerData.class, HandlerDataLoader.class, SkillHandler.class);
        DataFactory.register("handlerSummonSkill", HandlerData.class, HandlerDataLoader.class, SummonSkillHandler.class);
        DataFactory.register("handlerSkillBook", HandlerData.class, HandlerDataLoader.class, SkillBookHandler.class);
        DataFactory.register("handlerStateGain", HandlerData.class, HandlerDataLoader.class, StateGainHandler.class);
        DataFactory.register("handlerStateRemove", HandlerData.class, HandlerDataLoader.class, StateRemoveHandler.class);
        DataFactory.register("handlerItemSkill", HandlerData.class, HandlerDataLoader.class, ItemSkillHandler.class);
        DataFactory.register("handlerMap", HandlerData.class, HandlerDataLoader.class, MapHandler.class);
        
        // HitChecker
        DataFactory.register("hitChecker",  HitCheckerData.class, HitCheckerDataLoader.class, SimpleHitChecker.class);
        
        // Item
        DataFactory.register("item",  ItemData.class, ItemDataLoader.class, null);

        // Magic
        DataFactory.register("magicState", MagicData.class, MagicDataLoader.class, StateMagic.class);
        DataFactory.register("magicAttributeHit", MagicData.class, MagicDataLoader.class, AttributeHitMagic.class);

        // Module 
        DataFactory.register("moduleAction",  ActionModuleData.class, ModuleDataLoader.class, ActionModule.class);
        DataFactory.register("moduleActor",  ModuleData.class, ModuleDataLoader.class, ActorModule.class);
        DataFactory.register("moduleAttribute",  ModuleData.class, ModuleDataLoader.class, AttributeModule.class);
        DataFactory.register("moduleChannel",  ModuleData.class, ModuleDataLoader.class, ChannelModule.class);
        DataFactory.register("moduleChat",  ModuleData.class, ModuleDataLoader.class, ChatModule.class);
        DataFactory.register("moduleDrop",  ModuleData.class, ModuleDataLoader.class, DropModule.class);
        DataFactory.register("moduleItem",  ModuleData.class, ModuleDataLoader.class, ItemModule.class);
        DataFactory.register("moduleLevel",  ModuleData.class, ModuleDataLoader.class, LevelModule.class);
        DataFactory.register("moduleLogic",  ModuleData.class, ModuleDataLoader.class, LogicModule.class);
        DataFactory.register("moduleResist",  ModuleData.class, ModuleDataLoader.class, ResistModule.class);
        DataFactory.register("moduleSkill",  ModuleData.class, ModuleDataLoader.class, SkillModule.class);
        DataFactory.register("moduleSkin",  ModuleData.class, ModuleDataLoader.class, SkinModule.class);
        DataFactory.register("moduleState",  ModuleData.class, ModuleDataLoader.class, StateModule.class);
        DataFactory.register("moduleTalent",  ModuleData.class, ModuleDataLoader.class, TalentModule.class);
        DataFactory.register("moduleTask",  ModuleData.class, ModuleDataLoader.class, TaskModule.class);
        
        // Position
        DataFactory.register("positionRandomSphere",  PositionData.class, PositionDataLoader.class, RandomSpherePosition.class);
        DataFactory.register("positionRandomBox",  PositionData.class, PositionDataLoader.class, RandomBoxPosition.class);
        DataFactory.register("positionRandomCircle",  PositionData.class, PositionDataLoader.class, RandomCirclePosition.class);
        DataFactory.register("positionFixedPoint",  PositionData.class, PositionDataLoader.class, FixedPosition.class);
        DataFactory.register("positionView",  PositionData.class, PositionDataLoader.class, ViewPosition.class);
        
        // Resist
        DataFactory.register("resistSimple",  ResistData.class, ResistDataLoader.class, SimpleResist.class);
        DataFactory.register("resistAll",  ResistData.class, ResistDataLoader.class, AllResist.class);
        
        // Scene
        DataFactory.register("scene", SceneData.class, SceneDataLoader.class, Scene.class);
        DataFactory.register("sceneRandom", SceneData.class, RandomSceneLoader.class, Scene.class);

        // Shape
        DataFactory.register("shapeBox",  ShapeData.class, ShapeDataLoader.class, BoxShape.class);
        
        // Skill
        DataFactory.register("skillWalk",  SkillData.class, SkillDataLoader.class, WalkSkill.class);
        DataFactory.register("skillRun",  SkillData.class, SkillDataLoader.class, RunSkill.class);
        DataFactory.register("skillWait",  SkillData.class, SkillDataLoader.class, WaitSkill.class);
        DataFactory.register("skillIdle",  SkillData.class, SkillDataLoader.class, IdleSkill.class);
        DataFactory.register("skillHurt",  SkillData.class, SkillDataLoader.class, HurtSkill.class);
        DataFactory.register("skillDead",  SkillData.class, SkillDataLoader.class, DeadSkill.class);
//        DataFactory.register("skillDeadRagdoll",  SkillData.class, SkillDataLoader.class, DeadRagdollSkill.class);
        DataFactory.register("skillAttack",  SkillData.class, SkillDataLoader.class, AttackSkill.class);
        DataFactory.register("skillShot",  SkillData.class, SkillDataLoader.class, ShotSkill.class);
        DataFactory.register("skillShotBow",  SkillData.class, SkillDataLoader.class, ShotBowSkill.class);
        DataFactory.register("skillSummon",  SkillData.class, SkillDataLoader.class, SummonSkill.class);
        DataFactory.register("skillBack",  SkillData.class, SkillDataLoader.class, BackSkill.class);
        DataFactory.register("skillReady",  SkillData.class, SkillDataLoader.class, ReadySkill.class);
        DataFactory.register("skillDefend",  SkillData.class, SkillDataLoader.class, DefendSkill.class);
        DataFactory.register("skillDuck",  SkillData.class, SkillDataLoader.class, DuckSkill.class);
        DataFactory.register("skillReset",  SkillData.class, SkillDataLoader.class, ResetSkill.class);
        DataFactory.register("skillSkin",  SkillData.class, SkillDataLoader.class, SkinSkill.class);

        // Skin
        DataFactory.register("skinOutfit",  SkinData.class, SkinDataLoader.class, OutfitSkin.class);
        DataFactory.register("skinWeapon",  SkinData.class, SkinDataLoader.class, WeaponSkin.class);
        
        // Slot
        DataFactory.register("slot",  SlotData.class, SlotDataLoader.class, null);
        
        // Sound
        DataFactory.register("sound",  SoundData.class, SoundDataLoader.class, Sound.class);
        
        // State
        DataFactory.register("stateAttribute", StateData.class, StateDataLoader.class, AttributeState.class);
        DataFactory.register("stateAttributeMove", StateData.class, StateDataLoader.class, MoveSpeedState.class);
        DataFactory.register("stateAttributeDynamic", StateData.class, StateDataLoader.class, AttributeDynamicState.class);
        DataFactory.register("stateSkillLocked", StateData.class, StateDataLoader.class, SkillLockedState.class);
        DataFactory.register("stateEssential", StateData.class, StateDataLoader.class, EssentialState.class);
        DataFactory.register("stateSkill", StateData.class, StateDataLoader.class, SkillState.class);
        DataFactory.register("stateClean", StateData.class, StateDataLoader.class, CleanState.class);
        DataFactory.register("stateGroup", StateData.class, StateDataLoader.class, GroupState.class);
        
        // Talent
        DataFactory.register("talentAttribute",  TalentData.class, TalentDataLoader.class, AttributeTalent.class);
        
        // Task
        DataFactory.register("taskCollect",  TaskData.class, TaskDataLoader.class, CollectTask.class);
                
        // View
        DataFactory.register("viewText",  ViewData.class, ViewDataLoader.class, TextView.class);
        DataFactory.register("viewTextPanel",  ViewData.class, ViewDataLoader.class, TextPanelView.class);
        DataFactory.register("viewTimer",  ViewData.class, ViewDataLoader.class, TimerView.class);
    }
    
    private static void loadData() {
        try {
            DataFactory.loadDataFile("/data/object/action.xml");
            DataFactory.loadDataFile("/data/object/actor.xml");
            DataFactory.loadDataFile("/data/object/actorAnim.xml");
            DataFactory.loadDataFile("/data/object/actorLogic.xml");
            DataFactory.loadDataFile("/data/object/anim.xml");
            DataFactory.loadDataFile("/data/object/attribute.xml");
            DataFactory.loadDataFile("/data/object/bullet.xml");
            DataFactory.loadDataFile("/data/object/channel.xml");
            DataFactory.loadDataFile("/data/object/chat.xml");
            DataFactory.loadDataFile("/data/object/config.xml");
            DataFactory.loadDataFile("/data/object/drop.xml");
            DataFactory.loadDataFile("/data/object/effect.xml");
            DataFactory.loadDataFile("/data/object/el.xml");
            DataFactory.loadDataFile("/data/object/emitter.xml");
            DataFactory.loadDataFile("/data/object/position.xml");
            DataFactory.loadDataFile("/data/object/env.xml");
            DataFactory.loadDataFile("/data/object/game.xml");
            DataFactory.loadDataFile("/data/object/gameLogic.xml");
            DataFactory.loadDataFile("/data/object/handler.xml");
            DataFactory.loadDataFile("/data/object/hitChecker.xml");
            DataFactory.loadDataFile("/data/object/magic.xml");
            DataFactory.loadDataFile("/data/object/module.xml");
            DataFactory.loadDataFile("/data/object/item.xml");
            DataFactory.loadDataFile("/data/object/resist.xml");
            DataFactory.loadDataFile("/data/object/scene.xml");
            DataFactory.loadDataFile("/data/object/shape.xml");
            
            // 技能
            DataFactory.loadDataFile("/data/object/skill.xml");
            DataFactory.loadDataFile("/data/object/skill_monster.xml");
            DataFactory.loadDataFile("/data/object/skill_skin.xml");
            
            // 装备、武器
            DataFactory.loadDataFile("/data/object/skin.xml");
            DataFactory.loadDataFile("/data/object/skin_weapon.xml");
            DataFactory.loadDataFile("/data/object/skin_male.xml");
            
            // 武器槽位配置
            DataFactory.loadDataFile("/data/object/slot.xml");
            
            DataFactory.loadDataFile("/data/object/sound.xml");
            DataFactory.loadDataFile("/data/object/state.xml");
            DataFactory.loadDataFile("/data/object/talent.xml");
            DataFactory.loadDataFile("/data/object/task.xml");
            DataFactory.loadDataFile("/data/object/view.xml");
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(LY.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(LY.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LY.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void registerMessage() {
        // Serializer.registerClass(Enum.class, new MyEnumSerializer());
        // 注：EnumSerializer在处理Collection和Set、Map等包含Enum类的字段时会报错
        // 所以一般字段不要使用如： Set<Enum>,Map<Enum,xxx>,List<Enum>作为字段。
        // throw new SerializerException("Class has no enum constants:" + c);
        
        // 对LinkedHashMap有序MAP的支持
        Serializer.registerClass(LinkedHashMap.class, new MapSerializer());
        
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
        
        // remove20160830
//        Serializer.registerClass(MessProtoSync.class);

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
        Serializer.registerClass(MessAttributeNumberAddValue.class);
        Serializer.registerClass(MessAttributeNumberHit.class);
        
        // Skill
        Serializer.registerClass(MessSkill.class);
        Serializer.registerClass(MessSkillAbstract.class);
        Serializer.registerClass(MessActorLookAt.class);
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
    
    /**
     * 获取Application
     * @return 
     */
    public static Application getApp() {
        return app;
    }

    public static AssetManager getAssetManager() {
        if (app == null) {
            DesktopAssetManager am = new DesktopAssetManager(Thread.currentThread().getContextClassLoader().getResource("com/jme3/asset/Desktop.cfg"));
            return am;
        }
        return app.getAssetManager();
    }
    
    public static RenderManager getRenderManager() {
        return app.getRenderManager();
    }
    
    public static InputManager getInputManager() {
        return app.getInputManager();
    }
    
    public static AppStateManager getStateManager() {
        return app.getStateManager();
    }

    public static AppSettings getSettings() {
        return settings;
    }
    
    public static BitmapFont getFont() {
        if (font == null) {
            font = getAssetManager().loadFont("data/font/chinese.fnt");
        }
        return font;
    }
    
    public static void setFont(BitmapFont font) {
        LY.font = font;
    }
    
    public static PlayState getPlayState() {
        return app.getStateManager().getState(PlayState.class);
    }
    
    /**
     * 获取当前光标位置
     * @return 
     */
    public static Vector2f getCursorPosition() {
        return app.getInputManager().getCursorPosition();
    }
    
    /**
     * @deprecated 
     * 获取当前游戏时间,返回游戏运行到当前时间的毫秒数
     * @return 
     */
    public static long getGameTime() {
        return System.currentTimeMillis();
    }
    
    public static long getGameNanoTime() {
        return System.nanoTime();
    }
    
    public static void preloadScene(Spatial spatial) {
        app.getRenderManager().preloadScene(spatial);
    }
}
