/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.data.HandlerData;
import name.huliqing.fighter.data.HitCheckerData;
import name.huliqing.fighter.data.ItemData;
import name.huliqing.fighter.data.LogicData;
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
import name.huliqing.fighter.data.TalentData;
import name.huliqing.fighter.data.TaskData;
import name.huliqing.fighter.data.ViewData;
import name.huliqing.fighter.enums.DataType;
import name.huliqing.fighter.loader.data.ActionDataLoader;
import name.huliqing.fighter.loader.data.ActorAnimDataLoader;
import name.huliqing.fighter.loader.data.ActorDataLoader;
import name.huliqing.fighter.loader.data.AnimDataLoader;
import name.huliqing.fighter.loader.data.AttributeDataLoader;
import name.huliqing.fighter.loader.data.BulletDataLoader;
import name.huliqing.fighter.loader.data.ChannelDataLoader;
import name.huliqing.fighter.loader.data.ChatDataLoader;
import name.huliqing.fighter.loader.data.ConfigDataLoader;
import name.huliqing.fighter.loader.data.DataLoader;
import name.huliqing.fighter.loader.data.DropDataLoader;
import name.huliqing.fighter.loader.data.EmitterDataLoader;
import name.huliqing.fighter.loader.data.PositionDataLoader;
import name.huliqing.fighter.loader.data.GameDataLoader;
import name.huliqing.fighter.loader.data.HandlerDataLoader;
import name.huliqing.fighter.loader.data.HitCheckerDataLoader;
import name.huliqing.fighter.loader.data.ElDataLoader;
import name.huliqing.fighter.loader.data.LogicDataLoader;
import name.huliqing.fighter.loader.data.ObjDataLoader;
import name.huliqing.fighter.loader.data.ObjectLoader;
import name.huliqing.fighter.loader.data.ResistDataLoader;
import name.huliqing.fighter.loader.data.ShapeDataLoader;
import name.huliqing.fighter.loader.data.SkillDataLoader;
import name.huliqing.fighter.loader.data.SkinDataLoader;
import name.huliqing.fighter.loader.data.SlotDataLoader;
import name.huliqing.fighter.loader.data.SoundDataLoader;
import name.huliqing.fighter.loader.data.TalentDataLoader;
import name.huliqing.fighter.loader.data.TaskDataLoader;
import name.huliqing.fighter.loader.data.ViewDataLoader;

/**
 * 
 * @deprecated 这个类将不再使用,后续会重构掉
 * @author huliqing
 */
public class DataLoaderFactory {
    private static final Logger LOG = Logger.getLogger(DataLoaderFactory.class.getName());
    
    private final static Map<DataType, DataLoader> loaders = 
            new EnumMap<DataType, DataLoader>(DataType.class);
    
//    private final static String ATTRIBUTE_EXTENDS = "extends";
    
    static {
        loaders.put(DataType.action, new ActionDataLoader());
        loaders.put(DataType.actorAnim, new ActorAnimDataLoader());
        loaders.put(DataType.actor, new ActorDataLoader());
        loaders.put(DataType.anim, new AnimDataLoader());
        loaders.put(DataType.attribute, new AttributeDataLoader());
        loaders.put(DataType.bullet, new BulletDataLoader());
        loaders.put(DataType.channel, new ChannelDataLoader());
        loaders.put(DataType.chat, new ChatDataLoader());
        loaders.put(DataType.config, new ConfigDataLoader());
        loaders.put(DataType.drop, new DropDataLoader());
        loaders.put(DataType.el, new ElDataLoader());
        loaders.put(DataType.emitter, new EmitterDataLoader());
        loaders.put(DataType.position, new PositionDataLoader());
        loaders.put(DataType.game, new GameDataLoader());
        loaders.put(DataType.hitChecker, new HitCheckerDataLoader());
        loaders.put(DataType.logic, new LogicDataLoader());
        loaders.put(DataType.item, new ObjDataLoader());
        loaders.put(DataType.resist, new ResistDataLoader());
        loaders.put(DataType.shape, new ShapeDataLoader());
        loaders.put(DataType.skill, new SkillDataLoader());
        loaders.put(DataType.skin, new SkinDataLoader());
        loaders.put(DataType.slot, new SlotDataLoader());
        loaders.put(DataType.sound, new SoundDataLoader());
        loaders.put(DataType.talent, new TalentDataLoader());
        loaders.put(DataType.task, new TaskDataLoader());
        loaders.put(DataType.view, new ViewDataLoader());
        
        // remove
//        loaders.put(DataType.effect, new EffectDataLoader());
//        loaders.put(DataType.env, new EnvDataLoader());
//        loaders.put(DataType.magic, new MagicDataLoader());
//        loaders.put(DataType.handler, new HandlerDataLoader());
//        loaders.put(DataType.state, new StateDataLoader());
//        loaders.put(DataType.scene, new SceneDataLoader());
    }
    
