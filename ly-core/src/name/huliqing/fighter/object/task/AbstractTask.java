/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.task;

import com.jme3.font.BitmapFont;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.InterfaceConstants;
import name.huliqing.fighter.constants.ResConstants;
import name.huliqing.fighter.object.DataLoaderFactory;
import name.huliqing.fighter.data.TaskData;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.network.ItemNetwork;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.view.tiles.IconLabel;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.ui.LinearLayout;
import name.huliqing.fighter.ui.Text;
import name.huliqing.fighter.ui.UI;
import name.huliqing.fighter.ui.UIFactory;
import name.huliqing.fighter.ui.Window;
import name.huliqing.fighter.utils.ConvertUtils;

/**
 *
 * @author huliqing
 */
public abstract class AbstractTask implements Task {
    private final static PlayService playService = Factory.get(PlayService.class);
//    private final static ActorService actorService = Factory.get(ActorService.class);
    private final static ActorNetwork atorNetwork = Factory.get(ActorNetwork.class);
    private final static ItemNetwork itemNetwork = Factory.get(ItemNetwork.class);

    protected TaskData data;
    protected Actor actor;
    
    protected int rewardExp;
    protected RewardItem[] rewardItems;
    
    // ---- inner
    protected Window detailWin;
    
    public AbstractTask(TaskData data) {
        this.data = data;
        this.rewardExp = data.getAsInteger("rewardExp", 0);
        // "item1|count1,item2|count2,..."
        String[] rewardItemsArr = data.getAsArray("rewardItems");
        if (rewardItemsArr != null) {
            rewardItems = new RewardItem[rewardItemsArr.length];
            for (int i = 0; i < rewardItemsArr.length; i++) {
                String[] tempArr = rewardItemsArr[i].split("\\|");
                RewardItem ri = new RewardItem();
                ri.itemId = tempArr[0];
                ri.count = tempArr.length > 1 ? ConvertUtils.toInteger(tempArr[1], 1) : 1;
                rewardItems[i] = ri;
            }
        }
    }

    @Override
    public TaskData getData() {
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
                itemNetwork.addItem(actor, ri.itemId, ri.count);
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
    private class RewardItem {
        String itemId;
        int count;
    }
    
    private class DetailWindow extends Window {
        // 任务说明
        private Text taskDetail;
        // 任务奖励：列出任务完成后的奖励物品列表
        private LinearLayout taskRewardPanel;
        private Text rewardHead;
        
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
                            , DataLoaderFactory.createData(ri.itemId).getIcon()
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
