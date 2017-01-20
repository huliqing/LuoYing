/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.manager;

import javafx.scene.layout.Pane;
import name.huliqing.editor.Editor;
import name.huliqing.editor.edit.JfxAbstractEdit;
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
//        Editor editor = (Editor) Jfx.getJmeApp();
//        JfxSceneEditView ev = new JfxSceneEditView(jfxEditZone);
//        editor.setFormView(ev);
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
        JfxSceneEdit newEv = new JfxSceneEdit();
        newEv.setScene(sceneId);
        editor.setJfxEdit(newEv);
    }

//    public static void notifyDragStarted() {
//        Editor editor = (Editor) Jfx.getJmeApp();
//        JfxAbstractEdit sev = (JfxAbstractEdit) editor.getJfxEdit();
//        sev.jfxOnDragStarted();
//    }
//    
//    public static void notifyDragEnded() {
//        Editor editor = (Editor) Jfx.getJmeApp();
//        JfxAbstractEdit sev = (JfxAbstractEdit) editor.getJfxEdit();
//        sev.jfxOnDragEnded();
//    }
    
    
}
