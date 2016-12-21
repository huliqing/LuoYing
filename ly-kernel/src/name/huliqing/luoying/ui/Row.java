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
package name.huliqing.luoying.ui;

/**
 * @author huliqing
 * @param <T>
 */
public abstract class Row<T> extends LinearLayout {
    
    protected ListView parentList;
    
    /**
     * @deprecated 使用 {@link #Row(name.huliqing.fighter.ui.ListView) }代替。
     * 当row中的内容更新后可能要同时更新整个ListView,所以最好传递父ListView作为
     * 参数（因为ListView不是UILayout类型，与Row不是实际的父子关系，当Row内容
     * 更新的时候,row的setNeedUpdate()无法传递到父ListView,这些导致一些刷新延迟
     * 的问题，所以后续不再使用这个构造方法）。
     */
    public Row() {
        super();
    }
    
    public Row(ListView parentView) {
        super();
        this.parentList = parentView;
    }

    @Override
    public void setNeedUpdate() {
        super.setNeedUpdate();
        // 通知父ListView需要更新
        if (parentList != null) {
            parentList.setNeedUpdate();
        }
    }
    
    /**
     * 渲染显示行数据
     * @param data
     */
    public abstract void displayRow(T data);
}
