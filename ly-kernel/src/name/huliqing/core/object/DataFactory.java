/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object;

import name.huliqing.core.xml.DataProcessor;
import name.huliqing.core.xml.DataLoader;
import com.jme3.network.serializing.Serializer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Config;
import name.huliqing.core.constants.DataTypeConstants;
import name.huliqing.core.data.ActionData;
import name.huliqing.core.data.ActorAnimData;
import name.huliqing.core.data.ActorData;
import name.huliqing.core.data.AnimData;
import name.huliqing.core.data.AttributeApply;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.data.AttributeUse;
import name.huliqing.core.data.BulletData;
import name.huliqing.core.data.ChannelData;
import name.huliqing.core.data.ChatData;
import name.huliqing.core.data.ConfigData;
import name.huliqing.core.xml.DataAttribute;
import name.huliqing.core.data.DropData;
import name.huliqing.core.data.DropItem;
import name.huliqing.core.data.EffectData;
import name.huliqing.core.data.ElData;
import name.huliqing.core.data.EmitterData;
import name.huliqing.core.data.EnvData;
import name.huliqing.core.data.GameData;
import name.huliqing.core.data.GameLogicData;
import name.huliqing.core.data.HandlerData;
import name.huliqing.core.data.HitCheckerData;
import name.huliqing.core.data.ItemData;
import name.huliqing.core.data.ActorLogicData;
import name.huliqing.core.data.MagicData;
import name.huliqing.core.data.PkgItemData;
import name.huliqing.core.data.PositionData;
import name.huliqing.core.xml.Proto;
import name.huliqing.core.data.ProtoData;
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
import name.huliqing.core.object.action.ActionDataLoader;
import name.huliqing.core.object.action.FightDynamicAction;
import name.huliqing.core.object.action.FollowPathAction;
import name.huliqing.core.object.action.IdleDynamicAction;
import name.huliqing.core.object.action.IdlePatrolAction;
import name.huliqing.core.object.action.IdleStaticAction;
import name.huliqing.core.object.action.RunPathAction;
import name.huliqing.core.object.action.RunSimpleAction;
import name.huliqing.core.object.actor.ActorControl;
import name.huliqing.core.object.actor.ActorDataLoader;
import name.huliqing.core.object.actor.ItemStore;
import name.huliqing.core.object.actor.SkillStore;
import name.huliqing.core.object.actoranim.ActorAnimDataLoader;
import name.huliqing.core.object.actoranim.ActorCurveMove;
import name.huliqing.core.object.anim.AnimDataLoader;
import name.huliqing.core.object.anim.ColorAnim;
import name.huliqing.core.object.anim.CurveMoveAnim;
import name.huliqing.core.object.anim.MoveAnim;
import name.huliqing.core.object.anim.RandomRotationAnim;
import name.huliqing.core.object.anim.RotationAnim;
import name.huliqing.core.object.anim.ScaleAnim;
import name.huliqing.core.object.attribute.AttributeDataLoader;
import name.huliqing.core.object.bullet.BulletDataLoader;
import name.huliqing.core.object.bullet.CurveBullet;
import name.huliqing.core.object.bullet.CurveTrailBullet;
import name.huliqing.core.object.bullet.SimpleBullet;
import name.huliqing.core.object.bullet.StraightBullet;
import name.huliqing.core.object.channel.ChannelDataLoader;
import name.huliqing.core.object.channel.SimpleChannel;
import name.huliqing.core.object.chat.ChatDataLoader;
import name.huliqing.core.object.chat.GroupChat;
import name.huliqing.core.object.chat.SellChat;
import name.huliqing.core.object.chat.SendChat;
import name.huliqing.core.object.chat.ShopChat;
import name.huliqing.core.object.chat.TaskChat;
import name.huliqing.core.object.config.ConfigDataLoader;
import name.huliqing.core.object.drop.DropDataLoader;
import name.huliqing.core.object.effect.EffectDataLoader;
import name.huliqing.core.object.effect.EncircleHaloEffect;
import name.huliqing.core.object.effect.GroupEffect;
import name.huliqing.core.object.effect.HaloEffect;
import name.huliqing.core.object.effect.ModelEffect;
import name.huliqing.core.object.effect.ParticleEffect;
import name.huliqing.core.object.effect.ProjectionEffect;
import name.huliqing.core.object.effect.SimpleGroupEffect;
import name.huliqing.core.object.effect.SlideColorEffect;
import name.huliqing.core.object.effect.SlideColorIOSplineEffect;
import name.huliqing.core.object.effect.SlideColorSplineEffect;
import name.huliqing.core.object.effect.TextureCylinderEffect;
import name.huliqing.core.object.effect.TextureEffect;
import name.huliqing.core.object.el.ElDataLoader;
import name.huliqing.core.object.el.HitEl;
import name.huliqing.core.object.el.LevelEl;
import name.huliqing.core.object.el.XpDropEl;
import name.huliqing.core.object.emitter.Emitter;
import name.huliqing.core.object.emitter.EmitterDataLoader;
import name.huliqing.core.object.env.AudioEnv;
import name.huliqing.core.object.env.BoundaryBoxEnv;
import name.huliqing.core.object.env.CameraChaseEnv;
import name.huliqing.core.object.env.LightDirectionalEnv;
import name.huliqing.core.object.env.EnvDataLoader;
import name.huliqing.core.object.env.LightAmbientEnv;
import name.huliqing.core.object.env.ModelEnv;
import name.huliqing.core.object.env.ModelEnvData;
import name.huliqing.core.object.env.ModelEnvLoader;
import name.huliqing.core.object.env.PhysicsEnv;
import name.huliqing.core.object.env.PlantEnv;
import name.huliqing.core.object.env.PlantEnvLoader;
import name.huliqing.core.object.env.ProxyPlatformEnv;
import name.huliqing.core.object.env.ShadowEnv;
import name.huliqing.core.object.env.SkyEnv;
import name.huliqing.core.object.env.TerrainEnv;
import name.huliqing.core.object.env.TreeEnv;
import name.huliqing.core.object.env.WaterAdvanceEnv;
import name.huliqing.core.object.env.WaterSimpleEnv;
import name.huliqing.core.object.game.GameDataLoader;
import name.huliqing.core.object.game.impl.StoryGbGame;
import name.huliqing.core.object.game.impl.StoryGuardGame;
import name.huliqing.core.object.game.impl.StoryTreasureGame;
import name.huliqing.core.object.game.impl.SurvivalGame;
import name.huliqing.core.object.handler.AttributeHandler;
import name.huliqing.core.object.handler.HandlerDataLoader;
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
import name.huliqing.core.object.hitchecker.HitCheckerDataLoader;
import name.huliqing.core.object.hitchecker.SimpleHitChecker;
import name.huliqing.core.object.item.ItemDataLoader;
import name.huliqing.core.object.actorlogic.AttributeChangeActorLogic;
import name.huliqing.core.object.actorlogic.DefendActorLogic;
import name.huliqing.core.object.actorlogic.FightActorLogic;
import name.huliqing.core.object.actorlogic.FollowActorLogic;
import name.huliqing.core.object.actorlogic.IdleActorLogic;
import name.huliqing.core.object.actorlogic.ActorLogicDataLoader;
import name.huliqing.core.object.actorlogic.NotifyActorLogic;
import name.huliqing.core.object.actorlogic.PlayerActorLogic;
import name.huliqing.core.object.actorlogic.PositionActorLogic;
import name.huliqing.core.object.actorlogic.SearchEnemyActorLogic;
import name.huliqing.core.object.actorlogic.ShopActorLogic;
import name.huliqing.core.object.game.RpgGame;
import name.huliqing.core.object.gamelogic.PlayerDeadCheckerGameLogic;
import name.huliqing.core.object.gamelogic.ActorCleanGameLogic;
import name.huliqing.core.object.gamelogic.AttributeChangeGameLogic;
import name.huliqing.core.object.gamelogic.GameLogicDataLoader;
import name.huliqing.core.object.magic.AttributeHitMagic;
import name.huliqing.core.object.magic.MagicDataLoader;
import name.huliqing.core.object.magic.StateMagic;
import name.huliqing.core.object.position.FixedPosition;
import name.huliqing.core.object.position.PositionDataLoader;
import name.huliqing.core.object.position.RandomBoxPosition;
import name.huliqing.core.object.position.RandomCirclePosition;
import name.huliqing.core.object.position.RandomSpherePosition;
import name.huliqing.core.object.position.ViewPosition;
import name.huliqing.core.object.resist.AllResist;
import name.huliqing.core.object.resist.ResistDataLoader;
import name.huliqing.core.object.resist.SimpleResist;
import name.huliqing.core.object.scene.RandomSceneLoader;
import name.huliqing.core.object.scene.Scene;
import name.huliqing.core.object.scene.SceneDataLoader;
import name.huliqing.core.object.shape.BoxShape;
import name.huliqing.core.object.shape.ShapeDataLoader;
import name.huliqing.core.object.skill.SkillDataLoader;
import name.huliqing.core.object.skill.AttackSkill;
import name.huliqing.core.object.skill.BackSkill;
import name.huliqing.core.object.skill.DeadRagdollSkill;
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
import name.huliqing.core.object.skill.SkinSkill;
import name.huliqing.core.object.skill.SummonSkill;
import name.huliqing.core.object.skill.WaitSkill;
import name.huliqing.core.object.skill.WalkSkill;
import name.huliqing.core.object.skin.OutfitSkin;
import name.huliqing.core.object.skin.SkinDataLoader;
import name.huliqing.core.object.skin.WeaponSkin;
import name.huliqing.core.object.slot.SlotDataLoader;
import name.huliqing.core.object.sound.SoundDataLoader;
import name.huliqing.core.object.state.AttributeDynamicState;
import name.huliqing.core.object.state.AttributeState;
import name.huliqing.core.object.state.CleanState;
import name.huliqing.core.object.state.EssentialState;
import name.huliqing.core.object.state.MoveSpeedState;
import name.huliqing.core.object.state.SkillLockedState;
import name.huliqing.core.object.state.SkillState;
import name.huliqing.core.object.state.StateDataLoader;
import name.huliqing.core.object.talent.AttributeTalent;
import name.huliqing.core.object.talent.TalentDataLoader;
import name.huliqing.core.object.task.CollectTask;
import name.huliqing.core.object.task.TaskDataLoader;
import name.huliqing.core.object.view.TextPanelView;
import name.huliqing.core.object.view.TextView;
import name.huliqing.core.object.view.TimerView;
import name.huliqing.core.object.view.ViewDataLoader;

