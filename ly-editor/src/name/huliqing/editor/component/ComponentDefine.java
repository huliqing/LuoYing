/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.component;

import java.io.Serializable;

/**
 *
 * @author huliqing
 */
public class ComponentDefine implements Serializable {
    
    private String id;
    private String type;
    private String icon;
    private String converterClass;
    
    public ComponentDefine() {}
    
    public ComponentDefine(String id, String type, String icon, String converterClass) {
        this.id = id;
        this.type = type;
        this.icon = icon;
        this.converterClass = converterClass;
    }
    
    public String getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public String getType() {
        return type;
    }

    public String getConverterClass() {
        return converterClass;
    }

    @Override
    public String toString() {
        return "ComponentDefine{" + "id=" + id + ", type=" + type + ", icon=" + icon + ", converterClass=" + converterClass + '}';
    }

}
