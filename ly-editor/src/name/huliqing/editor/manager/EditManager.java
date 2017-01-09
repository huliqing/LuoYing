/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.manager;

import javafx.scene.layout.Pane;
import name.huliqing.editor.Editor;
import name.huliqing.editor.edit.EditView;
import name.huliqing.editor.edit.JfxEditView;
import name.huliqing.editor.edit.scene.JfxSceneEditView;
import name.huliqing.editor.edit.spatial.JfxSpatialEditView;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.constants.IdConstants;

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
        EditView ev = editor.getFormView();
        if (ev instanceof JfxSpatialEditView) {
            ((JfxSpatialEditView) ev).getEditForm().setFilePath(fileAbsolutePath);
        } else {
            JfxSpatialEditView newEv = new JfxSpatialEditView(jfxEditZone);
            newEv.getEditForm().setFilePath(fileAbsolutePath);
            editor.setFormView(newEv);
        }
    }
    
    public static void openSceneEditor(String sceneId) {
        Editor editor = (Editor) Jfx.getJmeApp();
        JfxSceneEditView newEv = new JfxSceneEditView(jfxEditZone);
        newEv.getEditForm().setScene(sceneId);
        editor.setFormView(newEv);
    }

    public static void notifyDragStarted() {
        Editor editor = (Editor) Jfx.getJmeApp();
        JfxEditView sev = (JfxEditView) editor.getFormView();
        sev.jfxOnDragStarted();
    }
    
    public static void notifyDragEnded() {
        Editor editor = (Editor) Jfx.getJmeApp();
        JfxEditView sev = (JfxEditView) editor.getFormView();
        sev.jfxOnDragEnded();
    }
    
    
}