    public static <T extends ProtoData> T createData(String objectId, Class<T> type) {
        T data = (T) createData(objectId);
        if (data == null) 
            LOG.log(Level.SEVERE, "Could not create data, objectId={0}, type={1}"
                    , new Object[] {objectId, type});
        return data;
    }
    
    public static ActionData createActionData(String objectId) {
        return createData(objectId, ActionData.class);
    }
    public static ActorAnimData createActorAnimData(String objectId) {
        return createData(objectId, ActorAnimData.class);
    }
    public static ActorData createActorData(String objectId) {
        return createData(objectId, ActorData.class);
    }    
    public static ChannelData createChannelData(String objectId) {
        return createData(objectId, ChannelData.class);
    }
    public static ChatData createChatData(String objectId) {
        return createData(objectId, ChatData.class);
    }
    public static ConfigData createConfigData(String objectId) {
        return createData(objectId, ConfigData.class);
    }
    public static AnimData createAnimData(String objectId) {
        return createData(objectId, AnimData.class);
    }
    public static AttributeData createAttributeData(String objectId) {
        return createData(objectId, AttributeData.class);
    }
    public static BulletData createBulletData(String objectId) {
        return createData(objectId, BulletData.class);
    }
    public static DropData createDropData(String objectId) {
        return createData(objectId, DropData.class);
    }
    public static EmitterData createEmitterData(String objectId) {
        return createData(objectId, EmitterData.class);
    }
    public static PositionData createPositionData(String objectId) {
        return createData(objectId, PositionData.class);
    }
    public static GameData createGameData(String objectId) {
        return createData(objectId, GameData.class);
    }
    public static HitCheckerData createHitCheckerData(String objectId) {
        return createData(objectId, HitCheckerData.class);
    }
    public static ElData createElData(String objectId) {
        return createData(objectId, ElData.class);
    }
    public static LogicData createLogicData(String objectId) {
        return createData(objectId, LogicData.class);
    }
    public static ItemData createObjData(String objectId) {
        return createData(objectId, ItemData.class);
    }
    public static ResistData createResistData(String objectId) {
        return createData(objectId, ResistData.class);
    }
    public static SceneData createSceneData(String objectId) {
        return createData(objectId, SceneData.class);
    }
    public static ShapeData createShapeData(String objectId) {
        return createData(objectId, ShapeData.class);
    }
    public static SkillData createSkillData(String objectId) {
        return createData(objectId, SkillData.class);
    }
    public static SkinData createSkinData(String objectId) {
        return createData(objectId, SkinData.class);
    }
    public static SlotData createSlotData(String objectId) {
        return createData(objectId, SlotData.class);
    }
    public static SoundData createSoundData(String objectId) {
        return createData(objectId, SoundData.class);
    } 
    public static TalentData createTalentData(String objectId) {
        return createData(objectId, TalentData.class);
    }
    public static TaskData createTaskData(String objectId) {
        return createData(objectId, TaskData.class);
    }
    public static ViewData createViewData(String objectId) {
        return createData(objectId, ViewData.class);
    }
    
    // ---- ======主方法======
    
