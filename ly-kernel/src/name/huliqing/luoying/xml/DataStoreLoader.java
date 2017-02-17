/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.xml;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author huliqing
 */
public class DataStoreLoader {
    
    /**
     * 载入xml格式的数据.
     * @param xmlStr 
     * @return  
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException 
     */
    public List<Proto> loadData(String xmlStr) 
            throws ParserConfigurationException, SAXException, IOException {
        String xmlEscapsed = XmlElEscape.convert(xmlStr);
        Element root = XmlUtils.newDocument(xmlEscapsed).getDocumentElement();
        NodeList children = root.getChildNodes();
        int length = children.getLength();
        List<Proto> protos = new ArrayList<Proto>(length);
        for (int i = 0; i < length; i++) {
            Node node = children.item(i);
            if (!(node instanceof Element)) {
                continue;
            }
            Element ele = (Element) node;
            Map<String, String> attributes = XmlUtils.getAttributes(ele);
            Proto proto = new Proto(ele.getTagName(), attributes);
            protos.add(proto);
        }
        return protos;
    }
    
    /**
     * 通过数据流的方式载入数据，这些数据将会添加到当前数据容器中,如果容器中存在重复ID的数据，则数据会被覆盖替换。
     * @param dataStream 
     * @param encoding 
     * @return  
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException 
     */
    public List<Proto> loadData(InputStream dataStream, String encoding) 
            throws ParserConfigurationException, SAXException, IOException {
        
        // remove20170217
//        String xmlStr = readFile(dataStream, encoding);
//        String xmlEscapsed = XmlElEscape.convert(xmlStr);
//        Element root = XmlUtils.newDocument(xmlEscapsed).getDocumentElement();
//        NodeList children = root.getChildNodes();
//        int length = children.getLength();
//        List<Proto> protos = new ArrayList<Proto>(length);
//        for (int i = 0; i < length; i++) {
//            Node node = children.item(i);
//            if (!(node instanceof Element)) {
//                continue;
//            }
//            Element ele = (Element) node;
//            Map<String, String> attributes = XmlUtils.getAttributes(ele);
//            Proto proto = new Proto(ele.getTagName(), attributes);
//            protos.add(proto);
//        }
//        return protos;

        return loadData(readFile(dataStream, encoding));
    }
    
    /**
     * 将文件读取为字符串.
     * @param path
     * @return 
     */
    private String readFile(InputStream is, String charset) {
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(is);
            byte[] buff = new byte[2048];
            int len;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((len = bis.read(buff)) != -1) {
                baos.write(buff, 0, len);
            }
            return baos.toString(charset != null ? charset : "utf-8");
        } catch (Exception ioe) {
            throw new RuntimeException(ioe);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    Logger.getLogger(DataStore.class.getName())
                            .log(Level.SEVERE, "Could not close stream!", e);
                }
            }
        }
    }
}
