/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view.talk;

/**
 * 处理谈话中的延迟
 * @author huliqing
 */
public class TalkLogicDelay extends AbstractTalkLogic{
    
    public TalkLogicDelay(float delay) {
        setUseTime(delay);
    }

    @Override
    protected void doInit() {
        // nothing
    }
    
    @Override
    protected void doTalkLogic(float tpf) {
        // ignore
    }
    
}
