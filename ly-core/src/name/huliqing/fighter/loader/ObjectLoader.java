/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import name.huliqing.fighter.GameException;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.enums.DataType;
import name.huliqing.fighter.utils.ModelFileUtils;
import name.huliqing.fighter.utils.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 这个类主要用于载入原始数据。/data/目标下的所有数据文件，并缓存在内存中
 * @author huliqing
 */
public class ObjectLoader {
    private final static Logger logger = Logger.getLogger(ObjectLoader.class.getName());
    
    private final static Map<String, Proto> protoDefMap = new HashMap<String, Proto>();
    public final static List<String> scripts = new ArrayList<String>();
    
    public static Proto findObjectDef(String id) {
        String tmp = id.trim();
        Proto result = protoDefMap.get(tmp);
        if (result != null) {
            return result;
        }
        logger.log(Level.WARNING, "Unknow objectId={0}", tmp);
        return null;
    }
    
    public static Collection<Proto> findAll() {
        return Collections.unmodifiableCollection(protoDefMap.values());
    }
    
    public static void initData() {
        try {
            loadDataFile(DataType.action, "/data/object/action.xml");
            loadDataFile(DataType.actor, "/data/object/actor.xml");
            loadDataFile(DataType.actorAnim, "/data/object/actorAnim.xml"); // 角色运动动画
            loadDataFile(DataType.actorLogic, "/data/object/actorLogic.xml");
            loadDataFile(DataType.anim, "/data/object/anim.xml");
            loadDataFile(DataType.attribute, "/data/object/attribute.xml");
            loadDataFile(DataType.bullet, "/data/object/bullet.xml");
            loadDataFile(DataType.channel, "/data/object/channel.xml");
            loadDataFile(DataType.chat, "/data/object/chat.xml");
            loadDataFile(DataType.config, "/data/object/config.xml");
            loadDataFile(DataType.drop, "/data/object/drop.xml");
            loadDataFile(DataType.effect, "/data/object/effect.xml");
            loadDataFile(DataType.el, "/data/object/el.xml");
            loadDataFile(DataType.emitter, "/data/object/emitter.xml");
            loadDataFile(DataType.position, "/data/object/position.xml");
            loadDataFile(DataType.env, "/data/object/env.xml");
            loadDataFile(DataType.game, "/data/object/game.xml");
            loadDataFile(DataType.handler, "/data/object/handler.xml");
            loadDataFile(DataType.hitChecker, "/data/object/hitChecker.xml");
            loadDataFile(DataType.magic, "/data/object/magic.xml");
            loadDataFile(DataType.item, "/data/object/item.xml");
            loadDataFile(DataType.resist, "/data/object/resist.xml");
            loadDataFile(DataType.scene, "/data/object/scene.xml");
            loadDataFile(DataType.shape, "/data/object/shape.xml");
            
            loadDataFile(DataType.skill, "/data/object/skill.xml");
            loadDataFile(DataType.skill, "/data/object/skill_monster.xml");
            loadDataFile(DataType.skill, "/data/object/skill_skin.xml");
            
            // -- skin分多个文件，分别为女性和男性
            loadDataFile(DataType.skin, "/data/object/skin.xml");
            loadDataFile(DataType.skin, "/data/object/skin_weapon.xml");
            loadDataFile(DataType.skin, "/data/object/skin_male.xml");
            
            // 武器槽位配置
            loadDataFile(DataType.slot, "/data/object/slot.xml");
            
            loadDataFile(DataType.sound, "/data/object/sound.xml");
            loadDataFile(DataType.state, "/data/object/state.xml");
            loadDataFile(DataType.talent, "/data/object/talent.xml");
            loadDataFile(DataType.task, "/data/object/task.xml");
            loadDataFile(DataType.view, "/data/object/view.xml");
        } catch (Exception e) {
            throw new GameException("Could not load data!", e);
        }
    }
    
    private static void loadDataFile(DataType protoType, String dataFile) throws ParserConfigurationException, SAXException, IOException  {
        Element root = XmlUtils.newDocument(ModelFileUtils.readFile(dataFile))
                .getDocumentElement();
        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (!(node instanceof Element)) {
                continue;
            }
            String tagName = ((Element) node).getTagName();
            Element ele = (Element) node;
            
            // 提取脚本
            if (tagName.equals("script")) {
                scripts.add(ele.getTextContent());
                continue;
            }
            
            Proto objectDef = new Proto(protoType, XmlUtils.getAttributes(ele), tagName);
            protoDefMap.put(objectDef.getId(), objectDef);
        }
    }
    
    public static void main(String[] args) {
        initData();
        System.out.println(ObjectLoader.findObjectDef("actorPlayer"));
    }
}
