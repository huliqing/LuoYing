package name.huliqing.test.fxjme;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.jme3.system.AppSettings;
import name.huliqing.editor.Editor;

/**
 *
 * @author huliqing
 */
public class TestEditor {

    public static void main(String[] args) {
        
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1024, 768);
        settings.setFrameRate(60);
        
        Editor app = new Editor();
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
        
    }
}
