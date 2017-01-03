/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.editviews;

import name.huliqing.editor.formview.FormView;

/**
 * 编辑器的UI界面接口, 主要作为编辑器的UI编辑界面，包含各种编辑工具等。
 * @author huliqing
 */
public interface EditView {
    
    void initialize(FormView formView);
    
    boolean isInitialized();
    
    void update(float tpf);
    
    void cleanup();
    
    FormView getFormView();
}
