/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import com.jme3.system.AppSettings;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import name.huliqing.editor.manager.UIManager;
import name.huliqing.editor.ui.Quit;
import name.huliqing.fxswing.Jfx;

/**
 * @author huliqing
 */
public class Starter {
    
    public static void main(String[] args) {
        new Starter().start();
    }
    
    private void start() {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1280, 720);
        settings.setFrameRate(60);
        settings.setSamples(4);
        
        Jfx.create(Editor.class.getName(), settings);
        Jfx.getMainFrame().setLocationRelativeTo(null);
        Jfx.getMainFrame().setVisible(true);
        Jfx.getMainFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Jfx.getMainFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Quit.doQuit(); // 由Quicker负责退出
            }
        });
        
        // 开始构建JFX界面
        // 这里要等待JmeApp执行完simpleInit方法之后再开始执行Jfx UI,因为UI要依赖于JME的初始化
        Jfx.runOnJme(() -> {
            Jfx.runOnJfx(() -> {
                UIManager.initializeLayout(Jfx.getJfxRoot());
            });
        });
        
    }
    
}
