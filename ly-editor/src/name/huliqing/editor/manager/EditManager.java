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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.Pane;
import name.huliqing.editor.Editor;
import name.huliqing.editor.edit.scene.JfxSceneEdit;
import name.huliqing.editor.edit.spatial.JfxSpatialEdit;
import name.huliqing.fxswing.Jfx;
import name.huliqing.editor.edit.JfxEdit;
import name.huliqing.luoying.data.SceneData;

/**
 *
 * @author huliqing 
 */
public class EditManager {
     
    private static Pane jfxEditZone;
    
    public final static void registerEditZone(Pane jfxEditZone) {
        EditManager.jfxEditZone = jfxEditZone;
    }
    
    public final static Pane getJfxEditZone() {
        return jfxEditZone;
    }
    
    public final static void openEdit(String fileAbsolutePath) {
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
    
    public static void openSpatialEditor(String fileAbsolutePath) {
        Editor editor = (Editor) Jfx.getJmeApp();
        JfxEdit ev = editor.getJfxEdit();
        if (ev instanceof JfxSpatialEdit) {
            ((JfxSpatialEdit) ev).setFilePath(fileAbsolutePath);
        } else {
            JfxSpatialEdit newEv = new JfxSpatialEdit();
            newEv.setFilePath(fileAbsolutePath);
            editor.setJfxEdit(newEv);
        }
    }

    
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