/**
 * 管理数据的各种加载和处理的工厂类
 * @author huliqing
 */
public class DataFactory {
    private static final Logger LOG = Logger.getLogger(DataFactory.class.getName());
    
    // DataType -> ProtoData，默认的数据容器，如果没有自定义的数据容器，则使用DEFAULT_DATA中匹配的数据类型作为数据容器
    private final static Map<Integer, Class<? extends ProtoData>> DEFAULT_DATAS = new HashMap<Integer, Class<? extends ProtoData>>();
    
    // DataType -> DataLoader,默认的用于载入数据的“载入器”，当找不到匹配的数据载入器时将根据proto的dataType来确
    // 定要使用哪一个数据载入器
    private final static Map<Integer, Class<? extends DataLoader>> DEFAULT_LOADERS = new HashMap<Integer, Class<? extends DataLoader>>();
    
    // DataType -> DataProcessor,默认的用于处理数据的“处理器”当找不到匹配的数据处理器时将根据proto的dataType来确
    // 定要使用哪一个数据处理器
    private final static Map<Integer, Class<? extends DataProcessor>> DEFAULT_PROCESSORS = new HashMap<Integer, Class<? extends DataProcessor>>();
    
    // TagName -> ProtoData,用于注册自定义的数据容器
    private final static Map<String, Class<? extends ProtoData>> TAG_DATAS = new HashMap<String, Class<? extends ProtoData>>();
    // TagName -> DataLoader, 自定义数据载入器
    private final static Map<String, Class<? extends DataLoader>> TAG_LOADERS = new HashMap<String, Class<? extends DataLoader>>();
    // TagName -> DataProcessor, 自定义数据处理器
    private final static Map<String, Class<? extends DataProcessor>> TAG_PROCESSORS = new HashMap<String, Class<? extends DataProcessor>>();

