/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.events;

/**
 *
 * @author huliqing
 */
public interface EventListener {
    
    /**
     * 当事件发生时该方法被调用，这表示EventMapping所绑定的键发生了事件，在键按下或弹起时该事件都会触发，
     * 需要通过{@link Event#isMatch() }来判断是否匹配。
     * @param e 
     */
    void onEvent(Event e);
}
