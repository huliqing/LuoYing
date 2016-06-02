/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.manager.talk;

import com.jme3.app.Application;
import com.jme3.util.SafeArrayList;
import java.util.List;
import name.huliqing.fighter.manager.Manager;

/**
 * 说话，谈话管理,该类主要管理谈话逻辑，每个逻辑都包装成Talk，每个Talk在执
 * 行完之后都会被移除。如果需要循环执行一个talk，则需要判断talk是否结束，再
 * 将talk添加到当前管理器中{@link #addTalk(name.huliqing.fighter.game.manager.talk.Talk) }
 * ,Talk不增加循环设置主要是为了避免无意的操作，在添加循环talk之后即忘记移除的情况发生，造成资源浪费。
 * @author huliqing
 */
public class TalkManager implements Manager {
    
    private final static TalkManager instance = new TalkManager();
    private final List<Talk> talks = new SafeArrayList<Talk>(Talk.class);
    
    private TalkManager() {}

    public static TalkManager getInstance() {
        return instance;
    }
    
    @Override
    public void init(Application app) {}

    @Override
    public void update(float tpf) {
        if (talks.isEmpty())
            return;
        
        for (Talk t : talks) {
            if (t.isEnd()) {
                talks.remove(t);
            } else {
                t.update(tpf);
            }
        }
    }

    @Override
    public void cleanup() {
        for (Talk talk : talks) {
            if (!talk.isEnd()) {
                talk.cleanup();
            }
        }
        talks.clear();
    }

    /**
     * 执行一段谈话，talk是一个说话或对话序列，该talk在执行完成之后将被立即
     * 移除。
     * @param talk 
     */
    public void startTalk(Talk talk) {
        talk.start();
        talks.add(talk);
    }
    
}
