/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.manager;

import com.jme3.export.Savable;
import com.jme3.export.binary.BinaryImporter;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.util.Callback;
import name.huliqing.editor.Editor;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.edit.scene.JfxSceneEdit;
import name.huliqing.editor.edit.spatial.JfxSpatialEdit;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.data.SceneData;

/**
 *
 * @author huliqing 
 */
public class EditManager {
     
//    private static Pane jfxEditZone;
//    
//    public final static void registerEditZone(Pane jfxEditZone) {
//        EditManager.jfxEditZone = jfxEditZone;
//    }
//    
//    public final static Pane getJfxEditZone() {
//        return jfxEditZone;
//    }
    
    public final static void openEdit(String fileAbsolutePath) {
        doCheckSave(p -> {
            openEditInner(fileAbsolutePath);
            return null;
        });
    }
    
    private static void openEditInner(String fileAbsolutePath) {
        Editor editor = (Editor) Jfx.getJmeApp();
        String path = fileAbsolutePath;
        if (path.endsWith(".j3o")) {
            JfxSpatialEdit newEv = new JfxSpatialEdit();
            newEv.setFilePath(fileAbsolutePath);
            editor.setJfxEdit(newEv);
        } else if (path.endsWith(".ying")) {
            try {
                Savable savable = BinaryImporter.getInstance().load(new File(path));
                if (savable instanceof SceneData) {
                    JfxSceneEdit jfxSceneEdit = new JfxSceneEdit();
                    jfxSceneEdit.setSceneData((SceneData) savable, path);
                    editor.setJfxEdit(jfxSceneEdit);
                }
            } catch (IOException ex) {
                Logger.getLogger(EditManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static void doCheckSave(Callback callback) {
        Editor editor = (Editor) Jfx.getJmeApp();
        if (!editor.isModified()) {
            callback.call(null);
            return;
        }
        Jfx.runOnJfx(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(Manager.getRes(ResConstants.COMMON_QUICK_CONFIRM));
            Optional<ButtonType> result = alert.showAndWait();
            if (!result.isPresent()) {
                return;
            }
            // 按下OK或Cancel都需要退出
            ButtonType bt = result.get();
            if (bt == ButtonType.YES || bt == ButtonType.APPLY || bt == ButtonType.OK) {
                editor.saveAll();
            }
            callback.call(null);
        });
    }
    
    // remove20170210
//    public static void openSpatialEditor(String fileAbsolutePath) {
//        Editor editor = (Editor) Jfx.getJmeApp();
//        JfxEdit ev = editor.getJfxEdit();
//        if (ev instanceof JfxSpatialEdit) {
//            ((JfxSpatialEdit) ev).setFilePath(fileAbsolutePath);
//        } else {
//            JfxSpatialEdit newEv = new JfxSpatialEdit();
//            newEv.setFilePath(fileAbsolutePath);
//            editor.setJfxEdit(newEv);
//        }
//    }
    
    // remove20170210
//    public static void openTestFormView() {
//        openSceneEditor(IdConstants.SYS_SCENE_TEST);
//    }
//    public static void openSceneEditor(String sceneId) {
//        Editor editor = (Editor) Jfx.getJmeApp();
//        JfxSceneEdit jfxEdit = new JfxSceneEdit();
//        jfxEdit.setScene(sceneId);
//        editor.setJfxEdit(jfxEdit);
//    }
    
}
