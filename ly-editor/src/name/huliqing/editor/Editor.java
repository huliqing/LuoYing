/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import com.jme3.app.SimpleApplication;
import name.huliqing.editor.forms.Form;
import name.huliqing.editor.forms.SimpleForm;

/**
 *
 * @author huliqing
 */
public class Editor extends SimpleApplication{
    
    private final EditorInputManager editorInputManager = new EditorInputManager();
    
    private static Editor instance;
    private Form form;

    @Override
    public void simpleInitApp() {
        instance = this;
        editorInputManager.initialize(this);
        setForm(new SimpleForm());
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        form.update(tpf);
        editorInputManager.update(tpf);
    }
    
    public static Editor getEditor() {
        return instance;
    }
    
    public Form getForm() {
        return form;
    }

    public void setForm(Form newForm) {
        if (form != null) {
            form.cleanup();
        }
        form = newForm;
        if (!form.isInitialized()) {
            form.initialize(this);
        }
    }
    
    
}
