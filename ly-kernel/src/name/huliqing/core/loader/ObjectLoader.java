/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import name.huliqing.core.constants.DataTypeConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import name.huliqing.core.xml.Proto;
import name.huliqing.core.xml.XmlUtils;

/**
 * 这个类主要用于载入原始数据。/data/目标下的所有数据文件，并缓存在内存中
 * @author huliqing
 */
public class ObjectLoader {
    private final static Logger LOG = Logger.getLogger(ObjectLoader.class.getName());
    
    private final static Map<String, Proto> PROTO_MAP = new HashMap<String, Proto>();
    public final static List<String> SCRIPTS = new ArrayList<String>();
    
    public static Proto findObjectDef(String id) {
        String tmp = id.trim();
        Proto result = PROTO_MAP.get(tmp);
        if (result != null) {
            return result;
        }
        LOG.log(Level.WARNING, "Unknow objectId={0}", tmp);
        return null;
        
    }
    
    public static Collection<Proto> findAll() {
        return Collections.unmodifiableCollection(PROTO_MAP.values());
    }
    
    public static void initData() {
        try {
            loadDataFile(DataTypeConstants.ACTION, "/data/object/action.xml");
            loadDataFile(DataTypeConstants.ACTOR, "/data/object/actor.xml");
            loadDataFile(DataTypeConstants.ACTOR_ANIM, "/data/object/actorAnim.xml"); // 角色运动动画
            loadDataFile(DataTypeConstants.ACTOR_LOGIC, "/data/object/actorLogic.xml");
            loadDataFile(DataTypeConstants.ANIM, "/data/object/anim.xml");
            loadDataFile(DataTypeConstants.ATTRIBUTE, "/data/object/attribute.xml");
            loadDataFile(DataTypeConstants.BULLET, "/data/object/bullet.xml");
            loadDataFile(DataTypeConstants.CHANNEL, "/data/object/channel.xml");
            loadDataFile(DataTypeConstants.CHAT, "/data/object/chat.xml");
            loadDataFile(DataTypeConstants.CONFIG, "/data/object/config.xml");
            loadDataFile(DataTypeConstants.DROP, "/data/object/drop.xml");
            loadDataFile(DataTypeConstants.EFFECT, "/data/object/effect.xml");
            loadDataFile(DataTypeConstants.EL, "/data/object/el.xml");
            loadDataFile(DataTypeConstants.EMITTER, "/data/object/emitter.xml");
            loadDataFile(DataTypeConstants.POSITION, "/data/object/position.xml");
            loadDataFile(DataTypeConstants.ENV, "/data/object/env.xml");
            loadDataFile(DataTypeConstants.GAME, "/data/object/game.xml");
            loadDataFile(DataTypeConstants.GAME_LOGIC, "/data/object/gameLogic.xml");
            loadDataFile(DataTypeConstants.HANDLER, "/data/object/handler.xml");
            loadDataFile(DataTypeConstants.HIT_CHECKER, "/data/object/hitChecker.xml");
            loadDataFile(DataTypeConstants.MAGIC, "/data/object/magic.xml");
            loadDataFile(DataTypeConstants.ITEM, "/data/object/item.xml");
            loadDataFile(DataTypeConstants.RESIST, "/data/object/resist.xml");
            loadDataFile(DataTypeConstants.SCENE, "/data/object/scene.xml");
            loadDataFile(DataTypeConstants.SHAPE, "/data/object/shape.xml");
            
            loadDataFile(DataTypeConstants.SKILL, "/data/object/skill.xml");
            loadDataFile(DataTypeConstants.SKILL, "/data/object/skill_monster.xml");
            loadDataFile(DataTypeConstants.SKILL, "/data/object/skill_skin.xml");
            
            // -- skin分多个文件，分别为女性和男性
            loadDataFile(DataTypeConstants.SKIN, "/data/object/skin.xml");
            loadDataFile(DataTypeConstants.SKIN, "/data/object/skin_weapon.xml");
            loadDataFile(DataTypeConstants.SKIN, "/data/object/skin_male.xml");
            
            // 武器槽位配置
            loadDataFile(DataTypeConstants.SLOT, "/data/object/slot.xml");
            
            loadDataFile(DataTypeConstants.SOUND, "/data/object/sound.xml");
            loadDataFile(DataTypeConstants.STATE, "/data/object/state.xml");
            loadDataFile(DataTypeConstants.TALENT, "/data/object/talent.xml");
            loadDataFile(DataTypeConstants.TASK, "/data/object/task.xml");
            loadDataFile(DataTypeConstants.VIEW, "/data/object/view.xml");
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Could not load data!", e);
        } catch (SAXException e) {
            throw new RuntimeException("Could not load data!", e);
        } catch (IOException e) {
            throw new RuntimeException("Could not load data!", e);
        }
    }
    
    private static void loadDataFile(int dataType, String dataFile) throws ParserConfigurationException, SAXException, IOException  {
        Element root = XmlUtils.newDocument(readFile(dataFile))
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
                SCRIPTS.add(ele.getTextContent());
                continue;
            }
            
            Proto objectDef = new Proto(dataType, XmlUtils.getAttributes(ele), tagName);
            PROTO_MAP.put(objectDef.getId(), objectDef);
        }
    }
    
    /**
     * 将文件读取为字符串.
     * @param path
     * @return 
     */
    public static String readFile(String path) {
        BufferedInputStream bis = null;
        String result = null;
        try {
        InputStream is = ObjectLoader.class.getResourceAsStream(path);
            bis = new BufferedInputStream(is);
            byte[] buff = new byte[2048];
            int len;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((len = bis.read(buff)) != -1) {
                baos.write(buff, 0, len);
            }
            // 必须指定编码，否则在win下可能中文乱码
            result = baos.toString("utf-8");
        } catch (IOException ioe) {
            Logger.getLogger(ObjectLoader.class.getName())
                    .log(Level.SEVERE, "Couldnot read file: {0}", path);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    Logger.getLogger(ObjectLoader.class.getName())
                            .log(Level.SEVERE, "Could not close stream!", e);
                }
            }
        }
        return result;
    }
    
    public static void main(String[] args) {
        initData();
        System.out.println(ObjectLoader.findObjectDef("actorPlayer"));
    }
}
