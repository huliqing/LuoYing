/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import name.huliqing.editor.Editor;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class Quit {
    
    public final static void doQuit() {
        Editor editor = (Editor) Jfx.getJmeApp();
        if (!editor.isModified()) {
            Jfx.getMainFrame().dispose();
            return;
        }
        Jfx.runOnJfx(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setHeaderText(Manager.getRes(ResConstants.COMMON_QUICK));
            alert.setContentText(Manager.getRes(ResConstants.COMMON_QUICK_CONFIRM));
            Optional<ButtonType> result = alert.showAndWait();
            if (!result.isPresent()) {
                return; // 不按下任何按键,则不退出
            }
            // 按下OK或Cancel都需要退出
            ButtonType bt = result.get();
            if (bt == ButtonType.YES || bt == ButtonType.APPLY || bt == ButtonType.OK) {
                editor.saveAll();
            }
            Jfx.runOnSwing(() -> {Jfx.getMainFrame().dispose();});
        });
    }
}
