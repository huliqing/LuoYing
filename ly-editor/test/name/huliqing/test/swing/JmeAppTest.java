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
package name.huliqing.test.swing;

import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;

/**
 *
 * @author huliqing
 */
public class JmeAppTest extends SimpleApplication {

        @Override
        public void simpleInitApp() {
            inputManager.setCursorVisible(true);
            flyCam.setDragToRotate(true);
            flyCam.setMoveSpeed(10);
            rootNode.addLight(new DirectionalLight());
            rootNode.addLight(new AmbientLight());

            for (int i = 0; i < 10; i++) {
                Spatial sinbad = this.assetManager.loadModel("Models/Sinbad.mesh.j3o");

                sinbad.setLocalScale(0.5f);
                sinbad.setLocalTranslation(5 * FastMath.nextRandomFloat() - 2.5f, 5 * FastMath.nextRandomFloat() - 2.5f, 5 * FastMath.nextRandomFloat() - 2.5f);
                sinbad.getControl(AnimControl.class).createChannel().setAnim("run");
                this.rootNode.attachChild(sinbad);
            }
            System.out.println("TestJmeApp started......");
        }

    }
