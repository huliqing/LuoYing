/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import com.jme3.export.Savable;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.utils.FileUtils; 

/**
 * 
 * @author huliqing
 */
public class SaveServiceImpl implements SaveService {
    private static final Logger LOG = Logger.getLogger(SaveServiceImpl.class.getName());
    private ConfigService configService;

    @Override
    public void inject() {
        configService = Factory.get(ConfigService.class);
    }
    
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
        File dir = getDir();
        try {
            FileUtils.saveFile(data, new File(dir, key));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not save datas! key={0}", key);
        }
    }

    @Override
    public void delete(String key) {
        if (key == null) 
            return;
        File dir = getDir();
        try {
            File delFile = new File(dir, key);
            if (delFile.isFile()) {
                delFile.delete();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not delete save key! key={0}", key);
        }
    }

    /**
     * 实现读取数据，根据给定的key把保存的数据读取回来。
     * @param key
     * @return 
     */
    @Override
    public byte[] load(String key) {
        File dir = getDir();
        try {
            File saveFile = new File(dir, key);
            if (!saveFile.isFile()) {
                return null;
            }
            return FileUtils.readFile(saveFile);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not load data! key={0}", key);
        }
        return null;
    }

    @Override
    public boolean existsSaveKey(String key) {
        if (key == null) 
            return false;
        File dir = getDir();
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
        ByteArrayOutputStream os = new ByteArrayOutputStream(10240);
        BinaryExporter exp = BinaryExporter.getInstance();
        try {
            exp.save(data, os);
            return os.toByteArray();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Could not encode save data.", e);
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
            LOG.log(Level.SEVERE, "Could not decode save data.", e);
        }
        return null;
    }
    
    private File getDir() {
        File file = new File(configService.getSaveDir());
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        return file;
    }
}
