/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying;

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
import name.huliqing.luoying.data.ActionData;
import name.huliqing.luoying.data.ActorData;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.data.AnimData;
import name.huliqing.luoying.data.AttributeApply;
import name.huliqing.luoying.data.AttributeData;
import name.huliqing.luoying.data.AttributeMatch;
import name.huliqing.luoying.data.AttributeUse;
import name.huliqing.luoying.data.BulletData;
import name.huliqing.luoying.data.ChannelData;
import name.huliqing.luoying.data.ChatData;
import name.huliqing.luoying.data.ConfigData;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.data.CustomUserData;
import name.huliqing.luoying.data.DefineData;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.data.DropData;
import name.huliqing.luoying.data.DropItem;
import name.huliqing.luoying.data.ElData;
import name.huliqing.luoying.data.EmitterData;
import name.huliqing.luoying.data.ControlData;
import name.huliqing.luoying.data.EffectData;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.data.GameData;
import name.huliqing.luoying.data.GameLogicData;
import name.huliqing.luoying.data.HitCheckerData;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.data.MagicData;
import name.huliqing.luoying.data.ModelEntityData;
import name.huliqing.luoying.data.PositionData;
import name.huliqing.luoying.data.ResistData;
import name.huliqing.luoying.data.SceneData;
import name.huliqing.luoying.data.ShapeData;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.luoying.data.SlotData;
import name.huliqing.luoying.data.SoundData;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.data.TalentData;
import name.huliqing.luoying.data.TaskData;
import name.huliqing.luoying.data.ViewData;
import name.huliqing.luoying.mess.MessActionRun;
import name.huliqing.luoying.mess.MessActorAddSkill;
import name.huliqing.luoying.mess.MessActorFollow;
import name.huliqing.luoying.mess.MessActorKill;
import name.huliqing.luoying.mess.MessActorPhysics;
import name.huliqing.luoying.mess.MessActorSetGroup;
import name.huliqing.luoying.mess.MessActorSetLevel;
import name.huliqing.luoying.mess.MessActorSetTarget;
import name.huliqing.luoying.mess.MessActorTeam;
import name.huliqing.luoying.mess.MessActorTransform;
import name.huliqing.luoying.mess.MessActorTransformDirect;
import name.huliqing.luoying.mess.MessActorViewDir;
import name.huliqing.luoying.mess.MessAutoAttack;
import name.huliqing.luoying.mess.MessClient;
import name.huliqing.luoying.mess.MessPing;
import name.huliqing.luoying.mess.MessPlayActorLoaded;
import name.huliqing.luoying.mess.MessPlayActorSelect;
import name.huliqing.luoying.mess.MessPlayActorSelectResult;
import name.huliqing.luoying.mess.MessPlayClientExit;
import name.huliqing.luoying.mess.MessPlayGetClients;
import name.huliqing.luoying.mess.MessPlayGetGameData;
import name.huliqing.luoying.mess.MessPlayGetServerState;
import name.huliqing.luoying.mess.MessPlayLoadSavedActor;
import name.huliqing.luoying.mess.MessPlayLoadSavedActorResult;
import name.huliqing.luoying.mess.MessProtoAdd;
import name.huliqing.luoying.mess.MessProtoRemove;
import name.huliqing.luoying.mess.MessSCActorRemove;
import name.huliqing.luoying.mess.MessSCClientList;
import name.huliqing.luoying.mess.MessSCGameData;
import name.huliqing.luoying.mess.MessSCInitGameOK;
import name.huliqing.luoying.mess.MessSCServerState;
import name.huliqing.luoying.mess.MessActorLookAt;
import name.huliqing.luoying.mess.MessSkillWalk;
import name.huliqing.luoying.mess.MessSkinWeaponTakeOn;
import name.huliqing.luoying.mess.MessStateAdd;
import name.huliqing.luoying.mess.MessStateRemove;
import name.huliqing.luoying.mess.MessTalentAdd;
import name.huliqing.luoying.mess.MessTalentAddPoint;
import name.huliqing.luoying.mess.MessTaskAdd;
import name.huliqing.luoying.mess.MessTaskApplyItem;
import name.huliqing.luoying.mess.MessTaskComplete;
import name.huliqing.luoying.object.SyncData;
import name.huliqing.luoying.loader.ActionDataLoader;
import name.huliqing.luoying.object.action.DynamicFightAction;
import name.huliqing.luoying.object.action.PathFollowAction;
import name.huliqing.luoying.object.action.DynamicIdleAction;
import name.huliqing.luoying.object.action.PatrolIdleAction;
import name.huliqing.luoying.object.action.StaticIdleAction;
import name.huliqing.luoying.object.action.PathRunAction;
import name.huliqing.luoying.object.action.SimpleRunAction;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.loader.ActorDataLoader;
import name.huliqing.luoying.object.actoranim.ActorCurveMove;
import name.huliqing.luoying.loader.LogicDataLoader;
import name.huliqing.luoying.object.logic.AttributeChangeLogic;
import name.huliqing.luoying.object.logic.DefendLogic;
import name.huliqing.luoying.object.logic.FightLogic;
import name.huliqing.luoying.object.logic.FollowLogic;
import name.huliqing.luoying.object.logic.IdleLogic;
import name.huliqing.luoying.object.logic.NotifyLogic;
import name.huliqing.luoying.object.logic.PlayerLogic;
import name.huliqing.luoying.object.logic.PositionLogic;
import name.huliqing.luoying.object.logic.SearchEnemyLogic;
import name.huliqing.luoying.object.logic.ShopLogic;
import name.huliqing.luoying.loader.AnimDataLoader;
import name.huliqing.luoying.object.anim.ColorAnim;
import name.huliqing.luoying.object.anim.CurveMoveAnim;
import name.huliqing.luoying.object.anim.MoveAnim;
import name.huliqing.luoying.object.anim.RandomRotationAnim;
import name.huliqing.luoying.object.anim.RotationAnim;
import name.huliqing.luoying.object.anim.ScaleAnim;
import name.huliqing.luoying.loader.AttributeDataLoader;
import name.huliqing.luoying.loader.BulletDataLoader;
import name.huliqing.luoying.object.bullet.CurveBullet;
import name.huliqing.luoying.object.bullet.CurveTrailBullet;
import name.huliqing.luoying.object.bullet.SimpleBullet;
import name.huliqing.luoying.object.bullet.StraightBullet;
import name.huliqing.luoying.object.module.ChannelModule;
import name.huliqing.luoying.loader.ChannelDataLoader;
import name.huliqing.luoying.object.channel.SimpleChannel;
//import name.huliqing.luoying.loader.ChatDataLoader;
//import name.huliqing.luoying.object.chat.GroupChat;
//import name.huliqing.luoying.object.chat.SellChat;
//import name.huliqing.luoying.object.chat.SendChat;
//import name.huliqing.luoying.object.chat.ShopItemChat;
//import name.huliqing.luoying.object.chat.TaskChat;
import name.huliqing.luoying.loader.ConfigDataLoader;
import name.huliqing.luoying.object.module.ActionModule;
import name.huliqing.luoying.object.module.AttributeModule;
import name.huliqing.luoying.object.module.ActorModule;
//import name.huliqing.luoying.object.module.ChatModule;
import name.huliqing.luoying.object.module.ItemModule;
import name.huliqing.luoying.object.module.LogicModule;
import name.huliqing.luoying.object.module.SkinModule;
import name.huliqing.luoying.object.module.ResistModule;
import name.huliqing.luoying.object.module.SkillModule;
import name.huliqing.luoying.object.module.StateModule;
import name.huliqing.luoying.object.module.TalentModule;
import name.huliqing.luoying.object.module.TaskModule;
import name.huliqing.luoying.loader.DropDataLoader;
import name.huliqing.luoying.object.effect.EncircleHaloEffect;
import name.huliqing.luoying.object.effect.GroupEffect;
import name.huliqing.luoying.object.effect.HaloEffect;
import name.huliqing.luoying.object.effect.ModelEffect;
import name.huliqing.luoying.object.effect.ParticleEffect;
import name.huliqing.luoying.object.effect.ProjectionEffect;
import name.huliqing.luoying.object.effect.SlideColorEffect;
import name.huliqing.luoying.object.effect.SlideColorIOSplineEffect;
import name.huliqing.luoying.object.effect.SlideColorSplineEffect;
import name.huliqing.luoying.object.effect.TextureCylinderEffect;
import name.huliqing.luoying.object.effect.TextureEffect;
import name.huliqing.luoying.loader.ElDataLoader;
import name.huliqing.luoying.object.el.HitEl;
import name.huliqing.luoying.object.el.LevelEl;
import name.huliqing.luoying.object.el.AttributeEl;
import name.huliqing.luoying.object.emitter.Emitter;
import name.huliqing.luoying.loader.EmitterDataLoader;
import name.huliqing.luoying.object.env.AudioEnv;
//import name.huliqing.luoying.object.env.BoundaryBoxEnv;
import name.huliqing.luoying.object.env.ChaseCameraEnv;
import name.huliqing.luoying.object.env.AmbientLightEnv;
import name.huliqing.luoying.object.env.DirectionalLightEnv;
import name.huliqing.luoying.layer.service.ConfigService;
import name.huliqing.luoying.loader.DefineDataLoader;
import name.huliqing.luoying.loader.EffectDataLoader;
import name.huliqing.luoying.loader.EntityDataLoader;
import name.huliqing.luoying.object.env.PhysicsEnv;
import name.huliqing.luoying.object.env.PlatformProxyEnv;
import name.huliqing.luoying.object.env.ShadowEnv;
import name.huliqing.luoying.object.env.SkyEnv;
import name.huliqing.luoying.object.env.TerrainEnv;
import name.huliqing.luoying.object.env.TreeEnv;
import name.huliqing.luoying.object.env.AdvanceWaterEnv;
import name.huliqing.luoying.object.env.SimpleWaterEnv;
import name.huliqing.luoying.loader.GameDataLoader;
import name.huliqing.luoying.object.gamelogic.ActorCleanGameLogic;
import name.huliqing.luoying.object.gamelogic.AttributeChangeGameLogic;
import name.huliqing.luoying.loader.GameLogicDataLoader;
import name.huliqing.luoying.loader.HitCheckerDataLoader;
import name.huliqing.luoying.object.hitchecker.SimpleHitChecker;
import name.huliqing.luoying.loader.ItemDataLoader;
import name.huliqing.luoying.object.magic.AttributeHitMagic;
import name.huliqing.luoying.loader.MagicDataLoader;
import name.huliqing.luoying.object.magic.StateMagic;
import name.huliqing.luoying.object.position.FixedPosition;
import name.huliqing.luoying.loader.PositionDataLoader;
import name.huliqing.luoying.object.position.RandomBoxPosition;
import name.huliqing.luoying.object.position.RandomCirclePosition;
import name.huliqing.luoying.object.position.RandomSpherePosition;
import name.huliqing.luoying.object.position.ViewPosition;
import name.huliqing.luoying.object.resist.AllResist;
import name.huliqing.luoying.loader.ResistDataLoader;
import name.huliqing.luoying.object.resist.SimpleResist;
import name.huliqing.luoying.loader.SceneDataLoader;
import name.huliqing.luoying.object.shape.BoxShape;
import name.huliqing.luoying.loader.ShapeDataLoader;
import name.huliqing.luoying.object.skill.AttackSkill;
import name.huliqing.luoying.object.skill.BackSkill;
import name.huliqing.luoying.object.skill.DeadSkill;
import name.huliqing.luoying.object.skill.DefendSkill;
import name.huliqing.luoying.object.skill.DuckSkill;
import name.huliqing.luoying.object.skill.HurtSkill;
import name.huliqing.luoying.object.skill.IdleSkill;
import name.huliqing.luoying.object.skill.ReadySkill;
import name.huliqing.luoying.object.skill.ResetSkill;
import name.huliqing.luoying.object.skill.RunSkill;
import name.huliqing.luoying.object.skill.ShotBowSkill;
import name.huliqing.luoying.object.skill.ShotSkill;
import name.huliqing.luoying.loader.SkillDataLoader;
import name.huliqing.luoying.object.skill.SkinSkill;
import name.huliqing.luoying.object.skill.SummonSkill;
import name.huliqing.luoying.object.skill.WaitSkill;
import name.huliqing.luoying.object.skill.WalkSkill;
import name.huliqing.luoying.object.skin.OutfitSkin;
import name.huliqing.luoying.loader.SkinDataLoader;
import name.huliqing.luoying.object.skin.WeaponSkin;
import name.huliqing.luoying.loader.SlotDataLoader;
import name.huliqing.luoying.object.sound.Sound;
import name.huliqing.luoying.loader.SoundDataLoader;
import name.huliqing.luoying.object.state.AttributeDynamicState;
import name.huliqing.luoying.object.state.AttributeState;
import name.huliqing.luoying.object.state.CleanState;
import name.huliqing.luoying.object.state.EssentialState;
import name.huliqing.luoying.object.state.MoveSpeedState;
import name.huliqing.luoying.object.state.SkillLockedState;
import name.huliqing.luoying.object.state.SkillState;
import name.huliqing.luoying.loader.StateDataLoader;
import name.huliqing.luoying.object.talent.AttributeTalent;
import name.huliqing.luoying.loader.TalentDataLoader;
import name.huliqing.luoying.object.task.CollectTask;
import name.huliqing.luoying.loader.TaskDataLoader;
import name.huliqing.luoying.loader.ModuleDataLoader;
import name.huliqing.luoying.loader.PlantEnvLoader;
import name.huliqing.luoying.mess.MessActorSetLocation;
import name.huliqing.luoying.mess.MessAttributeNumberAddValue;
import name.huliqing.luoying.mess.MessAttributeNumberHit;
import name.huliqing.luoying.mess.MessItemAdd;
import name.huliqing.luoying.mess.MessItemRemove;
import name.huliqing.luoying.mess.MessItemUse;
import name.huliqing.luoying.mess.MessSkillPlay;
import name.huliqing.luoying.mess.MessSkinAdd;
import name.huliqing.luoying.mess.MessSkinAttach;
import name.huliqing.luoying.mess.MessSkinDetach;
import name.huliqing.luoying.mess.MessSkinRemove;
import name.huliqing.luoying.object.attribute.BooleanAttribute;
import name.huliqing.luoying.object.attribute.FloatAttribute;
import name.huliqing.luoying.object.attribute.GroupAttribute;
import name.huliqing.luoying.object.attribute.IntegerAttribute;
import name.huliqing.luoying.object.attribute.LevelFloatAttribute;
import name.huliqing.luoying.object.attribute.LevelIntegerAttribute;
import name.huliqing.luoying.object.attribute.LimitIntegerAttribute;
import name.huliqing.luoying.object.attribute.LongAttribute;
import name.huliqing.luoying.object.attribute.StringAttribute;
import name.huliqing.luoying.object.attribute.StringListAttribute;
import name.huliqing.luoying.object.define.MatDefine;
import name.huliqing.luoying.object.define.SkillTagDefine;
import name.huliqing.luoying.object.define.SkinPartDefine;
import name.huliqing.luoying.object.define.WeaponTypeDefine;
import name.huliqing.luoying.object.drop.AttributeDrop;
import name.huliqing.luoying.object.drop.GroupDrop;
import name.huliqing.luoying.object.drop.ItemDrop;
import name.huliqing.luoying.object.drop.SkinDrop;
import name.huliqing.luoying.object.env.GrassEnv;
import name.huliqing.luoying.object.env.ModelEnv;
import name.huliqing.luoying.object.game.SimpleGame;
import name.huliqing.luoying.object.item.AttributeItem;
import name.huliqing.luoying.object.item.BookItem;
//import name.huliqing.luoying.object.item.MapItem;
import name.huliqing.luoying.object.item.SimpleItem;
import name.huliqing.luoying.object.item.SkillItem;
import name.huliqing.luoying.object.item.StateItem;
import name.huliqing.luoying.object.item.StateRemoveItem;
import name.huliqing.luoying.object.item.SummonItem;
import name.huliqing.luoying.object.item.TestItem;
import name.huliqing.luoying.object.module.DropModule;
import name.huliqing.luoying.object.module.LevelModule;
import name.huliqing.luoying.object.module.PhysicsModule;
import name.huliqing.luoying.object.scene.SimpleScene;
import name.huliqing.luoying.object.slot.Slot;
import name.huliqing.luoying.object.state.GroupState;
import name.huliqing.luoying.xml.Data;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.xml.Proto;

