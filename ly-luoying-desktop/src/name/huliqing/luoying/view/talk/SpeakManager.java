/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.view.talk;

import com.jme3.util.SafeArrayList;
import name.huliqing.ly.object.AbstractPlayObject;
import name.huliqing.ly.object.entity.Entity;

/**
 * 说话，谈话管理,该类主要管理谈话逻辑，每个逻辑都包装成Talk，每个Talk在执
 * 行完之后都会被移除。如果需要循环执行一个talk，则需要判断talk是否结束，再
 * 将talk添加到当前管理器中{@link #addTalk(name.huliqing.fighter.game.manager.talk.Talk) }
 * ,Talk不增加循环设置主要是为了避免无意的操作，在添加循环talk之后即忘记移除的情况发生，造成资源浪费。
 * @author huliqing
 */
public class SpeakManager extends AbstractPlayObject {
    
    private final static SpeakManager INSTANCE = new SpeakManager();
    
    private final SafeArrayList<Speak> speaks = new SafeArrayList<Speak>(Speak.class);
    
    private SpeakManager() {}
    
    public static SpeakManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void update(float tpf) {
        if (speaks.isEmpty())
            return;
        
        for (Speak s : speaks.getArray()) {
            if (s.isEnd()) {
                speaks.remove(s);
            } else {
                s.update(tpf);
            }
        }
    }

    @Override
    public void cleanup() {
        for (Speak s : speaks) {
            if (!s.isEnd()) {
                s.cleanup();
            }
        }
        speaks.clear();
        super.cleanup();
    }

    /**
     * 立即让角色说话
     * @param actor
     * @param mess 
     * @param useTime 
     */
    public void doSpeak(Entity actor, String mess, float useTime) {
        if (!isInitialized()) {
            return;
        }
        
        // 首先清除指定的角色旧的说话内容，否则内容可能重叠在一起
        for (Speak s : speaks) {
            if (s.getActor() == actor) {
                s.cleanup();
            }
        }
        
        // 开始新的说话内容
        Speak speak = new SpeakImpl(actor, mess, useTime);
        speak.start();
        speaks.add(speak);
    }
    
}
