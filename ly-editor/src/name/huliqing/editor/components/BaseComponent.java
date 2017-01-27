/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.components;

import name.huliqing.editor.edit.JfxEdit;

/**
 *
 * @author huliqing
 * @param <T> JfxEdit类型
 */
public abstract class BaseComponent<T extends JfxEdit> implements Component<T>{

    /**
     * 组件的id
     */
    protected String id;
    protected String name;
    protected String icon;
    
    public BaseComponent() {}
    
    public BaseComponent(String id) {
        this(id, id);
    }
    
    public BaseComponent(String id, String name) {
        this(id, name, null);
    }
    
    public BaseComponent(String id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }
}
