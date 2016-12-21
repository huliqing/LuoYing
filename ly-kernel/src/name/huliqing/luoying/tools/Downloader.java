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
