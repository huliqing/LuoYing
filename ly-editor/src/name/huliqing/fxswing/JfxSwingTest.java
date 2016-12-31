/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fxswing;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 *
 * @author huliqing
 */
public class JfxSwingTest {
    
    public static void main(String[] args) {
        
        Jfx.create(JmeAppTest.class.getName(), 640, 480);
        Jfx.getMainFrame().setLocationRelativeTo(null);
        Jfx.getMainFrame().setVisible(true);
        
        Jfx.runOnJfx(()-> {
            Button btn = new Button("Close app!");
            btn.setOnAction((e) -> {
                System.exit(0);
            });
            TextField text = new TextField("This is a textfield");
            Jfx.getJfxRoot().getChildren().addAll(btn, text);
        });
        
        Jfx.runOnJfx(() -> {
            Button btn = new Button("Click点击，生成新的按钮");
            btn.setOnAction((e) -> {
                Button newButton = new Button("I'm new button");
                Jfx.getJfxRoot().getChildren().add(newButton);
            });
            Jfx.getJfxRoot().getChildren().add(btn);
        });

        
    }
    
    
}
