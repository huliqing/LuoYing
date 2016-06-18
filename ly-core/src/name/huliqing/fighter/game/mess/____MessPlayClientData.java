///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.game.mess;
//
//import com.jme3.network.serializing.Serializable;
//
///**
// * 封装连接的客户端信息
// * @author huliqing
// */
//@Serializable
//public class MessPlayClientData {
//    private int connId;         // 客户端的连接ID，该ID对于每个客户端连接都是唯一的
//    private String address;     // 连接地址，即客户端的IP信息
//    private String name;        // 客户端名称标识，如PC名称，手机名称等，不一定唯一。
//    private String actorName;   // 角色名称
//    
//    public MessPlayClientData() {}
//    
//    public MessPlayClientData(int connId, String address, String name) {
//        this.connId = connId;
//        this.address = address;
//        this.name = name;
//    }
//
//    public int getConnId() {
//        return connId;
//    }
//
//    public void setConnId(int connId) {
//        this.connId = connId;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getActorName() {
//        return actorName;
//    }
//
//    public void setActorName(String actorName) {
//        this.actorName = actorName;
//    }
//    
//    
//}
