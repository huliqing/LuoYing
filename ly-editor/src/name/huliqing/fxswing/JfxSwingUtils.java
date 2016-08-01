/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fxswing;

import com.jme3.app.LegacyApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import java.awt.Canvas;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javax.swing.JFrame;
import javax.swing.JWindow;

/**
 *
 * @author huliqing
 */
public class JfxSwingUtils {

    private static final Logger LOG = Logger.getLogger(JfxSwingUtils.class.getName());

    /**
     * 创建游戏,默认帧率60.
     * @param appClass
     * @param width
     * @param height
     * @return 
     */
    public static JfxSwing create(String appClass, int width, int height) {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(width, height);
        settings.setFrameRate(60);
        return create(appClass, settings, new VBox());
    }
    
    /**
     * 使用指定的settings及指定的jfxRoot根节点来创建游戏。
     * @param appClass
     * @param settings
     * @param jfxRoot
     * @return 
     */
    public static JfxSwing create(String appClass, AppSettings settings, Pane jfxRoot) {
        JfxSwing js = new JfxSwing();
        
        // 创建主界面，这个界面会包含Canvas，用于渲染游戏。
        JFrame mainFrame = createMainFrame(js, appClass, settings);
        
        // 创建Jfx界面，这个界面会包含JFX组件，作为UI界面，该界面始终覆盖在mainFrame上面
        JWindow jfxWindow = createJfxWindow(js, mainFrame, jfxRoot);
        
        // 绑定Jfx窗口，使Jfx窗口始终与mainFrame的位置和大小一致。
        JfxBindingController jbc = new JfxBindingController(); 
        jbc.bind(jfxWindow, mainFrame);
        
        // 当主窗口关闭时要同时关闭JME游戏
        js.getMainFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                // 这里应该不需要放在jme app线程
                js.getJmeApp().stop();
            }
        });
        
        mainFrame.setVisible(true);
        return js;
    }

    /**
     * 创建主界面面板。
     * @param ms
     * @param appClass
     * @param settings
     * @return 
     */
    private static JFrame createMainFrame(JfxSwing ms, String appClass, AppSettings settings) {
        JFrame jframe = new JFrame("Test");
        jframe.setName("MainFrame");
        jframe.setSize(settings.getWidth(), settings.getHeight());
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jframe.add(createCanvas(ms, appClass, settings));
        jframe.setLocationRelativeTo(null);
        ms.setMainFrame(jframe);
        return jframe;
    }
    
    /**
     * 创建JWindow,该Window用于作为Jfx组件的容器，这个window始终覆盖在frame上。
     * @param ms
     * @param mainFrame
     * @param jfxRoot
     * @return 
     */
    private static JWindow createJfxWindow(JfxSwing ms, JFrame mainFrame, final Pane jfxRoot) {
        final JWindow jfxWin = new JWindow(mainFrame);
        jfxWin.setName("JFXWindow");
        jfxWin.setSize(mainFrame.getSize());
        jfxWin.setLocationRelativeTo(null);
        jfxWin.setVisible(true);
        
        JFXPanel jfxPanel = new JFXPanel();
        jfxWin.getContentPane().add(jfxPanel);

        Platform.runLater(() -> {
            // 设置JFX主场景，并让JFX主界面变得透明，这样不会覆盖整个Canvas.
            jfxRoot.setBackground(javafx.scene.layout.Background.EMPTY);
            Scene scene = new Scene(jfxRoot, Color.TRANSPARENT);
            jfxPanel.setScene(scene);
        });
        
        ms.setJfxWindow(jfxWin);
        ms.setJfxRoot(jfxRoot);
        return jfxWin;
    }

    /**
     * 创建Canvas并启动游戏。
     * @param ms
     * @param appClass
     * @param settings
     * @return 
     */
    private static Canvas createCanvas(JfxSwing ms, String appClass, AppSettings settings) {
        LegacyApplication app;
        try {
            Class<? extends LegacyApplication> clazz = (Class<? extends LegacyApplication>) Class.forName(appClass);
            app = clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, "Could not createCanvas:{0}", ex);
            return null;
        }
        app.setSettings(settings);
        app.setPauseOnLostFocus(false);
        app.createCanvas();
        app.startCanvas();

        JmeCanvasContext context = (JmeCanvasContext) app.getContext();
        Canvas canvas = context.getCanvas();
        canvas.setSize(settings.getWidth(), settings.getHeight());
        
        ms.setJmeApp(app);
        return canvas;
    }
    
  
}
