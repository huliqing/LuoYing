/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import name.huliqing.editor.Toolbar;

/**
 * 编辑工具
 * @author huliqing
 * @param <T>
 */
public interface Tool<T extends Toolbar> {
    
    String getName();
    
    void initialize();
    
    boolean isInitialized();
    
    void update(float tpf);

    void cleanup();
    
    void setToolbar(T toolbar);
    

}
