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
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.ObjectData;

/**
 *
 * @author huliqing 
 */
public class EditManager {
    private static final Logger LOG = Logger.getLogger(EditManager.class.getName());
     
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
            try {
                openEditInner(fileAbsolutePath);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Could not open editor, file={0}", fileAbsolutePath);
            }
            return null;
        });
    }
    
    private static void openEditInner(String fileAbsolutePath) {
        String fullPath = fileAbsolutePath;
        Editor editor = (Editor) Jfx.getJmeApp();
        // open j3o
        if (fullPath.endsWith(".j3o")) {
            JfxSpatialEdit newEv = new JfxSpatialEdit();
            newEv.setFilePath(fileAbsolutePath);
            editor.setJfxEdit(newEv);
            return;
        } 
        
        // open lyo
        if (fullPath.endsWith(".lyo")) {
            String pathInAssets = toAssetsPath(fileAbsolutePath);
            ObjectData od = Loader.loadDataByPath(pathInAssets);
            if (od instanceof SceneData) {
                JfxSceneEdit jfxSceneEdit = new JfxSceneEdit();
                jfxSceneEdit.setSceneData((SceneData) od, fullPath);
                editor.setJfxEdit(jfxSceneEdit);
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
    
    private static String toAssetsPath(String absolutePath) {
        String assetPath = Manager.getConfigManager().getMainAssetDir();
        String fileInAssets = absolutePath.replace(assetPath, "").replace("\\", "/");
        return fileInAssets;
    }
}
