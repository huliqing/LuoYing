/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object;

import com.jme3.network.serializing.Serializer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.data.EffectData;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.data.HandlerData;
import name.huliqing.fighter.data.MagicData;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.data.StateData;
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
import name.huliqing.fighter.object.env.Env;
import name.huliqing.fighter.object.env.EnvLoader;
import name.huliqing.fighter.object.env.PlantEnvLoader;
import name.huliqing.fighter.object.env.SkyEnv;
import name.huliqing.fighter.object.env.TerrainEnv;
import name.huliqing.fighter.object.env.TreeEnv;
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
import name.huliqing.fighter.object.scene.RandomSceneData;
import name.huliqing.fighter.object.scene.RandomScene;
import name.huliqing.fighter.object.scene.RandomSceneLoader;
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
    
    // TagName -> ProtoData, 每一个TagName对应一个ProtoData类型
    private final static Map<String, Class<? extends ProtoData>>
            DATA_TYPE_MAP = new HashMap<String, Class<? extends ProtoData>>();
    
    // TagName -> DataLoader, 每一个TagName对应一个DataLoader类型。
    private final static Map<String, DataLoader>
            DATA_LOADER_MAP = new HashMap<String, DataLoader>();
    
    // TagName -> DataProcessor, 每一个TagName对应一个DataProcessor类型
    private final static Map<String, Class<? extends DataProcessor>> 
            DATA_PROCESSOR_MAP = new HashMap<String, Class<? extends DataProcessor>>();
    
    /**
     * 注册一个数据类型
     * @param tagName 
     * @param dataTypeClass 
     */
    public static void registerDataType(String tagName, Class<? extends ProtoData> dataTypeClass) {
        DATA_TYPE_MAP.put(tagName, dataTypeClass);
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
    public static void registerDataLoader(String tagName, Class<? extends DataLoader> dataLoaderClass) {
        try {
            DATA_LOADER_MAP.put(tagName, dataLoaderClass.newInstance());
        } catch (InstantiationException ex) {
            Logger.getLogger(DataFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DataFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /** 
     * 注册一个数据处理器
     * @param tagName 
     * @param dataProcessorClass 
     */
    public static void registerDataProcessor(String tagName, Class<? extends DataProcessor> dataProcessorClass) {
        DATA_PROCESSOR_MAP.put(tagName, dataProcessorClass);
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
        registerDataLoader(tagName, dataLoaderClass);
        registerDataProcessor(tagName, dataProcessorClass);
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
        
        Class<? extends ProtoData> dataClass = DATA_TYPE_MAP.get(proto.getTagName());
        if (dataClass == null) {
            throw new NullPointerException("Could not find DataType for tagName! "
                    + "id=" + id 
                    + ", tagName=" + proto.getTagName()
                    + ", use registerDataType(...) to register a DataType for tagName"
                    );
        }
        
        DataLoader loader = DATA_LOADER_MAP.get(proto.getTagName());
        if (loader == null) {
            throw new NullPointerException("Could not find DataLoader!"
                    + " id=" + id 
                    + ", tagName=" + proto.getTagName()
                    + ", use registerDataLoader(...) to register a DataLoader for tagName"
                    );
        }
        
        try {
            ProtoData data = dataClass.newInstance();
            data.setId(id);
            loader.load(proto, data);
            return (T) data;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Could not createData, id={0}, tagName={1}, dataType={2}, dataLoader={3}, error={4}"
                    , new Object[] {id, proto.getTagName(), dataClass.getName(), loader.getClass().getName(), ex.getMessage()});
//            ex.printStackTrace();
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
        String tagName = data.getTagName();
        Class<? extends DataProcessor> dpClass = DATA_PROCESSOR_MAP.get(tagName);
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
        
        register("handlerTest", HandlerData.class, HandlerLoader.class, TestHandler.class);
        register("handlerSummon", HandlerData.class, HandlerLoader.class, SummonHandler.class);
        register("handlerAttribute", HandlerData.class, HandlerLoader.class, AttributeHandler.class);
        register("handlerOutfit", HandlerData.class, HandlerLoader.class, OutfitHandler.class);
        register("handlerWeapon", HandlerData.class, HandlerLoader.class, WeaponHandler.class);
        register("handlerSkill", HandlerData.class, HandlerLoader.class, SkillHandler.class);
        register("handlerSummonSkill", HandlerData.class, HandlerLoader.class, SummonSkillHandler.class);
        register("handlerSkillBook", HandlerData.class, HandlerLoader.class, SkillBookHandler.class);
        register("handlerStateGain", HandlerData.class, HandlerLoader.class, StateGainHandler.class);
        register("handlerStateRemove", HandlerData.class, HandlerLoader.class, StateRemoveHandler.class);
        register("handlerItemSkill", HandlerData.class, HandlerLoader.class, ItemSkillHandler.class);
        register("handlerMap", HandlerData.class, HandlerLoader.class, MapHandler.class);
        
        // Scene
        register("sceneRandom", RandomSceneData.class, RandomSceneLoader.class, RandomScene.class);
        
        // Env
        register("envSky", EnvData.class, EnvLoader.class, SkyEnv.class);
        register("envTerrain", EnvData.class, EnvLoader.class, TerrainEnv.class);
        register("envTree", EnvData.class, PlantEnvLoader.class, TreeEnv.class);
        register("envGrass", EnvData.class, PlantEnvLoader.class, Env.class);
        
        // States
        register("stateAttribute", StateData.class, StateLoader.class, AttributeState.class);
        register("stateAttributeMove", StateData.class, StateLoader.class, MoveSpeedState.class);
        register("stateAttributeDynamic", StateData.class, StateLoader.class, AttributeDynamicState.class);
        register("stateSkillLocked", StateData.class, StateLoader.class, SkillLockedState.class);
        register("stateEssential", StateData.class, StateLoader.class, EssentialState.class);
        register("stateSkill", StateData.class, StateLoader.class, SkillState.class);
        register("stateClean", StateData.class, StateLoader.class, CleanState.class);
        
        register("magicState", MagicData.class, MagicLoader.class, StateMagic.class);
        register("magicAttributeHit", MagicData.class, MagicLoader.class, AttributeHitMagic.class);
        
        register("effectHalo", EffectData.class, EffectLoader.class, HaloEffect.class);
        register("effectParticle", EffectData.class, EffectLoader.class, ParticleEffect.class);
        register("effectSimpleGroup", EffectData.class, EffectLoader.class, SimpleGroupEffect.class);
        register("effectGroup", EffectData.class, EffectLoader.class, GroupEffect.class);
        register("effectEncircleHalo", EffectData.class, EffectLoader.class, EncircleHaloEffect.class);
        register("effectTexture", EffectData.class, EffectLoader.class, TextureEffect.class);
        register("effectTextureCylinder", EffectData.class, EffectLoader.class, TextureCylinderEffect.class);
        register("effectModel", EffectData.class, EffectLoader.class, ModelEffect.class);
        register("effectSlideColor", EffectData.class, EffectLoader.class, SlideColorEffect.class);
        register("effectSlideColorSpline", EffectData.class, EffectLoader.class, SlideColorSplineEffect.class);
        register("effectSlideColorIOSpline", EffectData.class, EffectLoader.class, SlideColorIOSplineEffect.class);
        register("effectProjection", EffectData.class, EffectLoader.class, ProjectionEffect.class);
        
        register("game", GameData.class, GameLoader.class, Game.class);
        register("gameStoryTreasure", GameData.class, GameLoader.class, StoryTreasureGame.class);
        register("gameStoryGb", GameData.class, GameLoader.class, StoryGbGame.class);
        register("gameStoryGuard", GameData.class, GameLoader.class, StoryGuardGame.class);
        register("gameSurvival", GameData.class, GameLoader.class, SurvivalGame.class);
    }
    
}
