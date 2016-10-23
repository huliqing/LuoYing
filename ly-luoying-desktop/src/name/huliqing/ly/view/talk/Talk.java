/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.talk;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 该功能主要让角色之间能够实现一个序列的谈话。示例：
 * face(A, B);
 * addSpeak(A, "How are you!");
 * delay(1);
 * face(B, A);
 * addSpeak(B, "Fine, thank you, and you!");
 * @author huliqing
 * @since 1.3
 */
public interface Talk {
    
    /**
     * 开始谈话内容
     */
    void start();
    
    /**
     * 更新逻辑
     * @param tpf 
     */
    void update(float tpf);
    
    /**
     * 判断谈话是否结束
     * @return 
     */
    boolean isEnd();
    
    /**
     * 清理数据,该方法在talk执行完成之后都应该执行，不管是正常执行完还是被打断，
     * 都应该在最后被执行。该方法会结束当前talk逻辑，如果还有在逻辑在执行，则会
     * 被打断。
     */
    void cleanup();
    
    /**
     * 让目标说话
     * @param actor
     * @param mess
     * @param delay 
     * @return 返回当前talk对象
     */
    Talk speak(Entity actor, String mess);
    
    /**
     * 延迟时间,单位秒
     * @param delay 
     * @return 返回当前talk对象
     */
    Talk delay(float delay);
    
    /**
     * 让源角色面向目标角色， 比如在说话的时候，可能需要面向目标。
     * @param actor 源角色
     * @param target 目标角色
     * @param force 是否强制面向目标，因为在一些情况下，比如源角色正在打怪或做
     * 其它事情，这个时候让源角色朝向目标角色是不太合理的。而force可以让源目标
     * 强制朝向目标角色。
     * @return 返回当前talk对象
     */
    Talk face(Entity actor, Entity target, boolean force);
    
    /**
     * 让角色面向目标位置，参考：{@link #face(name.huliqing.fighter.actor.Actor, name.huliqing.fighter.actor.Actor, boolean) }
     * 该方法只朝向目标位置，不依赖目标角色。
     * @param actor
     * @param position
     * @param force 
     * @return 返回当前talk对象
     */
    Talk face(Entity actor, Vector3f position, boolean force);
    
    /**
     * 添加谈话侦听器
     * @param listener
     * @return 
     */
    Talk addListener(TalkListener listener);
    
    /**
     * 添加一个行为逻辑
     * @param talkLogic 
     * @return  
     */
    Talk addTalkLogic(TalkLogic talkLogic);
    
    /**
     * 是否让谈话在客户端和服务端同时显示,否则只在本地进行显示
     * @param network 
     */
    void setNetwork(boolean network);
}
