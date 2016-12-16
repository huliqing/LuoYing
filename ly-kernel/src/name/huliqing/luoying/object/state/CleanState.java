/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.state;

import java.util.List;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.object.entity.EntityDataListener;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 这类状态可以用来清除角色身上的一些其它状态,当状态在运行时会不停的检查角色当前的状态
 * 列表，并偿试清除指定的状态，甚至清除自身状态。可用用来作为一些净化类的技能使用。
 * @author huliqing
 */
public class CleanState extends AbstractState implements EntityDataListener {
//    private static final Logger LOG = Logger.getLogger(CleanState.class.getName());
    
    // 要清理的状态ID列表
    private List<String> states;
    
    @Override
    public void setData(StateData data) {
        super.setData(data); 
        states = data.getAsStringList("states");
    }
    
    @Override
    public void initialize() {
        super.initialize();
        
        // 立即进行一次清理,如果states中包含当前状态的id,则可能也会把自身清理掉。
        // 那么状态所设置的时间就无意义了。不过这对于一些只要立即清理一次就可以的
        // 状态效果来说是有意义的,即不需要持续的清理。
        doCleanStates();
        
        entity.addEntityDataListener(this);
    }

    @Override
    public void cleanup() {
        entity.removeEntityDataListener(this);
        super.cleanup(); 
    }
    
    private void doCleanStates() {
        if (states != null) {
            for (String s : states) {
                StateData state = entity.getData().getObjectData(s);
                if (state != null) {
                    entity.removeObjectData(state, 1);
                }
            }
        }
    }

    @Override
    public void onDataAdded(ObjectData data, int amount) {
        // 当检查到新添加的状态刚好是清除列表中的状态时，则立即清除掉。
        if (data instanceof StateData) {
            if (states != null && states.contains(data.getId())) {
                entity.removeObjectData(data, 1);
            }
        }
    }
    
    @Override
    public final void onDataRemoved(ObjectData data, int amount) {
        // ignore
    }
    
    @Override
    public final void onDataUsed(ObjectData data) {
        // ignore
    }
}
