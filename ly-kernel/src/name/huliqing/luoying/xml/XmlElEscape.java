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
