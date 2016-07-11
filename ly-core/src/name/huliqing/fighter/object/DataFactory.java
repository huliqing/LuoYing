/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object;

import com.jme3.network.serializing.Serializer;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.data.ActionData;
import name.huliqing.fighter.data.ActorAnimData;
import name.huliqing.fighter.data.ActorData;
import name.huliqing.fighter.data.AnimData;
import name.huliqing.fighter.data.AttributeData;
import name.huliqing.fighter.data.BulletData;
import name.huliqing.fighter.data.ChannelData;
import name.huliqing.fighter.data.ChatData;
import name.huliqing.fighter.data.ConfigData;
import name.huliqing.fighter.data.DropData;
import name.huliqing.fighter.data.EffectData;
import name.huliqing.fighter.data.ElData;
import name.huliqing.fighter.data.EmitterData;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.data.HandlerData;
import name.huliqing.fighter.data.HitCheckerData;
import name.huliqing.fighter.data.ItemData;
import name.huliqing.fighter.data.LogicData;
import name.huliqing.fighter.data.MagicData;
import name.huliqing.fighter.data.PositionData;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.data.ResistData;
import name.huliqing.fighter.data.SceneData;
import name.huliqing.fighter.data.ShapeData;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.data.SkinData;
import name.huliqing.fighter.data.SlotData;
import name.huliqing.fighter.data.SoundData;
import name.huliqing.fighter.data.StateData;
import name.huliqing.fighter.data.TalentData;
import name.huliqing.fighter.data.TaskData;
import name.huliqing.fighter.data.ViewData;
import name.huliqing.fighter.enums.DataType;
import name.huliqing.fighter.object.effect.EffectLoader;
import name.huliqing.fighter.object.effect.EncircleHaloEffect;
import name.huliqing.fighter.object.effect.GroupEffect;
import name.huliqing.fighter.object.effect.HaloEffect;
import name.huliqing.fighter.object.effect.ModelEffect;
import name.huliqing.fighter.object.effect.ParticleEffect;
import name.huliqing.fighter.object.effect.ProjectionEffect;
import name.huliqing.fighter.object.effect.SimpleGroupEffect;
import name.huliqing.fighter.object.effect.SlideColorEffect;
import name.huliqing.fighter.object.effect.SlideColorIOSplineEffect;
import name.huliqing.fighter.object.effect.SlideColorSplineEffect;
import name.huliqing.fighter.object.effect.TextureCylinderEffect;
import name.huliqing.fighter.object.effect.TextureEffect;
import name.huliqing.fighter.object.env.AudioEnv;
import name.huliqing.fighter.object.env.BoundaryBoxEnv;
import name.huliqing.fighter.object.env.CameraChaseEnv;
import name.huliqing.fighter.object.env.LightDirectionalEnv;
import name.huliqing.fighter.object.env.EnvLoader;
import name.huliqing.fighter.object.env.LightAmbientEnv;
import name.huliqing.fighter.object.env.ModelEnv;
import name.huliqing.fighter.object.env.ModelEnvData;
import name.huliqing.fighter.object.env.ModelEnvLoader;
import name.huliqing.fighter.object.env.PhysicsEnv;
import name.huliqing.fighter.object.env.PlantEnv;
import name.huliqing.fighter.object.env.PlantEnvLoader;
import name.huliqing.fighter.object.env.ProxyPlatformEnv;
import name.huliqing.fighter.object.env.ShadowEnv;
import name.huliqing.fighter.object.env.SkyEnv;
import name.huliqing.fighter.object.env.TerrainEnv;
import name.huliqing.fighter.object.env.TreeEnv;
import name.huliqing.fighter.object.env.WaterAdvanceEnv;
import name.huliqing.fighter.object.env.WaterSimpleEnv;
import name.huliqing.fighter.object.game.Game;
import name.huliqing.fighter.object.game.GameLoader;
import name.huliqing.fighter.object.game.StoryGbGame;
import name.huliqing.fighter.object.game.StoryGuardGame;
import name.huliqing.fighter.object.game.StoryTreasureGame;
import name.huliqing.fighter.object.game.SurvivalGame;
import name.huliqing.fighter.object.handler.AttributeHandler;
import name.huliqing.fighter.object.handler.HandlerLoader;
import name.huliqing.fighter.object.handler.ItemSkillHandler;
import name.huliqing.fighter.object.handler.MapHandler;
import name.huliqing.fighter.object.handler.OutfitHandler;
import name.huliqing.fighter.object.handler.SkillBookHandler;
import name.huliqing.fighter.object.handler.SkillHandler;
import name.huliqing.fighter.object.handler.StateGainHandler;
import name.huliqing.fighter.object.handler.StateRemoveHandler;
import name.huliqing.fighter.object.handler.SummonHandler;
import name.huliqing.fighter.object.handler.SummonSkillHandler;
import name.huliqing.fighter.object.handler.TestHandler;
import name.huliqing.fighter.object.handler.WeaponHandler;
import name.huliqing.fighter.object.magic.AttributeHitMagic;
import name.huliqing.fighter.object.magic.MagicLoader;
import name.huliqing.fighter.object.magic.StateMagic;
import name.huliqing.fighter.object.scene.RandomSceneLoader;
import name.huliqing.fighter.object.scene.Scene;
import name.huliqing.fighter.object.scene.SceneLoader;
import name.huliqing.fighter.object.state.AttributeDynamicState;
import name.huliqing.fighter.object.state.AttributeState;
import name.huliqing.fighter.object.state.CleanState;
import name.huliqing.fighter.object.state.EssentialState;
import name.huliqing.fighter.object.state.MoveSpeedState;
import name.huliqing.fighter.object.state.SkillLockedState;
import name.huliqing.fighter.object.state.SkillState;
import name.huliqing.fighter.object.state.StateLoader;