    /**
     * 注册一个数据类型
     * @param tagName 
     * @param dataTypeClass 
     */
    public static void registerDataType(String tagName, Class<? extends ProtoData> dataTypeClass) {
        TAG_DATAS.put(tagName, dataTypeClass);
        // 注：Serializer.registerClass不能放在静态代码块(static{})中执行,否则注册无效。
        // 这个方法只能在运行时，即起动应用后执行。
        Serializer.registerClass(dataTypeClass);
        if (Config.debug) {
            LOG.log(Level.INFO, "Serializer registerClass={0}", dataTypeClass);
        }
    }
    
    /**
     * 注册一个数据载入器
     * @param tagName
     * @param dataLoaderClass 
     */
    public static void registerTagLoader(String tagName, Class<? extends DataLoader> dataLoaderClass) {
        if (dataLoaderClass == null) {
            TAG_LOADERS.remove(tagName);
            return;
        }
        TAG_LOADERS.put(tagName, dataLoaderClass);
    }
    
    /** 
     * 注册一个数据处理器
     * @param tagName 
     * @param dataProcessorClass 
     */
    public static void registerTagProcessor(String tagName, Class<? extends DataProcessor> dataProcessorClass) {
        if (dataProcessorClass == null) {
            TAG_PROCESSORS.remove(tagName);
            return;
        }
        TAG_PROCESSORS.put(tagName, dataProcessorClass);
    }
    
