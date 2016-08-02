/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.mess;

import com.jme3.network.serializing.Serializable;

/**
 * 服务端向客户端发送初始化完成，可以开始接收来自服务端的游戏数据的标记。
 * 只是一个标记.客户端在接到这个消息之前不应该处理来自服务端的“游戏命令消息”，
 * 如载入模型，执行技能等。
 * @author huliqing
 */
@Serializable
public class MessSCInitGameOK extends MessBase {
    
}
