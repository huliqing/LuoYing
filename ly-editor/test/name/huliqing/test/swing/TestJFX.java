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

import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import name.huliqing.editor.ui.MenuForm;

/**
 *
 * @author huliqing
 */
public class TestJFX extends Application {

    @Override
    public void start(Stage s) throws Exception {
        VBox root = new VBox();
        root.setBackground(Background.EMPTY);
        Scene scene = new Scene(root, 1024, 768, Color.TRANSPARENT);
        
        ObservableList<Node>  rootNode = root.getChildren();
        MenuForm menuView = new MenuForm();
        rootNode.add(menuView);
        
        DoubleBinding db = scene.heightProperty().subtract(menuView.heightProperty()).subtract(-0.1);
        SplitPane sp = new SplitPane();
        sp.setBackground(Background.EMPTY);
        sp.minHeightProperty().bind(db);
        sp.maxHeightProperty().bind(db);
//        sp.setDividerPositions(0, 0.3);
        rootNode.add(sp);

        VBox left = new VBox();
        left.setStyle("-fx-background-color:#c0c0c0;");
        left.getChildren().add(new Label("this is Left"));
        
        SplitPane rightPane = new SplitPane();
        rightPane.setBackground(Background.EMPTY);
        rightPane.setOrientation(Orientation.VERTICAL);
        sp.getItems().addAll(left, rightPane);
        
        VBox editPane = new VBox();
        editPane.setBackground(Background.EMPTY);
        
        TextArea consolePane = new TextArea();
        consolePane.setPrefRowCount(10);
        rightPane.getItems().addAll(editPane, consolePane);
        
        s.setScene(scene);
        s.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
