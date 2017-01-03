/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.formview;

import name.huliqing.editor.Editor;
import name.huliqing.editor.editforms.EditForm;
import name.huliqing.editor.editviews.EditView;

/**
 * FormView作为编辑器的主编辑界面，主要由两部分：3D界面编辑器和UI界面编辑器组成
 * @author huliqing
 * @param <F> 3D编辑界面
 * @param <V> UI编辑界面
 */
public interface FormView<F extends EditForm, V extends EditView>{
    
    void initialize(Editor editor);
    
    boolean isInitialized();
    
    void update(float tpf);
    
    void cleanup();
    
    Editor getEditor();
    
    F getEditForm();
    
    void setEditForm(F form);
    
    V getEditView();
    
    void setEditView(V view);
}
