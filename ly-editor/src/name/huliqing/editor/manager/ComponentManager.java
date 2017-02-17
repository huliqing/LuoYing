/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import name.huliqing.editor.component.ComponentConverter;
import name.huliqing.editor.component.ComponentDefine;
import name.huliqing.editor.edit.JfxEdit;
import name.huliqing.luoying.utils.FileUtils;
import name.huliqing.luoying.xml.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 组件管理。
 * @author huliqing
 */
public class ComponentManager {

    private static final Logger LOG = Logger.getLogger(ComponentManager.class.getName());
    
    // 缓存组件转换器
    private final static Map<String, ComponentConverter> CONVERTER_CACHE = new HashMap();
    
    // key = Componety Type
    private final static Map<String, List<ComponentDefine>> COMPONENT_DEFINES = new LinkedHashMap();
    
    /**
     * 给定组件定义文件的根目录，重新载入组件定义.“组件定义”的根目录路径示例：<br>
     * componentDir=C:\home\workspace\luoying\ly-editor\assets\LuoYing\editor\component <br>
     * 该根目录下的组件定义文件必须是以".xml"为后缀结尾的文件。
     * @param componentDir 组件定义的根目录
     */
    public final static void reloadComponents(File componentDir) {
        COMPONENT_DEFINES.clear();
        CONVERTER_CACHE.clear();
        for (File componentFile : componentDir.listFiles()) {
            if (componentFile.exists() && componentFile.isFile() && componentFile.getAbsolutePath().endsWith(".xml")) {
                try {
                    loadComponentFile(componentFile);
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "Could not load component define file, file={0}", componentFile.getAbsolutePath());
                }
            }
        }
    }
    
    private static void loadComponentFile(File componentFile) throws IOException, ParserConfigurationException, SAXException {
        String xmlStr = FileUtils.readFile(componentFile, "utf-8");
        Element root = XmlUtils.newDocument(xmlStr).getDocumentElement();
        NodeList componentList = root.getElementsByTagName("component");
        int componentSize = componentList.getLength();
        for (int i = 0; i < componentSize; i++) {
            Element cEle = (Element) componentList.item(i);
            String id = cEle.getAttribute("id");
            String type = cEle.getAttribute("type");
            String icon = cEle.getAttribute("icon");
            String converterClass = cEle.getAttribute("converterClass");
            addComponentDefine(new ComponentDefine(id, type, icon, converterClass));
        }
    }
    
    /**
     * 添加一个组件定义
     * @param cd 
     */
    public final static void addComponentDefine(ComponentDefine cd) {
        List<ComponentDefine> typeList = COMPONENT_DEFINES.get(cd.getType());
        if (typeList == null) {
            typeList = new ArrayList();
            COMPONENT_DEFINES.put(cd.getType(), typeList);
        }
        typeList.add(cd);
    }
    
    /**
     * 创建组件
     * @param cd
     * @param jfxEdit 编辑器
     */
    public final static void createComponent(ComponentDefine cd, JfxEdit  jfxEdit) {
        try {
            ComponentConverter cc = getConverter(cd);
            if (cc == null) {
                LOG.log(Level.WARNING, "Could not find component converter, cd={0}", cd);
                return;
            }
            cc.create(cd, jfxEdit);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ComponentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 通过组件定义查找组件的转换器
     * @param cd
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    public final static ComponentConverter getConverter(ComponentDefine cd) throws 
            ClassNotFoundException, InstantiationException, IllegalAccessException {
        ComponentConverter converter = CONVERTER_CACHE.get(cd.getConverterClass());
        if (converter == null) {
            synchronized (CONVERTER_CACHE) {
                Class clazz = Class.forName(cd.getConverterClass());
                converter = (ComponentConverter) clazz.newInstance();
                CONVERTER_CACHE.put(cd.getConverterClass(), converter);
            }
        }
        return converter;
    }
    
    /**
     * 获取所有组件定义
     * @param store
     * @return 
     */
    public final static List<ComponentDefine> getComponents(List<ComponentDefine> store) {
        if (store == null) {
            store = new ArrayList();
        }
        for (List<ComponentDefine> list : COMPONENT_DEFINES.values()) {
            store.addAll(list);
        }
        return store;
    }

    /**
     * 获取指定类型的组件定义列表
     * @param componentType 组件类型
     * @return 
     */
    public final static List<ComponentDefine> getComponentsByType(String componentType) {
        return COMPONENT_DEFINES.get(componentType);
    }
    
    // remove20170218
//        public static void loadComponents() {
//         COMPONENT_DEFINES.clear();
//         File rootDir = new File("data/component");
//         if (!rootDir.exists() || !rootDir.isDirectory()) {
//             LOG.log(Level.SEVERE, "data/component direction not found!");
//             return;
//         }
//
//         // 递归载入data/component目录下的转换器配置，data/component下允许多层次目录
//         // 每个目录放一个config文件，config文件中每一行定义一个要载入的转换器配置
//         loadComponentFromDir(rootDir);
//     }
    //    private static void loadComponentFromConfigFile(File configFile) throws UnsupportedEncodingException, FileNotFoundException, IOException {
//        // 从config中读取 converter xml配置文件
//        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), "utf-8"));
//        String line;
//        while ((line = br.readLine()) != null) {
//            if (line.trim().length() <= 0) 
//                continue;
//            if (line.trim().startsWith("#")) 
//                continue;
//            File converterFile = new File(configFile.getParent(), line);
//            if (!converterFile.exists() || !converterFile.isFile()) 
//                continue;
//            try {
//                loadComponentFile(converterFile);
//            } catch (Exception e) {
//                LOG.log(Level.SEVERE, "Could not load component file=" + line, e);
//            }
//        }
//        try {
//            br.close();
//        } catch (IOException e) {
//            LOG.log(Level.SEVERE, null, e);
//        }
//    }
    
    //    private static void loadComponentFromDir(File dir) {
//        if (!dir.exists() || !dir.isDirectory())
//            return;
//        // 载入当前文件夹中的配置
//        try {
//            File configFile = new File(dir, "config");
//            if (!configFile.exists() || !configFile.isFile()) {
//                LOG.log(Level.WARNING, "Config file not found!");
//            } else {
//                loadComponentFromConfigFile(configFile);
//            }
//        } catch (FileNotFoundException ex) {
//            LOG.log(Level.SEVERE, "Could not load component, dir=" + dir.getAbsolutePath(), ex);
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Could not load component, dir=" + dir.getAbsolutePath(), ex);
//        }
//        // 载入子文件夹中的配置
//        File[] children = dir.listFiles();
//        for (File f : children) {
//            loadComponentFromDir(f);
//        }
//    }
}
