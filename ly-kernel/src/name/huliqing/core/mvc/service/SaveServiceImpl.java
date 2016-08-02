/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import com.jme3.export.Savable;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.utils.FileUtils; 

/**
 * 
 * @author huliqing
 */
public class SaveServiceImpl implements SaveService {
    
    // 存档保存路径
    private final String saveDir = System.getProperty("user.home") + File.separator + "ly3dSave";

    @Override
    public void inject() {
        // ignore
    }
    
    /**
     * 获取存档目录，如果不存在则创建一个新的.重写这个方法来实现在不同的平台
     * 下保存在不同的环境目录下，注：该目录最好是独立，尽量不要与其它程序或
     * 系统文件使用相同目录。当前游戏所有保存的文件都以key值为文件名保存在该
     * 目录下。
     * @return 
     */
    protected File getSaveDir() {
        File file = new File(saveDir);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        return file;
    }
    
    // -------------------------
    
    /**
     * 实现保存数据，注：key是唯一的，如果不同的数据保存到同一样key中，则数
     * 据将会被覆盖。
     * @param key
     * @param data 
     */
    @Override
    public void save(String key, byte[] data) {
        if (key == null || key.trim().isEmpty()) 
            throw new NullPointerException();
        File dir = getSaveDir();
        try {
            FileUtils.saveFile(data, new File(dir, key));
        } catch (Exception e) {
            Logger.getLogger(SaveServiceImpl.class.getName())
                    .log(Level.SEVERE, "Could not save datas! key={0}", key);
        }
    }

    @Override
    public void delete(String key) {
        if (key == null) 
            return;
        File dir = getSaveDir();
        try {
            File delFile = new File(dir, key);
            if (delFile.isFile()) {
                delFile.delete();
            }
        } catch (Exception e) {
            Logger.getLogger(SaveServiceImpl.class.getName())
                    .log(Level.SEVERE, "Could not delete save key! key={0}", key);
        }
    }

    /**
     * 实现读取数据，根据给定的key把保存的数据读取回来。
     * @param key
     * @return 
     */
    @Override
    public byte[] load(String key) {
        File dir = getSaveDir();
        try {
            File saveFile = new File(dir, key);
            if (!saveFile.isFile()) {
                return null;
            }
            return FileUtils.readFile(saveFile);
        } catch (Exception e) {
            Logger.getLogger(SaveServiceImpl.class.getName())
                    .log(Level.SEVERE, "Could not load data! key={0}", key);
        }
        return null;
    }

    @Override
    public boolean existsSaveKey(String key) {
        if (key == null) 
            return false;
        File dir = getSaveDir();
        File target = new File(dir, key);
        return target.exists();
    }

    @Override
    public final void saveSavable(String key, Savable data) {
        byte[] bytes = encode(data);
        save(key, bytes);
    }

    @Override
    public final Savable loadSavable(String key) {
        byte[] bytes = load(key);
        if (bytes == null) 
            return null;
        return decode(bytes);
    }
    
    /**
     * 如果有需要，可以重写encode和decode方法来对保存数据进行加密解密编码.
     * @param data
     * @return 
     */
    protected byte[] encode(Savable data) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        BinaryExporter exp = BinaryExporter.getInstance();
        try {
            exp.save(data, os);
            return os.toByteArray();
        } catch (IOException e) {
            Logger.getLogger(SaveService.class.getName())
                    .log(Level.SEVERE, "Could not encode save data.", e);
        }
        return null;
    }

    /**
     * 如果有需要，可以重写encode和decode方法来对保存数据进行加密解密编码.
     * @param bytes
     * @return 
     */
    protected Savable decode(byte[] bytes) {
        BinaryImporter bi = BinaryImporter.getInstance();
        try {
            return bi.load(bytes);
        } catch (IOException e) {
            Logger.getLogger(SaveService.class.getName())
                    .log(Level.SEVERE, "Could not decode save data.", e);
        }
        return null;
    }
}
