/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data;

import name.huliqing.luoying.xml.ObjectData;
import com.jme3.network.serializing.Serializable;

/**
 * 任务数据
 * @author huliqing
 */
@Serializable
public class TaskData extends ObjectData {
    
    /**
     * 判断任务是否执行完成,完成即表示任务达成目标，中断或放弃不算完成。
     * @return 
     */
    public boolean isCompletion() {
        return getAsBoolean("completion", false);
    }

    /**
     * 设置任务“完成”状态
     * @param completion 
     */
    public void setCompletion(boolean completion) {
        this.setAttribute("completion", completion);
    }
    
}
