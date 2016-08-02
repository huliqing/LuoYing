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
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

/**
 * 该帧听器用于将JWindow绑定到主界面(JFrame)中，使JWindow的位置和大小始终和主界面一样,并让JWindow变得透明，
 * 让JWindow始终覆盖在JFrame上
 * @author huliqing
 */
public final class JfxBindingController implements WindowListener, WindowStateListener, WindowFocusListener, ComponentListener {

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
        System.out.println(e.getWindow().getName() + "=windowOpened");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println(e.getWindow().getName() + "=windowClosing");
    }

    @Override
    public void windowClosed(WindowEvent e) {
        System.out.println(e.getWindow().getName() + "=windowClosed");
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
        System.out.println(e.getWindow().getName() + "=windowIconified");
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        System.out.println(e.getWindow().getName() + "=windowDeiconified");
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
        System.out.println(e.getWindow().getName() + "=windowActivated");
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        System.out.println(e.getWindow().getName() + "=windowDeactivated");
    }

    @Override
    public void windowStateChanged(WindowEvent e) {
        System.out.println(e.getWindow().getName() + "=windowStateChanged:" + e.getOldState() + "->" + e.getNewState());
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
        System.out.println(e.getWindow().getName() + "=windowGainedFocus");
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        System.out.println(e.getWindow().getName() + "=windowLostFocus");
    }

    @Override
    public void componentResized(ComponentEvent e) {
        updateSize(win, frame);
        updateLocation(win, frame);
        // 这里特别让fxWin更新一下，因为一些情况下当主窗口调整大小的时候fxWin中JFXPanel的JFX组件的宽度和高度的更新稍
        // 微有一些延迟。
        SwingUtilities.updateComponentTreeUI(win);
        
        System.out.println(e.getComponent().getName() + "=componentResized");
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        updateLocation(win, frame);
        
        System.out.println(e.getComponent().getName() + "=componentMoved");
    }

    @Override
    public void componentShown(ComponentEvent e) {
        System.out.println(e.getComponent().getName() + "=componentShown");
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        System.out.println(e.getComponent().getName() + "=componentHidden");
    }

    private void updateSize(JWindow win, Container parent) {
        Insets insets =parent.getInsets();
        win.setSize(parent.getSize().width - insets.left - insets.right, parent.getSize().height - insets.top - insets.bottom);
    }
    
    private void updateLocation(JWindow win, Container parent) {
        Insets insets =parent.getInsets();
        win.setLocation(parent.getLocation().x + insets.left, parent.getLocation().y + insets.top);
    }
}
