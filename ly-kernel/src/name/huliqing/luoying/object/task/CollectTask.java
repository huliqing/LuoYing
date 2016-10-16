/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.task;

import com.jme3.math.FastMath;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.TaskData;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.network.TaskNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.TaskService;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ActorListener;
import name.huliqing.luoying.utils.ConvertUtils;
import name.huliqing.luoying.utils.MathUtils;

/**
 * 收集类任务, 任务执行方式：
 * 1.打死指定的任务目标角色; 2.从任务目标身上掉落任务物品; 3.物品收集完毕后
 * 回去向指定NPC角色交任务。
 * @author huliqing
 * @param <T>
 */
public class CollectTask<T extends TaskData> extends AbstractTask<T> implements ActorListener {
    private final ActorService actorService = Factory.get(ActorService.class);
    private final TaskService taskService = Factory.get(TaskService.class);
    private final TaskNetwork taskNetwork = Factory.get(TaskNetwork.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);

    // 需要收集的物品
    private List<ItemWrap> items;
    // 任务目标角色id，只有打死这些角色才可能收集到物品
    private List<String> targets;
    // 物品的掉落率
    private float dropFactor = 0.5f;
    
    // ---- inner

// remove20161010    
// 显示任务目标
//    private GoldPanel goldPanel;
    
    // 标记是否已经收集完毕
    private boolean collected;

    @Override
    public void setData(T data) {
        super.setData(data);
        // 格式，"item1|count1,item2|count2,...",
        String[] itemsArr = data.getAsArray("items");
        items = new ArrayList<ItemWrap>(itemsArr.length);
        for (int i = 0; i < itemsArr.length; i++) {
            String[] itemArr  = itemsArr[i].split("\\|");
            ItemWrap iw = new ItemWrap();
            iw.itemId = itemArr[0];
            iw.total = itemArr.length > 1 ? ConvertUtils.toInteger(itemArr[1], 1) : 1;
            items.add(iw);
        }
        targets = data.getAsStringList("targets");
        dropFactor = MathUtils.clamp(data.getAsFloat("dropFactor", dropFactor), 0f, 1f);
    }

    @Override
    public void initialize() {
        // 监听当前角色的行为,当角色杀怪时判断打死的是不是任务指定的目标NPC，以便
        // 给角色掉落任务物品
        // 不要使用与普通掉落物品一样的方式去掉落任务物品，因为任务物品只有在接
        // 受了任务之后才会对任务执行者掉落物品.并且任务物品的收集方式与普通物品
        // 不太一样，因任务物品不可使用、删除、出售等，所以任务物品不会保存在普通
        // 角色包裹中。
        actorService.addActorListener(actor, this);
    }

