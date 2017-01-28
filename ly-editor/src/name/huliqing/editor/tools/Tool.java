/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import name.huliqing.editor.edit.JmeEdit;
import name.huliqing.editor.toolbar.Toolbar;

/**
 * 编辑工具
 * @author huliqing
 * @param <E>
 * @param <T>
 */
public interface Tool<E extends JmeEdit, T extends Toolbar> {
    
    String getName();
    
    void initialize(E jmeEdit, T toolbar);
    
    boolean isInitialized();
    
    void update(float tpf);

    void cleanup();

}
