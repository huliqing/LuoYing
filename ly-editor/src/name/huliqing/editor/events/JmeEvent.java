/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.events;

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;

/**
 * JME事件
 * @author huliqing
 */
public class JmeEvent extends AbstractEvent implements Comparable<JmeEvent>{
    
    // 事件的优先级, 同时发生的事件可能存在多个（比如相同的按键），各个事件可以拥有不同的优先级。
    // 这个优先级只是一个设置标记。具体作用和实现由外部调用处理。
    protected int prior;
    
    public JmeEvent(String name) {
        super(name);
    }
    
    public final static JmeEventAppState getJmeEventAppState() {
        return JmeEventAppState.getInstance();
    }
    
    /**
     * 获取事件的优先级
     * @return 
     */
    public int getPrior() {
        return prior;
    }

    /**
     * 设置事件的优先级,当相同的按键事件同时响应时，优先级较高的事件会先获得响应执行，如果事件在执行之后
     * 调用{@link #setConsumed(boolean) }方法，则后续事件将不再响应，这可以用于避免相同按键事件的冲突。
     * @param <E>
     * @param prior 
     * @return  
     */
    public <E extends JmeEvent> E setPrior(int prior) {
        this.prior = prior;
        return (E) this;
    }

    /**
     * 绑定键盘按键事件
     * @param <E>
     * @param keyCode see {@link KeyInput}
     * @param usePressed 是否使用”按下“状态作为触发事件的判断
     * @return 返回当前工具实例
     */
    public <E extends JmeEvent> E bindKey(int keyCode, boolean usePressed) {
        checkInputManager();
        JmeKeyMapping em = new JmeKeyMapping(JmeEventAppState.getInstance().getInputManager());
        em.bindKey(keyCode).setUsePressed(usePressed);
        addKeyMapping(em);
        return (E) this;
    }
    
    /**
     * 绑定鼠标按钮事件
     * @param <E>
     * @param mouseButton see {@link MouseInput}
     * @param usePressed 是否使用”按下“状态作为触发事件的判断
     * @return 返回当前工具实例
     */
    public <E extends JmeEvent> E bindButton(int mouseButton, boolean usePressed) {
        checkInputManager();
        JmeKeyMapping em = new JmeKeyMapping(JmeEventAppState.getInstance().getInputManager());
        em.bindButton(mouseButton).setUsePressed(usePressed);
        addKeyMapping(em);
        return (E)this;
    }

    /**
     * 绑定鼠标滚轴事件
     * @param <E>
     * @param mouseAxis see {@link MouseInput}
     * @param negative
     * @param usePressed 是否使用”按下“状态作为触发事件的判断
     * @return 返回当前工具实例
     */
    public <E extends JmeEvent> E bindAxis(int mouseAxis, boolean negative, boolean usePressed) {
        checkInputManager();
        JmeKeyMapping em = new JmeKeyMapping(JmeEventAppState.getInstance().getInputManager());
        em.bindAxis(mouseAxis, negative).setUsePressed(usePressed);
        addKeyMapping(em);
        return (E) this;
    }
 
    private void checkInputManager() {
        if (JmeEventAppState.getInstance().getInputManager() == null) {
            throw new NullPointerException("InputManager not found! ");
        }
    }

    @Override
    public void onKeyMapping(KeyMapping em) {
        super.onKeyMapping(em);
        JmeEventAppState.getInstance().addEventQueue(this);
    }
    
    @Override
    public int compareTo(JmeEvent o) {
        return Integer.compare(o.getPrior(), prior); // 高优先级的放在前面
    }

    @Override
    public String toString() {
        return super.toString(); 
    }
    
    
}
