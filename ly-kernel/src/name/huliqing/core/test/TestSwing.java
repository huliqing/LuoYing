/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.test;

import com.jme3.app.LegacyApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import name.huliqing.ly.Fighter;

/**
 *
 * @author huliqing
 */
public class TestSwing {
    
    private static JmeCanvasContext context;
    private static Canvas canvas;
    private static LegacyApplication app;
    private static JFrame frame;
    private static Container canvasPanel1;
    private static JTabbedPane tabbedPane;
    
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
        tabbedPane = new JTabbedPane();

        canvasPanel1 = new JPanel();
        canvasPanel1.setLayout(new BorderLayout());
        tabbedPane.addTab("jME3 Canvas 1", canvasPanel1);

        frame.getContentPane().add(tabbedPane);
        canvasPanel1.add(canvas);
    }
    
    public static void main(String[] args){

        createCanvas(Fighter.class.getName());

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
