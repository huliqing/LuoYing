/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.task;

import com.jme3.font.BitmapFont;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.InterfaceConstants;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.data.TaskData;
import name.huliqing.core.mvc.network.ActorNetwork;
import name.huliqing.core.mvc.network.ProtoNetwork;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.ProtoService;
import name.huliqing.core.view.IconLabel;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.ui.LinearLayout;
import name.huliqing.core.ui.Text;
import name.huliqing.core.ui.UI;
import name.huliqing.core.ui.UIFactory;
import name.huliqing.core.ui.Window;
import name.huliqing.core.utils.ConvertUtils;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractTask<T extends TaskData> implements Task<T> {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ProtoService protoService = Factory.get(ProtoService.class);
    private final ActorNetwork atorNetwork = Factory.get(ActorNetwork.class);
    private final ProtoNetwork protoNetwork = Factory.get(ProtoNetwork.class);

    protected T data;
    protected Actor actor;
    
    protected int rewardExp;
    protected List<RewardItem> rewardItems;
    
    // ---- inner
    protected Window detailWin;

    @Override
    public void setData(T data) {
        this.data = data;
        this.rewardExp = data.getAsInteger("rewardExp", 0);
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
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setActor(Actor actor) {
        this.actor = actor;
    }

    @Override
    public Actor getActor() {
        return actor;
    }

    @Override
    public String getId() {
        return data.getId();
    }

    @Override
    public void doCompletion() {
        // 奖励经验 
        if (rewardExp > 0) {
            atorNetwork.applyXp(actor, rewardExp);
        }
        // 奖励物品
        if (rewardItems != null) {
            for (RewardItem ri : rewardItems) {
                protoNetwork.addData(actor, protoService.createData(ri.itemId), ri.count);
            }
        }
    }

    @Override
    public Window getTaskDetail() {
        if (detailWin == null) {
            detailWin = new DetailWindow(playService.getScreenWidth() * 0.5f
                    , playService.getScreenHeight() * 0.4f);
            detailWin.setCloseable(true);
            detailWin.resize();
            detailWin.setToCorner(UI.Corner.CC);
        }
        return detailWin;
    }

    // 奖励的物品及数量
    protected class RewardItem {
        String itemId;
        int count;
    }
    
    private class DetailWindow extends Window {
        // 任务说明
        private final Text taskDetail;
        // 任务奖励：列出任务完成后的奖励物品列表
        private final LinearLayout taskRewardPanel;
        private final Text rewardHead;
        
        public DetailWindow(float width, float height) {
            super(width, height);
            setTitle(ResourceManager.get(ResConstants.TASK_TASK) + "-" + ResourceManager.getObjectName(data.getId()));
            setPadding(10, 10, 10, 10);
            
            float cw = getContentWidth();
            float ch = getContentHeight();
            float panelHeight = UIFactory.getUIConfig().getTitleHeight();
            taskDetail = new Text(ResourceManager.getObjectDes(data.getId()));
            taskDetail.setWidth(cw);
//            taskDetail.setHeight(ch - panelHeight);// 不要固定高度
            taskDetail.updateView();
            taskDetail.resize(); // 缩小宽度
            
            // 列出要奖励的东西
            taskRewardPanel = new LinearLayout(cw, panelHeight);
            taskRewardPanel.setLayout(Layout.horizontal);
            
            rewardHead = new Text(ResourceManager.get(ResConstants.TASK_REWARD) + ":  ");
            rewardHead.setHeight(panelHeight);
            rewardHead.setVerticalAlignment(BitmapFont.VAlign.Center);
            taskRewardPanel.addView(rewardHead);
            
            if (rewardExp > 0) {
                IconLabel il = new IconLabel("_EXP_", InterfaceConstants.ITEM_EXP, rewardExp + "");
                taskRewardPanel.addView(il);
            }
            
            if (rewardItems != null) {
                for (RewardItem ri : rewardItems) {
                    IconLabel il = new IconLabel(ri.itemId
                            , ((ObjectData)DataFactory.createData(ri.itemId)).getIcon()
                            , ri.count + "");
                    taskRewardPanel.addView(il);
                }
            }
            
            List<UI> cuis = taskRewardPanel.getViews();
            if (cuis.size() > 1) {
                float iconLabelWidth = (cw - rewardHead.getWidth()) / (cuis.size() - 1);
                IconLabel temp;
                for (UI ui : cuis) {
                    if (ui instanceof IconLabel) {
                        temp = (IconLabel) ui;
                        temp.setWidth(iconLabelWidth);
                        temp.setHeight(panelHeight);
                    }
                }
            }
            
            addView(taskDetail);
            addView(taskRewardPanel);
        }
    }
}
