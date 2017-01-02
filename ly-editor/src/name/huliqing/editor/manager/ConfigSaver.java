/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.utils.FileUtils;

/**
 *
 * @author huliqing
 */
public class ConfigSaver {

    private static final Logger LOG = Logger.getLogger(ConfigSaver.class.getName());
    
    private final Map<String, String> configs = new LinkedHashMap<>();
    
    /**
     * 载入配置文件
     * @param file 
     */
    public void load(File file) {
        configs.clear();
        try {
            configs.putAll(loadResource(new FileInputStream(file), "utf-8"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 将配置文件保存起来
     * @param file 指定的文件路径
     */
    public void save(File file) {
        StringBuilder sb = new StringBuilder("");
        configs.forEach((k, v) -> {
            if (k.startsWith("#")) { // 注释不改变
                sb.append(v).append("\r\n");
            } else {
                sb.append(k).append("=").append(v).append("\r\n");
            }
        });
        String result = sb.toString();
        saveFile(result.getBytes(), file);
    }
    
    public void set(String key, String value) {
        configs.put(key, value);
//        LOG.log(Level.INFO, "setKey={0}, value={1}", new Object[] {key, value});
    }
    
    public void set(String key, List<String> values) {
        if (values == null || values.isEmpty()) {
            configs.put(key, "");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String v : values) {
            if (v == null || v.trim().isEmpty()) {
                continue;
            }
            sb.append(v).append(",");
        }
        configs.put(key, sb.substring(0, sb.length() - 1));
//        LOG.log(Level.INFO, "setKeyList={0}, value={1}", new Object[] {key, sb.substring(0, sb.length() - 1)});
    }
    
    /**
     * @param key
     * @return 
     */
    public String get(String key) {
        String line = configs.get(key);
        return line;
    }
    
    /**
     * 使用集合方式获取键值，默认使用","(半角逗号)分隔。
     * @param key
     * @return 
     */
    public List<String> getAsList(String key) {
        return getAsList(key, ",");
    }
    
    public List<String> getAsList(String key, String split) {
        String line = get(key);
        if (line == null)
            return null;
        return new ArrayList<String>(Arrays.asList(line.split(split)));
    }
    
    private Map<String, String> loadResource(InputStream is, String encoding) {
        BufferedReader br = null;
        Map<String, String> result = new LinkedHashMap<String, String>();
        try {
            br = new BufferedReader(new InputStreamReader(is, encoding));
            String lineStr;
            int index;
            int count = 0;
            while ((lineStr = br.readLine()) != null) {
                count++;
                if (lineStr.startsWith("#")) {
                    result.put("#" + count, lineStr);
                    continue;
                }
                
                index = lineStr.indexOf("=");
                if (index == -1) {
                    result.put("#" + count, lineStr);
                    continue;
                }
                
                String key = lineStr.substring(0, index).trim();
                String value = lineStr.substring(index + 1);
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
    
    private void saveFile(byte[] saveData, File saveFile) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(saveFile);
            fos.write(saveData != null ? saveData : "".getBytes());
        } catch (IOException ex) {
            Logger.getLogger(ConfigSaver.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }
}
