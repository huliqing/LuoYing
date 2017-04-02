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
package name.huliqing.editor.log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;
import javafx.scene.paint.Color;
import name.huliqing.editor.manager.UIManager;

/**
 *
 * @author huliqing
 */
public class OutputFormHandler extends StreamHandler {
    
    public OutputFormHandler() {
        try {
            setEncoding("UTF-8");
        } catch (SecurityException | UnsupportedEncodingException ex) {
            Logger.getLogger(OutputFormHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        setOutputStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {}
        });
    }
    
    @Override
    public synchronized void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }
        String msg = getFormatter().format(record);
        if (record.getLevel() == Level.SEVERE) {
            UIManager.output(msg, Color.RED);
        } else if (record.getLevel() == Level.WARNING) {
            UIManager.output(msg, new Color(0.7, 0.25, 0, 1));
        } else {
            UIManager.output(msg, new Color(0.2, 0.2, 0.2, 1));
        }
    }
    
}
