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
package name.huliqing.test.fxjme;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author huliqing
 */
public class Test {
    public static void main(String[] args) {
//        Vector3f a = new Vector3f(1,0,0);
//        Vector3f b = new Vector3f(0,1,0);
//        List<Vector3f> test = new ArrayList<Vector3f>();
//        test.add(a);
//        test.add(b);

        List<Integer> test = new ArrayList<Integer>();
        test.add(1);
        test.add(2);
        test.add(3);
        test.stream().filter(t -> t == 2).forEach(t -> {System.out.println("t=" + t);});
    }
}
