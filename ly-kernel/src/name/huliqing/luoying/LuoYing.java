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
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.network.serializing.Serializer;
import com.jme3.network.serializing.serializers.MapSerializer;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;
import com.jme3.scene.UserData;
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
import name.huliqing.luoying.data.AttributeUse;
import name.huliqing.luoying.data.BulletData;
import name.huliqing.luoying.data.ChannelData;
import name.huliqing.luoying.data.ConfigData;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.data.DefineData;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.data.DropData;
import name.huliqing.luoying.data.DropItem;
import name.huliqing.luoying.data.ElData;
import name.huliqing.luoying.data.EmitterData;
import name.huliqing.luoying.data.EffectData;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.data.GameData;
import name.huliqing.luoying.data.GameLogicData;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.data.MagicData;
import name.huliqing.luoying.data.ModelEntityData;
import name.huliqing.luoying.data.PositionData;
import name.huliqing.luoying.data.ResistData;
import name.huliqing.luoying.data.SavableArrayList;
import name.huliqing.luoying.data.SceneData;
import name.huliqing.luoying.data.ProgressData;
import name.huliqing.luoying.data.SavableString;
import name.huliqing.luoying.data.ShapeData;
import name.huliqing.luoying.data.ShortcutData;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.luoying.data.SlotData;
import name.huliqing.luoying.data.SoundData;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.data.TalentData;
import name.huliqing.luoying.data.TaskData;
import name.huliqing.luoying.data.TradeObjectData;
import name.huliqing.luoying.data.define.TradeInfo;
import name.huliqing.luoying.mess.ActorPhysicsMess;
import name.huliqing.luoying.mess.ActorTransformMess;
import name.huliqing.luoying.mess.ActorTransformDirectMess;
import name.huliqing.luoying.mess.ActorViewDirMess;
import name.huliqing.luoying.mess.ActorFightMess;
import name.huliqing.luoying.mess.network.ClientMess;
import name.huliqing.luoying.mess.network.PingMess;
import name.huliqing.luoying.mess.ActorSelectMess;
import name.huliqing.luoying.mess.ActorSelectResultMess;
import name.huliqing.luoying.mess.network.ClientExitMess;
import name.huliqing.luoying.mess.network.GetClientsMess;
import name.huliqing.luoying.mess.network.GetGameDataMess;
import name.huliqing.luoying.mess.network.GetServerStateMess;
import name.huliqing.luoying.mess.ActorLoadSavedMess;
import name.huliqing.luoying.mess.ActorLoadSavedResultMess;
import name.huliqing.luoying.mess.network.ClientsMess;
import name.huliqing.luoying.mess.network.GameDataMess;
import name.huliqing.luoying.mess.network.ServerStateMess;
import name.huliqing.luoying.mess.ActorLookAtMess;
import name.huliqing.luoying.mess.SkillWalkMess;
import name.huliqing.luoying.mess.SkinWeaponTakeOnMess;
import name.huliqing.luoying.mess.TalentAddPointMess;
import name.huliqing.luoying.mess.TaskCompleteMess;
import name.huliqing.luoying.loader.ActionDataLoader;
import name.huliqing.luoying.object.action.DynamicFightAction;
import name.huliqing.luoying.object.action.PathFollowAction;
import name.huliqing.luoying.object.action.DynamicIdleAction;
import name.huliqing.luoying.object.action.PatrolIdleAction;
import name.huliqing.luoying.object.action.StaticIdleAction;
import name.huliqing.luoying.object.action.PathRunAction;
import name.huliqing.luoying.object.action.SimpleRunAction;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.actoranim.ActorCurveMove;
import name.huliqing.luoying.loader.LogicDataLoader;
import name.huliqing.luoying.object.logic.AttributeChangeLogic;
import name.huliqing.luoying.object.logic.DefendLogic;
import name.huliqing.luoying.object.logic.FightLogic;
import name.huliqing.luoying.object.logic.FollowLogic;
import name.huliqing.luoying.object.logic.IdleLogic;
import name.huliqing.luoying.object.logic.NotifyLogic;
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
import name.huliqing.luoying.loader.BulletDataLoader;
import name.huliqing.luoying.object.bullet.CurveBullet;
import name.huliqing.luoying.object.bullet.CurveTrailBullet;
import name.huliqing.luoying.object.bullet.SimpleBullet;
import name.huliqing.luoying.object.bullet.StraightBullet;
import name.huliqing.luoying.object.module.ChannelModule;
import name.huliqing.luoying.loader.ChannelDataLoader;
import name.huliqing.luoying.object.channel.SimpleChannel;
import name.huliqing.luoying.object.module.ActionModule;
import name.huliqing.luoying.object.module.ActorModule;
import name.huliqing.luoying.object.module.ItemModule;
import name.huliqing.luoying.object.module.LogicModule;
import name.huliqing.luoying.object.module.SkinModule;
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
import name.huliqing.luoying.object.emitter.Emitter;
import name.huliqing.luoying.loader.EmitterDataLoader;
import name.huliqing.luoying.object.entity.impl.AudioEntity;
import name.huliqing.luoying.object.entity.impl.ChaseCameraEntity;
import name.huliqing.luoying.object.entity.impl.AmbientLightEntity;
import name.huliqing.luoying.object.entity.impl.DirectionalLightEntity;
import name.huliqing.luoying.loader.ActorDataLoader;
import name.huliqing.luoying.loader.DefineDataLoader;
import name.huliqing.luoying.loader.EffectDataLoader;
import name.huliqing.luoying.loader.EntityDataLoader;
import name.huliqing.luoying.object.entity.impl.PhysicsEntity;
import name.huliqing.luoying.object.entity.impl.PlatformProxyEntity;
import name.huliqing.luoying.object.entity.impl.DirectionalLightFilterShadowEntity;
import name.huliqing.luoying.object.entity.impl.SkyBoxEntity;
import name.huliqing.luoying.object.entity.impl.SimpleTerrainEntity;
import name.huliqing.luoying.object.entity.impl.TreeEntity;
import name.huliqing.luoying.object.entity.impl.AdvanceWaterEntity;
import name.huliqing.luoying.object.entity.impl.SimpleWaterEntity;
import name.huliqing.luoying.loader.GameDataLoader;
import name.huliqing.luoying.object.gamelogic.ActorCleanGameLogic;
import name.huliqing.luoying.object.gamelogic.AttributeChangeGameLogic;
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
import name.huliqing.luoying.object.state.MoveSpeedState;
import name.huliqing.luoying.object.state.SkillLockedState;
import name.huliqing.luoying.object.state.SkillState;
import name.huliqing.luoying.loader.StateDataLoader;
import name.huliqing.luoying.object.talent.AttributeTalent;
import name.huliqing.luoying.loader.TalentDataLoader;
import name.huliqing.luoying.object.task.CollectTask;
import name.huliqing.luoying.loader.TaskDataLoader;
import name.huliqing.luoying.loader.PlantEnvLoader;
import name.huliqing.luoying.loader.RandomSceneDataLoader;
import name.huliqing.luoying.manager.ResManager;
import name.huliqing.luoying.mess.ActorSetLocationMess;
import name.huliqing.luoying.mess.EntityAddMess;
import name.huliqing.luoying.mess.EntityAddDataMess;
import name.huliqing.luoying.mess.EntityHitNumberAttributeMess;
import name.huliqing.luoying.mess.EntityHitAttributeMess;
import name.huliqing.luoying.mess.EntityRemoveMess;
import name.huliqing.luoying.mess.EntityRemoveDataMess;
import name.huliqing.luoying.mess.EntityUseDataMess;
import name.huliqing.luoying.mess.EntityUseDataByIdMess;
import name.huliqing.luoying.mess.RandomSeedMess;
import name.huliqing.luoying.mess.SceneLoadedMess;
import name.huliqing.luoying.mess.SkillPlayMess;
import name.huliqing.luoying.mess.network.RequestGameInitMess;
import name.huliqing.luoying.mess.network.RequestGameInitStartMess;
import name.huliqing.luoying.object.attribute.BooleanAttribute;
import name.huliqing.luoying.object.attribute.FloatAttribute;
import name.huliqing.luoying.object.attribute.GroupAttribute;
import name.huliqing.luoying.object.attribute.IntegerAttribute;
import name.huliqing.luoying.object.attribute.LevelFloatAttribute;
import name.huliqing.luoying.object.attribute.LevelIntegerAttribute;
import name.huliqing.luoying.object.attribute.LimitIntegerAttribute;
import name.huliqing.luoying.object.attribute.LongAttribute;
import name.huliqing.luoying.object.attribute.RelateBooleanAttribute;
import name.huliqing.luoying.object.attribute.StringAttribute;
import name.huliqing.luoying.object.attribute.StringListAttribute;
import name.huliqing.luoying.object.attribute.Vector2fAttribute;
import name.huliqing.luoying.object.attribute.Vector3fAttribute;
import name.huliqing.luoying.object.attribute.Vector4fAttribute;
import name.huliqing.luoying.object.define.MatDefine;
import name.huliqing.luoying.object.define.SkillTypeDefine;
import name.huliqing.luoying.object.define.SkinPartDefine;
import name.huliqing.luoying.object.define.WeaponTypeDefine;
import name.huliqing.luoying.object.drop.AttributeDrop;
import name.huliqing.luoying.object.drop.GroupDrop;
import name.huliqing.luoying.object.drop.ItemDrop;
import name.huliqing.luoying.object.drop.SkinDrop;
import name.huliqing.luoying.object.el.CustomEl;
import name.huliqing.luoying.object.el.STBooleanEl;
import name.huliqing.luoying.object.el.STNumberEl;
import name.huliqing.luoying.object.el.LNumberEl;
import name.huliqing.luoying.object.el.SBooleanEl;
import name.huliqing.luoying.object.el.SkillHitNumberEl;
import name.huliqing.luoying.object.entity.impl.DirectionalLightShadowEntity;
import name.huliqing.luoying.object.entity.impl.GrassEntity;
import name.huliqing.luoying.object.entity.impl.SimpleModelEntity;
import name.huliqing.luoying.object.entity.impl.UnshadedEntity;
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
import name.huliqing.luoying.object.module.ColorModule;
import name.huliqing.luoying.object.module.DropModule;
import name.huliqing.luoying.object.module.LevelModule;
import name.huliqing.luoying.object.module.PhysicsModule;
import name.huliqing.luoying.object.scene.SimpleScene;
import name.huliqing.luoying.object.progress.SimpleProgress;
import name.huliqing.luoying.object.slot.Slot;
import name.huliqing.luoying.object.state.BooleanAttributeState;
import name.huliqing.luoying.object.state.GroupState;
import name.huliqing.luoying.object.state.PrivateGroupState;
import name.huliqing.luoying.serializer.ColorRGBASerializer;
import name.huliqing.luoying.serializer.QuaternionSerializer;
import name.huliqing.luoying.serializer.UserDataSerializer;
import name.huliqing.luoying.serializer.Vector2fSerializer;
import name.huliqing.luoying.serializer.Vector4fSerializer;
import name.huliqing.luoying.xml.Data;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.xml.Proto;

