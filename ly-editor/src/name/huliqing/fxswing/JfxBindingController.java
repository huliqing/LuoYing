/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fxswing;

import java.awt.Color;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

/**
 * 该帧听器用于将JWindow绑定到主界面(JFrame)中，使JWindow的位置和大小始终和主界面一样,并让JWindow变得透明，
 * 让JWindow始终覆盖在JFrame上
 * @author huliqing
 */
public class JfxBindingController implements WindowListener, WindowStateListener, WindowFocusListener, ComponentListener {
    private static final Logger LOG = Logger.getLogger(JfxBindingController.class.getName());

    private JWindow win;
    private JFrame frame;
    
    // 这是一个透明颜色，用于在frame激活时，让win变得透明，并覆盖在frame上。
    private final Color TRANSPARENT = new Color(0, 0, 0, 0);

    public JfxBindingController() {}
    
    /**
     * 绑定JWindow和JFrame,使JWindow的位置和大小始终和主界面一样,让JWindow变得透明并始终覆盖在JFrame上面。
     * @param win
     * @param frame 
     */
    public void bind(JWindow win, JFrame frame) {
        // 如果已经绑定过则移除旧的绑定
        if (this.frame != null) {
            this.frame.removeWindowListener(this);
            this.frame.removeComponentListener(this);
        }

        this.win = win;
        this.frame = frame;
        this.frame.addWindowListener(this);
        this.frame.addComponentListener(this);
    }

    @Override
    public void windowOpened(WindowEvent e) {
//        LOG.info(e.getWindow().getName() + "=windowOpened");
    }

    @Override
    public void windowClosing(WindowEvent e) {
//        LOG.info(e.getWindow().getName() + "=windowClosing");
    }

    @Override
    public void windowClosed(WindowEvent e) {
//        LOG.info(e.getWindow().getName() + "=windowClosed");
    }

    /**
     * 最小化时需要把JWindow的背景设置为White，在重新恢复时再把窗口背景重新设置为透明
     * (fxWin.setBackground(new java.awt.Color(0, 0, 0, 0)), 否则这个JWindow在最小化后重新显示时会消失不见，
     * 这是一个BUG。
     * @param e
     */
    @Override
    public void windowIconified(WindowEvent e) {
        win.setBackground(java.awt.Color.WHITE);
//        LOG.log(Level.INFO, "{0}=windowIconified", e.getWindow().getName());
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
//        LOG.log(Level.INFO, "{0}=windowDeiconified", e.getWindow().getName());
    }

    /**
     * 当Window重新激话时必须把JfxWindow重新设置为透明的,这样不会挡住主界面(MainFrame),但是要注意：这里有一个
     * JWindow的BUG，当设置了JWindow的背景后（setBackground），在最小化后再打开时，这个JWindow会莫明奇妙消
     * 失。所以必须在最小化窗口的时候把这个JWindow的背景设置回White(see windowIconified(e)),在重新激活时再设置为透明。
     * 打
     * @param e
     */
    @Override
    public void windowActivated(WindowEvent e) {
        win.setBackground(TRANSPARENT);
        updateLocation(win, e.getWindow());
        updateSize(win, e.getWindow());
//        LOG.log(Level.INFO, "{0}=windowActivated", e.getWindow().getName());
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
//        LOG.log(Level.INFO, "{0}=windowDeactivated", e.getWindow().getName());
    }

    @Override
    public void windowStateChanged(WindowEvent e) {
//        LOG.log(Level.INFO, "{0}=windowStateChanged:{1}->{2}", new Object[]{e.getWindow().getName(), e.getOldState(), e.getNewState()});
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
//        LOG.log(Level.INFO, "{0}=windowGainedFocus", e.getWindow().getName());
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
//        LOG.log(Level.INFO, "{0}=windowLostFocus", e.getWindow().getName());
    }

    @Override
    public void componentResized(ComponentEvent e) {
        updateSize(win, frame);
        updateLocation(win, frame);
        // 这里特别让fxWin更新一下，因为一些情况下当主窗口调整大小的时候fxWin中JFXPanel的JFX组件的宽度和高度的更新稍
        // 微有一些延迟。
        SwingUtilities.updateComponentTreeUI(win);
//        LOG.info(e.getComponent().getName() + "=componentResized");
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        updateLocation(win, frame);
//        LOG.info(e.getComponent().getName() + "=componentMoved");
    }

    @Override
    public void componentShown(ComponentEvent e) {
//        LOG.info(e.getComponent().getName() + "=componentShown");
    }

    @Override
    public void componentHidden(ComponentEvent e) {
//        LOG.info(e.getComponent().getName() + "=componentHidden");
    }

    protected void updateSize(JWindow win, Container parent) {
        Insets insets =parent.getInsets();
        win.setSize(parent.getSize().width - insets.left - insets.right, parent.getSize().height - insets.top - insets.bottom);
    }
    
    protected void updateLocation(JWindow win, Container parent) {
        Insets insets =parent.getInsets();
        win.setLocation(parent.getLocation().x + insets.left, parent.getLocation().y + insets.top);
    }
}
