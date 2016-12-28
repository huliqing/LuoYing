/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.events;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * 按键匹配器
 * @author huliqing
 */
public class JmeKeyMapping extends AbstractKeyMapping {

    private static final Logger LOG = Logger.getLogger(JmeKeyMapping.class.getName());

    // 键盘、鼠标按钮、鼠标滚轴
    public enum Type {
        key, button, axis;
    }
    
    private Type type;
    // 默认使用 -1,这样不会误匹配到其它事件
    private int code = -1;
    private boolean usePressed;
    // 仅对MouseAxis类型按键有效
    private boolean negative;
    
    // 按键匹配的有效时间，毫秒，仅对于非usePressed类型的按键。
    // 对于”弹起“匹配类型的按键必须有一个有效时间限制，超过这个时间之后，无论如何则都视为不匹配，
    // 否则当按键弹起之后会处于一直为匹配的状态。
    private float validTime = 50;
    // 最近一次匹配的时间,单位毫秒
    private long lastMappingTime;
    
    private final InputManager inputManager;
    private final ActionListener actionListener = new MappingActionListener();
    private String mappingName;
    private boolean mappingResult;
    
    private final List<KeyMappingListener> listeners = new ArrayList<KeyMappingListener>(1);
    
    public JmeKeyMapping(InputManager inputManager) {
        this.inputManager = inputManager;
    }
    
    /**
     * 初始化事件
     */
    @Override
    public void initialize() {
        super.initialize();
        mappingName = type.name() + "_" + code + "_" + UUID.randomUUID().toString();
        // 绑定事件
        bindListener();
    }
    
    /**
     * 清理事件绑定，并释放资源
     */
    @Override
    public void cleanup() {
        inputManager.deleteMapping(mappingName);
        mappingName = null;
        mappingResult = false;
        super.cleanup();
    }

    @Override
    public String getMappingName() {
        return mappingName;
    }
    
    /**
     * 判断当前事件是否匹配。
     * @return 
     */
    @Override
    public boolean isMatch() {
        // 对于"按下"类型的匹配，始终使用当按键按下时的匹配结果，直到按键弹起。
        if (usePressed) {
            return mappingResult;
        }
        
        // 对于"弹起"类型的匹配，会有一个弹起后的有效匹配时间限制。
        float timeDelay = System.currentTimeMillis() - lastMappingTime;
        boolean finalResult = mappingResult && timeDelay <= validTime;
//        LOG.log(Level.INFO, "mappingName={0}, timeDelay={1}, validTime={2}, mappingResult={3}, finalResult={4}"
//            , new Object[] {mappingName, timeDelay, validTime, mappingResult, finalResult});
        return finalResult;
    }
    
    /**
     * 绑定一个键盘按键来触发事件， 注：{@link #bindKey(int) } 、{@link #bindButton(int) }、 {@link #bindAxis(int, boolean) }
     * 只能选择一个进行绑定。
     * @param <T>
     * @param key {@link KeyInput}
     * @return 返回当前EventMapping
     */
    public <T extends JmeKeyMapping> T bindKey(int key) {
        type = Type.key;
        code = key;
        if (isInitialized()) {
            bindListener();
        }
        return (T) this;
    }
    
    /**
     * 绑定一个鼠标按钮来触发事件， 注：{@link #bindKey(int) } 、{@link #bindButton(int) }、 {@link #bindAxis(int, boolean) }
     * 只能选择一个进行绑定。
     * @param <T>
     * @param mouseButton 参考: {@link MouseInput}
     * @return 返回当前EventMapping
     */
    public <T extends JmeKeyMapping> T bindButton(int mouseButton) {
        type = Type.button;
        code = mouseButton;
        if (isInitialized()) {
            bindListener();
        }
        return (T) this;
    }
    
    /**
     * 绑定一个鼠标轴来触发事件， 注：{@link #bindKey(int) } 、{@link #bindButton(int) }、 {@link #bindAxis(int, boolean) }
     * 只能选择一个进行绑定。
     * @param <T>
     * @param mouseAxis 参考: {@link MouseInput}
     * @param negative
     * @return 返回当前EventMapping
     */
    public <T extends JmeKeyMapping> T bindAxis(int mouseAxis, boolean negative) {
        type = Type.axis;
        code = mouseAxis;
        this.negative = negative;
        if (isInitialized()) {
            bindListener();
        }
        return (T) this;
    }
    
    /**
     * 是否使用按键的“按下"状态来判断事件是否匹配。
     * @param <T>
     * @param usePressed
     * @return 返回当前EventMapping
     */
    public <T extends JmeKeyMapping> T setUsePressed(boolean usePressed) {
        this.usePressed = usePressed;
        return (T) this;
    }
    
    /**
     * 设置一个按键的有效匹配时间，默认情况下，对于使用"按下(usePressed=true)"的按键匹配，当按键按下后匹配结果始终为true,
     * 而对于"弹起(usePressed=false)"匹配类型的按键，当按键弹起后匹配结果为true,则需要有一个匹配结果的有效时间限制，
     * 默认情况下为50秒毫秒。
     * @param timeInMilli “弹起”类型匹配的有效匹配结果时间限制，单位毫秒。 
     */
    public void setValidTime(float timeInMilli) {
        this.validTime = timeInMilli;
    }

    /**
     * 判断是否使用按键的”按下“状态作为匹配判断。
     * @return 
     */
    public boolean isUsePressed() {
        return usePressed;
    }

    /**
     * 针对Axis类型，判断是否使用Negative匹配
     * @return 
     */
    public boolean isNegative() {
        return negative;
    }

    /**
     * 获取按键类型
     * @return 
     */
    public Type getType() {
        return type;
    }

    /**
     * 获取按键值
     * @return 
     */
    public int getCode() {
        return code;
    }
    
    /**
     * 添加一个按键匹配侦听器
     * @param listener 
     */
    @Override
    public void addListener(KeyMappingListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener); 
        }
    }
    
    /**
     * 移除指定的事件侦听器
     * @param listener
     * @return true如果侦听器存在
     */
    @Override
    public boolean removeListener(KeyMappingListener listener) {
        return listeners.remove(listener);
    }
    
    @Override
    public String toString() {
        return "type=" + type + ", code=" + code + ", usePressed=" + usePressed;
    }
    
    private void bindListener() {
        // delete old
        inputManager.deleteMapping(mappingName);
        
        // bind new
        switch (type) {
            case key:
                inputManager.addMapping(mappingName, new KeyTrigger(code));
                break;
            case button:
                inputManager.addMapping(mappingName, new MouseButtonTrigger(code));
                break;
            case axis:
                inputManager.addMapping(mappingName, new MouseAxisTrigger(code, negative));
                break;
            default:
                throw new UnsupportedOperationException("Unsupported event type=" + type + ", eventMapping=" + this);
        }
        inputManager.addListener(actionListener, mappingName);
    }
    
    private void onActionMappingCheck(String name, boolean isPressed, float tpf) {
        // 判断是否匹配（name不需要判断，因为name是唯一的）
        this.mappingResult = usePressed == isPressed;
        this.lastMappingTime = System.currentTimeMillis();
//        LOG.log(Level.INFO, "KeyMapping trigger, name={0}, result={1}", new Object[]{name, mappingResult});
        // 事件响应
        for (KeyMappingListener el : listeners) {
            el.onKeyMapping(this);
        }
    }
    
    private class MappingActionListener implements ActionListener {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            onActionMappingCheck(name, isPressed, tpf);
        }
    }
}