/**
 * @author huliqing
 */
public class LuoYing {
    private static final Logger LOG = Logger.getLogger(LuoYing.class.getName());
    
    private static Application app;
//    private static AppSettings settings; 
    private static BitmapFont font;
    
    /**
     * 初始化环境, 这个方法必须在
     * @param app
     */
    public static void initialize(Application app) {
        LuoYing.app = app;
//        LuoYing.settings = settings;
        
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
            throw new LuoYingException("Could not load SysData!", e);
        }
        
        // 载入资源文件
        ResManager.loadResource("/LuoYing/Resources/resource_en_US", "utf-8", "en_US");
        ResManager.loadResource("/LuoYing/Resources/resource_zh_CN", "utf-8", "zh_CN");
        
    }

    private static void registerSerializer() {
        
        Serializer.registerClass(Vector2f.class,  new Vector2fSerializer());
//        Serializer.registerClass(Vector3f.class,  new Vector3fSerializer()); // Serializer已经内置
        Serializer.registerClass(Vector4f.class,  new Vector4fSerializer());
        Serializer.registerClass(Quaternion.class,  new QuaternionSerializer());
        Serializer.registerClass(ColorRGBA.class,  new ColorRGBASerializer());
        Serializer.registerClass(UserData.class,  new UserDataSerializer());
        Serializer.registerClass(LinkedHashMap.class, new MapSerializer());
        
        Serializer.registerClass(TradeInfo.class);
        
        Serializer.registerClass(Proto.class);
        Serializer.registerClass(AttributeApply.class);
        Serializer.registerClass(AttributeUse.class);
        Serializer.registerClass(Data.class);
        Serializer.registerClass(DropItem.class);
        
        Serializer.registerClass(ActionData.class);
        Serializer.registerClass(ActorData.class);
        Serializer.registerClass(AnimData.class);
        Serializer.registerClass(AttributeData.class);
        Serializer.registerClass(BulletData.class);
        Serializer.registerClass(ChannelData.class);
        Serializer.registerClass(ConfigData.class);
        Serializer.registerClass(ConnData.class);
        Serializer.registerClass(DefineData.class);
        Serializer.registerClass(DropData.class);
        Serializer.registerClass(EffectData.class);
        Serializer.registerClass(ElData.class);
        Serializer.registerClass(EmitterData.class);
        Serializer.registerClass(EntityData.class);
        Serializer.registerClass(GameData.class);
        Serializer.registerClass(GameLogicData.class);
        Serializer.registerClass(ItemData.class);
        Serializer.registerClass(LogicData.class);
        Serializer.registerClass(MagicData.class);
        Serializer.registerClass(ModelEntityData.class);
        Serializer.registerClass(ModuleData.class);
        Serializer.registerClass(PositionData.class);
        Serializer.registerClass(ProgressData.class);
        Serializer.registerClass(ResistData.class);
        Serializer.registerClass(SavableArrayList.class);
        Serializer.registerClass(SavableString.class);
        Serializer.registerClass(SceneData.class);
        Serializer.registerClass(ShapeData.class);
        Serializer.registerClass(ShortcutData.class);
        Serializer.registerClass(SkillData.class);
        Serializer.registerClass(SkinData.class);
        Serializer.registerClass(SlotData.class);
        Serializer.registerClass(SoundData.class);
        Serializer.registerClass(StateData.class);
        Serializer.registerClass(TalentData.class);
        Serializer.registerClass(TaskData.class);
        Serializer.registerClass(TradeObjectData.class);
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
        
        // Anim
        DataFactory.register("animMove",  AnimData.class, AnimDataLoader.class, MoveAnim.class); 
        DataFactory.register("animCurveMove",  AnimData.class, AnimDataLoader.class, CurveMoveAnim.class);
        DataFactory.register("animRotation",  AnimData.class, AnimDataLoader.class, RotationAnim.class);
        DataFactory.register("animRandomRotation",  AnimData.class, AnimDataLoader.class, RandomRotationAnim.class);
        DataFactory.register("animScale", AnimData.class, AnimDataLoader.class,  ScaleAnim.class);
        DataFactory.register("animColor",  AnimData.class, AnimDataLoader.class, ColorAnim.class);
        
        // Attribute
        DataFactory.register("attributeBoolean",  AttributeData.class, null, BooleanAttribute.class);
        DataFactory.register("attributeFloat",  AttributeData.class, null, FloatAttribute.class);
        DataFactory.register("attributeGroup",  AttributeData.class, null, GroupAttribute.class);
        DataFactory.register("attributeInteger",  AttributeData.class, null, IntegerAttribute.class);
        DataFactory.register("attributeLevelFloat",  AttributeData.class, null, LevelFloatAttribute.class);
        DataFactory.register("attributeLevelInteger",  AttributeData.class, null, LevelIntegerAttribute.class);
        DataFactory.register("attributeLimitInteger",  AttributeData.class, null, LimitIntegerAttribute.class);
        DataFactory.register("attributeLong",  AttributeData.class, null, LongAttribute.class);
        DataFactory.register("attributeRelateBoolean",  AttributeData.class, null, RelateBooleanAttribute.class);
        DataFactory.register("attributeString",  AttributeData.class, null, StringAttribute.class);
        DataFactory.register("attributeStringList",  AttributeData.class, null, StringListAttribute.class);
        DataFactory.register("attributeVector2f",  AttributeData.class, null, Vector2fAttribute.class);
        DataFactory.register("attributeVector3f",  AttributeData.class, null, Vector3fAttribute.class);
        DataFactory.register("attributeVector4f",  AttributeData.class, null, Vector4fAttribute.class);
        
        // Bullet
        DataFactory.register("bulletSimple",  BulletData.class, BulletDataLoader.class, SimpleBullet.class);
        DataFactory.register("bulletStraight",  BulletData.class, BulletDataLoader.class, StraightBullet.class);
        DataFactory.register("bulletCurve",  BulletData.class, BulletDataLoader.class, CurveBullet.class);
        DataFactory.register("bulletCurveTrail",  BulletData.class, BulletDataLoader.class, CurveTrailBullet.class);
        
        // Channel
        DataFactory.register("channel",  ChannelData.class, ChannelDataLoader.class, SimpleChannel.class);
        
        // Config
        DataFactory.register("config",  ConfigData.class, null, null);
        
        // Define
        DataFactory.register("defineSkillType", DefineData.class, DefineDataLoader.class, SkillTypeDefine.class);
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
        DataFactory.registerDataProcessor("elCustom",  CustomEl.class);
        DataFactory.registerDataProcessor("elLNumber",  LNumberEl.class);
        DataFactory.registerDataProcessor("elSBoolean",  SBooleanEl.class);
        DataFactory.registerDataProcessor("elSTBoolean",  STBooleanEl.class);
        DataFactory.registerDataProcessor("elSTNumber",  STNumberEl.class);
        DataFactory.registerDataProcessor("elSkillHitNumber",  SkillHitNumberEl.class);
        
        // Emitter
        DataFactory.register("emitter",  EmitterData.class, EmitterDataLoader.class, Emitter.class);
        
        // Env
        DataFactory.register("entityAdvanceWater", EntityData.class, EntityDataLoader.class, AdvanceWaterEntity.class);
        DataFactory.register("entityAmbientLight", EntityData.class, EntityDataLoader.class, AmbientLightEntity.class);
        DataFactory.register("entityAudio", ModelEntityData.class, EntityDataLoader.class, AudioEntity.class);
        DataFactory.register("entityChaseCamera", EntityData.class, EntityDataLoader.class, ChaseCameraEntity.class);
        DataFactory.register("entityDirectionalLight", EntityData.class, EntityDataLoader.class, DirectionalLightEntity.class);
        DataFactory.register("entityDirectionalLightFilterShadow", EntityData.class, EntityDataLoader.class, DirectionalLightFilterShadowEntity.class);
        DataFactory.register("entityDirectionalLightShadow", EntityData.class, EntityDataLoader.class, DirectionalLightShadowEntity.class);
        DataFactory.register("entityGrass", ModelEntityData.class, PlantEnvLoader.class, GrassEntity.class);
        DataFactory.register("entityPhysics", EntityData.class, EntityDataLoader.class, PhysicsEntity.class);
        DataFactory.register("entityPlatformProxy", EntityData.class, EntityDataLoader.class, PlatformProxyEntity.class);
        DataFactory.register("entitySimpleModel", ModelEntityData.class, EntityDataLoader.class, SimpleModelEntity.class);
        DataFactory.register("entitySimpleTerrain", ModelEntityData.class, EntityDataLoader.class, SimpleTerrainEntity.class);
        DataFactory.register("entitySimpleWater", ModelEntityData.class, EntityDataLoader.class, SimpleWaterEntity.class);
        DataFactory.register("entitySkyBox", ModelEntityData.class, EntityDataLoader.class, SkyBoxEntity.class);
        DataFactory.register("entityTree", ModelEntityData.class, PlantEnvLoader.class, TreeEntity.class);
        DataFactory.register("entityUnshaded", EntityData.class, EntityDataLoader.class, UnshadedEntity.class);
        
        // Game
        DataFactory.register("gameSimple", GameData.class, GameDataLoader.class, SimpleGame.class);
        
        // GameLogic
        DataFactory.register("gameLogicActorClean", GameLogicData.class, null, ActorCleanGameLogic.class);
        DataFactory.register("gameLogicAttributeChange", GameLogicData.class, null, AttributeChangeGameLogic.class);
        
        // Item
        DataFactory.register("itemTest",  ItemData.class, ItemDataLoader.class, TestItem.class);
        DataFactory.register("itemAttribute",  ItemData.class, ItemDataLoader.class, AttributeItem.class);
        DataFactory.register("itemBook",  ItemData.class, ItemDataLoader.class, BookItem.class);
        DataFactory.register("itemSimple",  ItemData.class, ItemDataLoader.class, SimpleItem.class);
        DataFactory.register("itemSkill",  ItemData.class, ItemDataLoader.class, SkillItem.class);
        DataFactory.register("itemState",  ItemData.class, ItemDataLoader.class, StateItem.class);
        DataFactory.register("itemStateRemove",  ItemData.class, ItemDataLoader.class, StateRemoveItem.class);
        DataFactory.register("itemSummon",  ItemData.class, ItemDataLoader.class, SummonItem.class);
//        DataFactory.register("itemMap",  ItemData.class, ItemDataLoader.class, MapItem.class);

        // Logic
        DataFactory.register("logicFight",  LogicData.class, LogicDataLoader.class, FightLogic.class);
        DataFactory.register("logicFollow",  LogicData.class, LogicDataLoader.class,  FollowLogic.class);
        DataFactory.register("logicNotify",  LogicData.class, LogicDataLoader.class,  NotifyLogic.class);
        DataFactory.register("logicPosition",  LogicData.class, LogicDataLoader.class,  PositionLogic.class);
        DataFactory.register("logicSearchEnemy",  LogicData.class, LogicDataLoader.class,  SearchEnemyLogic.class);
        DataFactory.register("logicAttributeChange",  LogicData.class, LogicDataLoader.class,  AttributeChangeLogic.class);
        DataFactory.register("logicDefend",  LogicData.class, LogicDataLoader.class,  DefendLogic.class);
        DataFactory.register("logicIdle",  LogicData.class, LogicDataLoader.class,  IdleLogic.class);
        DataFactory.register("logicShop",  LogicData.class, LogicDataLoader.class,  ShopLogic.class);
        
        // Magic
        DataFactory.register("magicState", MagicData.class, MagicDataLoader.class, StateMagic.class);
        DataFactory.register("magicAttributeHit", MagicData.class, MagicDataLoader.class, AttributeHitMagic.class);

        // Module 
        DataFactory.register("moduleAction",  ModuleData.class, null, ActionModule.class);
        DataFactory.register("moduleActor",  ModuleData.class, null, ActorModule.class);
        DataFactory.register("moduleChannel",  ModuleData.class, null, ChannelModule.class);
        DataFactory.register("moduleColor",  ModuleData.class, null, ColorModule.class);
        DataFactory.register("moduleDrop",  ModuleData.class, null, DropModule.class);
        DataFactory.register("moduleItem",  ModuleData.class, null, ItemModule.class);
        DataFactory.register("moduleLevel",  ModuleData.class, null, LevelModule.class);
        DataFactory.register("moduleLogic",  ModuleData.class, null, LogicModule.class);
        DataFactory.register("modulePhysics",  ModuleData.class, null, PhysicsModule.class);
        DataFactory.register("moduleSkill",  ModuleData.class, null, SkillModule.class);
        DataFactory.register("moduleSkin",  ModuleData.class, null, SkinModule.class);
        DataFactory.register("moduleState",  ModuleData.class, null, StateModule.class);
        DataFactory.register("moduleTalent",  ModuleData.class, null, TalentModule.class);
        DataFactory.register("moduleTask",  ModuleData.class, null, TaskModule.class);
        
        // Position
        DataFactory.register("positionRandomSphere",  PositionData.class, PositionDataLoader.class, RandomSpherePosition.class);
        DataFactory.register("positionRandomBox",  PositionData.class, PositionDataLoader.class, RandomBoxPosition.class);
        DataFactory.register("positionRandomCircle",  PositionData.class, PositionDataLoader.class, RandomCirclePosition.class);
        DataFactory.register("positionFixedPoint",  PositionData.class, PositionDataLoader.class, FixedPosition.class);
        DataFactory.register("positionView",  PositionData.class, PositionDataLoader.class, ViewPosition.class);
        
        // Progress
        DataFactory.register("progressSimple", ProgressData.class, null, SimpleProgress.class);
        
        // Resist
        DataFactory.register("resistSimple",  ResistData.class, ResistDataLoader.class, SimpleResist.class);
        DataFactory.register("resistAll",  ResistData.class, ResistDataLoader.class, AllResist.class);
        
        // Scene
        DataFactory.register("scene", SceneData.class, SceneDataLoader.class, SimpleScene.class);
        DataFactory.register("sceneRandom", SceneData.class, RandomSceneDataLoader.class, SimpleScene.class);
        
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
        DataFactory.register("stateSkill", StateData.class, StateDataLoader.class, SkillState.class);
        DataFactory.register("stateClean", StateData.class, StateDataLoader.class, CleanState.class);
        DataFactory.register("stateGroup", StateData.class, StateDataLoader.class, GroupState.class);
        DataFactory.register("statePrivateGroup", StateData.class, StateDataLoader.class, PrivateGroupState.class);
        DataFactory.register("stateBooleanAttribute", StateData.class, StateDataLoader.class, BooleanAttributeState.class);
        
        // Talent
        DataFactory.register("talentAttribute",  TalentData.class, TalentDataLoader.class, AttributeTalent.class);
        
        // Task
        DataFactory.register("taskCollect",  TaskData.class, TaskDataLoader.class, CollectTask.class);
    }
    
    private static void loadSysData() throws LuoYingException {
        
        loadData("/LuoYing/Data/action.xml");
        loadData("/LuoYing/Data/channel.xml");
        loadData("/LuoYing/Data/el.xml");
        loadData("/LuoYing/Data/entity.xml");
        loadData("/LuoYing/Data/game.xml");
        loadData("/LuoYing/Data/module.xml");
        loadData("/LuoYing/Data/scene.xml");
        loadData("/LuoYing/Data/progress.xml");
        
//        loadData("/LuoYing/Data/config.xml");
//        loadData("/LuoYing/Data/actor.xml");
//        loadData("/LuoYing/Data/actorAnim.xml");
//        loadData("/LuoYing/Data/anim.xml");
//        loadData("/LuoYing/Data/attribute.xml");
//        loadData("/LuoYing/Data/bullet.xml");
//        loadData("/LuoYing/Data/define.xml");
//        loadData("/LuoYing/Data/drop.xml");
//        loadData("/LuoYing/Data/effect.xml");
//        loadData("/LuoYing/Data/emitter.xml");
//        loadData("/LuoYing/Data/gameLogic.xml");
//        loadData("/LuoYing/Data/item.xml");
//        loadData("/LuoYing/Data/logic.xml");
//        loadData("/LuoYing/Data/magic.xml");
//        loadData("/LuoYing/Data/position.xml");
//        loadData("/LuoYing/Data/resist.xml");
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
     * 载入数据, 如：
     * <code>
     * <pre>
     * loadData("/data/game.xml");
     * loadData("/data/scene.xml");
     * ...
     * </pre>
     * </code>
     * @param dataFile
     * @throws LuoYingException 
     */
    public static void loadData(String dataFile) throws LuoYingException {
        loadData(LuoYing.class.getResourceAsStream(dataFile), null);
    }
    
    /**
     * 载入数据
     * @param inputStream
     * @param encoding
     * @throws LuoYingException 
     */
    public static void loadData(InputStream inputStream, String encoding) throws LuoYingException {
        DataFactory.loadData(inputStream, encoding);
    }
    
    private static void registerMessage() {
        // Network Message
        Serializer.registerClass(ClientExitMess.class);
        Serializer.registerClass(ClientMess.class);
        Serializer.registerClass(ClientsMess.class);
        Serializer.registerClass(GameDataMess.class);
        Serializer.registerClass(GetClientsMess.class);
        Serializer.registerClass(GetGameDataMess.class);
        Serializer.registerClass(GetServerStateMess.class);
        Serializer.registerClass(PingMess.class);
        Serializer.registerClass(RequestGameInitMess.class);
        Serializer.registerClass(RequestGameInitStartMess.class);
        Serializer.registerClass(ServerStateMess.class);
        
        Serializer.registerClass(ActorFightMess.class);
        Serializer.registerClass(ActorLoadSavedMess.class);
        Serializer.registerClass(ActorLoadSavedResultMess.class);
        Serializer.registerClass(ActorLookAtMess.class);
        Serializer.registerClass(ActorPhysicsMess.class);
        Serializer.registerClass(ActorSelectMess.class);
        Serializer.registerClass(ActorSelectResultMess.class);
        Serializer.registerClass(ActorSetLocationMess.class);
        Serializer.registerClass(ActorTransformDirectMess.class);
        Serializer.registerClass(ActorTransformMess.class);
        Serializer.registerClass(ActorViewDirMess.class);
        
        
        // Entity
        Serializer.registerClass(EntityAddDataMess.class);
        Serializer.registerClass(EntityAddMess.class);
        Serializer.registerClass(EntityHitAttributeMess.class);
        Serializer.registerClass(EntityHitNumberAttributeMess.class);
        Serializer.registerClass(EntityRemoveDataMess.class);
        Serializer.registerClass(EntityRemoveMess.class);
        Serializer.registerClass(EntityUseDataByIdMess.class);
        Serializer.registerClass(EntityUseDataMess.class);
        
        // 随机种子
        Serializer.registerClass(RandomSeedMess.class);
        
        // Scene
        Serializer.registerClass(SceneLoadedMess.class);
        
        // Skill
        Serializer.registerClass(SkillPlayMess.class);
        Serializer.registerClass(SkillWalkMess.class);
        
        // Skin
        Serializer.registerClass(SkinWeaponTakeOnMess.class);
        
        // Talents
        Serializer.registerClass(TalentAddPointMess.class);
        
        // Task
        Serializer.registerClass(TaskCompleteMess.class);
        
    }
    
    /**
     * 获取Application
     * @return 
     */
    public final static Application getApp() {
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
        return app.getContext().getSettings();
    }
    
    public static BitmapFont getFont() {
        if (font == null) {
            font = getAssetManager().loadFont("Interface/Fonts/Default.fnt");
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
