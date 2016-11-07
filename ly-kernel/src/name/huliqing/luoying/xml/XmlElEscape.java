/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于将EL表达式中的特殊字符进行转义
 * @author huliqing
 */
public class XmlElEscape {
    private final static Pattern PATTERN=  Pattern.compile("#\\{.*?\\}");
    
    public static String convert(String xmlStr) {
        Matcher m = PATTERN.matcher(xmlStr);
        StringBuffer sb = new StringBuffer();
        String temp;
        while(m.find()) {
            temp = xmlStr.substring(m.start(), m.end());
            temp = temp
                    .replaceAll("&", "&amp;")
                    .replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;")
                    .replaceAll("'", "&apos;")
                    .replaceAll("\"", "&quot;");
            m.appendReplacement(sb, temp);
        }
        m.appendTail(sb);
        return sb.toString();
    }
    
//    public static void main(String[] args) {
//        String line = "#{aa&&bb},#{cc>=dd}";
//        String result = new XmlElEscape().convert(line);
//        System.out.println("result=" + result);
//    }
}
