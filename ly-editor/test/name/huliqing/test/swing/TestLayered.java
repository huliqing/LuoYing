/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.test.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;

/**
 *
 * @author huliqing
 */
public class TestLayered {

    private int width = 640;
    private int height = 480;
    
    private JFrame jframe;
    private JWindow jfxWindow;

    private void createFrame() {
        jframe = new JFrame("Test");
        jframe.setName("MainWindow");
        jframe.setSize(width, height);
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jframe.setUndecorated(false);
//        jframe.setBackground(new Color(0,0,0,0f));
//        jframe.add(SwingUtils.createCanvas());
//        jfxWindow = createJfxWindow(jframe);

        OverlayLayout layout = new OverlayLayout(jframe);
        
        
        
        JPanel jp1 = new TranslucentPane();
        jp1.setSize(width, (int) (height * 0.5));
        jp1.setBackground(new Color(0,0,0,0));
        jp1.add(new JButton("1111"));
//        jp1.setOpaque(false);
        
        JPanel jp2 = new JPanel();
        jp2.setSize(width, height);
//        jp2.setBackground(new Color(0,0,0,0.1f));
        jp2.add(new JButton("22222222222222"));
        jp2.add(SwingUtils.createCanvas());
        
        
        jframe.add(jp1);
        jframe.add(jp2);

    }
    
//    private Component createJfxWindow() {
//        
//        
//        JFXPanel jfxPanel = new JFXPanel();
//        jfxPanel.setSize(width, height);
//        window.getContentPane().add(jfxPanel);
//        
//        Platform.runLater(() -> {
//            Button btn = new Button("Hello JFX!");
//            StackPane root = new StackPane();
//            root.setBackground(javafx.scene.layout.Background.EMPTY);
//            root.getChildren().add(btn);
//            Scene scene = new Scene(root);
//            scene.setFill(Color.TRANSPARENT);
//            jfxPanel.setScene(scene);
//        });
//        
//        SwingUtilities.invokeLater(() -> {
//            window.setLocation(500, 100);
//            window.setAlwaysOnTop(true);
//        });
//        SwingUtils.setDragable(window);
//        window.setVisible(true);
//        return window;
//    }
    
    private void start() {
        SwingUtilities.invokeLater(() -> {
            createFrame();
            jframe.setLocationRelativeTo(null);
            jframe.setVisible(true);
        });
    }
    
    public static void main(String[] args) {
        new TestLayered().start();
    }
    
    public class TranslucentPane extends JPanel {

        public TranslucentPane() {
            setOpaque(false);
            setBackground(new java.awt.Color(0,0,0,0));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); 

            Graphics2D g2d = (Graphics2D) g.create();
//            g2d.setComposite(AlphaComposite.SrcOver.derive(0.85f));
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.01f));
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

    }
}