/**
 * @author huliqing
 */
public class LuoYing {
    private static final Logger LOG = Logger.getLogger(LuoYing.class.getName());
    
    private static Application app;
    private static AppSettings settings; 
    private static BitmapFont font;
    
    /**
     * 初始化环境, 这个方法必须在
     * @param app
     * @param settings 
     */
    public static void initialize(Application app, AppSettings settings) {
        LuoYing.app = app;
        LuoYing.settings = settings;
        
        // 注册需要序列化的数据，对于网络版进行序列化时需要用到。
        registerSerializer();
        LOG.log(Level.INFO, "registerSerializer ok");
        
        // 注册数据处理器
        registerProcessor();
        LOG.log(Level.INFO, "registerProcessor ok.");
        
        // 注册messages,用于network通信
        registerMessage();
        LOG.log(Level.INFO, "registerMessage ok.");
        
        // 载入系统数据
        try {
            loadSysData();
            LOG.log(Level.INFO, "loadSysData ok.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        // 2.载入语言环境及系统配置
        Factory.get(ConfigService.class).loadGlobalConfig();
        Factory.get(ConfigService.class).loadLocale();
    }

    private static void registerSerializer() {

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
        Serializer.registerClass(ConnData.class);
        Serializer.registerClass(ControlData.class);
        Serializer.registerClass(CustomUserData.class);
        Serializer.registerClass(DefineData.class);
        Serializer.registerClass(DropData.class);
        Serializer.registerClass(EffectData.class);
        Serializer.registerClass(ElData.class);
        Serializer.registerClass(EmitterData.class);
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
    }
    
    private static void registerProcessor() {
        
        // Action
        DataFactory.register("actionStaticIdle", ActionData.class, ActionDataLoader.class, StaticIdleAction.class);
        DataFactory.register("actionDynamicIdle", ActionData.class, ActionDataLoader.class,  DynamicIdleAction.class);
        DataFactory.register("actionPatrolIdle", ActionData.class, ActionDataLoader.class,  PatrolIdleAction.class);
        DataFactory.register("actionPathRun", ActionData.class, ActionDataLoader.class,  PathRunAction.class);
        DataFactory.register("actionSimpleRun", ActionData.class, ActionDataLoader.class, SimpleRunAction.class);
        DataFactory.register("actionPathFollow", ActionData.class, ActionDataLoader.class,  PathFollowAction.class);
        DataFactory.register("actionDynamicFight", ActionData.class, ActionDataLoader.class,  DynamicFightAction.class);
        
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
        
//        // Chat
//        DataFactory.register("chatGroup",  ChatData.class, ChatDataLoader.class, GroupChat.class);
//        DataFactory.register("chatSend",  ChatData.class, ChatDataLoader.class, SendChat.class);
//        DataFactory.register("chatShopItem",  ChatData.class, ChatDataLoader.class, ShopItemChat.class);
//        DataFactory.register("chatSell",  ChatData.class, ChatDataLoader.class, SellChat.class);
//        DataFactory.register("chatTask",  ChatData.class, ChatDataLoader.class, TaskChat.class);
        
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
        DataFactory.register("envAdvanceWater", EntityData.class, EntityDataLoader.class, AdvanceWaterEnv.class);
        DataFactory.register("envAmbientLight", EntityData.class, EntityDataLoader.class, AmbientLightEnv.class);
        DataFactory.register("envAudio", EntityData.class, EntityDataLoader.class, AudioEnv.class);
//        DataFactory.register("envBoundaryBox", ModelEntityData.class, EntityDataLoader.class, BoundaryBoxEnv.class); // remove
        DataFactory.register("envChaseCamera", EntityData.class, EntityDataLoader.class, ChaseCameraEnv.class);
        DataFactory.register("envDirectionalLight", EntityData.class, EntityDataLoader.class, DirectionalLightEnv.class);
        DataFactory.register("envModel", ModelEntityData.class, EntityDataLoader.class, ModelEnv.class);
        DataFactory.register("envPhysics", EntityData.class, EntityDataLoader.class, PhysicsEnv.class);
        DataFactory.register("envPlatformProxy", EntityData.class, EntityDataLoader.class, PlatformProxyEnv.class);
        DataFactory.register("envShadow", EntityData.class, EntityDataLoader.class, ShadowEnv.class);
        DataFactory.register("envSimpleWater", EntityData.class, EntityDataLoader.class, SimpleWaterEnv.class);
        DataFactory.register("envSky", EntityData.class, EntityDataLoader.class, SkyEnv.class);
        DataFactory.register("envTerrain", ModelEntityData.class, EntityDataLoader.class, TerrainEnv.class);
        DataFactory.register("envTree", ModelEntityData.class, PlantEnvLoader.class, TreeEnv.class);
        DataFactory.register("envGrass", ModelEntityData.class, PlantEnvLoader.class, GrassEnv.class);
        
        
        // Game
        DataFactory.register("gameSimple", GameData.class, GameDataLoader.class, SimpleGame.class);
        
        // remove20161009,移动到了ly-luoying-desktop
//        DataFactory.register("gameStoryTreasure", GameData.class, GameDataLoader.class, StoryTreasureGame.class);
//        DataFactory.register("gameStoryGb", GameData.class, GameDataLoader.class, StoryGbGame.class);
//        DataFactory.register("gameStoryGuard", GameData.class, GameDataLoader.class, StoryGuardGame.class);
//        DataFactory.register("gameSurvival", GameData.class, GameDataLoader.class, SurvivalGame.class);
        
        // GameLogic
//        DataFactory.register("gameLogicPlayerDeadChecker", GameLogicData.class, GameLogicDataLoader.class, PlayerDeadCheckerGameLogic.class);
        DataFactory.register("gameLogicActorClean", GameLogicData.class, GameLogicDataLoader.class, ActorCleanGameLogic.class);
        DataFactory.register("gameLogicAttributeChange", GameLogicData.class, GameLogicDataLoader.class, AttributeChangeGameLogic.class);
        
        // HitChecker
        DataFactory.register("hitChecker",  HitCheckerData.class, HitCheckerDataLoader.class, SimpleHitChecker.class);
        
        // Item
        DataFactory.register("itemTest",  ItemData.class, ItemDataLoader.class, TestItem.class);
        DataFactory.register("itemAttribute",  ItemData.class, ItemDataLoader.class, AttributeItem.class);
        DataFactory.register("itemBook",  ItemData.class, ItemDataLoader.class, BookItem.class);
//        DataFactory.register("itemMap",  ItemData.class, ItemDataLoader.class, MapItem.class);
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
//        DataFactory.register("moduleChat",  ModuleData.class, ModuleDataLoader.class, ChatModule.class);
        DataFactory.register("moduleDrop",  ModuleData.class, ModuleDataLoader.class, DropModule.class);
        DataFactory.register("moduleItem",  ModuleData.class, ModuleDataLoader.class, ItemModule.class);
        DataFactory.register("moduleLevel",  ModuleData.class, ModuleDataLoader.class, LevelModule.class);
        DataFactory.register("moduleLogic",  ModuleData.class, ModuleDataLoader.class, LogicModule.class);
        DataFactory.register("modulePhysics",  ModuleData.class, ModuleDataLoader.class, PhysicsModule.class);
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
        DataFactory.register("scene", SceneData.class, SceneDataLoader.class, SimpleScene.class);

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
//        DataFactory.register("viewText",  ViewData.class, ViewDataLoader.class, TextView.class);
//        DataFactory.register("viewTextPanel",  ViewData.class, ViewDataLoader.class, TextPanelView.class);
//        DataFactory.register("viewTimer",  ViewData.class, ViewDataLoader.class, TimerView.class);
    }
    
    private static void loadSysData() throws LyException {
        
        // remove20161006,以后由其它实现去主动载入
        loadData("/LuoYing/Data/action.xml");
//        loadData("/LuoYing/Data/actor.xml");
//        loadData("/LuoYing/Data/actorAnim.xml");
//        loadData("/LuoYing/Data/anim.xml");
//        loadData("/LuoYing/Data/attribute.xml");
//        loadData("/LuoYing/Data/bullet.xml");
        loadData("/LuoYing/Data/channel.xml");
        loadData("/LuoYing/Data/config.xml");
//        loadData("/LuoYing/Data/define.xml");
//        loadData("/LuoYing/Data/drop.xml");
//        loadData("/LuoYing/Data/effect.xml");
//        loadData("/LuoYing/Data/el.xml");
//        loadData("/LuoYing/Data/emitter.xml");
        loadData("/LuoYing/Data/env.xml");
        loadData("/LuoYing/Data/game.xml");
//        loadData("/LuoYing/Data/gameLogic.xml");
//        loadData("/LuoYing/Data/hitChecker.xml");
//        loadData("/LuoYing/Data/item.xml");
//        loadData("/LuoYing/Data/logic.xml");
//        loadData("/LuoYing/Data/magic.xml");
        loadData("/LuoYing/Data/module.xml");
//        loadData("/LuoYing/Data/position.xml");
//        loadData("/LuoYing/Data/resist.xml");
        loadData("/LuoYing/Data/scene.xml");
//        loadData("/LuoYing/Data/shape.xml");
//
//        // 技能
//        loadData("/LuoYing/Data/skill.xml");
//        loadData("/LuoYing/Data/skill_monster.xml");
//        loadData("/LuoYing/Data/skill_skin.xml");
//
//        // 装备、武器
//        loadData("/LuoYing/Data/skin.xml");
//        loadData("/LuoYing/Data/skin_male.xml");
//        loadData("/LuoYing/Data/skin_weapon.xml");
//
//        // 武器槽位配置
//        loadData("/LuoYing/Data/slot.xml");
//
//        loadData("/LuoYing/Data/sound.xml");
//        loadData("/LuoYing/Data/state.xml");
//        loadData("/LuoYing/Data/talent.xml");
//        loadData("/LuoYing/Data/task.xml");
//        loadData("/LuoYing/Data/view.xml");
    }
    
    /**
     * 载入数据
     * @param dataFile
     * @throws LyException 
     */
    public static void loadData(String dataFile) throws LyException {
        loadData(LuoYing.class.getResourceAsStream(dataFile), null);
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
//        Serializer.registerClass(MessPlayInitGame.class);
        Serializer.registerClass(MessPlayLoadSavedActor.class);
        Serializer.registerClass(MessPlayLoadSavedActorResult.class);
        Serializer.registerClass(MessPlayClientExit.class);
        Serializer.registerClass(MessSCClientList.class);
        Serializer.registerClass(MessSCGameData.class);
        Serializer.registerClass(MessSCInitGameOK.class);
        Serializer.registerClass(MessSCServerState.class);
        
        // ---- Lan 
        Serializer.registerClass(MessPing.class);
        Serializer.registerClass(MessPlayActorSelect.class);
        
        Serializer.registerClass(MessProtoAdd.class);
        Serializer.registerClass(MessProtoRemove.class);

//        Serializer.registerClass(MessProtoUse.class);
//        Serializer.registerClass(MessMessage.class);
        Serializer.registerClass(MessPlayActorLoaded.class);
        Serializer.registerClass(MessSCActorRemove.class);
        Serializer.registerClass(MessPlayActorSelectResult.class);
        
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
//        Serializer.registerClass(MessActorSpeak.class);
        Serializer.registerClass(MessActorTeam.class);
        Serializer.registerClass(MessActorTransform.class);
        Serializer.registerClass(MessActorTransformDirect.class);
        Serializer.registerClass(MessActorViewDir.class);
        
        // Attribute
        Serializer.registerClass(MessAttributeNumberAddValue.class);
        Serializer.registerClass(MessAttributeNumberHit.class);
        
        // Chat
//        Serializer.registerClass(MessChatSell.class); 
//        Serializer.registerClass(MessChatSend.class); 
//        Serializer.registerClass(MessChatShop.class); 
        
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
//        Serializer.registerClass(MessSyncObject.class);
        Serializer.registerClass(SyncData.class);
        
        // Talents
        Serializer.registerClass(MessTalentAdd.class);
        Serializer.registerClass(MessTalentAddPoint.class);
        
        // Task
        Serializer.registerClass(MessTaskAdd.class);
        Serializer.registerClass(MessTaskApplyItem.class);
        Serializer.registerClass(MessTaskComplete.class);
        
        // View
//        Serializer.registerClass(MessViewAdd.class);
//        Serializer.registerClass(MessViewRemove.class);
        
        
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
        LuoYing.font = font;
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
