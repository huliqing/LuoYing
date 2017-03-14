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
package name.huliqing.luoying.object;

import com.jme3.asset.AssetManager;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.LuoYingException;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.utils.FileUtils;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.xml.DataProcessor;
import name.huliqing.luoying.xml.ObjectData;

public class Loader {
    
    /**
     * 通过物体的id来载入一个物体的数据。
     * @param <T>
     * @param id
     * @return 
     */
    public static <T extends ObjectData> T loadData(String id) {
        return DataFactory.createData(id);
    }
    
    /**
     * 通过文件路径载入物体数据
     * @param <T>
     * @param path
     * @return 
     */
    public static <T extends ObjectData> T loadDataByPath(String path) {
        ObjectData od = (ObjectData) LuoYing.getAssetManager().loadAsset(path);
        return (T) od;
    }
    
    /**
     * 通过字节流载入物体
     * @param <T>
     * @param is
     * @return
     */
    public static <T extends ObjectData> T loadData(InputStream is) {
        try {
            byte[] data = read(is);
            BinaryImporter bi = BinaryImporter.getInstance();
            return (T) bi.load(data);
        } catch (IOException e) {
            throw new LuoYingException(e);
        }
    }
    
    /**
     * 直接通过id载入物体
     * @param <T>
     * @param id
     * @return 
     */
    public static <T extends DataProcessor> T load(String id) {
        return load(DataFactory.createData(id));
    }
    
    /**
     * 通过物体数据定义载入物体
     * @param <T>
     * @param data
     * @return 
     */
    public static <T extends DataProcessor> T load(ObjectData data) {
        T dp = DataFactory.createProcessor(data);
        if (dp instanceof Entity) {
            ((Entity) dp).initialize();
        }
        return dp;
    }
    
    /**
     * 通过Lyo文件路径来载入物体,例如: "Scenes/scene.lyo"
     * @param <T>
     * @param path
     * @return 
     */
    public static <T extends DataProcessor> T loadObject(String path) {
        ObjectData od = (ObjectData) LuoYing.getAssetManager().loadAsset(path);
        return load(od);
    }

    /**
     * 载入模型
     * @param file 模型路径
     * @return 
     */
    public static Spatial loadModel(String file) {
        return AssetLoader.loadModel(file);
    }
    
    /**
     * 载入材质
     * @param j3mFile
     * @return 
     */
    public static Material loadMaterial(String j3mFile) {
        AssetManager am = LuoYing.getAssetManager();
        return am.loadMaterial(j3mFile);
    }
    
    private static byte[] read(InputStream is) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        try {
            byte[] buff = new byte[1024];
            int len;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((len = bis.read(buff, 0, buff.length)) != -1) {
                baos.write(buff, 0, len);
            }
            return baos.toByteArray();
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
