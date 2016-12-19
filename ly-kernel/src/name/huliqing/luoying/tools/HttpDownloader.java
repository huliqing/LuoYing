/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.tools;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author huliqing
 */
public class HttpDownloader extends AbstractDownloader {

    // 下载文件路径
    private long length = -1;
    
    /**
     * 从网络上下载数据
     * @param url 网络上的url路径，如：http://www.example.com/abc.txt
     * @param out 给定的输出流，不能为null，该输出流在结束时会自动被关闭
     * @throws IOException 
     */
    public void load(String url, OutputStream out) throws IOException {
        if (isLoading()) 
            return;
        URL httpUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
        length = conn.getContentLength();
        if (length <= -1) {
            conn.disconnect();
            return;
        }
        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
        load(bis, out);
    }
    
    /**
     * 获取文件长度，注意：只有在开始loading之后才能获得文件大小
     * @return
     * @throws IOException 
     */
    @Override
    public long getLength() throws IOException {
        return length;
    }
    
//    public static void main(String[] args) {
//        HttpDownloader loader = new HttpDownloader();
//        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        try {
//            // 从网上载入配置
//            loader.addListener(new LoadListener() {
//                @Override
//                public void loading(long process) {}
//                @Override
//                public void error(String errorMessage) {}
//                
//                @Override
//                public void done() {
//                    final String content = baos.toString();
//                    System.out.println("====content===");
//                    System.out.println(content);
//                }
//            });
//            loader.load("http://app.huliqing.name/ly3d/adconfig", baos);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
}
