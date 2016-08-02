/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.utils;

import com.jme3.export.binary.BinaryExporter;
import com.jme3.scene.Spatial;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author huliqing
 */
public class ModelFileUtils {
    
    /**
     * 将文件读取为字符串.
     * @param path
     * @return 
     */
    public static String readFile(String path) {
        InputStream is;
        BufferedInputStream bis = null;
        String result = null;
        try {
            is = ModelFileUtils.class.getResourceAsStream(path);
            bis = new BufferedInputStream(is);
            byte[] buff = new byte[2048];
            int len;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((len = bis.read(buff)) != -1) {
                baos.write(buff, 0, len);
            }
            // 必须指定编码，否则在win下可能中文乱码
            result = baos.toString("utf-8");
        } catch (IOException ioe) {
            Logger.getLogger(ModelFileUtils.class.getName())
                    .log(Level.SEVERE, "Couldnot read file: {0}", path);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    Logger.getLogger(ModelFileUtils.class.getName())
                            .log(Level.SEVERE, "Could not close stream!", e);
                }
            }
        }
        return result;
    }
    
    /**
     * 保存模型文件到目标位置。
     * @param model
     * @param distFile 完整目标文件路径,如：Models/env/example.j3o
     */
    public static void saveTo(Spatial model, String distFile) {
        // 备份文件
        try {
            backup(distFile);
        } catch (Exception e) {
            Logger.getLogger(ModelFileUtils.class.getName()).log(Level.WARNING, "Could not backup file:{0}", distFile);
        }
        
        // 保存文件
        BinaryExporter exp = BinaryExporter.getInstance();
        URL url = Thread.currentThread().getContextClassLoader().getResource(distFile);
        File file = new File(url.getPath());
        try {
            exp.save(model, file);
        } catch (IOException ex) {
            Logger.getLogger(ModelFileUtils.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }
    
    private static void backup(String distFile) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(distFile);
        File source = new File(url.getPath());
        File dist = new File(url.getPath() + "_bak");
        fileCopy(source, dist);
    }
    
    public static void fileCopy(File source, File dist) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(source);
            fo = new FileOutputStream(dist);
            in = fi.getChannel();
            out = fo.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            Logger.getLogger(ModelFileUtils.class.getName()).log(Level.SEVERE, "Could not copy file!", e);
        } finally {
            try {
                if (fi != null) 
                    fi.close();
                if (fo != null) 
                    fo.close();
            } catch (IOException e) {
                Logger.getLogger(ModelFileUtils.class.getName()).log(Level.SEVERE, "Could not close stream!", e);
            }
        }
    }
    
    /**
     * 临时保存文件
     * @param model
     * @param distFile 使用绝对路径，如：c:\temp\abc.j3o
     */
    public static void tempSave(Spatial model, String distFile) {
        BinaryExporter exp = BinaryExporter.getInstance();
        File file = new File(distFile);
        try {
            exp.save(model, file);
        } catch (Exception ex) {
            Logger.getLogger(ModelFileUtils.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }
}
