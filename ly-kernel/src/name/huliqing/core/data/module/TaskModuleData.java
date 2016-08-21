/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data.module;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.TaskData;

/**
 *
 * @author huliqing
 */
@Serializable
public class TaskModuleData extends ModuleData {
    
//    private List<TaskData> taskDatas;
//
//    public List<TaskData> getTaskDatas() {
//        return taskDatas;
//    }
//
//    public void setTaskDatas(List<TaskData> taskDatas) {
//        this.taskDatas = taskDatas;
//    }
//    
//    @Override
//    public void write(JmeExporter ex) throws IOException {
//        super.write(ex);
//        OutputCapsule oc = ex.getCapsule(this);
//        if (taskDatas != null) {
//            oc.writeSavableArrayList(new ArrayList<TaskData>(taskDatas), "taskDatas", null);
//        }
//    }
//
//    @Override
//    public void read(JmeImporter im) throws IOException {
//        super.read(im);
//        InputCapsule ic = im.getCapsule(this);
//        taskDatas = ic.readSavableArrayList("taskDatas", null);
//    }
    
    
}
