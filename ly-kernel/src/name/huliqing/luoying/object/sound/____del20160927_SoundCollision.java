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
//package name.huliqing.core.object.sound;
//
//import java.util.ArrayList;
//import java.util.List;
//import name.huliqing.core.data.ObjectData;
//import name.huliqing.core.data.define.MatObject;
//
///**
// * 用于为两个材质的物体计算一个碰撞声音。
// * @author huliqing
// */
//public class SoundCollision {
//    private List<Collision> soundCollisions; 
//    
//    private void initSoundCollision() {
//        soundCollisions = new ArrayList<Collision>();
//        
//        // v2 wood hit stone
//        soundCollisions.add(new Collision(Mat.wood, Mat.stone, "soundCollisionWS"));
//        // v2 wood hit body
//        soundCollisions.add(new Collision(Mat.wood, Mat.body,  "soundCollisionWB"));
//        // v2 wood hit wood
//        soundCollisions.add(new Collision(Mat.wood, Mat.wood, "soundCollisionWW"));
//        // v2 wood hit metal
//        soundCollisions.add(new Collision(Mat.wood, Mat.metal, "soundCollisionWM"));
//        
//        // old
//        soundCollisions.add(new Collision(Mat.metal, Mat.metal, "soundCollisionMM"));
//        soundCollisions.add(new Collision(Mat.metal, Mat.stone, "soundCollisionMS"));
//        soundCollisions.add(new Collision(Mat.metal, Mat.body,  "soundCollisionMB"));
//        soundCollisions.add(new Collision(Mat.stone, Mat.body,  "soundCollisionMB"));
//        soundCollisions.add(new Collision(Mat.body, Mat.body,   "soundCollisionMB"));
//    }
//    
//    /**
//     * 通过两种物体来获取物体之间的碰撞声音，返回结束为声音ID
//     * @param obj1
//     * @param obj2 
//     * @return  
//     */
//    public String getCollisionSound(ObjectData obj1, ObjectData obj2) {
//        if (!(obj1 instanceof MatObject) || !(obj2 instanceof MatObject)) {
//            return null;
//        }
//        return getCollisionSound(((MatObject)obj1).getMat(), ((MatObject)obj2).getMat());
//    }
//    
//    /**
//     * 通过两种材质获取材质碰撞声音，返回结束为声音ID
//     * @param mat1
//     * @param mat2
//     * @return 
//     */
//    public String getCollisionSound(Mat mat1, Mat mat2) {
//        if (soundCollisions == null) {
//            initSoundCollision();
//        }
//        for (Collision sc : soundCollisions) {
//            if (sc.checkCound(mat1, mat2)) {
//                return sc.getSound();
//            }
//        }
//        return null;
//    }
//    
//    private class Collision {
//        private final Mat mat1;
//        private final Mat mat2;
//        private final String sound;
//        public Collision(Mat mat1, Mat mat2, String sound) {
//            this.mat1 = mat1;
//            this.mat2 = mat2;
//            this.sound = sound;
//        }
//        
//        public boolean checkCound(Mat mat1, Mat mat2) {
//            return (this.mat1 == mat1 && this.mat2 == mat2) 
//                    || (this.mat1 == mat2 && this.mat2 == mat1);
//        }
//        
//        public String getSound() {
//            return this.sound;
//        }
//
//        @Override
//        public String toString() {
//            return "SoundCollision{" + "mat1=" + mat1 + ", mat2=" + mat2 + ", sound=" + sound + '}';
//        }
//    }
//}
