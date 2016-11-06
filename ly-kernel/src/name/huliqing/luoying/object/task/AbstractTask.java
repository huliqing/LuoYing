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
import name.huliqing.luoying.ui.Window;
import name.huliqing.luoying.utils.ConvertUtils;
import name.huliqing.luoying.layer.network.ObjectNetwork;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractTask<T extends TaskData> implements Task<T> {
    private final ObjectNetwork protoNetwork = Factory.get(ObjectNetwork.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);

    protected T data;
    protected Entity actor;
    
    protected List<RewardItem> rewardItems;
    protected List<RewardAttribute> rewardAttributes;
    
    // ---- inner
//    protected Window detailWin;
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
                entityNetwork.hitAttribute(actor, ra.attributeName, ra.value, null);
            }
        }
        // 奖励物品
        if (rewardItems != null) {
            for (RewardItem ri : rewardItems) {
                protoNetwork.addData(actor, ri.itemId, ri.count);
            }
        }
        // 标记为“完结”
        data.setCompletion(true);
    }

    // remove20161010
//    @Override
//    public Window getTaskDetail() {
//        if (detailWin == null) {
//            detailWin = new DetailWindow(playService.getScreenWidth() * 0.5f
//                    , playService.getScreenHeight() * 0.4f);
//            detailWin.setCloseable(true);
//            detailWin.resize();
//            detailWin.setToCorner(UI.Corner.CC);
//        }
//        return detailWin;
//    }

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
    
    // remove20161010
//    private class DetailWindow extends Window {
//        // 任务说明
//        private final Text taskDetail;
//        // 任务奖励：列出任务完成后的奖励物品列表
//        private final LinearLayout taskRewardPanel;
//        private final Text rewardHead;
//        
//        public DetailWindow(float width, float height) {
//            super(width, height);
//            setTitle(ResourceManager.get(ResConstants.TASK_TASK) + "-" + ResourceManager.getObjectName(data.getId()));
//            setPadding(10, 10, 10, 10);
//            
//            float cw = getContentWidth();
//            float ch = getContentHeight();
//            float panelHeight = UIFactory.getUIConfig().getTitleHeight();
//            taskDetail = new Text(ResourceManager.getObjectDes(data.getId()));
//            taskDetail.setWidth(cw);
////            taskDetail.setHeight(ch - panelHeight);// 不要固定高度
//            taskDetail.updateView();
//            taskDetail.resize(); // 缩小宽度
//            
//            // 列出要奖励的东西
//            taskRewardPanel = new LinearLayout(cw, panelHeight);
//            taskRewardPanel.setLayout(Layout.horizontal);
//            
//            rewardHead = new Text(ResourceManager.get(ResConstants.TASK_REWARD) + ":  ");
//            rewardHead.setHeight(panelHeight);
//            rewardHead.setVerticalAlignment(BitmapFont.VAlign.Center);
//            taskRewardPanel.addView(rewardHead);
//            
//            // remove20160829
////            if (rewardExp > 0) {
////                IconLabel il = new IconLabel("_EXP_", InterfaceConstants.ITEM_EXP, rewardExp + "");
////                taskRewardPanel.addView(il);
////            }
//
//            if (rewardAttributes != null && !rewardAttributes.isEmpty()) {
//                for (RewardAttribute ra : rewardAttributes) {
//                    IconLabel label = new IconLabel(ra.attributeName
//                            ,  ((ObjectData)DataFactory.createData(ra.attributeName)).getIcon()
//                            , ra.value + "");
//                    taskRewardPanel.addView(label);
//                }
//            }
//            
//            if (rewardItems != null) {
//                for (RewardItem ri : rewardItems) {
//                    IconLabel label = new IconLabel(ri.itemId
//                            , ((ObjectData)DataFactory.createData(ri.itemId)).getIcon()
//                            , ri.count + "");
//                    taskRewardPanel.addView(label);
//                }
//            }
//            
//            List<UI> cuis = taskRewardPanel.getViews();
//            if (cuis.size() > 1) {
//                float iconLabelWidth = (cw - rewardHead.getWidth()) / (cuis.size() - 1);
//                IconLabel temp;
//                for (UI ui : cuis) {
//                    if (ui instanceof IconLabel) {
//                        temp = (IconLabel) ui;
//                        temp.setWidth(iconLabelWidth);
//                        temp.setHeight(panelHeight);
//                    }
//                }
//            }
//            
//            addView(taskDetail);
//            addView(taskRewardPanel);
//        }
//    }
}
