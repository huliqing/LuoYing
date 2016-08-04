/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;

/**
 * 任务数据
 * @author huliqing
 */
@Serializable
public class TaskData extends ObjectData {
    // 这个ID前缀用于标记任务物品ID,角色完成的任务物品数量并不是存放在角色的包裹里面
    // 而是放在TaskData里。因为任务物品比较特殊，不能出售、不能销毁和使用.
    // 并且可能一些不同的任务会需要收集相同的任务物品（物品ID相同）。所以收集到的任务
    // 物品应该在不同任务之间区分开来。
    private final static String TASK_ITEM_PREFIX = "_T_ITEM_";
    
    // remove20160322
//    // 任务的发起者ID
//    private String startActorId;
//    // 任务的结束者的ID
//    private String endActorId;
    
    // 判断任务是否执行完成
    private boolean completion;
    
    public TaskData(){super();}
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        
        // remove20160322
//        oc.write(startActorId, "startActorId", null);
//        oc.write(endActorId, "endActorId", null);
        
        oc.write(completion, "completion", false);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        
        // remove20160322
//        startActorId = ic.readString("startActorId", null);
//        endActorId = ic.readString("endActorId", null);
        
        completion = ic.readBoolean("completion", false);
    }
    
    /**
     * 判断任务是否执行完成,完成即表示任务达成目标，中断或放弃不算完成。
     * @return 
     */
    public boolean isCompletion() {
        return completion;
    }

    /**
     * 设置任务“完成”状态
     * @param completion 
     */
    public void setCompletion(boolean completion) {
        this.completion = completion;
    }
    
    /**
     * 增加或减少获得的任务物品数量，
     * @param itemId 任务物品ID
     * @param amount 可正可负
     */
    public void applyTaskItem(String itemId, int amount) {
        String tid = makeId(itemId);
        int total = getAsInteger(tid, 0);
        total += amount;
        setAttribute(tid, total);
    }
    
    /**
     * 已经获得的任务物品数量
     * @param itemId
     * @return 
     */
    public int getTaskItemTotal(String itemId) {
        return getAsInteger(makeId(itemId), 0);
    }
    
    private String makeId(String itemId) {
        return TASK_ITEM_PREFIX + itemId;
    }
}
