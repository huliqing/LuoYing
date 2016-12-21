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
package name.huliqing.luoying.object.bullet;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.object.scene.Scene;

/**
 * 直线型子弹
 * @author huliqing
 */
public class StraightBullet extends AbstractBullet {

    private final Vector3f dir = new Vector3f();
    private final Vector3f temp = new Vector3f();
    
    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        updateDir(getCurrentEndPos());
    }

    @Override
    protected void doUpdatePosition(float tpf, Vector3f endPos) {
        if (trace) {
            updateDir(endPos);
        }
        temp.set(dir).multLocal(baseSpeed * speed * tpf);
        bulletNode.move(temp);
    }
    
    private void updateDir(Vector3f endPos) {
        endPos.subtract(bulletNode.getWorldTranslation(), dir).normalizeLocal();
        if (facing) {
            bulletNode.lookAt(endPos, Vector3f.UNIT_Y);
        }
    }

}
