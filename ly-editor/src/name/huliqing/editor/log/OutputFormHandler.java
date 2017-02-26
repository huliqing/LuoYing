/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
