package name.huliqing.luoying.object.sound;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
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
