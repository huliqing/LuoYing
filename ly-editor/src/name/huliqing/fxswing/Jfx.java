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
import com.sun.javafx.stage.EmbeddedWindow;
import java.awt.Canvas;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Window;
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
    private static JWindow jWindow;
    
    /**
     * JME app
     */
    private static Application jmeApp;
    
    private static JFXPanel jfxPanel;
    
    /**
     * JFX根节点，包含于JFXPanel下面。
     */
    private static Pane jfxRoot;
    
    /**
     * 主编辑3d Canvas界面，位于mainFrame中
     */
    private static Canvas jmeCanvas;
    
    private static CanvasJfxBindingController bindingController;
    
    /**
     * 获取主界面
     * @return 
     */
    public static JFrame getMainFrame() {
        return mainFrame;
    }
    
    /**
     * 获取JWindow窗器，这个容器包含JFX根节点，层次关系是这样的: 
     * MainFrame -> JWindow -> JFXPanel -> JfxRoot
     * @return 
     */
    public static JWindow getJWindow() {
        return jWindow;
    }

    /**
     * 获取JME Application
     * @return 
     */
    public static Application getJmeApp() {
        return jmeApp;
    }
    
    /**
     * 获取canvas组件
     * @return 
     */
    public static Canvas getJmeCanvas() {
        return jmeCanvas;
    }
    
    public static JFXPanel getJfxPanel() {
        return jfxPanel;
    }
    
    public static EmbeddedWindow getJfxWindow() {
        return (EmbeddedWindow) jfxPanel.getScene().getWindow();
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
     * 获取绑定控器
     * @return 
     */
    public static CanvasJfxBindingController getBindingController() {
        return bindingController;
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
        mainFrame = createMainFrame(appClass, settings);
        mainFrame.setTitle("落梅");
        
        jmeApp = createAppCanvas(appClass, settings);
        
        jmeCanvas = ((JmeCanvasContext) jmeApp.getContext()).getCanvas();
        jmeCanvas.setSize(settings.getWidth(), settings.getHeight());
        
        // 创建Jfx界面，这个界面会包含JFX组件，作为UI界面，该界面始终覆盖在mainFrame上面
        jWindow = createJWindow(mainFrame, jfxRoot);
        jWindow.setName("JFXWindow");
        jWindow.setSize(mainFrame.getSize());
        
        mainFrame.add(jmeCanvas);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                // 这里应该不需要放在jme app线程
                jmeApp.stop();
            }
        });
        mainFrame.setVisible(true);
        
        // 绑定Jfx窗口，使Jfx窗口始终与mainFrame的位置和大小一致。
        bindingController = new CanvasJfxBindingController();
        bindingController.bind(jWindow, mainFrame);
    }

    /**
     * 创建主界面面板。
     * @param ms
     * @param appClass
     * @param settings
     * @return 
     */
    private static JFrame createMainFrame(String appClass, AppSettings settings) {
        JFrame jframe = new JFrame();
        jframe.setSize(settings.getWidth(), settings.getHeight());
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jframe.setLocationRelativeTo(null);
        return jframe;
    }
    
    /**
     * 创建JWindow,该Window用于作为Jfx组件的容器，这个window始终覆盖在frame上。
     * @param ms
     * @param mainFrame
     * @param jfxRoot
     * @return 
     */
    private static JWindow createJWindow(JFrame parent, final Pane jfxRoot) {
        JWindow jwin = new JWindow(parent);
        jwin.setLocationRelativeTo(null);
        jwin.setVisible(true);
        
        jfxPanel = new JFXPanel();
        jwin.getContentPane().add(jfxPanel);

        Platform.runLater(() -> {
            // 设置JFX主场景，并让JFX主界面变得透明，这样不会覆盖整个Canvas.
            jfxRoot.setBackground(Background.EMPTY);
            jfxPanel.setScene(new Scene(jfxRoot, Color.TRANSPARENT));
        });
        return jwin;
    }

    /**
     * 创建Canvas并启动游戏。
     * @param ms
     * @param appClass
     * @param settings
     * @return 
     */
    private static Application createAppCanvas(String appClass, AppSettings settings) {
        try {
            Class<? extends LegacyApplication> clazz = (Class<? extends LegacyApplication>) Class.forName(appClass);
            LegacyApplication app = clazz.newInstance();
            app.setSettings(settings);
            app.setPauseOnLostFocus(false);
            app.createCanvas();
            app.startCanvas();
            return app;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, "Could not createCanvas:{0}", ex);
            return null;
        }
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
     * 强制刷新JFx UI
     */
    public static void jfxForceUpdate() {
        // 注：这里使用一个特殊的方式来刷新一下JFX UI, 因为在JFX嵌入Swing时，
        // 一些情况下，如在程序中动态添加JFX UI时发现无法实时刷新界面。即使调用requestLayout都没有用。
        // 只有在手动调整一下界面大小的时候才会刷新，这可能是一个BUG。
        // 这里使用一种比较特殊的方式处理：稍微改变一下swing组件的高度大小，然后再改回来。
        int width = jWindow.getWidth();
        int height = jWindow.getHeight();
        Jfx.runOnSwing(() -> {
            jWindow.setSize(width, height + 1); 
            Jfx.runOnSwing(() -> {
                jWindow.setSize(width, height);
            });
        });
    }
    
    public static void jfxCanvasBind(Region target) {
        bindingController.jfxCanvasBind(target);
    }
    
    /**
     * 把焦点定位到指定的JFX节点上，使用这个方法是因为如果当前焦点是在swing组件上，
     * 可能无法直接在JFX线程中请求将焦点定位到JFX节点上。
     * @param win 
     */
    public static void requestFocus(Window win) {
        if (Platform.isFxApplicationThread()) {
            jfxPanel.requestFocus();
            win.requestFocus();
        } else {
            Jfx.runOnJfx(() -> {
                jfxPanel.requestFocus();
                win.requestFocus();
            });
        }
    }
    
    /**
     * 把焦点定位到指定的JFX节点上，使用这个方法是因为如果当前焦点是在swing组件上，
     * 可能无法直接在JFX线程中请求将焦点定位到JFX节点上。
     * @param node 
     */
    public static void requestFocus(Node node) {
         if (Platform.isFxApplicationThread()) {
            jfxPanel.requestFocus();
            node.requestFocus();
        } else {
            Jfx.runOnJfx(() -> {
                jfxPanel.requestFocus();
                node.requestFocus();
            });
        }
    }
    
    /**
     * 将焦点定位到编辑器中（jmeCanvas）
     */
    public static void requestFocusCanvas() {
        runOnSwing(() -> {
            jmeCanvas.requestFocus();
        });
    }
}
