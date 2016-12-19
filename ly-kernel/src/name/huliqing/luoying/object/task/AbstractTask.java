/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.task;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.TaskData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.utils.ConvertUtils;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractTask<T extends TaskData> implements Task<T> {
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);

    protected T data;
    protected Entity actor;
    
    protected List<RewardItem> rewardItems;
    protected List<RewardAttribute> rewardAttributes;
    
    // ---- inner
    protected boolean initialized;

    @Override
    public void setData(T data) {
        this.data = data;
        
        // "item1|count1,item2|count2,..."
        String[] rewardItemsArr = data.getAsArray("rewardItems");
        if (rewardItemsArr != null) {
            rewardItems = new ArrayList<RewardItem>(rewardItemsArr.length);
            for (int i = 0; i < rewardItemsArr.length; i++) {
                String[] tempArr = rewardItemsArr[i].split("\\|");
                RewardItem ri = new RewardItem();
                ri.itemId = tempArr[0];
                ri.count = tempArr.length > 1 ? ConvertUtils.toInteger(tempArr[1], 1) : 1;
                rewardItems.add(ri);
            }
        }
        
        // 格式, "attributeName1|addValue1,attributeName2|addValue2,...", 
        String[] rewardAttributesArr = data.getAsArray("rewardAttributes");
        if (rewardAttributesArr != null) {
            rewardAttributes = new ArrayList<RewardAttribute>(rewardAttributesArr.length);
            for (int i = 0; i < rewardAttributesArr.length; i++) {
                String[] tempArr = rewardAttributesArr[i].split("\\|");
                RewardAttribute ri = new RewardAttribute();
                ri.attributeName = tempArr[0];
                ri.value = tempArr.length > 1 ? ConvertUtils.toFloat(tempArr[1], 1) : 1;
                rewardAttributes.add(ri);
            }
        }
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
    }

    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("Task already initialized! taskId=" + getId() + ", actorId=" + actor.getData().getId());
        }
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        initialized = false;
    }

    @Override
    public void setActor(Entity actor) {
        this.actor = actor;
    }

    @Override
    public Entity getActor() {
        return actor;
    }

    @Override
    public String getId() {
        return data.getId();
    }

    @Override
    public void doCompletion() {
        if (data.isCompletion())
            return;

        // 奖励属性值
        if (rewardAttributes != null) {
            for (RewardAttribute ra : rewardAttributes) {
                entityNetwork.hitNumberAttribute(actor, ra.attributeName, ra.value, null);
            }
        }
        // 奖励物品
        if (rewardItems != null) {
            for (RewardItem ri : rewardItems) {
                entityNetwork.addObjectData(actor, Loader.loadData(ri.itemId), ri.count);
            }
        }
        // 标记为“完结”
        data.setCompletion(true);
    }

    // 奖励的物品及数量
    protected class RewardItem {
        String itemId;
        int count;
    }
    
    // 奖励到属性上
    protected class RewardAttribute {
        String attributeName;
        float value;
    }
   
}
