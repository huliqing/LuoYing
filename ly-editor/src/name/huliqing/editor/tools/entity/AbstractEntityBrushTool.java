/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.entity;

import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.toolbar.EntityBrushToolbar;
import name.huliqing.editor.tools.AbstractTool;

/**
 * 场景实体刷的基本类,主要用于包装一些共用方法。
 * @author huliqing
 */
public abstract class AbstractEntityBrushTool extends AbstractTool<SimpleJmeEdit, EntityBrushToolbar> {

    public AbstractEntityBrushTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

}
