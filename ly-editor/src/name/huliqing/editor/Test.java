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
package name.huliqing.editor;

import name.huliqing.luoying.utils.modifier.TreeFixUtils;

/**
 *
 * @author huliqing
 */
public class Test {
    
    public static void main(String[] args) {
        TreeFixUtils.setAlphaFallOff(0.1f);
        TreeFixUtils.fix("Models/plants/Bush-1064.j3o", "Bush-1064-geom-0");
        TreeFixUtils.fix("Models/plants/Bush-1086.j3o", "Bush-1086-geom-0");
        TreeFixUtils.fix("Models/plants/Bush-1158.j3o", "Bush-1158-geom-0");
        TreeFixUtils.fix("Models/plants/Flower-242.j3o", "Flower-242-geom-0", "Flower-242-geom-1");
        TreeFixUtils.fix("Models/plants/Flower-261.j3o", "Flower-261-geom-0", "Flower-261-geom-1");
        TreeFixUtils.fix("Models/plants/Flower-428.j3o", "Flower-428-geom-0", "Flower-428-geom-1");
        TreeFixUtils.fix("Models/plants/Flower-441.j3o", "Flower-441-geom-0", "Flower-441-geom-1");
        TreeFixUtils.fix("Models/plants/Flower-603.j3o", "Flower-603-geom-0", "Flower-603-geom-2"); // 2
        TreeFixUtils.fix("Models/plants/Tree-1003.j3o", "Tree-1003-geom-0");
        TreeFixUtils.fix("Models/plants/Tree-1010.j3o", "Tree-1010-geom-0");
        TreeFixUtils.fix("Models/plants/Tree-771.j3o", "Tree-771-geom-0");
        TreeFixUtils.fix("Models/plants/Tree-799.j3o", "Tree-799-geom-0");
        TreeFixUtils.fix("Models/plants/bush-11219.j3o", "bush-11219-geom-0");
        TreeFixUtils.fix("Models/plants/dry-651.j3o", "dry-651-geom-0");
        TreeFixUtils.fix("Models/plants/dry-807.j3o", "dry-807-geom-0");
        TreeFixUtils.fix("Models/plants/fir-456.j3o", "fir-456-geom-0");
        TreeFixUtils.fix("Models/plants/palm-11111.j3o", "palm-11111-geom-1");
        TreeFixUtils.fix("Models/plants/palm-11124.j3o", "palm-11124-geom-1", "palm-11124-geom-2");
        TreeFixUtils.fix("Models/plants/palm-11144.j3o", "palm-11144-geom-1", "palm-11144-geom-2");
        TreeFixUtils.fix("Models/plants/palm-11225.j3o", "palm-11225-geom-1");
    }
}
