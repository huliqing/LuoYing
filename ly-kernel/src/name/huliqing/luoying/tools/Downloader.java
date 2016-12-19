/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author huliqing
 */
public interface Downloader {
    
    public interface LoadListener {

        /**
         * 下载过程
         * @param process 已经处理的字节数
         */
        void loading(long process);

        /**
         * 下载结束时执行，注意：下载正常或中断结束都要执行该方法。
         */
        void done();

        /**
         * 发生下载失败时
         * @param errorMessage
         */
        void error(String errorMessage);
    }
    
    /**
     * 获取总文件长度。
     * @return 
     * @throws java.io.IOException 
     */
    long getLength() throws IOException ;
    
    /**
     * 开始下载文件
     * @param in 给定的输入流和输出流
     * @param out 
     */
    void load(InputStream in, OutputStream out);
    
    /**
     * 添加下载侦听器
     * @param listener 
     */
    void addListener(LoadListener listener);
    
    /**
     * 判断当前是否正在下载数据
     * @return 
     */
    boolean isLoading();
}
