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
import name.huliqing.editor.edit.spatial.JfxSpatialEditView;
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
    
    public static void openTestFormView() {
//        Editor editor = (Editor) Jfx.getJmeApp();
//        JfxSceneEditView ev = new JfxSceneEditView(jfxEditZone);
//        editor.setFormView(ev);
        openSceneEditor(IdConstants.SYS_SCENE_TEST);
    }
    
    public static void openSpatialEditor(String fileAbsolutePath) {
        Editor editor = (Editor) Jfx.getJmeApp();
        JfxEdit ev = editor.getFormView();
        if (ev instanceof JfxSpatialEditView) {
            ((JfxSpatialEditView) ev).setFilePath(fileAbsolutePath);
        } else {
            JfxSpatialEditView newEv = new JfxSpatialEditView(jfxEditZone);
            newEv.setFilePath(fileAbsolutePath);
            editor.setFormView(newEv);
        }
    }
    
    public static void openSceneEditor(String sceneId) {
        Editor editor = (Editor) Jfx.getJmeApp();
        JfxSceneEdit newEv = new JfxSceneEdit(jfxEditZone);
        newEv.setScene(sceneId);
        editor.setFormView(newEv);
    }

    public static void notifyDragStarted() {
        Editor editor = (Editor) Jfx.getJmeApp();
        JfxAbstractEdit sev = (JfxAbstractEdit) editor.getFormView();
        sev.jfxOnDragStarted();
    }
    
    public static void notifyDragEnded() {
        Editor editor = (Editor) Jfx.getJmeApp();
        JfxAbstractEdit sev = (JfxAbstractEdit) editor.getFormView();
        sev.jfxOnDragEnded();
    }
    
    
}
