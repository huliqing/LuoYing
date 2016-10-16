package name.huliqing.luoying.mess;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.ly.mess;
//
//import com.jme3.network.serializing.Serializable;
//import name.huliqing.ly.Factory;
//import name.huliqing.ly.layer.service.ActorService;
//import name.huliqing.ly.layer.service.PlayService;
//import name.huliqing.ly.object.entity.Entity;
//
///**
// * 让目标说话
// * @author huliqing
// */
//@Serializable
//public class MessActorSpeak extends MessBase {
//    
//    private long actorId;
//    // 说话的内容
//    private String mess;
//    // 说话的显示时间,单位秒,如果该值<=0,则该时间会被自动重新计算
//    private float useTime;
//
//    public long getActorId() {
//        return actorId;
//    }
//
//    public void setActorId(long actorId) {
//        this.actorId = actorId;
//    }
//
//    public String getMess() {
//        return mess;
//    }
//
//    public void setMess(String mess) {
//        this.mess = mess;
//    }
//
//    public float getUseTime() {
//        return useTime;
//    }
//
//    public void setUseTime(float useTime) {
//        this.useTime = useTime;
//    }
//
//    @Override
//    public void applyOnClient() {
//        PlayService playService = Factory.get(PlayService.class);
//        ActorService actorService = Factory.get(ActorService.class);
//        Entity actor = playService.getEntity(actorId);
//        if (actor != null) {
//            actorService.speak(actor, mess, useTime);
//        }
//    }
//    
//}
