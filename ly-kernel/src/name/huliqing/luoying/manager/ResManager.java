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
package name.huliqing.luoying.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.FormatterClosedException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 资源文件读取类
 * @author huliqing
 */
public class ResManager {
    private static final Logger LOG = Logger.getLogger(ResManager.class.getName());
    
    // KEY -> locale, Value = ResourceMap
    private final static Map<String, Map<String, String>> LOCALE_RES = new HashMap<String, Map<String, String>>();
    
    /** 默认的本地环境 */
    private static String locale_default;
    
    /** 当前要使用哪一个本地环境 */
    private static String locale;
    
    /**
     * 载入classPath路径下的资源文件,示例: <br>
     * <pre>
     *  loadResource("/data/resource", "utf-8", "zh_CN")
     * </pre>
     * @param classPathFile classPath资源路径
     * @param encoding 字符集编码，如果没有指定则默认使用"utf-8"
     * @param locale 
     * @see #loadResource(java.io.InputStream, java.lang.String, java.lang.String) 
     */
    public static void loadResource(String classPathFile, String encoding, String locale) {
        loadResource(ResManager.class.getResourceAsStream(classPathFile), encoding, locale);
    }
    
    /**
     * 载入资源文件, 可以指定语言环境，如果没有指定，则载入到默认资源环境。
     * @param is 资源流
     * @param encoding 字符集编码，如果没有指定则默认使用"utf-8"
     * @param locale 指定一个语言环境, 如：en, en_US, zh, zh_CN, zh_HK, zh_TW
     * @see #loadResource(java.lang.String, java.lang.String, java.lang.String) 
     */
    public static void loadResource(InputStream is, String encoding, String locale) {
        Map<String, String> resLoaded = loadResource(is, encoding != null ? encoding : "utf-8");
        Map<String, String> resLocale = LOCALE_RES.get(locale);
        if (resLocale == null) {
            resLocale = new HashMap<String, String>();
            LOCALE_RES.put(locale, resLocale);
        }
        resLocale.putAll(resLoaded);
    }
    
    /**
     * 设置要特别使用的本地环境，如：en, en_US, zh, zh_CN, h_HK, zh_TW, ... 当设置这个本地环境之后，
     * 将一直使用，除非进行重新设置或清除，当资源文件不支持这个环境时，将使用默认的环境设置. <br>
     * {@link #setLocaleDefault(java.lang.String) }.
     * @param locale 
     * @see #setLocaleDefault(java.lang.String) 
     */
    public static void setLocale(String locale) {
        ResManager.locale = locale;
    }
    
    /**
     * 设置一个默认的本地环境, 当资源环境设置不支持{@link #setLocale(java.lang.String) }所指定的环境时，
     * 将默认使用这个环境。通常情况下都应该为资源指定一个默认的环境。
     * @param localeDefault 
     */
    public static void setLocaleDefault(String localeDefault) {
        ResManager.locale_default = localeDefault;
    }
    
    /**
     * 获取当前使用的本地环境
     * @return 
     */
    public static String getLocale() {
        if (locale == null) {
            locale = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
        }
        return locale;
    }
    
   /**
     * 清理所有已经载入的资源信息
     */
    public static void clearResources() {
        LOCALE_RES.clear();
    }
    
   /**
     * 从默认资源文件中获取资源
     * @param key
     * @return 
     * @see #get(java.lang.String, java.lang.Object[]) 
     * 
     */
    public static String get(String key) {
        return get(key, null, getLocale());
    }
    
    /**
     * 从默认资源文件中获取资源
     * @param key
     * @param params 参数
     * @return 
     * @see #get(java.lang.String) 
     */
    public static String get(String key, Object... params) {
        return get(key, params, getLocale());
    }
    
    /**
     * 从默认资源文件中获取资源
     * @param key
     * @param params
     * @param locale
     * @return 
     */
    private static String get(String key, Object[] params, String locale) {
        if (locale == null || !LOCALE_RES.containsKey(locale)) {
            return getString(LOCALE_RES.get(locale_default), key, params);
        }
        return getString(LOCALE_RES.get(locale), key, params);
    }
    
    private static String getString(Map<String, String> resource, String key, Object[] params) {
        String value = resource.get(key);
        if (value == null) {
            value = "<" + key + ">";
        }
        if (params != null) {
            try {
                value = String.format(value, params);
            } catch (FormatterClosedException fce) {
                LOG.log(Level.WARNING, fce.getMessage(), fce);
                value = "<" + key + ">";
            }
        }
        return value;
    }
    
    /**
     * 载入资源文件，如果resource指定的名称不是绝对路径，则默认从/data/font/下查找资源文件。
     * 使用"/"开头来指定绝对资源文件的路径。
     * @param resource
     * @return 
     */
    private static Map<String, String> loadResource(InputStream is, String encoding) {
        BufferedReader br = null;
        Map<String, String> result = new HashMap<String, String>();
        try {
            // 必须指定编码格式，否则在非utf-8平台（如win）下中文会乱码。
            br = new BufferedReader(new InputStreamReader(is, encoding));
            String line;
            int index;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                index = line.indexOf("=");
                if (index == -1) {
                    continue;
                }
                String key = line.substring(0, index).trim();
                String value = line.substring(index + 1); // 没有trim()
                result.put(key, value);
            }
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, e.getMessage());
                }
            }
        }
        return result;
    }
}
