/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess.network;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.mess.BaseMess;

/**
 * 客户端向服务端请求初始化游戏, 这个消息只有在客户端处于ready状态，同时服务端处于running状态时才可以向服务端发送，
 * 以请求服务端场景实体的初始化数据。<br>
 * 当服务端接收到这个消息之后应该向客户端发送MessRequestGameInitOk消息，并立即向客户端发送场景初始化实体数据.
 * @author huliqing
 */
@Serializable
public class RequestGameInitMess extends BaseMess {
    
}
