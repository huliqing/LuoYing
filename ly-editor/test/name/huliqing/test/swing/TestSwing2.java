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
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import name.huliqing.test.fxjme.TestEditor;

/**
 *
 * @author huliqing
 */
public class TestSwing2 {

    private JFrame frame;
    private Container rootPanel;
    private LegacyApplication app;

    public static void main(String[] args) {

        new TestSwing2().start();

    }

    private  void createFrame() {
        frame = new JFrame("Test");
        frame.setSize(640, 480);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (app != null) {
                    app.stop();
                }
            }
        });

        createTabs();
    }

    private void createTabs() {
        rootPanel = new JPanel();
        rootPanel.setSize(640,480);
        rootPanel.setBackground(java.awt.Color.GREEN);
        OverlayLayout layout = new OverlayLayout(rootPanel);
        rootPanel.setLayout(layout);
  
        frame.getContentPane().add(rootPanel);
        
        Canvas canvas = createCanvas();
        canvas.setSize(200, 100);
        JPanel canvasPanel = new JPanel();
        canvasPanel.setSize(200, 100);
        canvasPanel.add(canvas);
        canvasPanel.setOpaque(false);
        
        
//        JFXPanel jfxPanel = new JFXPanel();
//        jfxPanel.setSize(100, 200);

        JPanel awtJfxPanel = new JPanel();
        awtJfxPanel.setOpaque(false);
        awtJfxPanel.setSize(100, 200);
//        awtJfxPanel.setBackground(java.awt.Color.red);
        
//        awtJfxPanel.add(jfxPanel);
        awtJfxPanel.add(new JButton("This button is on awtJfxPanel"));

        rootPanel.add(awtJfxPanel);
        rootPanel.add(canvasPanel);


//        Platform.runLater(() -> {
//            Button btn = new Button("Hello JFX!");
//            StackPane root = new StackPane();
//            root.setBackground(Background.EMPTY);
//            root.getChildren().add(btn);
////            root.setStyle("-fx-background-color: transparent;");
////            root.setStyle("-fx-background-color:#0000FF;");
//
//            Scene scene = new Scene(root);
//            scene.setFill(Color.TRANSPARENT);
//
//            jfxPanel.setScene(scene);
//
//        });
    }

    public void start() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createFrame();

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public Canvas createCanvas() {
        String appClass = TestEditor.class.getName();
        AppSettings settings = new AppSettings(true);
        settings.setWidth(640);
        settings.setHeight(480);
        settings.setFrameRate(30);

        try {
            Class<? extends LegacyApplication> clazz = (Class<? extends LegacyApplication>) Class.forName(appClass);
            app = clazz.newInstance();
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
}
