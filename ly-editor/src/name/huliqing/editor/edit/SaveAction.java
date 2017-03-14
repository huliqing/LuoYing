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
     * 这个方法会在编辑器保存的时候执行，如：结束编辑器或者编辑过程中按“保存”操作都会调用这个方法.
     * @param application
     */
    void doSave(Application application);
}
