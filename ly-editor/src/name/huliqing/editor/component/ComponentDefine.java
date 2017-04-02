/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