    /**
     * 创建物品,如果物品不存在则返回null.
     * @param objectId
     * @return 
     */
    public static ProtoData createData(String objectId) {
//        Proto proto = getProto(objectId);
        Proto proto = ProtoUtils.getProto(objectId);
        if (proto == null) {
//            throw new NullPointerException("Could createData by objectId=" + objectId);
            LOG.log(Level.SEVERE, "Object not found! objectId={0}", objectId);
            return null;
        }
        DataLoader loader = loaders.get(proto.getDataType());
        return loader.loadData(proto);
    }
    
//    /**
//     * 获取object原形
//     * @param objectId
//     * @return 
//     */
//    public synchronized static Proto getProto(String objectId) {
//        Proto proto = ObjectLoader.findObjectDef(objectId);
//        if (proto == null) {
//            LOG.log(Level.WARNING, "Could not find object={0}", objectId);
//            return null;
//        }
//        
//        String extId = proto.getAttribute(ATTRIBUTE_EXTENDS);
//        if (extId == null) {
//            return proto;
//        } else {
//            // checker用于记录“继承链”
//            List<String> checker = new ArrayList<String>(3);
//            checker.add(proto.getId());
//            proto = extendsProto(proto, ObjectLoader.findObjectDef(extId), checker);
//            return proto;
//        }
//    }
//    
//    /**
//     * 处理Proto的继承，注：假设继承关系是这样的，A -> B -> C, 即C继承自B,
//     * B继承自A, 则当查询一次C之后，所有参数都继续完毕（包含ABC)，ABC的继承
//     * 关系也将不再存在，下次查找C时，将不再需要重新去处理继承关系,因为Proto
//     * 是通用所有对象的,只要处理一次即可。
//     * @param p
//     * @param pp
//     * @return 
//     */
//    private static Proto extendsProto(Proto p, Proto pp, List<String> checker) {
//        if (Config.debug) {
//            LOG.log(Level.INFO, "====processor extends: {0} extends {1}", new Object[] {p, pp});
//        }
//        if (pp != null && p != pp) { // p != pp 防止无用的自继承
//            
//            // 限制不同DataType类型的继承，以避免复杂性,以防止死继承
//            if (p.getDataType() != pp.getDataType()) {
//                throw new UnsupportedOperationException("Unsupported difference "
//                        + "DataType extends! proto={0}" + p + ", parentProto=" + pp);
//            }
//            
//            checker.add(pp.getId());
//            String extId = pp.getAttribute(ATTRIBUTE_EXTENDS);
//            if (extId != null) {
//                // 检查是否存在无尽继承
//                if (checker.contains(extId)) {
//                    throw new UnsupportedOperationException("Unsupported endless loop extends => " 
//                            + checker + ", extId=" + extId);
//                }
//                
//                // 检查被继承的对象是否存在
//                Proto extProto = ObjectLoader.findObjectDef(extId);
//                if (extProto == null) {
//                    throw new RuntimeException("Could not find extends object=" + extId + ", extends=" + checker);
//                }
//                pp = extendsProto(pp, extProto, checker);
//            }
//            Map<String, String> pMap = p.getOriginAttributes();
//            Map<String, String> ppMap = pp.getOriginAttributes();
//            Map<String, String> merger = new HashMap<String, String>(ppMap.size());
//            merger.putAll(ppMap);
//            merger.putAll(pMap);
//            pMap.clear();
//            pMap.putAll(merger);
//
//            // 移除掉这个参数，这样下次就不需要让父类再去递归继承了，以节省性能。
//            pMap.remove(ATTRIBUTE_EXTENDS);
//            
//            if (Config.debug) {
//                LOG.log(Level.INFO, "processor extends result => {0}", p);
//            }
//            return p;
//        }
//        // 移除掉这个参数，这样下次就不需要再去继承，以节省性能。
//        p.getOriginAttributes().remove(ATTRIBUTE_EXTENDS);
//
//        return p;
//    }
    
    public static void main(String[] args) {
        ObjectLoader.initData();
        System.out.println(createData("testExtA"));
        System.out.println(createData("testExtB"));
        System.out.println(createData("testExtC"));
    }
}