    /**
     * 注册数据类型、数据载入器及数据处理器
     * @param tagName
     * @param dataTypeClass
     * @param dataLoaderClass
     * @param dataProcessorClass 
     */
    public static void register(String tagName
            , Class<? extends ProtoData> dataTypeClass
            , Class<? extends DataLoader> dataLoaderClass
            , Class<? extends DataProcessor> dataProcessorClass
            ) {
        registerDataType(tagName, dataTypeClass);
        registerTagLoader(tagName, dataLoaderClass);
        registerTagProcessor(tagName, dataProcessorClass);
    }
    
    /**
     * 通过ID来创建并载入Data
     * @param <T>
     * @param id
     * @return 
     */
    public static <T extends ProtoData> T createData(String id) {
        Proto proto = ProtoUtils.getProto(id);
        if (proto == null) {
            throw new NullPointerException("Could not find object, id=" + id);
        }
        
        try {
            String dataClass = proto.getDataClass();
            if (dataClass == null) {
                throw new NullPointerException("No \"dataClass\"  set for proto, id=" + id + ", proto=" + proto);
            }
            ProtoData protoData = (ProtoData) Class.forName(dataClass).newInstance();
            
            String dataLoader = proto.getDataLoader();
            if (dataLoader == null) {
                throw new NullPointerException("No \"dataLoader\" set for proto, id=" + id + ", proto=" + proto);
            }
            DataLoader dl = (DataLoader) Class.forName(dataLoader).newInstance();
            protoData.setId(id);
            dl.load(proto, protoData);
            return (T) protoData;
        } catch (NullPointerException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        } catch (InstantiationException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        } catch (IllegalAccessException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
        return null;
    }
    
    /**
     * 注册一个数据处理器
     * @param <T>
     * @param data
     * @return 
     */
    public static <T extends DataProcessor> T createProcessor(ProtoData data) {
        if (data == null) {
            LOG.log(Level.WARNING, "Data could not be null");
            return null;
        }
        Class<? extends DataProcessor> dpClass = TAG_PROCESSORS.get(data.getTagName());
        if (dpClass == null) {
            throw new NullPointerException("Could not find data processor to createProcessor"
                    + ", tagName=" + data.getTagName() 
                    + ", dataId=" + data.getId());
        }
        try {
            DataProcessor dp = dpClass.newInstance();
            dp.setData(data);
            return (T) dp;
        } catch (Exception ex) {
            throw new RuntimeException("Could not create processor! tagName=" + data.getTagName() 
                    + ", dataId=" + data.getId()
                    + ", dataProcessor=" + dpClass.getName()
                    + ", error=" + ex.getMessage()
                    );
        }
    }
    
    /**
     * 初始化注册一些内置的数据处理器和装载器。
     */
    public static void initRegister() {
        // ---- Register Class
        
        Serializer.registerClass(Proto.class);
        Serializer.registerClass(ItemStore.class);
        Serializer.registerClass(SkillStore.class);
        Serializer.registerClass(AttributeApply.class);
        Serializer.registerClass(AttributeUse.class);
        Serializer.registerClass(DataAttribute.class);
        Serializer.registerClass(DropItem.class);
        Serializer.registerClass(PkgItemData.class);
        
        Serializer.registerClass(ActionData.class);
        Serializer.registerClass(ActorData.class);
        Serializer.registerClass(ActorAnimData.class);
        Serializer.registerClass(AnimData.class);
        Serializer.registerClass(AttributeData.class);
        Serializer.registerClass(BulletData.class);
        Serializer.registerClass(ChannelData.class);
        Serializer.registerClass(ChatData.class);
        Serializer.registerClass(ConfigData.class);
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
        Serializer.registerClass(ActorLogicData.class);
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
        
        // ---- Register Default data
        // 默认的数据容器所有新增的DataType都要注册
        DEFAULT_DATAS.put(DataTypeConstants.ACTION, ActionData.class);
        DEFAULT_DATAS.put(DataTypeConstants.ACTOR, ActorData.class);
        DEFAULT_DATAS.put(DataTypeConstants.ACTOR_ANIM, ActorAnimData.class);
        DEFAULT_DATAS.put(DataTypeConstants.ANIM, AnimData.class);
        DEFAULT_DATAS.put(DataTypeConstants.ATTRIBUTE, AttributeData.class);
        DEFAULT_DATAS.put(DataTypeConstants.BULLET, BulletData.class);
        DEFAULT_DATAS.put(DataTypeConstants.CHANNEL, ChannelData.class);
        DEFAULT_DATAS.put(DataTypeConstants.CHAT, ChatData.class);
        DEFAULT_DATAS.put(DataTypeConstants.CONFIG, ConfigData.class);
        DEFAULT_DATAS.put(DataTypeConstants.DROP, DropData.class);
        DEFAULT_DATAS.put(DataTypeConstants.EFFECT, EffectData.class);
        DEFAULT_DATAS.put(DataTypeConstants.EL, ElData.class);
        DEFAULT_DATAS.put(DataTypeConstants.EMITTER, EmitterData.class);
        DEFAULT_DATAS.put(DataTypeConstants.ENV, EnvData.class);
        DEFAULT_DATAS.put(DataTypeConstants.GAME, GameData.class);
        DEFAULT_DATAS.put(DataTypeConstants.GAME_LOGIC, GameLogicData.class);
        DEFAULT_DATAS.put(DataTypeConstants.HANDLER, HandlerData.class);
        DEFAULT_DATAS.put(DataTypeConstants.HIT_CHECKER, HitCheckerData.class);
        DEFAULT_DATAS.put(DataTypeConstants.ITEM, ItemData.class);
        DEFAULT_DATAS.put(DataTypeConstants.ACTOR_LOGIC, ActorLogicData.class);
        DEFAULT_DATAS.put(DataTypeConstants.MAGIC, MagicData.class);
        DEFAULT_DATAS.put(DataTypeConstants.POSITION, PositionData.class);
        DEFAULT_DATAS.put(DataTypeConstants.RESIST, ResistData.class);
        DEFAULT_DATAS.put(DataTypeConstants.SCENE, SceneData.class);
        DEFAULT_DATAS.put(DataTypeConstants.SHAPE, ShapeData.class);
        DEFAULT_DATAS.put(DataTypeConstants.SKILL, SkillData.class);
        DEFAULT_DATAS.put(DataTypeConstants.SKIN, SkinData.class);
        DEFAULT_DATAS.put(DataTypeConstants.SLOT, SlotData.class);
        DEFAULT_DATAS.put(DataTypeConstants.SOUND, SoundData.class);
        DEFAULT_DATAS.put(DataTypeConstants.STATE, StateData.class);
        DEFAULT_DATAS.put(DataTypeConstants.TALENT, TalentData.class);
        DEFAULT_DATAS.put(DataTypeConstants.TASK, TaskData.class);
        DEFAULT_DATAS.put(DataTypeConstants.VIEW, ViewData.class);
        
        // 初始化默认的数据载入器,当一个标签找不到合适的载入器时，系统将根据标签的数据类型来选择一个数据载入器
        DEFAULT_LOADERS.put(DataTypeConstants.ACTION, ActionDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.ACTOR, ActorDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.ACTOR_ANIM, ActorAnimDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.ACTOR_LOGIC, ActorLogicDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.ANIM, AnimDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.ATTRIBUTE, AttributeDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.BULLET, BulletDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.CHANNEL, ChannelDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.CHAT, ChatDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.CONFIG, ConfigDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.DROP, DropDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.EFFECT, EffectDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.EL, ElDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.EMITTER, EmitterDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.ENV, EnvDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.GAME, GameDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.GAME_LOGIC, GameLogicDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.HANDLER, HandlerDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.HIT_CHECKER, HitCheckerDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.ITEM, ItemDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.MAGIC, MagicDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.POSITION, PositionDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.RESIST, ResistDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.SCENE, SceneDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.SHAPE, ShapeDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.SKILL, SkillDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.SKIN, SkinDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.SLOT, SlotDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.SOUND, SoundDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.STATE, StateDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.TALENT, TalentDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.TASK, TaskDataLoader.class);
        DEFAULT_LOADERS.put(DataTypeConstants.VIEW, ViewDataLoader.class);

        // 初始化默认的数据处理器
        DEFAULT_PROCESSORS.put(DataTypeConstants.SCENE, Scene.class);
        
        // ---- Register Tag
        
        // Action
        registerTagProcessor("actionIdleStatic",  IdleStaticAction.class);
        registerTagProcessor("actionIdleDynamic",  IdleDynamicAction.class);
        registerTagProcessor("actionIdlePatrol",  IdlePatrolAction.class);
        registerTagProcessor("actionRun",  RunPathAction.class);
        registerTagProcessor("actionRunSimple",  RunSimpleAction.class);
        registerTagProcessor("actionFollow",  FollowPathAction.class);
        registerTagProcessor("actionFight",  FightDynamicAction.class);
        
        // Actor
        registerTagProcessor("actor",  ActorControl.class);

        // ActorAnim
        registerTagProcessor("actorAnimCurveMove",  ActorCurveMove.class);
        
        // ActorLogic
        registerTagProcessor("actorLogicFight",  FightActorLogic.class);
        registerTagProcessor("actorLogicFollow",  FollowActorLogic.class);
        registerTagProcessor("actorLogicNotify",  NotifyActorLogic.class);
        registerTagProcessor("actorLogicPlayer",  PlayerActorLogic.class);
        registerTagProcessor("actorLogicPosition",  PositionActorLogic.class);
        registerTagProcessor("actorLogicSearchEnemy",  SearchEnemyActorLogic.class);
        registerTagProcessor("actorLogicAttributeChange",  AttributeChangeActorLogic.class);
        registerTagProcessor("actorLogicDefend",  DefendActorLogic.class);
        registerTagProcessor("actorLogicIdle",  IdleActorLogic.class);
        registerTagProcessor("actorLogicShop",  ShopActorLogic.class);
        
        // Anim
        registerTagProcessor("animMove",  MoveAnim.class); 
        registerTagProcessor("animCurveMove",  CurveMoveAnim.class);
        registerTagProcessor("animRotation",  RotationAnim.class);
        registerTagProcessor("animRandomRotation",  RandomRotationAnim.class);
        registerTagProcessor("animScale",  ScaleAnim.class);
        registerTagProcessor("animColor",  ColorAnim.class);
        
        // Bullet
        registerTagProcessor("bulletSimple",  SimpleBullet.class);
        registerTagProcessor("bulletStraight",  StraightBullet.class);
        registerTagProcessor("bulletCurve",  CurveBullet.class);
        registerTagProcessor("bulletCurveTrail",  CurveTrailBullet.class);
        
        // Channel
        registerTagProcessor("channel",  SimpleChannel.class);
        
        // Chat
        registerTagProcessor("chatGroup",  GroupChat.class);
        registerTagProcessor("chatSend",  SendChat.class);
        registerTagProcessor("chatShop",  ShopChat.class);
        registerTagProcessor("chatSell",  SellChat.class);
        registerTagProcessor("chatTask",  TaskChat.class);
        
        // Effect
        registerTagProcessor("effectHalo",  HaloEffect.class);
        registerTagProcessor("effectParticle", ParticleEffect.class);
        registerTagProcessor("effectSimpleGroup", SimpleGroupEffect.class);
        registerTagProcessor("effectGroup", GroupEffect.class);
        registerTagProcessor("effectEncircleHalo", EncircleHaloEffect.class);
        registerTagProcessor("effectTexture", TextureEffect.class);
        registerTagProcessor("effectTextureCylinder", TextureCylinderEffect.class);
        registerTagProcessor("effectModel", ModelEffect.class);
        registerTagProcessor("effectSlideColor", SlideColorEffect.class);
        registerTagProcessor("effectSlideColorSpline", SlideColorSplineEffect.class);
        registerTagProcessor("effectSlideColorIOSpline", SlideColorIOSplineEffect.class);
        registerTagProcessor("effectProjection", ProjectionEffect.class);
        
        // El
        registerTagProcessor("elLevel",  LevelEl.class);
        registerTagProcessor("elHit",  HitEl.class);
        registerTagProcessor("elXpDrop",  XpDropEl.class);
        
        // Emitter
        registerTagProcessor("emitter",  Emitter.class);
        
        // Env
        registerTagProcessor("envSky", SkyEnv.class);
        registerTagProcessor("envWaterSimple", WaterSimpleEnv.class);
        registerTagProcessor("envWaterAdvance", WaterAdvanceEnv.class);
        registerTagProcessor("envBoundaryBox", BoundaryBoxEnv.class);
        registerTagProcessor("envAudio", AudioEnv.class);
        registerTagProcessor("envLightDirectional", LightDirectionalEnv.class);
        registerTagProcessor("envLightAmbient", LightAmbientEnv.class);
        registerTagProcessor("envShadow", ShadowEnv.class);
        registerTagProcessor("envProxyPlatform", ProxyPlatformEnv.class);
        registerTagProcessor("envPhysics", PhysicsEnv.class);
        registerTagProcessor("envCameraChase", CameraChaseEnv.class);
        register("envModel", ModelEnvData.class, ModelEnvLoader.class, ModelEnv.class);
        register("envTerrain", ModelEnvData.class, ModelEnvLoader.class, TerrainEnv.class);
        register("envTree", ModelEnvData.class, PlantEnvLoader.class, TreeEnv.class);
        register("envGrass", ModelEnvData.class, PlantEnvLoader.class, PlantEnv.class);
        
        // Game
        registerTagProcessor("gameRpg", RpgGame.class);
        registerTagProcessor("gameStoryTreasure", StoryTreasureGame.class);
        registerTagProcessor("gameStoryGb", StoryGbGame.class);
        registerTagProcessor("gameStoryGuard", StoryGuardGame.class);
        registerTagProcessor("gameSurvival", SurvivalGame.class);
        
        // GameLogic
        registerTagProcessor("gameLogicPlayerDeadChecker", PlayerDeadCheckerGameLogic.class);
        registerTagProcessor("gameLogicActorClean", ActorCleanGameLogic.class);
        registerTagProcessor("gameLogicAttributeChange", AttributeChangeGameLogic.class);
        
        // HitChecker
        registerTagProcessor("hitChecker",  SimpleHitChecker.class);

        // Position
        registerTagProcessor("positionRandomSphere",  RandomSpherePosition.class);
        registerTagProcessor("positionRandomBox",  RandomBoxPosition.class);
        registerTagProcessor("positionRandomCircle",  RandomCirclePosition.class);
        registerTagProcessor("positionFixedPoint",  FixedPosition.class);
        registerTagProcessor("positionView",  ViewPosition.class);
        
        // Resist
        registerTagProcessor("resistSimple",  SimpleResist.class);
        registerTagProcessor("resistAll",  AllResist.class);

        // Shape
        registerTagProcessor("shapeBox",  BoxShape.class);
        
        // Handler
        registerTagProcessor("handlerTest", TestHandler.class);
        registerTagProcessor("handlerSummon", SummonHandler.class);
        registerTagProcessor("handlerAttribute", AttributeHandler.class);
        registerTagProcessor("handlerOutfit", OutfitHandler.class);
        registerTagProcessor("handlerWeapon", WeaponHandler.class);
        registerTagProcessor("handlerSkill", SkillHandler.class);
        registerTagProcessor("handlerSummonSkill", SummonSkillHandler.class);
        registerTagProcessor("handlerSkillBook", SkillBookHandler.class);
        registerTagProcessor("handlerStateGain", StateGainHandler.class);
        registerTagProcessor("handlerStateRemove", StateRemoveHandler.class);
        registerTagProcessor("handlerItemSkill", ItemSkillHandler.class);
        registerTagProcessor("handlerMap", MapHandler.class);
        
        // Scene
        registerTagProcessor("scene", Scene.class);
        register("sceneRandom", SceneData.class, RandomSceneLoader.class, Scene.class);
        
        // Magic
        registerTagProcessor("magicState", StateMagic.class);
        registerTagProcessor("magicAttributeHit", AttributeHitMagic.class);

        // Skill
        registerTagProcessor("skillWalk",  WalkSkill.class);
        registerTagProcessor("skillRun",  RunSkill.class);
        registerTagProcessor("skillWait",  WaitSkill.class);
        registerTagProcessor("skillIdle",  IdleSkill.class);
        registerTagProcessor("skillHurt",  HurtSkill.class);
        registerTagProcessor("skillDead",  DeadSkill.class);
        registerTagProcessor("skillDeadRagdoll",  DeadRagdollSkill.class);
        registerTagProcessor("skillAttack",  AttackSkill.class);
        registerTagProcessor("skillShot",  ShotSkill.class);
        registerTagProcessor("skillShotBow",  ShotBowSkill.class);
        registerTagProcessor("skillSummon",  SummonSkill.class);
        registerTagProcessor("skillBack",  BackSkill.class);
        registerTagProcessor("skillReady",  ReadySkill.class);
        registerTagProcessor("skillDefend",  DefendSkill.class);
        registerTagProcessor("skillDuck",  DuckSkill.class);
        registerTagProcessor("skillReset",  ResetSkill.class);
        registerTagProcessor("skillSkin",  SkinSkill.class);

        // Skin
        registerTagProcessor("skinOutfit",  OutfitSkin.class);
        registerTagProcessor("skinWeapon",  WeaponSkin.class);
        
        // State
        registerTagProcessor("stateAttribute", AttributeState.class);
        registerTagProcessor("stateAttributeMove", MoveSpeedState.class);
        registerTagProcessor("stateAttributeDynamic", AttributeDynamicState.class);
        registerTagProcessor("stateSkillLocked", SkillLockedState.class);
        registerTagProcessor("stateEssential", EssentialState.class);
        registerTagProcessor("stateSkill", SkillState.class);
        registerTagProcessor("stateClean", CleanState.class);
        
        // Talent
        registerTagProcessor("talentAttribute",  AttributeTalent.class);
        
        // Task
        registerTagProcessor("taskCollect",  CollectTask.class);
                
        // View
        registerTagProcessor("viewText",  TextView.class);
        registerTagProcessor("viewTextPanel",  TextPanelView.class);
        registerTagProcessor("viewTimer",  TimerView.class);

    }
    
    /**
     * 使用tagName来查找一个已经注册的数据容器(ProtoData)，如果找不到已经注册的数据容器，则根据dataType的默认配置
     * 来确定是否使用哪一个数据容器。如果都找不到，则抛出异常。
     * @param tagName
     * @param dataType
     * @return 
     */
    public static Class<? extends ProtoData> findProtoData(String tagName, int dataType) {
        Class<? extends ProtoData> clz = TAG_DATAS.get(tagName);
        if (clz != null) {
            return clz;
        }
        clz = DEFAULT_DATAS.get(dataType);
        if (clz != null) {
            return clz;
        }
        LOG.log(Level.WARNING, "Not data class for tagName={0}, dataType={1}", new Object[] {tagName, dataType});
        return null;
    }
    
    /**
     * 使用tagName来查找一个已经注册的数据载入器（dataLoader)，如果找不到已经注册的数据载入器，则使用dataType来
     * 确定是否使用哪一个数据载入器。如果都找不到，则抛出异常。
     * @param tagName
     * @param dataType
     * @return 
     * @throws NullPointerException
     */
    public static Class<? extends DataLoader> findDataLoader(String tagName, int dataType) {
        Class<? extends DataLoader> clz = TAG_LOADERS.get(tagName);
        if (clz != null) {
            return clz;
        }
        clz = DEFAULT_LOADERS.get(dataType);
        if (clz != null) {
            return clz;
        }
        LOG.log(Level.WARNING, "Not data loader for tagName={0}, dataType={1}", new Object[] {tagName, dataType});
        return null;
    }
    
    /**
     * 使用tagName来查找一个已经注册的数据处理器（dataProcessor)，如果找不到已经注册的数据处理器，则使用dataType来
     * 确定是否使用哪一个。如果都找不到，则抛出异常。
     * @param tagName
     * @param dataType
     * @return 
     * @throws NullPointerException
     */
    public static Class<? extends DataProcessor> findDataProcessor(String tagName, int dataType) {
         Class<? extends DataProcessor> clz = TAG_PROCESSORS.get(tagName);
        if (clz != null) {
            return clz;
        }
        clz = DEFAULT_PROCESSORS.get(dataType);
        if (clz != null) {
            return clz;
        }
//        LOG.log(Level.WARNING, "Not data processor for tagName={0}, dataType={1}", new Object[] {tagName, dataType});
        return null;
    }
}
