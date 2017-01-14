/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.events;

import com.jme3.app.state.AbstractAppState;
import com.jme3.input.InputManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author huliqing
 */
public class JmeEventAppState extends AbstractAppState {

    private static final Logger LOG = Logger.getLogger(JmeEventAppState.class.getName());
    
    private final static JmeEventAppState INSTANCE = new JmeEventAppState();
    private InputManager inputManager;
    
    private final List<JmeEvent> events = new ArrayList<JmeEvent>();
    private final Set<JmeEvent> tempSets = new HashSet<JmeEvent>(); // 临时集合

    private JmeEventAppState() {}
    
    public final static JmeEventAppState getInstance() {
        return INSTANCE;
    }
    
    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (events.isEmpty()) {
            return;
        }
        
        // 对优先级进行排序,高优先级在前
        Collections.sort(events);
//        LOG.log(Level.INFO, ">>>>Events={0}", events);
        // 响应事件
        JmeEvent e;
        for (int i = 0; i < events.size(); i++) {
            e = events.get(i);
            e.setConsumed(false);
            e.fireEventListeners();
            // 如果有一个事件调用了consume,则后续事件都将阻断
            if (e.isConsumed()) {
//                LOG.log(Level.INFO, "====Event consumed, event={0}", e.getName());
                break;
            }
        }
        tempSets.clear();
        events.clear();
    }

    @Override
    public void cleanup() {
        tempSets.clear();
        events.clear();
        super.cleanup();
    }
    
    /**
     * 添加一个事件到事件列表中，在每一次循环更新的时候事件列表中的事件会被执行，然后清空队列。
     * @param je 
     */
    void addEventQueue(JmeEvent je) {
        if (tempSets.contains(je)) {
            return;
        }
        tempSets.add(je);
        events.add(je);
    }

    public InputManager getInputManager() {
        return inputManager;
    }
    
}
