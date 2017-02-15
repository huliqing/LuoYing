/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.component;

import java.io.Serializable;
import name.huliqing.editor.edit.JfxEdit;

/**
 * 组件转换器
 * @author huliqing
 * @param <T>
 */
public interface ComponentConverter<T extends JfxEdit> extends Serializable {
    
    /**
     * 创建组件到编辑器JfxEdit
     * @param cd 组件的定义
     * @param jfxEdit 
     */
    void create(ComponentDefine cd, T jfxEdit);
    
}
