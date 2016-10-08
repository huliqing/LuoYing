/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly;

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
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.ly.data.ActionData;
import name.huliqing.ly.data.ActorData;
import name.huliqing.ly.data.LogicData;
import name.huliqing.ly.data.AnimData;
import name.huliqing.ly.data.AttributeApply;
import name.huliqing.ly.data.AttributeData;
import name.huliqing.ly.data.AttributeMatch;
import name.huliqing.ly.data.AttributeUse;
import name.huliqing.ly.data.BulletData;
import name.huliqing.ly.data.ChannelData;
import name.huliqing.ly.data.ChatData;
import name.huliqing.ly.data.ConfigData;
import name.huliqing.ly.data.ConnData;
import name.huliqing.ly.data.CustomUserData;
import name.huliqing.ly.data.DefineData;
import name.huliqing.ly.data.ModuleData;
import name.huliqing.ly.data.DropData;
import name.huliqing.ly.data.DropItem;
import name.huliqing.ly.data.EffectData;
import name.huliqing.ly.data.ElData;
import name.huliqing.ly.data.EmitterData;
import name.huliqing.ly.data.env.EnvData;
import name.huliqing.ly.data.GameData;
import name.huliqing.ly.data.GameLogicData;
import name.huliqing.ly.data.HitCheckerData;
import name.huliqing.ly.data.ItemData;
import name.huliqing.ly.data.MagicData;
import name.huliqing.ly.data.PositionData;
import name.huliqing.ly.data.ResistData;
import name.huliqing.ly.data.SceneData;
import name.huliqing.ly.data.ShapeData;
import name.huliqing.ly.data.SkillData;
import name.huliqing.ly.data.SkinData;
import name.huliqing.ly.data.SlotData;
import name.huliqing.ly.data.SoundData;
import name.huliqing.ly.data.StateData; 
import name.huliqing.ly.data.TalentData;
import name.huliqing.ly.data.TaskData;
import name.huliqing.ly.data.ViewData;
import name.huliqing.ly.mess.MessActionRun;
import name.huliqing.ly.mess.MessActorAddSkill;
import name.huliqing.ly.mess.MessActorFollow;
import name.huliqing.ly.mess.MessActorKill;
import name.huliqing.ly.mess.MessActorPhysics;
import name.huliqing.ly.mess.MessActorSetGroup;
import name.huliqing.ly.mess.MessActorSetLevel;
import name.huliqing.ly.mess.MessActorSetTarget;
import name.huliqing.ly.mess.MessActorSpeak;
import name.huliqing.ly.mess.MessActorTeam;
import name.huliqing.ly.mess.MessActorTransform;
import name.huliqing.ly.mess.MessActorTransformDirect;
import name.huliqing.ly.mess.MessActorViewDir;
import name.huliqing.ly.mess.MessAutoAttack;
import name.huliqing.ly.mess.MessChatSell;
import name.huliqing.ly.mess.MessChatSend;
import name.huliqing.ly.mess.MessChatShop;
import name.huliqing.ly.mess.MessClient;
import name.huliqing.ly.mess.MessMessage;
import name.huliqing.ly.mess.MessPing;
import name.huliqing.ly.mess.MessPlayActorLoaded;
import name.huliqing.ly.mess.MessPlayActorSelect;
import name.huliqing.ly.mess.MessPlayActorSelectResult;
import name.huliqing.ly.mess.MessPlayChangeGameState;
import name.huliqing.ly.mess.MessPlayClientExit;
import name.huliqing.ly.mess.MessPlayGetClients;
import name.huliqing.ly.mess.MessPlayGetGameData;
import name.huliqing.ly.mess.MessPlayGetServerState;
import name.huliqing.ly.mess.MessPlayInitGame;
import name.huliqing.ly.mess.MessPlayLoadSavedActor;
import name.huliqing.ly.mess.MessPlayLoadSavedActorResult;
import name.huliqing.ly.mess.MessProtoAdd;
import name.huliqing.ly.mess.MessProtoRemove;
import name.huliqing.ly.mess.MessProtoUse;
import name.huliqing.ly.mess.MessSCActorRemove;
import name.huliqing.ly.mess.MessSCClientList;
import name.huliqing.ly.mess.MessSCGameData;
import name.huliqing.ly.mess.MessSCInitGameOK;
import name.huliqing.ly.mess.MessSCServerState;
import name.huliqing.ly.mess.MessActorLookAt;
import name.huliqing.ly.mess.MessSkillWalk;
import name.huliqing.ly.mess.MessSkinWeaponTakeOn;
import name.huliqing.ly.mess.MessStateAdd;
import name.huliqing.ly.mess.MessStateRemove;
import name.huliqing.ly.mess.MessSyncObject;
import name.huliqing.ly.mess.MessTalentAdd;
import name.huliqing.ly.mess.MessTalentAddPoint;
import name.huliqing.ly.mess.MessTaskAdd;
import name.huliqing.ly.mess.MessTaskApplyItem;
import name.huliqing.ly.mess.MessTaskComplete;
import name.huliqing.ly.mess.MessViewAdd;
import name.huliqing.ly.mess.MessViewRemove;
import name.huliqing.ly.object.SyncData;
import name.huliqing.ly.loader.ActionDataLoader;
import name.huliqing.ly.object.action.FightDynamicAction;
import name.huliqing.ly.object.action.FollowPathAction;
import name.huliqing.ly.object.action.IdleDynamicAction;
import name.huliqing.ly.object.action.IdlePatrolAction;
import name.huliqing.ly.object.action.IdleStaticAction;
import name.huliqing.ly.object.action.RunPathAction;
import name.huliqing.ly.object.action.RunSimpleAction;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.loader.ActorDataLoader;
import name.huliqing.ly.object.actoranim.ActorCurveMove;
import name.huliqing.ly.loader.LogicDataLoader;
import name.huliqing.ly.object.logic.AttributeChangeLogic;
import name.huliqing.ly.object.logic.DefendLogic;
import name.huliqing.ly.object.logic.FightLogic;
import name.huliqing.ly.object.logic.FollowLogic;
import name.huliqing.ly.object.logic.IdleLogic;
import name.huliqing.ly.object.logic.NotifyLogic;
import name.huliqing.ly.object.logic.PlayerLogic;
import name.huliqing.ly.object.logic.PositionLogic;
import name.huliqing.ly.object.logic.SearchEnemyLogic;
import name.huliqing.ly.object.logic.ShopLogic;
import name.huliqing.ly.loader.AnimDataLoader;
import name.huliqing.ly.object.anim.ColorAnim;
import name.huliqing.ly.object.anim.CurveMoveAnim;
import name.huliqing.ly.object.anim.MoveAnim;
import name.huliqing.ly.object.anim.RandomRotationAnim;
import name.huliqing.ly.object.anim.RotationAnim;
import name.huliqing.ly.object.anim.ScaleAnim;
import name.huliqing.ly.loader.AttributeDataLoader;
import name.huliqing.ly.loader.BulletDataLoader;
import name.huliqing.ly.object.bullet.CurveBullet;
import name.huliqing.ly.object.bullet.CurveTrailBullet;
import name.huliqing.ly.object.bullet.SimpleBullet;
import name.huliqing.ly.object.bullet.StraightBullet;
import name.huliqing.ly.object.module.ChannelModule;
import name.huliqing.ly.loader.ChannelDataLoader;
import name.huliqing.ly.object.channel.SimpleChannel;
import name.huliqing.ly.loader.ChatDataLoader;
import name.huliqing.ly.object.chat.GroupChat;
import name.huliqing.ly.object.chat.SellChat;
import name.huliqing.ly.object.chat.SendChat;
import name.huliqing.ly.object.chat.ShopItemChat;
import name.huliqing.ly.object.chat.TaskChat;
import name.huliqing.ly.loader.ConfigDataLoader;
import name.huliqing.ly.object.module.ActionModule;
import name.huliqing.ly.object.module.AttributeModule;
import name.huliqing.ly.object.module.ActorModule;
import name.huliqing.ly.object.module.ChatModule;
import name.huliqing.ly.object.module.ItemModule;
import name.huliqing.ly.object.module.LogicModule;
import name.huliqing.ly.object.module.SkinModule;
import name.huliqing.ly.object.module.ResistModule;
import name.huliqing.ly.object.module.SkillModule;
import name.huliqing.ly.object.module.StateModule;
import name.huliqing.ly.object.module.TalentModule;
import name.huliqing.ly.object.module.TaskModule;
import name.huliqing.ly.loader.DropDataLoader;
import name.huliqing.ly.loader.EffectDataLoader;
import name.huliqing.ly.object.effect.EncircleHaloEffect;
import name.huliqing.ly.object.effect.GroupEffect;
import name.huliqing.ly.object.effect.HaloEffect;
import name.huliqing.ly.object.effect.ModelEffect;
import name.huliqing.ly.object.effect.ParticleEffect;
import name.huliqing.ly.object.effect.ProjectionEffect;
import name.huliqing.ly.object.effect.SlideColorEffect;
import name.huliqing.ly.object.effect.SlideColorIOSplineEffect;
import name.huliqing.ly.object.effect.SlideColorSplineEffect;
import name.huliqing.ly.object.effect.TextureCylinderEffect;
import name.huliqing.ly.object.effect.TextureEffect;
import name.huliqing.ly.loader.ElDataLoader;
import name.huliqing.ly.object.el.HitEl;
import name.huliqing.ly.object.el.LevelEl;
import name.huliqing.ly.object.el.AttributeEl;
import name.huliqing.ly.object.emitter.Emitter;
import name.huliqing.ly.loader.EmitterDataLoader;
import name.huliqing.ly.object.env.AudioEnv;
import name.huliqing.ly.object.env.BoundaryBoxEnv;
import name.huliqing.ly.object.env.CameraChaseEnv;
import name.huliqing.ly.loader.env.EnvDataLoader;
import name.huliqing.ly.object.env.LightAmbientEnv;
import name.huliqing.ly.object.env.LightDirectionalEnv;
import name.huliqing.ly.object.env.ModelEnv;
import name.huliqing.ly.data.env.ModelEnvData;
import name.huliqing.ly.layer.service.ConfigService;
import name.huliqing.ly.loader.DefineDataLoader;
import name.huliqing.ly.loader.env.ModelEnvLoader;
import name.huliqing.ly.object.env.PhysicsEnv;
import name.huliqing.ly.object.env.PlantEnv;
import name.huliqing.ly.loader.env.PlantEnvLoader;
import name.huliqing.ly.object.env.ProxyPlatformEnv;
import name.huliqing.ly.object.env.ShadowEnv;
import name.huliqing.ly.object.env.SkyEnv;
import name.huliqing.ly.object.env.TerrainEnv;
import name.huliqing.ly.object.env.TreeEnv;
import name.huliqing.ly.object.env.WaterAdvanceEnv;
import name.huliqing.ly.object.env.WaterSimpleEnv;
import name.huliqing.ly.loader.GameDataLoader;
import name.huliqing.ly.object.game.RpgGame;
import name.huliqing.ly.object.game.impl.StoryGbGame;
import name.huliqing.ly.object.game.impl.StoryGuardGame;
import name.huliqing.ly.object.game.impl.StoryTreasureGame;
import name.huliqing.ly.object.game.impl.SurvivalGame;
import name.huliqing.ly.object.gamelogic.ActorCleanGameLogic;
import name.huliqing.ly.object.gamelogic.AttributeChangeGameLogic;
import name.huliqing.ly.loader.GameLogicDataLoader;
import name.huliqing.ly.object.gamelogic.PlayerDeadCheckerGameLogic;
import name.huliqing.ly.loader.HitCheckerDataLoader;
import name.huliqing.ly.object.hitchecker.SimpleHitChecker;
import name.huliqing.ly.loader.ItemDataLoader;
import name.huliqing.ly.object.magic.AttributeHitMagic;
import name.huliqing.ly.loader.MagicDataLoader;
import name.huliqing.ly.object.magic.StateMagic;
import name.huliqing.ly.object.position.FixedPosition;
import name.huliqing.ly.loader.PositionDataLoader;
import name.huliqing.ly.object.position.RandomBoxPosition;
import name.huliqing.ly.object.position.RandomCirclePosition;
import name.huliqing.ly.object.position.RandomSpherePosition;
import name.huliqing.ly.object.position.ViewPosition;
import name.huliqing.ly.object.resist.AllResist;
import name.huliqing.ly.loader.ResistDataLoader;
import name.huliqing.ly.object.resist.SimpleResist;
import name.huliqing.ly.loader.RandomSceneLoader;
import name.huliqing.ly.object.scene.Scene;
import name.huliqing.ly.loader.SceneDataLoader;
import name.huliqing.ly.object.shape.BoxShape;
import name.huliqing.ly.loader.ShapeDataLoader;
import name.huliqing.ly.object.skill.AttackSkill;
import name.huliqing.ly.object.skill.BackSkill;
import name.huliqing.ly.object.skill.DeadSkill;
import name.huliqing.ly.object.skill.DefendSkill;
import name.huliqing.ly.object.skill.DuckSkill;
import name.huliqing.ly.object.skill.HurtSkill;
import name.huliqing.ly.object.skill.IdleSkill;
import name.huliqing.ly.object.skill.ReadySkill;
import name.huliqing.ly.object.skill.ResetSkill;
import name.huliqing.ly.object.skill.RunSkill;
import name.huliqing.ly.object.skill.ShotBowSkill;
import name.huliqing.ly.object.skill.ShotSkill;
import name.huliqing.ly.loader.SkillDataLoader;
import name.huliqing.ly.object.skill.SkinSkill;
import name.huliqing.ly.object.skill.SummonSkill;
import name.huliqing.ly.object.skill.WaitSkill;
import name.huliqing.ly.object.skill.WalkSkill;
import name.huliqing.ly.object.skin.OutfitSkin;
import name.huliqing.ly.loader.SkinDataLoader;
import name.huliqing.ly.object.skin.WeaponSkin;
import name.huliqing.ly.loader.SlotDataLoader;
import name.huliqing.ly.object.sound.Sound;
import name.huliqing.ly.loader.SoundDataLoader;
import name.huliqing.ly.object.state.AttributeDynamicState;
import name.huliqing.ly.object.state.AttributeState;
import name.huliqing.ly.object.state.CleanState;
import name.huliqing.ly.object.state.EssentialState;
import name.huliqing.ly.object.state.MoveSpeedState;
import name.huliqing.ly.object.state.SkillLockedState;
import name.huliqing.ly.object.state.SkillState;
import name.huliqing.ly.loader.StateDataLoader;
import name.huliqing.ly.object.talent.AttributeTalent;
import name.huliqing.ly.loader.TalentDataLoader;
import name.huliqing.ly.object.task.CollectTask;
import name.huliqing.ly.loader.TaskDataLoader;
import name.huliqing.ly.object.view.TextPanelView;
import name.huliqing.ly.object.view.TextView;
import name.huliqing.ly.object.view.TimerView;
import name.huliqing.ly.loader.ViewDataLoader;
import name.huliqing.ly.loader.ModuleDataLoader;
import name.huliqing.ly.mess.MessActorSetLocation;
import name.huliqing.ly.mess.MessAttributeNumberAddValue;
import name.huliqing.ly.mess.MessAttributeNumberHit;
import name.huliqing.ly.mess.MessItemAdd;
import name.huliqing.ly.mess.MessItemRemove;
import name.huliqing.ly.mess.MessItemUse;
import name.huliqing.ly.mess.MessSkillPlay;
import name.huliqing.ly.mess.MessSkinAdd;
import name.huliqing.ly.mess.MessSkinAttach;
import name.huliqing.ly.mess.MessSkinDetach;
import name.huliqing.ly.mess.MessSkinRemove;
import name.huliqing.ly.object.attribute.BooleanAttribute;
import name.huliqing.ly.object.attribute.FloatAttribute;
import name.huliqing.ly.object.attribute.GroupAttribute;
import name.huliqing.ly.object.attribute.IntegerAttribute;
import name.huliqing.ly.object.attribute.LevelFloatAttribute;
import name.huliqing.ly.object.attribute.LevelIntegerAttribute;
import name.huliqing.ly.object.attribute.LimitIntegerAttribute;
import name.huliqing.ly.object.attribute.LongAttribute;
import name.huliqing.ly.object.attribute.StringAttribute;
import name.huliqing.ly.object.attribute.StringListAttribute;
import name.huliqing.ly.object.define.MatDefine;
import name.huliqing.ly.object.define.SkillTagDefine;
import name.huliqing.ly.object.define.SkinPartDefine;
import name.huliqing.ly.object.define.WeaponTypeDefine;
import name.huliqing.ly.object.drop.AttributeDrop;
import name.huliqing.ly.object.drop.GroupDrop;
import name.huliqing.ly.object.drop.ItemDrop;
import name.huliqing.ly.object.drop.SkinDrop;
import name.huliqing.ly.object.item.AttributeItem;
import name.huliqing.ly.object.item.BookItem;
import name.huliqing.ly.object.item.MapItem;
import name.huliqing.ly.object.item.SimpleItem;
import name.huliqing.ly.object.item.SkillItem;
import name.huliqing.ly.object.item.StateItem;
import name.huliqing.ly.object.item.StateRemoveItem;
import name.huliqing.ly.object.item.SummonItem;
import name.huliqing.ly.object.item.TestItem;
import name.huliqing.ly.object.module.DropModule;
import name.huliqing.ly.object.module.LevelModule;
import name.huliqing.ly.object.slot.Slot;
import name.huliqing.ly.object.state.GroupState;
import name.huliqing.ly.state.PlayState;
import name.huliqing.ly.xml.Data;
import name.huliqing.ly.xml.DataFactory;
import name.huliqing.ly.xml.Proto;

