/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fxswing;

import com.jme3.app.Application;
import com.jme3.app.LegacyApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import java.awt.Canvas;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
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
import javax.swing.SwingUtilities;

/**
 * @author huliqing
 */
public class Jfx {

    private static final Logger LOG = Logger.getLogger(Jfx.class.getName());
 
    /**
     * 主界面
     */
    private static JFrame mainFrame;
    
    /**
     * JWindow,包含JFX根节点JFXPanel
     */
    private static JWindow jfxWindow;
    
    /**
     * JME app
     */
    private static LegacyApplication jmeApp;
    
    /**
     * JFX根节点，包含于JFXPanel下面。
     */
    private static Pane jfxRoot;
    
    /**
     * 获取主界面
     * @return 
     */
    public static JFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * 获取JME Application
     * @return 
     */
    public static Application getJmeApp() {
        return jmeApp;
    }

    /**
     * 获取JFX UI根节点容器,用这个根节点容器来添加JFX 组件，如：
     * <pre>
     * <code>
     * Platform.runLater(() -> {
     *      Button btn = new Button("This is a new button");
     *      jfxSwing.getJfxRoot().getChildren().add(btn);
     *  });
     * </code>
     * </pre>
     * @return 
     */
    public static Pane getJfxRoot() {
        return jfxRoot;
    }

    /**
     * 获取JFX UI JWindow窗器，这个容器包含JFX根节点，层次关系是这样的: 
     * MainFrame -> JfxWindow -> JFXPanel -> JfxRoot
     * @return 
     */
    public static JWindow getJfxWindow() {
        return jfxWindow;
    }
    
    /**
     * Run task on swing thread.this method just call:
     * <pre>
     * <code>SwingUtilities.invokeLater(runnable);</code>
     * </pre>
     * @param runnable 
     */
    public static void runOnSwing(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }
    
    /**
     * Run task on jfx thread. this method just call:
     * <pre>
     * <code>Platform.runLater(runnable);</code>
     * </pre>
     * @param runnable 
     */
    public static void runOnJfx(Runnable runnable) {
        Platform.runLater(runnable);
    }
    
    /**
     * Run task on jme thread, this method just call:
     * <pre>
     * <code>jmeApp.enqueue(runnable);</code>
     * </pre>
     * @param runnable 
     */
    public static void runOnJme(Runnable runnable) {
        jmeApp.enqueue(runnable);
    }
    
    /**
     * Run task on jme thread, this method just call:
     * <pre>
     * <code>jmeApp.enqueue(callable);</code>
     * </pre>
     * @param <V>
     * @param callable 
     * @return  
     */
    public static <V> Future<V> runOnJme(Callable<V> callable) {
        return jmeApp.enqueue(callable);
    }
    
    /**
     * 创建游戏,默认帧率60.
     * @param appClass
     * @param width
     * @param height
     */
    public static void create(String appClass, int width, int height) {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(width, height);
        settings.setFrameRate(60);
        create(appClass, settings, new VBox());
    }
    
    /**
     * 创建游戏
     * @param appClass
     * @param settings
     */
    public static void create(String appClass, AppSettings settings) {
        create(appClass, settings, new VBox());
    }
    
    /**
     * 使用指定的settings及指定的jfxRoot根节点来创建游戏。
     * @param appClass
     * @param settings
     * @param jfxRoot
     */
    public static void create(String appClass, AppSettings settings, Pane jfxRoot) {
        Jfx.jfxRoot = jfxRoot;
        
        // 创建主界面，这个界面会包含Canvas，用于渲染游戏。
        createMainFrame(appClass, settings);
        
        // 创建Jfx界面，这个界面会包含JFX组件，作为UI界面，该界面始终覆盖在mainFrame上面
        createJfxWindow(jfxRoot);
        
        // 绑定Jfx窗口，使Jfx窗口始终与mainFrame的位置和大小一致。
        JfxBindingController jbc = new JfxBindingController(); 
        jbc.bind(jfxWindow, mainFrame);
        
        // 当主窗口关闭时要同时关闭JME游戏
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                // 这里应该不需要放在jme app线程
                jmeApp.stop();
            }
        });
        mainFrame.setVisible(true);
    }

    /**
     * 创建主界面面板。
     * @param ms
     * @param appClass
     * @param settings
     * @return 
     */
    private static void createMainFrame(String appClass, AppSettings settings) {
        mainFrame = new JFrame("MainFrame");
        mainFrame.setName("MainFrame");
        mainFrame.setSize(settings.getWidth(), settings.getHeight());
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.add(createCanvas(appClass, settings));
        mainFrame.setLocationRelativeTo(null);
    }
    
    /**
     * 创建JWindow,该Window用于作为Jfx组件的容器，这个window始终覆盖在frame上。
     * @param ms
     * @param mainFrame
     * @param jfxRoot
     * @return 
     */
    private static void createJfxWindow(final Pane jfxRoot) {
        jfxWindow = new JWindow(mainFrame);
        jfxWindow.setName("JFXWindow");
        jfxWindow.setSize(mainFrame.getSize());
        jfxWindow.setLocationRelativeTo(null);
        jfxWindow.setVisible(true);
        
        final JFXPanel jfxPanel = new JFXPanel();
        jfxWindow.getContentPane().add(jfxPanel);

        Platform.runLater(() -> {
            // 设置JFX主场景，并让JFX主界面变得透明，这样不会覆盖整个Canvas.
            jfxRoot.setBackground(javafx.scene.layout.Background.EMPTY);
            Scene scene = new Scene(jfxRoot, Color.TRANSPARENT);
            jfxPanel.setScene(scene);
        });
    }

    /**
     * 创建Canvas并启动游戏。
     * @param ms
     * @param appClass
     * @param settings
     * @return 
     */
    private static Canvas createCanvas(String appClass, AppSettings settings) {
        try {
            Class<? extends LegacyApplication> clazz = (Class<? extends LegacyApplication>) Class.forName(appClass);
            jmeApp = clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, "Could not createCanvas:{0}", ex);
            return null;
        }
        jmeApp.setSettings(settings);
        jmeApp.setPauseOnLostFocus(false);
        jmeApp.createCanvas();
        jmeApp.startCanvas();

        JmeCanvasContext context = (JmeCanvasContext) jmeApp.getContext();
        Canvas canvas = context.getCanvas();
        canvas.setSize(settings.getWidth(), settings.getHeight());
        return canvas;
    }
}
