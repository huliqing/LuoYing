/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.test.swing;

import com.jme3.app.LegacyApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import name.huliqing.test.fxjme.TestEditorApp;

/**
 *
 * @author huliqing
 */
public class SwingUtils {
    
    private static Point mouseDownCompCoords;
    
    public static void setDragable(Component com) {
            com.addMouseListener(new MouseListener(){
            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords = null;
            }
            @Override
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords = e.getPoint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });

        com.addMouseMotionListener(new MouseMotionListener(){
            @Override
            public void mouseMoved(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                com.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
            }
        });
    }
    
    public static Canvas createCanvas() {
        String appClass = TestEditorApp.class.getName();
        AppSettings settings = new AppSettings(true);
        settings.setWidth(640);
        settings.setHeight(480);
        settings.setFrameRate(60);

        try {
            Class<? extends LegacyApplication> clazz = (Class<? extends LegacyApplication>) Class.forName(appClass);
            LegacyApplication app = clazz.newInstance();
            app.setPauseOnLostFocus(false);
            app.setSettings(settings);
            app.createCanvas();
            app.startCanvas();

            JmeCanvasContext context = (JmeCanvasContext) app.getContext();
            Canvas canvas = context.getCanvas();
            canvas.setSize(settings.getWidth(), settings.getHeight());

            return canvas;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    public static WindowListener createWindowListener() {
        return new WindowListener() {
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

            @Override
            public void windowIconified(WindowEvent e) {
                System.out.println(e.getWindow().getName() + "=windowIconified");
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                System.out.println(e.getWindow().getName() + "=windowDeiconified");
            }

            @Override
            public void windowActivated(WindowEvent e) {
                System.out.println(e.getWindow().getName() + "=windowActivated");
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                System.out.println(e.getWindow().getName() + "=windowDeactivated");
            }
        };
    }
}
