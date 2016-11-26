/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess.network;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.mess.MessBase;

/**
 * 客户端向服务端发送退出游戏的消息，即告诉服务端客户端正在退出游戏.
 * @author huliqing
 */
@Serializable
public class MessClientExit extends MessBase {
    
}
