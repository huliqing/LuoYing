/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.manager;

import javafx.scene.layout.Pane;
import name.huliqing.editor.Editor;
import name.huliqing.editor.editforms.SpatialEditForm;
import name.huliqing.editor.editviews.EditView;
import name.huliqing.editor.editviews.JfxSpatialEditView;
import name.huliqing.editor.editviews.SimpleEditView;
import name.huliqing.editor.formview.FormView;
import name.huliqing.editor.formview.SimpleFormView;
import name.huliqing.fxswing.Jfx;

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
        Jfx.runOnJme(() -> {
            Editor editor = (Editor) Jfx.getJmeApp();
            FormView formView = new SimpleFormView();
            formView.setEditForm(new SpatialEditForm());
            formView.setEditView(new JfxSpatialEditView(jfxEditZone));
            editor.setFormView(formView);
        });
    }
    
    public static void openSpatialEditor(String fileAbsolutePath) {
        Jfx.runOnJme(() -> {
            
            Editor editor = (Editor) Jfx.getJmeApp();
            SpatialEditForm form = new SpatialEditForm();
            form.setFilePath(fileAbsolutePath);
            EditView view = new JfxSpatialEditView(jfxEditZone);

            FormView formView = new SimpleFormView();
            formView.setEditForm(form);
            formView.setEditView(view);
            editor.setFormView(formView);
        
        });
    }

    public static void notifyDragStarted() {
        Editor editor = (Editor) Jfx.getJmeApp();
        SimpleEditView sev = (SimpleEditView) editor.getFormView().getEditView();
        sev.onDragStarted();
    }
    
    public static void notifyDragEnded() {
        Editor editor = (Editor) Jfx.getJmeApp();
        SimpleEditView sev = (SimpleEditView) editor.getFormView().getEditView();
        sev.onDragEnded();
    }
}
