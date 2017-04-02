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
package name.huliqing.editor.events;

/**
 * @author huliqing
 */
public interface KeyMappingListener {
    
    /**
     * 当按键事件匹配时该方法被调用，这表示EventMapping所绑定的键发生了事件，在键按下或弹起时该事件都会触发，
     * 需要通过{@link KeyMapping#isMatch() }来判断是否匹配。
     * @param em 
     */
    void onKeyMapping(KeyMapping em);
    
}
