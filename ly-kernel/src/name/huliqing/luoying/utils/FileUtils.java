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
package name.huliqing.luoying.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author huliqing
 */
public class FileUtils {
    
    /**
     * 保存文件
     * @param saveString 保存的字符串信息
     * @param saveFile (not null)保存的文件,必须指定路径
     * @throws IOException
     */
    public static void saveFile(String saveString, File saveFile) throws IOException {
        byte[] data = saveString != null ? saveString.getBytes() : "".getBytes();
        saveFile(data, saveFile);
    }
    
    /**
     * 保存字节文件
     * @param saveData
     * @param saveFile
     * @throws IOException 
     */
    public static void saveFile(byte[] saveData, File saveFile) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(saveFile);
            fos.write(saveData != null ? saveData : "".getBytes());
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
    
    /**
     * 保存文件
     * @param saveString 保存的字符串信息
     * @param savePath 保存的绝对路径
     * @return 
     */
    public static boolean saveFile(String saveString, String savePath) {
        try {
            saveFile(saveString, new File(savePath));
            return true;
        } catch (IOException e) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, "Could not save file:" + savePath, e);
            return false;
        }
    }
    
    /**
     * 从指定的File中读取信息
     * @param file
     * @param charset 指定的编码，默认"utf-8"
     * @return
     * @throws IOException 
     */
    public static String readFile(File file, String charset) throws IOException {
        byte[] data = readFile(file);
        String result = new String(data, charset);
        return result;
    }
    
    /**
     * 读取文件,如果文件不存在或读取失败，则返回null.
     * @param savePath 绝对路径
     * @param charset 要使用的文件编码，如果没有指定则使用默认的utf-8
     * @return 
     */
    public static String readFile(String savePath, String charset) {
        try {
            return readFile(new File(savePath), charset);
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, "Could not read file:" + savePath, ex);
        }
        return null;
    }
    
    public static String readFile(InputStream is, String charset) {
        try {
            byte[] data = readFile(is);
            return new String(data, 0, data.length, charset);
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static byte[] readFile(File file) throws IOException {
        return readFile(new FileInputStream(file));
    }
    
    public static byte[] readFile(InputStream is) throws IOException {
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
    
    /**
     * 读取文件，文件路径如: <br >
     * "/data/object/config.xml" <br>
     * "/data/ly16.png" <br>
     * ...
     * @param path
     * @return 
     */
    public static InputStream readFile(String path) {
        InputStream is = FileUtils.class.getResourceAsStream(path);
        return new BufferedInputStream(is);
    }
}
