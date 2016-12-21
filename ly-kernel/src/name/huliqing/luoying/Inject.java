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
package name.huliqing.luoying;

/**
 * 所有Service,Network都需要实现这个接口，需要实现Inject方法，
 * 用于初始化的时候实现对其它Service、Network的引用的注入。
 * @author huliqing
 */
public interface Inject {
    
    /**
     * 这个方法会在Service,Network初始化的时候调用，在Service、Network初始化完成后将不再调用。<br>
     * 主要用于在Service,Network初始化的时候注入对其它Service,Network类的引用。
     */
    void inject();

   
}
