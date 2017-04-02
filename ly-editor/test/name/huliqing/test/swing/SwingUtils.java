/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
import name.huliqing.test.fxjme.TestEditor;

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
        String appClass = TestEditor.class.getName();
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
