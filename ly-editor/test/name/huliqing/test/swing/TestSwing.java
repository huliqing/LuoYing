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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
public class TestSwing {
    
    private static JmeCanvasContext context;
    private static Canvas canvas;
    private static LegacyApplication app;
    private static JFrame frame;
    private static Container rootPanel;
    
    public static void createCanvas(String appClass){
        AppSettings settings = new AppSettings(true);
        settings.setWidth(640);
        settings.setHeight(480);

        try{
            Class<? extends LegacyApplication> clazz = (Class<? extends LegacyApplication>) Class.forName(appClass);
            app = clazz.newInstance();
        }catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }catch (InstantiationException ex){
            ex.printStackTrace();
        }catch (IllegalAccessException ex){
            ex.printStackTrace();
        }

        app.setPauseOnLostFocus(false);
        app.setSettings(settings);
        app.createCanvas();
        app.startCanvas();

        context = (JmeCanvasContext) app.getContext();
        canvas = context.getCanvas();
        canvas.setSize(settings.getWidth(), settings.getHeight());
    }
    
    private static void createFrame(){
        frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosed(WindowEvent e) {
                app.stop();
            }
        });

        createTabs();
    }
    
    private static void createTabs(){
        rootPanel = new JPanel();
//        canvasPanel.setSize(640, 480);
//        canvasPanel.add(canvas);
//        frame.setSize(640, 480);

        
        frame.getContentPane().add(rootPanel);
        OverlayLayout layout = new OverlayLayout(rootPanel);
//        BorderLayout layout = new BorderLayout(1, 1);
        
        JFXPanel jfxPanel = new JFXPanel();
        jfxPanel.setBounds(0, 0, 100, 100);
        jfxPanel.setSize(100, 100);
        
        rootPanel.setLayout(layout);
//        rootPanel.add(jfxPanel);
        rootPanel.add(new JButton("fsfdsfj;dsj;fjds;jfasf;afja;fd"));
        rootPanel.add(canvas);
        
        Platform.runLater(() -> {
            Button btn = new Button("Hello JFX!");
            StackPane root = new StackPane();
            root.setBackground(Background.EMPTY);
            root.getChildren().add(btn);
//            root.setStyle("-fx-background-color: transparent;");
//            root.setStyle("-fx-background-color:#0000FF;");
            
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        });
    }
    
    public static void main(String[] args){

        createCanvas(TestEditor.class.getName());

        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                createFrame();
                
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
        
        
        
    }
}
