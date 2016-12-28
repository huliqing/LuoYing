/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import name.huliqing.editor.forms.Form;
import com.jme3.app.SimpleApplication;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.forms.SimpleEditForm;

/**
 *
 * @author huliqing
 */
public class Editor extends SimpleApplication{
    
    private Form form;

    @Override
    public void simpleInitApp() {
        JmeEvent.registerInputManager(inputManager);
        setForm(new SimpleEditForm());
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        form.update(tpf);
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
