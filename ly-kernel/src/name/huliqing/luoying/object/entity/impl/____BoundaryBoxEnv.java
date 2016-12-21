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
//package name.huliqing.ly.object.env;
//
//import com.jme3.bullet.control.RigidBodyControl;
//import com.jme3.scene.Spatial;
//import com.jme3.scene.Spatial.CullHint;
//import name.huliqing.ly.constants.AssetConstants;
//import name.huliqing.ly.data.EntityData;
//import name.huliqing.ly.object.Loader;
//import name.huliqing.ly.object.entity.ModelEntity;
//
///**
// * box类型的边界盒，用于防止角色掉出场景边界。
// * @author huliqing
// */
//public class BoundaryBoxEnv  extends ModelEntity {
//
//    private boolean debug;
//     
//    // ---- inner
//    private Spatial boundary;
//    private RigidBodyControl control;
//    
//    @Override
//    public void setData(EntityData data) {
//        super.setData(data);
//        debug = data.getAsBoolean("debug", debug);
//    }
//    
//    @Override
//    protected Spatial loadModel() {
//        boundary = Loader.loadModel(AssetConstants.MODEL_BOUNDARY);
//        return boundary;
//    }
//    
//    @Override
//    public void initialize() {
//        super.initialize();
//        // 添加RigidBodyControl
//        control = new RigidBodyControl(0);
//        boundary.addControl(control);
//        boundary.setCullHint(debug ? CullHint.Never : CullHint.Always);
//    }
//
//
//}
