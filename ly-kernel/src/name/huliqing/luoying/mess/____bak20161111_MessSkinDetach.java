///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.luoying.mess;
//
//import com.jme3.network.HostedConnection;
//import com.jme3.network.serializing.Serializable;
//import name.huliqing.luoying.Factory;
//import name.huliqing.luoying.layer.network.SkinNetwork;
//import name.huliqing.luoying.layer.service.PlayService;
//import name.huliqing.luoying.layer.service.SkinService;
//import name.huliqing.luoying.network.GameServer;
//import name.huliqing.luoying.object.entity.Entity;
//import name.huliqing.luoying.object.skin.Skin;
//
///**
// * 角色脱装备
// * @author huliqing
// */
//@Serializable
//public class MessSkinDetach extends MessBase {
//    
//    private long actorId;
//    private String skinId;
//
//    public long getActorId() {
//        return actorId;
//    }
//
//    public void setActorId(long actorId) {
//        this.actorId = actorId;
//    }
//
//    public String getSkinId() {
//        return skinId;
//    }
//
//    public void setSkinId(String skinId) {
//        this.skinId = skinId;
//    }
//
//    @Override
//    public void applyOnServer(GameServer gameServer, HostedConnection source) {
//        Entity actor = Factory.get(PlayService.class).getEntity(actorId);
//        if (actor == null) {
//            return;
//        }
//        Skin skin = Factory.get(SkinService.class).getSkin(actor, skinId);
//        if (skin != null) {
//            Factory.get(SkinNetwork.class).detachSkin(actor, skin);
//        }
//    }
//
//    @Override
//    public void applyOnClient() {
//        Entity actor = Factory.get(PlayService.class).getEntity(actorId);
//        if (actor == null) {
//            return;
//        }
//        SkinService skinService = Factory.get(SkinService.class);
//        Skin skin = skinService.getSkin(actor, skinId);
//        if (skin != null) {
//            skinService.detachSkin(actor, skin);
//        }
//    }
//    
//}
