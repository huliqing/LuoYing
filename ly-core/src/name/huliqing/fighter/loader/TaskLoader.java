/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import name.huliqing.fighter.data.TaskData;
import name.huliqing.fighter.object.task.CollectTask;
import name.huliqing.fighter.object.task.Task;

/**
 * @author huliqing
 */
class TaskLoader {
    
    public static Task load(TaskData data) {
        String tagName = data.getTagName();
        if (tagName.equals("taskCollect")) {
            return new CollectTask(data);
        }
        
        throw new UnsupportedOperationException("Unknow tagName=" + tagName);
    }
}
