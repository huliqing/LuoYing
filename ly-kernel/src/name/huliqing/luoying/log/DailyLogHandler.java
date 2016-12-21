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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 * 每天一个日志,日志文件不限大小，日志将根据不同的日期写入不同的文件。
 * @author huliqing
 */
public class DailyLogHandler extends StreamHandler {
    
    private final static Calendar CALENDAR = Calendar.getInstance();
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    // 记录日志的文件夹
    private final File logDir;
    // 日志文件名，如： LuoYing
    private final String filename;
    
    private int dayOfYear = -1;
    
    private boolean loggable = true;
    
    /**
     * @param logDir 记录日志的文件夹，如果不存在，则尝试自动创建这个目录。
     * @param filename 日志记录名称，如 "LuoYing", 不需要带后缀名，系统将根据当前日期自动补全完整的日志文件名，
     * 格式示例："LuoYing20161211.log"
     */
    public DailyLogHandler(File logDir, String filename) {
        this.logDir = logDir;
        this.filename = filename;
    }

    @Override
    public synchronized void publish(LogRecord record) {
        if (!loggable) {
            return;
        }
        if (dayOfYear != CALENDAR.get(Calendar.DAY_OF_YEAR)) {
            dayOfYear = CALENDAR.get(Calendar.DAY_OF_YEAR);
            // 日期变更时重建输出流，因为需要写到另一个文件，这里不需要关闭流，因为setOutputStream的时候会自动关闭
            // 旧的流，并且flush, 所以切换后直接返回就可以。
            OutputStream os = createOutputStream(logDir, makeDailyFilename(filename));
            setOutputStream(os);
            return;
        }
        super.publish(record);
        super.flush();
    }
    
    private String makeDailyFilename(String filename) {
        return filename + DATE_FORMAT.format(new Date()) + ".log";
    }
    
    private OutputStream createOutputStream(File logDir, String filename) {
        FileOutputStream fos;
        try {
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            File logFile = new File(logDir, filename);
            fos = new FileOutputStream(logFile, true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DailyLogHandler.class.getName()).log(Level.SEVERE, null, ex);
            loggable = false;
            return null;
        }
        return new BufferedOutputStream(fos);
    }

}
