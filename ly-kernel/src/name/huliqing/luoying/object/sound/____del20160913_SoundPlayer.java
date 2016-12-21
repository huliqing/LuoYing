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
//import com.jme3.math.Vector3f;
//import java.util.HashMap;
//import java.util.Map;
//import name.huliqing.core.LY;
//import name.huliqing.core.Factory;
//import name.huliqing.core.data.SoundData;
//import name.huliqing.core.object.Loader;
//import name.huliqing.core.mvc.service.ConfigService;
//
///**
// *
// * @author huliqing
// */
//public class SoundPlayer {
////    private final ConfigService configService = Factory.get(ConfigService.class);
//    
//    // 允许播放声音的最远距离，在该距离之外则不播放声音。
//    // 该距离指与摄像机的距离,注意距离是平方表示，比较的时候用平方进行比较
//    // 减少一次开方操作
//    private final float MAX_DISTANCE_SQUARED = 80 * 80;
//    
//    private final Map<String, Sound> audioMap = new HashMap<String, Sound>();
//    
//    public void playSound(SoundData sound, Vector3f position) {
//        Sound audio = getSound(sound.getId());
//        Vector3f camLoc = LY.getApp().getCamera().getLocation();
//        // 注意：比较的是平方，可减少一次开方运算
//        float distanceSquared = camLoc.distanceSquared(position);
//        if (distanceSquared >= MAX_DISTANCE_SQUARED) {
//            return;
//        }
//        
//        // remove20160811,后续交由AudioNode去控制声音大小
////        float distanceFactor = (MAX_DISTANCE_SQUARED - distanceSquared) / MAX_DISTANCE_SQUARED;
////        audio.getAudio().setVolume(audio.getData().getVolume() * distanceFactor * configService.getSoundVolume());
//        audio.play();
//    }
//    
//    public void stopSound(SoundData sound) {
//        getSound(sound.getId()).stop();
//    }
//    
//    private Sound getSound(String soundId) {
//        Sound audio = audioMap.get(soundId);
//        if (audio == null) {
//            audio = Loader.load(soundId);
//            audio.setLoop(false);
//            audioMap.put(soundId, audio);
//        }
//        return audio;
//    }
//    
//}