/**
 * 管理数据的各种加载和处理的工厂类
 * @author huliqing
 */
public class DataFactory {
    private static final Logger LOG = Logger.getLogger(DataFactory.class.getName());
    
    // DataType -> ProtoData，默认的数据容器，如果没有自定义的数据容器，则使用DEFAULT_DATA中匹配的数据类型作为数据容器
    private final static Map<DataType, Class<? extends ProtoData>> DEFAULT_DATAS = new EnumMap<DataType, Class<? extends ProtoData>>(DataType.class);
    // DataType -> DataLoader,默认的用于载入数据的“载入器”，当找不到匹配的数据载入器时将根据proto的dataType来确
    // 定要使用哪一个数据载入器
    private final static Map<DataType, Class<? extends DataLoader>> DEFAULT_LOADERS = new EnumMap<DataType, Class<? extends DataLoader>>(DataType.class);
    // DataType -> DataProcessor,默认的用于处理数据的“处理器”当找不到匹配的数据处理器时将根据proto的dataType来确
    // 定要使用哪一个数据处理器
    private final static Map<DataType, Class<? extends DataProcessor>> DEFAULT_PROCESSORS = new EnumMap<DataType, Class<? extends DataProcessor>>(DataType.class);
    
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
            ProtoData protoData = (ProtoData) Class.forName(proto.getDataClass()).newInstance();
            DataLoader dataLoader = (DataLoader) Class.forName(proto.getDataLoader()).newInstance();
            protoData.setId(id);
            dataLoader.load(proto, protoData);
            return (T) protoData;
        } catch (Exception ex) {
            ex.printStackTrace();
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
        String tagName = data.getTagName();
        Class<? extends DataProcessor> dpClass = TAG_PROCESSORS.get(tagName);
        if (dpClass == null) {
            throw new NullPointerException("Could not find DataProcess for tagName!"
                    + " tagName=" + tagName 
                    + ", dataId=" + data.getId());
        }
        try {
            DataProcessor dp = dpClass.newInstance();
            dp.initData(data);
            return (T) dp;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Could not create processor! tagName=" + tagName 
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
        Serializer.registerClass(HandlerData.class);
        Serializer.registerClass(HitCheckerData.class);
        Serializer.registerClass(ItemData.class);
        Serializer.registerClass(LogicData.class);
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
        // 默认的数据容器
        DEFAULT_DATAS.put(DataType.action, ActionData.class);
        DEFAULT_DATAS.put(DataType.actor, ActorData.class);
        DEFAULT_DATAS.put(DataType.actorAnim, ActorAnimData.class);
        DEFAULT_DATAS.put(DataType.anim, AnimData.class);
        DEFAULT_DATAS.put(DataType.attribute, AttributeData.class);
        DEFAULT_DATAS.put(DataType.bullet, BulletData.class);
        DEFAULT_DATAS.put(DataType.channel, ChannelData.class);
        DEFAULT_DATAS.put(DataType.chat, ChatData.class);
        DEFAULT_DATAS.put(DataType.config, ConfigData.class);
        DEFAULT_DATAS.put(DataType.drop, DropData.class);
        DEFAULT_DATAS.put(DataType.effect, EffectData.class);
        DEFAULT_DATAS.put(DataType.el, ElData.class);
        DEFAULT_DATAS.put(DataType.emitter, EmitterData.class);
        DEFAULT_DATAS.put(DataType.env, EnvData.class);
        DEFAULT_DATAS.put(DataType.game, GameData.class);
        DEFAULT_DATAS.put(DataType.handler, HandlerData.class);
        DEFAULT_DATAS.put(DataType.hitChecker, HitCheckerData.class);
        DEFAULT_DATAS.put(DataType.item, ItemData.class);
        DEFAULT_DATAS.put(DataType.logic, LogicData.class);
        DEFAULT_DATAS.put(DataType.magic, MagicData.class);
        DEFAULT_DATAS.put(DataType.position, PositionData.class);
        DEFAULT_DATAS.put(DataType.resist, ResistData.class);
        DEFAULT_DATAS.put(DataType.scene, SceneData.class);
        DEFAULT_DATAS.put(DataType.shape, ShapeData.class);
        DEFAULT_DATAS.put(DataType.skill, SkillData.class);
        DEFAULT_DATAS.put(DataType.skin, SkinData.class);
        DEFAULT_DATAS.put(DataType.slot, SlotData.class);
        DEFAULT_DATAS.put(DataType.sound, SoundData.class);
        DEFAULT_DATAS.put(DataType.state, StateData.class);
        DEFAULT_DATAS.put(DataType.talent, TalentData.class);
        DEFAULT_DATAS.put(DataType.task, TaskData.class);
        DEFAULT_DATAS.put(DataType.view, ViewData.class);
        
        // 初始化默认的数据载入器
        DEFAULT_LOADERS.put(DataType.effect, EffectLoader.class);
        DEFAULT_LOADERS.put(DataType.env, EnvLoader.class);
        DEFAULT_LOADERS.put(DataType.game, GameLoader.class);
        DEFAULT_LOADERS.put(DataType.handler, HandlerLoader.class);
        DEFAULT_LOADERS.put(DataType.magic, MagicLoader.class);
        DEFAULT_LOADERS.put(DataType.scene, SceneLoader.class);
        DEFAULT_LOADERS.put(DataType.state, StateLoader.class);

        // 初始化默认的数据处理器
        DEFAULT_PROCESSORS.put(DataType.scene, Scene.class);
        
        // ---- Register Tag
        
        // 注册Tag载入器及处理器。
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
        
        // Scene
        registerTagProcessor("scene", Scene.class);
        register("sceneRandom", SceneData.class, RandomSceneLoader.class, Scene.class);
        
        // States
        registerTagProcessor("stateAttribute", AttributeState.class);
        registerTagProcessor("stateAttributeMove", MoveSpeedState.class);
        registerTagProcessor("stateAttributeDynamic", AttributeDynamicState.class);
        registerTagProcessor("stateSkillLocked", SkillLockedState.class);
        registerTagProcessor("stateEssential", EssentialState.class);
        registerTagProcessor("stateSkill", SkillState.class);
        registerTagProcessor("stateClean", CleanState.class);
        
        registerTagProcessor("magicState", StateMagic.class);
        registerTagProcessor("magicAttributeHit", AttributeHitMagic.class);

        registerTagProcessor("game", Game.class);
        registerTagProcessor("gameStoryTreasure", StoryTreasureGame.class);
        registerTagProcessor("gameStoryGb", StoryGbGame.class);
        registerTagProcessor("gameStoryGuard", StoryGuardGame.class);
        registerTagProcessor("gameSurvival", SurvivalGame.class);

    }
    
    /**
     * 使用tagName来查找一个已经注册的数据容器(ProtoData)，如果找不到已经注册的数据容器，则根据dataType的默认配置
     * 来确定是否使用哪一个数据容器。如果都找不到，则抛出异常。
     * @param tagName
     * @param dataType
     * @return 
     */
    public static Class<? extends ProtoData> findProtoData(String tagName, DataType dataType) {
        Class<? extends ProtoData> clz = TAG_DATAS.get(tagName);
        if (clz != null) {
            return clz;
        }
        clz = DEFAULT_DATAS.get(dataType);
        if (clz != null) {
            return clz;
        }
        return null;
//        throw new NullPointerException("Can't find ProtoData type class. tagName=" + tagName + ", dataType=" + dataType);
    }
    
    /**
     * 使用tagName来查找一个已经注册的数据载入器（dataLoader)，如果找不到已经注册的数据载入器，则使用dataType来
     * 确定是否使用哪一个数据载入器。如果都找不到，则抛出异常。
     * @param tagName
     * @param dataType
     * @return 
     * @throws NullPointerException
     */
    public static Class<? extends DataLoader> findDataLoader(String tagName, DataType dataType) {
        Class<? extends DataLoader> clz = TAG_LOADERS.get(tagName);
        if (clz != null) {
            return clz;
        }
        clz = DEFAULT_LOADERS.get(dataType);
        if (clz != null) {
            return clz;
        }
        return null;
//        throw new NullPointerException("Can't find dataLoader, tagName=" + tagName + ",dataType=" + dataType);
    }
    
    /**
     * 使用tagName来查找一个已经注册的数据处理器（dataProcessor)，如果找不到已经注册的数据处理器，则使用dataType来
     * 确定是否使用哪一个。如果都找不到，则抛出异常。
     * @param tagName
     * @param dataType
     * @return 
     * @throws NullPointerException
     */
    public static Class<? extends DataProcessor> findDataProcessor(String tagName, DataType dataType) {
         Class<? extends DataProcessor> clz = TAG_PROCESSORS.get(tagName);
        if (clz != null) {
            return clz;
        }
        clz = DEFAULT_PROCESSORS.get(dataType);
        if (clz != null) {
            return clz;
        }
        return null;
//        throw new NullPointerException("Can't find dataLoader, tagName=" + tagName + ",dataType=" + dataType);
    }
}
