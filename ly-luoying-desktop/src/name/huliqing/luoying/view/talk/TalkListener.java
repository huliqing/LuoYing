/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.view.talk;

/**
 *
 * @author huliqing
 */
public interface TalkListener {
    
    /**
     * 当谈话结束时,该方法会在cleanup之前调用。
     */
    void onTalkEnd();
    
}
