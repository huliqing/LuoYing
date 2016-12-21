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
package name.huliqing.luoying.log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * 日志记录工厂类
 * @author huliqing
 */
public class LogFactory {
    
    public final static void initialize() {
        try {
            InputStream is = LogFactory.class.getResourceAsStream("logging.properties");
            LogManager.getLogManager().readConfiguration(is);
        } catch (IOException ex) {
            Logger.getLogger(LogFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(LogFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 重新设置 Log
     * @param level 在开发状态时设置{@link Level#INFO}, 发布状态一般设置为{@link Level#WARNING}
     * @param useParentHandlers 在开发状态时应该设置为true，这允许在控制台输出log信息
     * 在发布状态时可设置为false
     */
    public final static void resetLogger(Level level, boolean useParentHandlers) {
        LogManager lm = LogManager.getLogManager();
        Enumeration<String> names = lm.getLoggerNames();
        if (names != null) {
            while (names.hasMoreElements()) {
                Logger logger = lm.getLogger(names.nextElement());
                if (logger != null) {
                    logger.setLevel(level);
                    logger.setUseParentHandlers(useParentHandlers);
                }
            }
        }
    }
    
//    /**
//     * 单文件日志
//     * @param logDir 记录日志的文件夹，如果不存在，则尝试自动创建这个目录。
//     * @param filename 日志记录名称，如 "LuoYing", 不需要带后缀名
//     * @param maxSize 日志文件限制大小, 单位字节
//     */
//    private static Handler createSimpleHandler(String logDir, String filename, long maxSize) {
//        try {
//            SimpleLogHandler handler = new SimpleLogHandler(new File(logDir), filename, maxSize);
//            handler.setEncoding("UTF-8");
//            return handler;
//        } catch (SecurityException ex) {
//            Logger.getLogger(LogFactory.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(LogFactory.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
    
//    /**
//     * 天天日志
//     * @param logDir 记录日志的文件夹，如果不存在，则尝试自动创建这个目录。
//     * @param filename  日志记录名称，如 "LuoYing", 不需要带后缀名，系统将根据当前日期自动补全完整的日志文件名，
//     * 格式示例："LuoYing20161211.log"
//     */
//    private static Handler createDailyHandler(String logDir, String filename) {
//        try {
//            DailyLogHandler handler = new DailyLogHandler(new File(logDir), filename);
//            handler.setEncoding("UTF-8");
//            return handler;
//        } catch (SecurityException ex) {
//            Logger.getLogger(LogFactory.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(LogFactory.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
    
}
