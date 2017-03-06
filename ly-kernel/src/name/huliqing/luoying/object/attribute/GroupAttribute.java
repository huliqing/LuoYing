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
package name.huliqing.luoying.object.attribute;

import java.util.List;
import name.huliqing.luoying.data.AttributeData;
import name.huliqing.luoying.object.Loader;

/**
 * 属性组，属性组允许将一系列的属性打包成一个属性。在属性组载入时会把这些子属性一个一个载入到角色中。
 * @author huliqing
 */
public class GroupAttribute extends AbstractAttribute<Void> {

    private AttributeManager module;
    
    // 属性组所管理的属性列表，这些属性是直接添加到实体上去的。在属性组移除时这些属性也要一起移除，需要注意的是：
    // 属性是可以动态替换的，所以在属性组移除时需要通过唯一ID来判断哪些属性是由当前属性组管理的，只要移除这些属性就可以。
    private List<AttributeData> attributes;
    
    // ---- inner
    // 标记着子属性是否已经添加到module上。
    private boolean attributesApplied;
    
    @Override
    public void setData(AttributeData data) {
        super.setData(data); 
        this.attributes = data.getAsObjectDataList("attributes");
    }
    
    @Override
    public void updateDatas() {
        // ignore
    }  
    
    @Override
    public Void getValue() {
        return null;
    }

    @Override
    public void initialize(AttributeManager module) {
        super.initialize(module);
        this.module = module;
        
        // 当属性是从存档中载入时，attributesApplied会为true,这时就不再需要去载入属性。因子属性已经在上次载入到
        // AttributeModule中去了，AttributeModule在重新载入时会自动去载入这些子属性,不需要再在这里载入
        if (attributesApplied) {
            return;
        }
        
        // 添加属性到attributeModule,注意要标记attributesApplied=true,并更新到data中去, 那么当下次从存档中载入时，
        // 就不再需要在这里载入到module中去了，因为会从attributeModule中直接载入。
        if (attributes != null) {
            for (AttributeData ad : attributes) {
                if (ad.getId().equals(getId())) {
                    continue; // 不能包含自己,以免死循环
                }
                Attribute attr = Loader.load(ad);
                module.addAttribute(attr);
            }
        }
        
        attributesApplied = true;
    }
    
    @Override
    public void cleanup() {
        if (attributesApplied) {
            // 属性组移除时要一同移除组内属性，注意：只能通过唯一ID来查找(也<b>不</b>能通过相同实例比较来查找，因为attributes中的数据有可能是从存档中读取的)
            // 属性有可能在运行时被替换，所以当GroupAttribute清理时，
            // 这些由GroupAttribute添加上去的属性并不能绝对保存还存在着。
            if (attributes != null) {
                for (AttributeData ad : attributes) {
                    Attribute attr = module.getAttribute(ad.getUniqueId());
                    if (attr != null)  {
                        module.removeAttribute(attr);
                    }
                }
                attributes = null;
            }
            attributesApplied = false;
        }
        super.cleanup();
    }
    
    @Override
    protected boolean doSetValue(Void newValue) {
        return false; // ignore
    }


    
}
