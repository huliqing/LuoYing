/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package name.huliqing.luoying.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author huliqing
 */
public class XmlUtils {
    /**
     * 在内存中创建一个Document文档对象
     * @return doc
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    public final static org.w3c.dom.Document newDocument() throws
            ParserConfigurationException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }

    public final static org.w3c.dom.Document newDocument(InputSource source) throws
            ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(source);
    }
    
    public final static org.w3c.dom.Document newDocument(InputSource source, EntityResolver entityResolver) throws
            ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver(entityResolver);
        return builder.parse(source);
    }

    /**
     * 将XML格式的字符串转换为Document类型
     * @param source
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public final static org.w3c.dom.Document newDocument(String source) throws
            ParserConfigurationException, SAXException, IOException {
        InputSource inputSource = new InputSource(new StringReader(source));
        return (newDocument(inputSource));
    }
    
    /**
     * 将XML格式的文件流转换为Document类型
     * @param source
     * @param encoding 编码，如："utf-8"
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public final static org.w3c.dom.Document newDocument(InputStream source, String encoding) throws 
            ParserConfigurationException, SAXException, IOException {
        InputSource inputSource = new InputSource(source);
        inputSource.setEncoding(encoding);
        return (newDocument(inputSource));
    }

    /**
     * 通过xml文件路径获取一个Document文档对象
     * @param xmlPath
     *      XML文件的路径
     * @return doc
     *      该XML文件的Document对象
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    public final static org.w3c.dom.Document getDocument(String xmlPath) throws
            SAXException, IOException, ParserConfigurationException {
        return getDocument(new File(xmlPath));
    }

    /**
     * 读取file对象并分析，返回一个Document对象
     * @param file 需要分析的文件
     * @return doc Document对象
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    public final static org.w3c.dom.Document getDocument(File file) throws
            SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
    }

    /**
     * 该方法用于将doc文件保存至xmlPath, 将内存中的整个Document保存至路径
     * @param doc
     *      已经读入内存的document文档对象
     * @param xmlPath
     *      保存路径
     * @throws javax.xml.transform.TransformerConfigurationException
     * @throws javax.xml.transform.TransformerException
     */
    public final static void saveDocument(Document doc, String xmlPath) throws
            TransformerConfigurationException, TransformerException {
        saveDocument(doc, new File(xmlPath));
    }

    /**
     * 该方法用于将doc文件保存至file, 将内存中的整个Document保存至file所在的路径
     * @param doc 已经读入内存的document文档对象
     * @param file 保存的文件对象
     * @throws javax.xml.transform.TransformerConfigurationException
     * @throws javax.xml.transform.TransformerException
     */
    public final static void saveDocument(Document doc, File file) throws
            TransformerConfigurationException, TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    /**
     * 输出doc
     * @param doc
     * @param writer
     * @throws javax.xml.transform.TransformerConfigurationException
     * @throws javax.xml.transform.TransformerException
     */
    public final static void write(Document doc, Writer writer) throws
            TransformerConfigurationException, TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
    }

    /**
     * 将内存对象中的Document转换为XML格式的字符串
     * @param doc
     * @return xmlString
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    public final static String toXmlString(Document doc)
            throws TransformerConfigurationException, TransformerException {
        StringWriter sw = new StringWriter();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(sw);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
//        transformer.setOutputProperty(OutputKeys.INDENT, "true");
        transformer.transform(source, result);
        return sw.toString();
    }
    
    // utils tools....
    
    /**
     * 获取给定的element的参数，并转换为目标类型
     * @param ele
     * @param attr
     * @param defValue
     * @return 
     */
    public static int getAsInteger(Element ele, String attr, int defValue) {
        String temp = ele.getAttribute(attr).trim();
        if (temp.isEmpty()) return defValue;
        return Integer.parseInt(temp);
    }
    
    public static float getAsFloat(Element ele, String attr, float defValue) {
        String temp = ele.getAttribute(attr).trim();
        if (temp.isEmpty()) return defValue;
        return Float.parseFloat(temp);
    }
    
    public static boolean getAsBoolean(Element ele, String attr, boolean defValue) {
        String temp = ele.getAttribute(attr).trim();
        if (temp.isEmpty()) return defValue;
        return "1".equals(temp);
    }
    
    public static String getAsString(Element ele, String attr, String defValue) {
        String temp = ele.getAttribute(attr).trim();
        if (temp.isEmpty()) return defValue;
        return temp;
    }
    
    public static Map<String, String> getAttributes(Element ele) {
        NamedNodeMap attMap = ele.getAttributes();
        int len = attMap.getLength();
        Map<String, String> resultMap = new HashMap<String, String>(len);
        for (int i = 0; i < len; i++) {
            Node node = attMap.item(i);
            resultMap.put(node.getNodeName(), node.getNodeValue());
        }
        return resultMap;
    }

    // ---------- help
    
    public final static void viewNode(Node ele) {
        System.out.println("getNodeName:" + ele.getNodeName());
        System.out.println("getLocalName:" + ele.getLocalName());
        System.out.println("getNodeValue:" + ele.getNodeValue());
        System.out.println("getTextContent:" + ele.getTextContent());
        System.out.println("getPrefix:" + ele.getPrefix());
        System.out.println("getBaseURI:" + ele.getBaseURI());
        System.out.println("getNamespaceURI:" + ele.getNamespaceURI());
        System.out.println("getNodeType:" + ele.getNodeType());
    }
}
