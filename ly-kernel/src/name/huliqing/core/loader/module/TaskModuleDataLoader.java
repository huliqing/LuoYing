/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader.module;

import java.util.ArrayList;
import name.huliqing.core.data.TaskData;
import name.huliqing.core.data.module.TaskModuleData;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.xml.DataLoader;
import name.huliqing.core.xml.Proto;

/**
 * @author huliqing
 */
public class TaskModuleDataLoader implements DataLoader<TaskModuleData> {

    @Override
    public void load(Proto proto, TaskModuleData data) {
//        String[] taskArr = proto.getAsArray("tasks");
//        if (taskArr != null && taskArr.length > 0) {
//            data.setTaskDatas(new ArrayList<TaskData>(taskArr.length));
//            for (String taskId : taskArr) {
//                data.getTaskDatas().add((TaskData) DataFactory.createData(taskId));
//            }
//        }
    }

}
