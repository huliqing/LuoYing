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

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import name.huliqing.fxswing.Jfx;

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