    @Override
    public boolean checkCompletion() {
        // 如果有任何一件任务物品收集未完，则判断任务为“未完成”
        for (ItemWrap item : items) {
            if (taskService.getItemTotal(actor, this, item.itemId) < item.total) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void doCompletion() {
        // 扣减任务物品数量
        for (ItemWrap item : items) {
            taskService.applyItem(actor, this, item.itemId, -item.total);
        }
        super.doCompletion();
    }

    @Override
    public void cleanup() {
        actorService.removeActorListener(actor, this);
    }

    // remove20161010
//    @Override
//    public Window getTaskDetail() {
//        Window win = super.getTaskDetail();
//        // 如果是已经完成过的任务则不显示进度
//        if (data.isCompletion()) {
//            if (goldPanel != null) {
//                goldPanel.setVisible(false);
//                win.resize();
//            }
//            return win;
//        }
//        
//        // 展示任务进度
//        if (goldPanel == null) {
//            goldPanel = new GoldPanel(win.getContentWidth(), UIFactory.getUIConfig().getTitleHeight());
//            win.addView(goldPanel);
//            win.resize();
//            win.setToCorner(UI.Corner.CC);
//        }
//        goldPanel.update(actor);
//        
//        return win;
//    }

    // ignore
    @Override
    public void onActorTargetLocked(Entity source, Entity other) {}
    
    // ignore
    @Override
    public void onActorTargetReleased(Entity source, Entity other) {}

    // ignore
    @Override
    public void onActorHitByTarget(Entity source, Entity attacker, String hitAttribute, float hitValue, boolean killedByHit) {}

    @Override
    public void onActorHitTarget(Entity sourceHitter, Entity beHit, String hitAttribute, float hitValue, boolean killedByHit) {
        // 几种情况可以不再需要“收集任务物品”的逻辑
        // 1.如果任务已经完成并且已经提交
        // 2.如果任务已经收集完毕但未提交
        // 3.如果目标不是被当前角色杀死的
        if (data.isCompletion() || collected || !killedByHit)
            return;
        
        // 如果打死的目标不是指定的任务目标则不处理
        String targetId = beHit.getData().getId();
        if (targets == null || !targets.contains(targetId)) {
            return;
        }
        
        // 根据机率掉落物品给当前任务执行者。
        boolean checkCollected = true; // 判断是否已经收集完
        int tempItemTotal;
        for (ItemWrap item : items) {
            // 收集完了的物品不再掉落
            tempItemTotal = taskService.getItemTotal(actor, this, item.itemId);
            if (tempItemTotal >= item.total) {
                continue;
            }
            // 按机率掉落
            if (dropFactor >= FastMath.nextRandomFloat()) {
                // 注:因为这里使用的是随机数，确定是否获得任务物品必须由服务端统一判断，
                // 所以这里必须使用taskNetwork.applyItem(...)而不是taskService.applyItem(...)
                taskNetwork.applyItem(actor, this, item.itemId, 1);
                tempItemTotal++;
   
                // remove20161010,需要重构
//                // 获得任务物品的提示,在本地提示就可以,因为只需要本地player知道获得物品就行,
//                // 不需要让其它角色的玩家知道
//                StringBuilder sb = new StringBuilder(24);
//                sb.append(ResourceManager.get(ResConstants.TASK_GET_TASK_ITEM))
//                        .append(":").append(ResourceManager.getObjectName(item.itemId))
//                        .append("(").append(tempItemTotal).append("/").append(item.total).append(")");
//                playNetwork.addMessage(actor, sb.toString(), MessageType.itemTask);
                
            }
            
            // 只要有一类物品还没有收集完，则要标记为“未完成”
            if (tempItemTotal < item.total) {
                checkCollected = false;
            }
        }
        
        // 收集完毕给予提示
        collected = checkCollected;
        if (collected) {
            
            // remove20161011
//            playNetwork.addMessage(actor, ResourceManager.get(ResConstants.TASK_SUCCESS)
//                        + ": " + ResourceManager.getObjectName(data.getId())
//                        , MessageType.notice);

        }
   
        // remove20161010
//        // 更新任务详情面板
//        if (goldPanel != null && goldPanel.isVisible()) {
//            goldPanel.update(actor);
//        }
    }
    
    private class ItemWrap {
        /** 需要收集的物品的ID */
        String itemId;
        
        /** 需要收集的物品总量 */
        int total;
    }
    
    // remove20161010
//    // 任务目标，列出需要收集的物品列表和收集进度
//    private class GoldPanel extends LinearLayout {
//        // 任务说明
//        private Text label;
//        
//        public GoldPanel(float width, float height) {
//            super(width, height);
//            setLayout(Layout.horizontal);
//            setBackground(UIFactory.getUIConfig().getBackground(), true);
//            setBackgroundColor(UIFactory.getUIConfig().getDesColor(), true);
//            label = new Text(ResourceManager.get(ResConstants.TASK_PROGRESS) + ": ");
//            label.setHeight(height);
//            label.setVerticalAlignment(BitmapFont.VAlign.Center);
//            addView(label);
//            
//            // 目标：列出需要收集的物品及数量
//            float ilWidth = (width - label.getWidth()) / items.size();
//            for (ItemWrap iw : items) {
//                IconLabel<ItemWrap> il = new IconLabel<ItemWrap>(iw
//                        , ((ObjectData)DataFactory.createData(iw.itemId)).getIcon()
//                        , "0/" + iw.total);
//                il.setWidth(ilWidth);
//                il.setHeight(height);
//                addView(il);
//            }
//        }
//        
//        // 更新所收集物品的进度
//        void update(Entity actor) {
//            IconLabel<ItemWrap> temp;
//            for (UI ui : getViews()) {
//                if (ui instanceof IconLabel) {
//                    temp = (IconLabel) ui;
//                    temp.setLabel(taskService.getItemTotal(actor, CollectTask.this, temp.getId().itemId)
//                            + "/" + temp.getId().total);
//                }
//            }
//        }
//    }
   
}
