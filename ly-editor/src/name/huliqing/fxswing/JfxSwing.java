/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fxswing;

import com.jme3.app.Application;
import com.jme3.system.AppSettings;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

/**
 * @author huliqing
 */
public class JfxSwing {
 
    /**
     * 主界面
     */
    private JFrame mainFrame;
    
    /**
     * JWindow,包含JFX根节点JFXPanel
     */
    private JWindow jfxWindow;
    
    /**
     * JME app
     */
    private Application jmeApp;
    
    /**
     * JFX根节点，包含于JFXPanel下面。
     */
    private Pane jfxRoot;
    
    public JfxSwing() {}

    /**
     * 获取主界面
     * @return 
     */
    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * 获取JME Application
     * @return 
     */
    public Application getJmeApp() {
        return jmeApp;
    }

    public void setJmeApp(Application jmeApp) {
        this.jmeApp = jmeApp;
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
    public Pane getJfxRoot() {
        return jfxRoot;
    }

    public void setJfxRoot(Pane jfxRoot) {
        this.jfxRoot = jfxRoot;
    }

    /**
     * 获取JFX UI JWindow窗器，这个容器包含JFX根节点，层次关系是这样的: 
     * MainFrame -> JfxWindow -> JFXPanel -> JfxRoot
     * @return 
     */
    public JWindow getJfxWindow() {
        return jfxWindow;
    }

    public void setJfxWindow(JWindow jfxWindow) {
        this.jfxWindow = jfxWindow;
    }
    
    /**
     * Run task on swing thread.this method just call:
     * <pre>
     * <code>SwingUtilities.invokeLater(runnable);</code>
     * </pre>
     * @param runnable 
     */
    public void runOnSwing(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }
    
    /**
     * Run task on jfx thread. this method just call:
     * <pre>
     * <code>Platform.runLater(runnable);</code>
     * </pre>
     * @param runnable 
     */
    public void runOnJfx(Runnable runnable) {
        Platform.runLater(runnable);
    }
    
    /**
     * Run task on jme thread, this method just call:
     * <pre>
     * <code>jmeApp.enqueue(runnable);</code>
     * </pre>
     * @param runnable 
     */
    public void runOnJme(Runnable runnable) {
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
    public <V> Future<V> runOnJme(Callable<V> callable) {
        return jmeApp.enqueue(callable);
    }
    
    /**
     * 创建游戏,默认帧率60.
     * @param appClass
     * @param width
     * @param height
     * @return 
     */
    public static JfxSwing create(String appClass, int width, int height) {
        return JfxSwingUtils.create(appClass, width, height);
    }
    
    /**
     * 使用指定的settings及指定的jfxRoot根节点来创建游戏。
     * @param appClass
     * @param settings
     * @param jfxRoot
     * @return 
     */
    public static JfxSwing create(String appClass, AppSettings settings, Pane jfxRoot) {
        return JfxSwingUtils.create(appClass, settings, jfxRoot);
    }
}
