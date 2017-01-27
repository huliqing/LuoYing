/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.components;

import java.io.Serializable;
import name.huliqing.editor.edit.JfxEdit;

/**
 *
 * @author huliqing
 * @param <T>
 */
public interface Component<T extends JfxEdit> extends Serializable {
    
    /**
     * 获取组件名称
     * @return 
     */
    String getId();
    
    void setId(String id);
    
    /**
     * 
     * @return 
     */
    String getName();
    
    void setName(String name);
    
    /**
     * 获取图标
     * @return 
     */
    String getIcon();
    
    void setIcon(String icon);
    
    /**
     * 创建组件到编辑器JfxEdit
     * @param jfxEdit 
     */
    void create(T jfxEdit);
    
    
}
