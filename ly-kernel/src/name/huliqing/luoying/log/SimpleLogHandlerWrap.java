/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.log;

import java.io.File;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.ConfigService;

/**
 *
 * @author huliqing
 */
public class SimpleLogHandlerWrap extends SimpleLogHandler {
    
    public SimpleLogHandlerWrap() {
        super();
        File logDir = new File(Factory.get(ConfigService.class).getLogDir());
        String filename = "LuoYing";
        long maxSize = 1024 * 1024 * 5;
        initConfig(logDir, filename, maxSize);
    }
    
}
