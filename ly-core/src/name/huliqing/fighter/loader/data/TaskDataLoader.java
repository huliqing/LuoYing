/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader.data;

import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.data.TaskData;

/**
 *
 * @author huliqing
 */
public class TaskDataLoader implements DataLoader<TaskData>{

    @Override
    public TaskData loadData(Proto proto) {
        TaskData data = new TaskData(proto.getId());
        return data;
    }
    
}
