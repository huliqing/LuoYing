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

import com.jme3.light.SpotLight;
import com.jme3.math.Vector3f;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.entity.LightEntity;
import name.huliqing.luoying.object.entity.NonModelEntity;
import name.huliqing.luoying.object.scene.Scene;

/**
 *  直射光实体
 * @author huliqing
 */
public class SpotLightEntity extends NonModelEntity implements LightEntity{
 
    private final SpotLight light = new SpotLight();
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        Vector3f location = data.getLocation();
        if (location != null) {
            light.setPosition(location);
        }
        light.setName(data.getName());
        light.setColor(data.getAsColor("color", light.getColor()));
        light.setDirection(data.getAsVector3f("direction", light.getDirection()));
        light.setSpotInnerAngle(data.getAsFloat("spotInnerAngle", light.getSpotInnerAngle()));
        light.setSpotOuterAngle(data.getAsFloat("spotOuterAngle", light.getSpotOuterAngle()));
        light.setSpotRange(data.getAsFloat("spotRange", light.getSpotRange()));
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setLocation(light.getPosition());
        data.setAttribute("name", light.getName());
        data.setAttribute("color", light.getColor());
        data.setAttribute("direction", light.getDirection());
        data.setAttribute("spotInnerAngle", light.getSpotInnerAngle());
        data.setAttribute("spotOuterAngle", light.getSpotOuterAngle());
        data.setAttribute("spotRange", light.getSpotRange());
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        light.setEnabled(enabled);
    }
    
    @Override
    public void initEntity() {
        light.setEnabled(isEnabled());
    }

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
    public SpotLight getLight() {
        return light;
    }

    
}
