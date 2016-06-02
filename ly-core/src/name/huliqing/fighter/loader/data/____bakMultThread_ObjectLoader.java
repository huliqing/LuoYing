///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.loader.data;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.xml.parsers.ParserConfigurationException;
//import name.huliqing.fighter.GameException;
//import name.huliqing.fighter.data.Proto;
//import name.huliqing.fighter.enums.DataType;
//import name.huliqing.fighter.utils.ModelFileUtils;
//import name.huliqing.fighter.utils.ThreadHelper;
//import name.huliqing.fighter.utils.XmlUtils;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
///**
// *
// * @author huliqing
// */
//public class ObjectLoader {
//    private final static Logger logger = Logger.getLogger(ObjectLoader.class.getName());
//    
//    private final static Map<String, Proto> protoDefMap = new HashMap<String, Proto>();
//    public final static List<String> scripts = new ArrayList<String>();
//    
//    public static Proto findObjectDef(String id) {
//        String tmp = id.trim();
//        Proto result = protoDefMap.get(tmp);
//        if (result != null) {
//            return result;
//        }
//        logger.log(Level.WARNING, "Unknow objectId={0}", tmp);
//        return null;
//    }
//    
//    public static Collection<Proto> findAll() {
//        return Collections.unmodifiableCollection(protoDefMap.values());
//    }
//    
//    public static void initData() {
//        try {
//            loadFile2();
//        } catch (Exception e) {
//            throw new GameException("Could not load data!", e);
//        }
//    }
//    
//    private static void loadFile2() throws Exception {
//        final Map<String, Proto> protoStore =Collections.synchronizedMap(new HashMap<String, Proto>());
//        final List<String> scriptStore = Collections.synchronizedList(new ArrayList<String>());
//        
//        Future action = loadFile(DataType.action, "/data/object/action.xml", protoStore, scriptStore);
//        Future actorAnim = loadFile(DataType.actorAnim, "/data/object/actorAnim.xml", protoStore, scriptStore);
//        Future actor = loadFile(DataType.actor, "/data/object/actor.xml", protoStore, scriptStore);
//        Future anim = loadFile(DataType.anim, "/data/object/anim.xml", protoStore, scriptStore);
//        Future attribute = loadFile(DataType.attribute, "/data/object/attribute.xml", protoStore, scriptStore);
//        Future bullet = loadFile(DataType.bullet, "/data/object/bullet.xml", protoStore, scriptStore);
//        Future channel = loadFile(DataType.channel, "/data/object/channel.xml", protoStore, scriptStore);
//        Future config = loadFile(DataType.config, "/data/object/config.xml", protoStore, scriptStore);
//        Future drop = loadFile(DataType.drop, "/data/object/drop.xml", protoStore, scriptStore);
//        Future effect = loadFile(DataType.effect, "/data/object/effect.xml", protoStore, scriptStore);
//        Future el = loadFile(DataType.el, "/data/object/el.xml", protoStore, scriptStore);
//        Future emitter = loadFile(DataType.emitter, "/data/object/emitter.xml", protoStore, scriptStore);
//        Future position = loadFile(DataType.position, "/data/object/position.xml", protoStore, scriptStore);
//        Future env = loadFile(DataType.env, "/data/object/env.xml", protoStore, scriptStore);
//        Future game = loadFile(DataType.game, "/data/object/game.xml", protoStore, scriptStore);
//        Future handler = loadFile(DataType.handler, "/data/object/handler.xml", protoStore, scriptStore);
//        Future hitChecker = loadFile(DataType.hitChecker, "/data/object/hitChecker.xml", protoStore, scriptStore);
//        Future logic = loadFile(DataType.logic, "/data/object/logic.xml", protoStore, scriptStore);
//        Future magic = loadFile(DataType.magic, "/data/object/magic.xml", protoStore, scriptStore);
//        Future obj = loadFile(DataType.obj, "/data/object/obj.xml", protoStore, scriptStore);
//        Future resist = loadFile(DataType.resist, "/data/object/resist.xml", protoStore, scriptStore);
//        Future scene = loadFile(DataType.scene, "/data/object/scene.xml", protoStore, scriptStore);
//        Future shape = loadFile(DataType.shape, "/data/object/shape.xml", protoStore, scriptStore);
//
//        Future skill = loadFile(DataType.skill, "/data/object/skill.xml", protoStore, scriptStore);
//        Future skillMonster = loadFile(DataType.skill, "/data/object/skill_monster.xml", protoStore, scriptStore);
//        Future skillSkin = loadFile(DataType.skill, "/data/object/skill_skin.xml", protoStore, scriptStore);
//        // -- skin分多个文件，分别为女性和男性
//        Future skin = loadFile(DataType.skin, "/data/object/skin.xml", protoStore, scriptStore);
//        Future skinWeapon = loadFile(DataType.skin, "/data/object/skin_weapon.xml", protoStore, scriptStore);
//        Future skinMale = loadFile(DataType.skin, "/data/object/skin_male.xml", protoStore, scriptStore);
//
//        // 武器槽位配置
//        Future slot = loadFile(DataType.slot, "/data/object/slot.xml", protoStore, scriptStore);
//
//        Future sound = loadFile(DataType.sound, "/data/object/sound.xml", protoStore, scriptStore);
//        Future state = loadFile(DataType.state, "/data/object/state.xml", protoStore, scriptStore);
//        Future talent = loadFile(DataType.talent, "/data/object/talent.xml", protoStore, scriptStore);
//        Future view = loadFile(DataType.view, "/data/object/view.xml", protoStore, scriptStore);
//        
//        action.get();
//        actorAnim.get();
//        actor.get();
//        anim.get();
//        attribute.get();
//        bullet.get();
//        channel.get();
//        config.get();
//        drop.get();
//        effect.get();
//        el.get();
//        emitter.get();
//        position.get();
//        env.get();
//        game.get();
//        handler.get();
//        hitChecker.get();
//        logic.get();
//        magic.get();
//        obj.get();
//        resist.get();
//        scene.get();
//        shape.get();
//        skill.get();
//        skillMonster.get();
//        skillSkin.get();
//        skin.get();
//        skinWeapon.get();
//        skinMale.get();
//        slot.get();
//        sound.get();
//        state.get();
//        talent.get();
//        view.get();
//        protoDefMap.clear();
//        scripts.clear();
//        protoDefMap.putAll(protoStore);
//        scripts.addAll(scriptStore);
//    }
//    
//    private static Future loadFile(final DataType protoType, final String dataFile, final Map<String, Proto> protoStore, final List<String> scriptStore) {
//        return ThreadHelper.submit(new Callable() {
//            @Override
//            public Object call() throws Exception {
//                loadDataFile(protoType, dataFile, protoStore, scriptStore);
//                return null;
//            }
//        });
//    }
//    
//    private static void loadDataFile(DataType protoType, String dataFile, Map<String, Proto> protoStore, List<String> scriptStore) 
//            throws ParserConfigurationException, SAXException, IOException  {
//        
//        Element root = XmlUtils.newDocument(ModelFileUtils.readFile(dataFile))
//                .getDocumentElement();
//        NodeList children = root.getChildNodes();
//        for (int i = 0; i < children.getLength(); i++) {
//            Node node = children.item(i);
//            if (!(node instanceof Element)) {
//                continue;
//            }
//            String tagName = ((Element) node).getTagName();
//            Element ele = (Element) node;
//            
//            // 提取脚本
//            if (tagName.equals("script")) {
//                scriptStore.add(ele.getTextContent());
//                continue;
//            }
//            
//            Proto objectDef = new Proto(protoType, XmlUtils.getAttributes(ele), tagName);
//            protoStore.put(objectDef.getId(), objectDef);
//        }
//    }
//    
//    public static void main(String[] args) {
//        initData();
//        System.out.println(ObjectLoader.findObjectDef("actorPlayer"));
//    }
//}
