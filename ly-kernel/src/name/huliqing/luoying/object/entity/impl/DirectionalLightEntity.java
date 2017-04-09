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
package name.huliqing.luoying.object.entity.impl;

import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.math.Vector3f;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.entity.LightEntity;
import name.huliqing.luoying.object.entity.NonModelEntity;
import name.huliqing.luoying.object.scene.Scene;

/**
 * @author huliqing
 */
public class DirectionalLightEntity extends NonModelEntity implements LightEntity{
 
    private final DirectionalLight light = new DirectionalLight();
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        light.setColor(data.getAsColor("color", light.getColor()));
        Vector3f direction = data.getAsVector3f("direction");
        if (direction != null) {
            light.setDirection(direction);
        }
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("color", light.getColor());
        data.setAttribute("direction", light.getDirection());
    }
    
    @Override
    public void initEntity() {}

    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene); 
        scene.getRoot().addLight(light);
    }

    @Override
    public void cleanup() {
        scene.getRoot().removeLight(light);
        super.cleanup();
    }
    
    @Override
    public Light getLight() {
        return light;
    }
    
    public Vector3f getDirection() {
        return light.getDirection();
    }
    
    public void setDirection(Vector3f direction) {
        light.setDirection(direction);
    }
    
//    private Spatial createDebugArrow(DirectionalLight light) {
//        Node node = new Node();
//        Spatial arrow1 = DebugUtils.createArrow(Vector3f.ZERO, Vector3f.ZERO.add(light.getDirection().normalize()), light.getColor());
//        Spatial arrow2 = arrow1.clone();
//        Spatial arrow3 = arrow1.clone();
//        arrow1.setLocalTranslation(0, 3, 0);
//        arrow2.setLocalTranslation(0, 3.5f, 0);
//        arrow3.setLocalTranslation(0, 4, 0);
//        node.attachChild(arrow1);
//        node.attachChild(arrow2);
//        node.attachChild(arrow3);
//        return node;
//    }


    
}