/**
 * @author huliqing
 */
public class Ly {
    private static final Logger LOG = Logger.getLogger(Ly.class.getName());
    
    private static Application app;
    private static AppSettings settings; 
    private static BitmapFont font;
    
    /**
     * 初始化环境, 这个方法必须在
     * @param app
     * @param settings 
     * @throws name.huliqing.ly.LyException 
     */
    public static void initialize(Application app, AppSettings settings) throws LyException {
        Ly.app = app;
        Ly.settings = settings;
        
        // 注册需要序列化的数据，对于网络版进行序列化时需要用到。
        registerSerializer();
        LOG.log(Level.INFO, "registerSerializer ok");
        
        // 注册数据处理器
        registerProcessor();
        LOG.log(Level.INFO, "registerProcessor ok.");
        
        // 注册messages,用于network通信
        registerMessage();
        LOG.log(Level.INFO, "registerMessage ok.");
        
        // 载入数据
        loadSysData();
        LOG.log(Level.INFO, "loadSysData ok.");
        
        // 2.载入语言环境及系统配置
        Factory.get(ConfigService.class).loadGlobalConfig();
        Factory.get(ConfigService.class).loadLocale();
    }

    private static void registerSerializer() {

        Serializer.registerClass(CustomUserData.class);
        
        Serializer.registerClass(Proto.class);
        Serializer.registerClass(AttributeApply.class);
        Serializer.registerClass(AttributeMatch.class);
        Serializer.registerClass(AttributeUse.class);
        Serializer.registerClass(Data.class);
        Serializer.registerClass(DropItem.class);
        
        Serializer.registerClass(ActionData.class);
        Serializer.registerClass(ActorData.class);
        Serializer.registerClass(AnimData.class);
        Serializer.registerClass(AttributeData.class);
        Serializer.registerClass(BulletData.class);
        Serializer.registerClass(ChannelData.class);
        Serializer.registerClass(ChatData.class);
        Serializer.registerClass(ConfigData.class);
        Serializer.registerClass(DefineData.class);
        Serializer.registerClass(DropData.class);
        Serializer.registerClass(EffectData.class);
        Serializer.registerClass(ElData.class);
        Serializer.registerClass(EmitterData.class);
        Serializer.registerClass(EnvData.class);
        Serializer.registerClass(GameData.class);
        Serializer.registerClass(GameLogicData.class);
        Serializer.registerClass(HitCheckerData.class);
        Serializer.registerClass(ItemData.class);
        Serializer.registerClass(LogicData.class);
        Serializer.registerClass(MagicData.class);
        Serializer.registerClass(ModuleData.class);
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
        DataFactory.register("actionRunPath", ActionData.class, ActionDataLoader.class,  RunPathAction.class);
        DataFactory.register("actionRunSimple", ActionData.class, ActionDataLoader.class, RunSimpleAction.class);
        DataFactory.register("actionFollowPath", ActionData.class, ActionDataLoader.class,  FollowPathAction.class);
        DataFactory.register("actionFightDynamic", ActionData.class, ActionDataLoader.class,  FightDynamicAction.class);
        
        // Actor
        DataFactory.register("actor",  ActorData.class, ActorDataLoader.class, Actor.class);
        
        //ActorAnim
        DataFactory.register("actorAnimCurveMove",  AnimData.class, AnimDataLoader.class, ActorCurveMove.class);
        
        // ActorLogic
        DataFactory.register("logicFight",  LogicData.class, LogicDataLoader.class, FightLogic.class);
        DataFactory.register("logicFollow",  LogicData.class, LogicDataLoader.class,  FollowLogic.class);
        DataFactory.register("logicNotify",  LogicData.class, LogicDataLoader.class,  NotifyLogic.class);
        DataFactory.register("logicPlayer",  LogicData.class, LogicDataLoader.class,  PlayerLogic.class);
        DataFactory.register("logicPosition",  LogicData.class, LogicDataLoader.class,  PositionLogic.class);
        DataFactory.register("logicSearchEnemy",  LogicData.class, LogicDataLoader.class,  SearchEnemyLogic.class);
        DataFactory.register("logicAttributeChange",  LogicData.class, LogicDataLoader.class,  AttributeChangeLogic.class);
        DataFactory.register("logicDefend",  LogicData.class, LogicDataLoader.class,  DefendLogic.class);
        DataFactory.register("logicIdle",  LogicData.class, LogicDataLoader.class,  IdleLogic.class);
        DataFactory.register("logicShop",  LogicData.class, LogicDataLoader.class,  ShopLogic.class);
        
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
        DataFactory.register("chatShopItem",  ChatData.class, ChatDataLoader.class, ShopItemChat.class);
        DataFactory.register("chatSell",  ChatData.class, ChatDataLoader.class, SellChat.class);
        DataFactory.register("chatTask",  ChatData.class, ChatDataLoader.class, TaskChat.class);
        
        // Config
        DataFactory.register("config",  ConfigData.class, ConfigDataLoader.class, null);
        
        // Define
        DataFactory.register("defineSkillTag", DefineData.class, DefineDataLoader.class, SkillTagDefine.class);
        DataFactory.register("defineSkinPart", DefineData.class, DefineDataLoader.class, SkinPartDefine.class);
        DataFactory.register("defineWeaponType", DefineData.class, DefineDataLoader.class, WeaponTypeDefine.class);
        DataFactory.register("defineMat", DefineData.class, DefineDataLoader.class, MatDefine.class);
        
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
        
        // HitChecker
        DataFactory.register("hitChecker",  HitCheckerData.class, HitCheckerDataLoader.class, SimpleHitChecker.class);
        
        // Item
        DataFactory.register("itemTest",  ItemData.class, ItemDataLoader.class, TestItem.class);
        DataFactory.register("itemAttribute",  ItemData.class, ItemDataLoader.class, AttributeItem.class);
        DataFactory.register("itemBook",  ItemData.class, ItemDataLoader.class, BookItem.class);
        DataFactory.register("itemMap",  ItemData.class, ItemDataLoader.class, MapItem.class);
        DataFactory.register("itemSimple",  ItemData.class, ItemDataLoader.class, SimpleItem.class);
        DataFactory.register("itemSkill",  ItemData.class, ItemDataLoader.class, SkillItem.class);
        DataFactory.register("itemState",  ItemData.class, ItemDataLoader.class, StateItem.class);
        DataFactory.register("itemStateRemove",  ItemData.class, ItemDataLoader.class, StateRemoveItem.class);
        DataFactory.register("itemSummon",  ItemData.class, ItemDataLoader.class, SummonItem.class);

        // Magic
        DataFactory.register("magicState", MagicData.class, MagicDataLoader.class, StateMagic.class);
        DataFactory.register("magicAttributeHit", MagicData.class, MagicDataLoader.class, AttributeHitMagic.class);

        // Module 
        DataFactory.register("moduleAction",  ModuleData.class, ModuleDataLoader.class, ActionModule.class);
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
        DataFactory.register("slot",  SlotData.class, SlotDataLoader.class, Slot.class);
        
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
    
    private static void loadSysData() throws LyException {
        
        // remove20161006,以后由其它实现去主动载入
//        loadData("/data/object/action.xml");
//        loadData("/data/object/actor.xml");
//        loadData("/data/object/actorAnim.xml");
//        loadData("/data/object/anim.xml");
//        loadData("/data/object/attribute.xml");
//        loadData("/data/object/bullet.xml");
//        loadData("/data/object/channel.xml");
//        loadData("/data/object/chat.xml");
//        loadData("/data/object/config.xml");
//        loadData("/data/object/define.xml");
//        loadData("/data/object/drop.xml");
//        loadData("/data/object/effect.xml");
//        loadData("/data/object/el.xml");
//        loadData("/data/object/emitter.xml");
//        loadData("/data/object/env.xml");
//        loadData("/data/object/game.xml");
//        loadData("/data/object/gameLogic.xml");
//        loadData("/data/object/hitChecker.xml");
//        loadData("/data/object/item.xml");
//        loadData("/data/object/logic.xml");
//        loadData("/data/object/magic.xml");
//        loadData("/data/object/module.xml");
//        loadData("/data/object/position.xml");
//        loadData("/data/object/resist.xml");
//        loadData("/data/object/scene.xml");
//        loadData("/data/object/shape.xml");
//
//        // 技能
//        loadData("/data/object/skill.xml");
//        loadData("/data/object/skill_monster.xml");
//        loadData("/data/object/skill_skin.xml");
//
//        // 装备、武器
//        loadData("/data/object/skin.xml");
//        loadData("/data/object/skin_male.xml");
//        loadData("/data/object/skin_weapon.xml");
//
//        // 武器槽位配置
//        loadData("/data/object/slot.xml");
//
//        loadData("/data/object/sound.xml");
//        loadData("/data/object/state.xml");
//        loadData("/data/object/talent.xml");
//        loadData("/data/object/task.xml");
//        loadData("/data/object/view.xml");
    }
    
    /**
     * 载入数据
     * @param dataFile
     * @throws LyException 
     */
    public static void loadData(String dataFile) throws LyException {
        loadData(Ly.class.getResourceAsStream(dataFile), null);
    }
    
    /**
     * 载入数据
     * @param inputStream
     * @param encoding
     * @throws LyException 
     */
    public static void loadData(InputStream inputStream, String encoding) throws LyException {
        DataFactory.loadData(inputStream, encoding);
    }
    
    private static void registerMessage() {
        // Serializer.registerClass(Enum.class, new MyEnumSerializer());
        // 注：EnumSerializer在处理Collection和Set、Map等包含Enum类的字段时会报错
        // 所以一般字段不要使用如： Set<Enum>,Map<Enum,yxz>,List<Enum>作为字段。
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
        Serializer.registerClass(MessActorLookAt.class);
        Serializer.registerClass(MessActorPhysics.class);
        Serializer.registerClass(MessActorSetGroup.class);
        Serializer.registerClass(MessActorSetLevel.class);
        Serializer.registerClass(MessActorSetLocation.class);
        Serializer.registerClass(MessActorSetTarget.class);
        Serializer.registerClass(MessActorSpeak.class);
        Serializer.registerClass(MessActorTeam.class);
        Serializer.registerClass(MessActorTransform.class);
        Serializer.registerClass(MessActorTransformDirect.class);
        Serializer.registerClass(MessActorViewDir.class);
        
        // Attribute
        Serializer.registerClass(MessAttributeNumberAddValue.class);
        Serializer.registerClass(MessAttributeNumberHit.class);
        
        // Chat
        Serializer.registerClass(MessChatSell.class); 
        Serializer.registerClass(MessChatSend.class); 
        Serializer.registerClass(MessChatShop.class); 
        
        // Item
        Serializer.registerClass(MessItemAdd.class); 
        Serializer.registerClass(MessItemRemove.class); 
        Serializer.registerClass(MessItemUse.class); 
        
        // Skill
        Serializer.registerClass(MessSkillPlay.class);
        Serializer.registerClass(MessSkillWalk.class);
        
        // Skin
        Serializer.registerClass(MessSkinAdd.class);
        Serializer.registerClass(MessSkinAttach.class);
        Serializer.registerClass(MessSkinDetach.class);
        Serializer.registerClass(MessSkinRemove.class);
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
        Ly.font = font;
    }
    
    public static PlayState getPlayState() {
        return app.getStateManager().getState(PlayState.class);
    }
    
    /**
     * 获取当前光标位置
     * @deprecated 
     * @return 
     */
    public static Vector2f getCursorPosition() {
        return app.getInputManager().getCursorPosition();
    }
    
    /**
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
