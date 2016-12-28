/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.events;

import com.jme3.input.InputManager;

/**
 * JME事件
 * @author huliqing
 */
public class JmeEvent extends AbstractEvent {
    
    private static InputManager inputManager;
    
    /**
     * 注册InputManager.
     * @param inputManager 
     */
    public final static void registerInputManager(InputManager inputManager) {
        JmeEvent.inputManager = inputManager;
    }
    
    public JmeEvent(String name) {
        super(name);
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
        JmeKeyMapping em = new JmeKeyMapping(inputManager);
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
        JmeKeyMapping em = new JmeKeyMapping(inputManager);
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
        JmeKeyMapping em = new JmeKeyMapping(inputManager);
        em.bindAxis(mouseAxis, negative).setUsePressed(usePressed);
        addKeyMapping(em);
        return (E) this;
    }
 
    private void checkInputManager() {
        if (inputManager == null) {
            throw new NullPointerException("InputManager not found! "
                    + "use JmeEvent.registerInputManager(InputManager) to register inputManager.");
        }
    }

}
