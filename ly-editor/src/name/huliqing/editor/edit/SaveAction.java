/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit;

import com.jme3.app.Application;

/**
 *
 * @author huliqing
 */
public interface SaveAction {
    
    /**
     * 保存存档
     * @param application
     */
    void doSave(Application application);
}
