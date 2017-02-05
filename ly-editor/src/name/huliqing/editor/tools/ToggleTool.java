/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import name.huliqing.editor.edit.JmeEdit;
import name.huliqing.editor.toolbar.Toolbar;

/**
 * 无任何方法，主要用于定义一种可开关的工具类型。
 * @author huliqing
 * @param <E>
 * @param <T>
 */
public interface ToggleTool<E extends JmeEdit, T extends Toolbar> extends Tool<E, T> {
    
}
