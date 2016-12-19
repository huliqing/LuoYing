/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.tools;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractDownloader implements Downloader {

    private List<LoadListener> listeners;
    private boolean started;
    private LoadThread loadThread;

    /**
     * 开始下载数据，给定的输入流和输出流都不能为null,并且在下载完成之后in
     * 和out都将被自动关闭。当下载正在进行时调用该方法将不会有任何响应。
     * @param in
     * @param out 
     */
    @Override
    public void load(InputStream in, OutputStream out) {
        if (isLoading()) {
            return;
        }
        started = true;
        loadThread = new LoadThread(in, out);
        loadThread.start();
    }

    /**
     * 添加下载侦听器，注：listener是在多线程中运行的。注意线程安全，特别是
     * 与UI的交互.
     * @param listener 
     */
    @Override
    public void addListener(LoadListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<LoadListener>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public boolean isLoading() {
        return started;
    }

    /**
     * 取消当前的下载
     */
    public synchronized void cancel() {
        started = false;
        if (loadThread != null) {
            loadThread.breakOff.set(true);
        }
    }
    
    private class LoadThread extends Thread {
        
        public AtomicBoolean breakOff = new AtomicBoolean(false);
        private InputStream in;
        private final OutputStream out;
        
        public LoadThread(InputStream in, OutputStream out) {
            this.in = in;
            this.out = out;
        }
        
        @Override
        public void run() {
            try {
                byte[] buff = new byte[4096];
                int len;
                long process = 0;
                while ((len = in.read(buff)) != -1) {
                    // 中断执行
                    if (breakOff.get()) {
                        break;
                    }
                    // 执行下载中
                    out.write(buff, 0, len);
                    process += len;
                    if (listeners != null) {
                        for (LoadListener listener : listeners) {
                            listener.loading(process);
                        }
                    }
                }
            } catch (Exception e) {
                if (listeners != null) {
                    for (LoadListener listener : listeners) {
                        listener.error(e.getMessage());
                    }
                }
            } finally {
                started = false;
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception be) {
                        Logger.getLogger(AbstractDownloader.class.getName()).log(Level.SEVERE, null, be);
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception be) {
                        Logger.getLogger(AbstractDownloader.class.getName()).log(Level.SEVERE, null, be);
                    }
                }
                if (listeners != null) {
                    for (LoadListener listener : listeners) {
                        listener.done();
                    }
                }
            }
        }
    }
}
