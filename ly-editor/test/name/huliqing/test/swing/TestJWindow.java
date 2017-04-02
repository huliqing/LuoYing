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

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

/**
 *
 * @author huliqing
 */
public class TestJWindow {

    private int width = 640;
    private int height = 480;
    
    private JFrame jframe;
    private JWindow jfxWindow;

    private void createFrame() {
        jframe = new JFrame("Test");
        jframe.setName("MainWindow");
        jframe.setSize(width, height);
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        jframe.setUndecorated(true);
        jframe.add(SwingUtils.createCanvas());
        jfxWindow = createJfxWindow(jframe);
        
        jframe.addWindowListener(new WindowAdapter() {
            
            /**
             * 当Window重新激话时必须把JfxWindow重新设置为透明的,这样不会挡住Canvas
             * @param e 
             */
            @Override
            public void windowActivated(WindowEvent e) {
                super.windowActivated(e);
                jfxWindow.setBackground(new java.awt.Color(0f, 0f, 0f, 0f));
                jfxWindow.setLocation(e.getWindow().getLocation());
                jfxWindow.setSize(e.getWindow().getSize());
                System.out.println("insets=" + jframe.getInsets());
            }

            /**
             * 最小化时需要把JWindow的背景重新设置为White，否则这个JWindow将会消息不见，这是一个BUG。
             * @param e 
             */
            @Override
            public void windowIconified(WindowEvent e) {
                super.windowIconified(e); 
                jfxWindow.setBackground(java.awt.Color.WHITE);
            }
        });
        
        jframe.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e); 
                jfxWindow.setLocation(e.getComponent().getLocation());
            }

            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e); 
                jfxWindow.setSize(e.getComponent().getSize());
                
                System.out.println("insets=" + jframe.getInsets());
            }
            
        });
        
        jframe.addWindowListener(SwingUtils.createWindowListener());
        
    }
    
    private JWindow createJfxWindow(JFrame parent) {
        final JWindow window = new JWindow(parent);
        window.setName("JFXWindow");
        window.setSize(parent.getSize());
        window.setVisible(true);
        
        JFXPanel jfxPanel = new JFXPanel();
        window.getContentPane().add(jfxPanel);
        
        Platform.runLater(() -> {
            Button btn = new Button("Hello JFX!");
            btn.setOnAction((e) -> {
                System.out.println("Hello this is JFX!");
                SwingUtilities.invokeLater(() -> {
                    jframe.setVisible(false);
                });
            });
            TextField text = new TextField("");
            
            VBox root = new VBox();
            root.setBackground(javafx.scene.layout.Background.EMPTY);
            root.getChildren().add(btn);
            root.getChildren().add(text);
            
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            jfxPanel.setScene(scene);
        });
        
//        SwingUtils.setDragable(window);
        return window;
    }
    
    private void start() {
        SwingUtilities.invokeLater(() -> {
            createFrame();
            jframe.setLocationRelativeTo(null);
            jframe.setVisible(true);
        });
    }
    
    public static void main(String[] args) {
        new TestJWindow().start();
    }
    
    
}
