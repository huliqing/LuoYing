/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.android;

import android.os.Environment;
import java.io.File;
import name.huliqing.luoying.layer.service.ConfigServiceImpl;

/**
 *
 * @author huliqing
 */
public class AndroidConfigServiceImpl extends ConfigServiceImpl {
    
    private String logDir;
    private String saveDir;
    
    @Override
    public String getSaveDir() {
//        File saveDir = Global.getContext().getDir("save", Context.MODE_PRIVATE);
//        return saveDir.getAbsolutePath();

        if (saveDir == null) {
            saveDir = new File(getArchiveRoot(), "save").getAbsolutePath();
        }
        return saveDir;
    }

    @Override
    public String getLogDir() {
        if (logDir == null) {
            logDir = new File(getArchiveRoot(), "log").getAbsolutePath();
        }
        return logDir;
    }
    
    private File getArchiveRoot(){
        
        // test
//        File sdDir = null;
//        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
//        if (sdCardExist) {
//            sdDir = Environment.getExternalStorageDirectory();
//        }
//        return sdDir.toString();

        return new File(Environment.getExternalStorageDirectory(), "LuoYing");
   }
}
