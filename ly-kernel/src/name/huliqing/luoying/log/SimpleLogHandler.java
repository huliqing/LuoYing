/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 * 简单的日志记录，日记只写入一个文件，当日志长度达到所限制的最高字节数时，日志将被删除后重建。
 * @author huliqing
 */
public class SimpleLogHandler extends StreamHandler {
 
    private boolean loggable = true;
    
    // 记录日志的文件夹
    private File logDir;
    // 日志文件名，如： LuoYing
    private String filename;
    // 允许的最大的日志大小
    private long maxSize;
    // 当前已经记录的日志的字节大小
    private long logSize;

    public SimpleLogHandler() {
        try {
            setEncoding("UTF-8");
        } catch (SecurityException ex) {
            Logger.getLogger(SimpleLogHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SimpleLogHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 初始化设置
     * @param logDir 记录日志的文件夹，如果不存在，则尝试自动创建这个目录。
     * @param filename 日志记录名称，如 "LuoYing", 不需要带后缀名
     * @param maxSize 日志文件限制大小, 单位字节
     */
    public void initConfig(File logDir, String filename, long maxSize) {
        this.logDir = logDir;
        this.filename = filename;
        this.maxSize = maxSize;
         
        File logFile = getLogFile();
        if (logFile.exists() && logFile.isFile()) {
            if (logFile.length() >= maxSize) {
                logFile.delete();
                logSize = 0;
            } else {
                logSize = logFile.length();
            }
        }
        setOutputStream(createOutputStream());
    }
    
    @Override
    public synchronized void publish(LogRecord record) {
        if (!loggable) {
            return;
        }
        // 当日志满时重建文件
        if (logSize > maxSize) {
            // 这里是用一个额外的ByteArrayOutputStream来让原有的流结束并关闭。
            // 这很重要，因为在重新创建日志文件之前必须先关闭流、释放文件锁定占用，才能删除日志文件，以便重建。
            setOutputStream(new ByteArrayOutputStream(0));
            // 删除日志文件。
            deleteLogFile();
            // 重建文件流
            setOutputStream(createOutputStream());
            // 重新计算文件字节。
            logSize = 0;
            return;
        }
        super.publish(record);
        super.flush();
    }
    
    private OutputStream createOutputStream() {
        FileOutputStream fos;
        try {
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            fos = new FileOutputStream(getLogFile(), true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DailyLogHandler.class.getName()).log(Level.SEVERE, null, ex);
            loggable = false;
            return null;
        }
        return new HandleBufferedOutputStream(fos);
    }
    
    private File getLogFile() {
        return new File(logDir, filename + ".log");
    }
    
    private void deleteLogFile() {
        File logFile = getLogFile();
        if (logFile.exists() && logFile.isFile()) {
            logFile.delete();
        }
    }
    
    /**
     * 用于动态记录日志写入的字节数
     */
    private class HandleBufferedOutputStream extends BufferedOutputStream {
        
        public HandleBufferedOutputStream(OutputStream out) {
            super(out);
        }

        @Override
        public synchronized void write(byte[] b, int off, int len) throws IOException {
            super.write(b, off, len);
            logSize += len;
        }

        @Override
        public synchronized void write(int b) throws IOException {
            super.write(b);
            logSize++;
        }

        @Override
        public void write(byte[] b) throws IOException {
            super.write(b);
            logSize += b.length;
        }
    }
}
