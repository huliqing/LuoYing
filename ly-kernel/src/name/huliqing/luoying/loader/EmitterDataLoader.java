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
package name.huliqing.luoying.loader;

import name.huliqing.luoying.data.EmitterData;
import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.xml.DataLoader;

/**
 *
 * @author huliqing
 */
public class EmitterDataLoader implements DataLoader<EmitterData> {

    @Override
    public void load(Proto proto, EmitterData data) {
        
        // remove20161128,不再需要，以后直接放在EmitterData内部。
//        data.setNumParticles(proto.getAsInteger("numParticles"));
//        data.setStartColor(proto.getAsColor("startColor"));
//        data.setEndColor(proto.getAsColor("endColor"));
//        data.setStartSize(proto.getAsFloat("startSize"));
//        data.setEndSize(proto.getAsFloat("endSize"));
//        data.setGravity(proto.getAsVector3f("gravity"));
//        data.setHighLife(proto.getAsFloat("highLife"));
//        data.setLowLife(proto.getAsFloat("lowLife"));
//        data.setTexture(proto.getAsString("texture"));
//        data.setImagesX(proto.getAsInteger("imagesX"));
//        data.setImagesY(proto.getAsInteger("imagesY"));
//        data.setParticlesPerSec(proto.getAsInteger("particlesPerSec"));
//        data.setRandomAngle(proto.getAsBoolean("randomAngle", false));
//        data.setRotateSpeed(proto.getAsFloat("rotateSpeed"));
//        data.setSelectRandomImage(proto.getAsBoolean("selectRandomImage", false));
//        data.setFaceNormal(proto.getAsVector3f("faceNormal"));
//        data.setFacingVelocity(proto.getAsBoolean("facingVelocity", false));
//        data.setInitialVelocity(proto.getAsVector3f("initialVelocity"));
//        data.setVelocityVariation(proto.getAsFloat("velocityVariation"));
//        data.setShape(proto.getAsString("shape"));
    }
    
}
