/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.test.swing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author huliqing
 */
public class TestJFX extends Application {

    @Override
    public void start(Stage s) throws Exception {
        
        FlowPane root = new FlowPane();
        root.getChildren().add(new Button("Hello JFX"));
//        root.setStyle("-fx-background-color: transparent;");  
        root.setBackground(Background.EMPTY);
        
        Scene scene = new Scene(root, 200, 200);
        scene.setFill(Color.TRANSPARENT);
        
//        Stage s = new Stage(); 
        s.setScene(scene);
        
        s.initStyle(StageStyle.TRANSPARENT);
        
        s.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
