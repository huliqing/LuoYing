/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.state;

import com.jme3.app.Application;
import java.util.List;
import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.data.StateData;
import name.huliqing.core.mvc.service.StateService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.StateListener;

/**
 * 这类状态可以用来清除角色身上的一些其它状态,当状态在运行时会不停的检查角色当前的状态
 * 列表，并偿试清除指定的状态，甚至清除自身状态。可用用来作为一些净化类的技能使用。
 * @author huliqing
 */
public class CleanState extends State implements StateListener {
//    private static final Logger LOG = Logger.getLogger(CleanState.class.getName());
    
    private final StateService stateService = Factory.get(StateService.class);

    private List<String> states;
    
    @Override
    public void setData(StateData data) {
        super.setData(data); 
        states = data.getAsList("states");
    }

    @Override
    public void initialize(Application app) {
        super.initialize(app);
        // 添加侦听器，以便侦听角色状态的变更。
        stateService.addListener(actor, this);
        
        // 立即进行一次清理,如果states中包含当前状态的id,则可能也会把自身清理掉。
        // 那么状态所设置的时间就无意义了。不过这对于一些只要立即清理一次就可以的
        // 状态效果来说是有意义的,即不需要持续的清理。
        
        // 注：如果doCleanStates中包含清理当前状态，则要注意并避免：
        // java.util.ConcurrentModificationException异常,因这会导致当前方法又回调
        // 到cleanup方法,引起StateListener的ConcurrentModification问题。这个问题应该在StateService处避免。
        doCleanStates();
    }

    @Override
    public void cleanup() {
        // 注意状态在销毁时要从角色身上移除当前侦听器。
        stateService.removeListener(actor, this);
        super.cleanup(); 
    }

    @Override
    public void onStateAdded(Actor source, State stateAdded) {
        // 当检查到新添加的状态刚好是清除列表中的状态时，则立即清除掉。
        String sid = stateAdded.getData().getId();
        if (states != null && states.contains(sid)) {
            stateService.removeState(actor, sid);
        }
    }

    @Override
    public void onStateRemoved(Actor source, State stateRemoved) {
        // ignore
    }
    
    private void doCleanStates() {
        if (states != null) {
            for (String s : states) {
                stateService.removeState(actor, s);
            }
        }
    }
}
