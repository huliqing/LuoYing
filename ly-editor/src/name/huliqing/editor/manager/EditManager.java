/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.manager;

import javafx.scene.layout.Pane;
import name.huliqing.editor.Editor;
import name.huliqing.editor.edit.scene.JfxSceneEdit;
import name.huliqing.editor.edit.spatial.JfxSpatialEdit;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.editor.edit.JfxEdit;

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
    
    public static void openTestFormView() {
        openSceneEditor(IdConstants.SYS_SCENE_TEST);
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
    
    public static void openSceneEditor(String sceneId) {
        Editor editor = (Editor) Jfx.getJmeApp();
        JfxSceneEdit jfxEdit = new JfxSceneEdit();
        jfxEdit.setScene(sceneId);
        editor.setJfxEdit(jfxEdit);
    }
    
}
