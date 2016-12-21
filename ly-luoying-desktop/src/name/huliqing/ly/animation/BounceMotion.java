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
package name.huliqing.ly.animation;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.logging.Logger;

/**
 * 弹跳的动画效果
 * @author huliqing
 */
public class BounceMotion extends AbstractAnimation {
    private final static Logger logger = Logger.getLogger(BounceMotion.class.getName());

    private float height = 50;
    // UI的原始位置
    private Vector3f origin = new Vector3f();
    
    public void setHeight(float height) {
        this.height = height;
    }
    
    @Override
    protected void doInit() {
        origin.set(target.getLocalTranslation());
//        logger.log(Level.INFO, "ui end position: local={0}, world={1}"
//                , new Object[] {ui.getLocalTranslation(), ui.getWorldTranslation()});
    }

    @Override
    protected void doAnimation(float tpf) {
        float sineFactor = FastMath.sin(time / useTime * FastMath.PI);
        if (sineFactor < 0) {
            return;
        }
        TempVars tv = TempVars.get();
        float yOffset = height * sineFactor;
        target.setLocalTranslation(tv.vect1.set(origin).addLocal(0, yOffset, 0));
        tv.release();
    }

    @Override
    public void cleanup() {
        // 1.do self cleanup...
        target.setLocalTranslation(origin);
        // 2.do super cleanup
        super.cleanup(); 
    }
    
    
}
