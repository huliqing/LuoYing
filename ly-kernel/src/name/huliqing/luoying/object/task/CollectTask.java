/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.object.task;

import com.jme3.math.FastMath;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.data.TaskData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ActorListener;
import name.huliqing.luoying.object.module.ActorModule;
import name.huliqing.luoying.utils.ConvertUtils;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 收集类任务, 任务执行方式：
 * 1.打死指定的任务目标角色; 2.从任务目标身上掉落任务物品; 3.物品收集完毕后
 * 回去向指定NPC角色交任务。
 * @author huliqing
 * @param <T>
 */
public class CollectTask<T extends TaskData> extends AbstractTask<T> implements ActorListener {
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    private final EntityService entityService = Factory.get(EntityService.class);
    private ActorModule actorModule;
    
    // 需要收集的物品
    private List<ItemWrap> items;
    // 任务目标角色id，只有打死这些角色才可能收集到物品
    private List<String> targets;
    // 物品的掉落率
    private float dropFactor = 0.5f;
    
    // ---- inner
    
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
        super.initialize();
        // 监听当前角色的行为,当角色杀怪时判断打死的是不是任务指定的目标NPC，以便
        // 给角色掉落任务物品
        // 不要使用与普通掉落物品一样的方式去掉落任务物品，因为任务物品只有在接
        // 受了任务之后才会对任务执行者掉落物品.
        actorModule = actor.getModule(ActorModule.class);
        if (actorModule != null) {
            actorModule.addActorListener(this);
        } 
    }
    
    @Override
    public void cleanup() {
        if (actorModule != null) {
            actorModule.removeActorListener(this);
        }
        super.cleanup();
    }

    @Override
    public boolean checkCompletion() {
        // 如果有任何一件任务物品收集未完，则判断任务为“未完成”
        for (ItemWrap item : items) {
            ObjectData od = actor.getData().getObjectData(item.itemId);
            if (!(od instanceof ItemData)) {
                return false;
            }
            if (((ItemData) od).getTotal() < item.total) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void doCompletion() {
        // 扣减任务物品数量
        for (ItemWrap item : items) {
            ObjectData od = actor.getData().getObjectData(item.itemId);
            if (od != null) {
                entityService.removeObjectData(actor, od.getUniqueId(), item.total);
            }
        }
        if (actorModule != null) {
            actorModule.removeActorListener(this);
        }
        super.doCompletion();
    }
    
    @Override
    public void onActorHitTarget(Entity sourceHitter, Entity beHit, Attribute hitAttribute, Object hitValue, Object oldValue, boolean killed) {
        // 几种情况可以不再需要“收集任务物品”的逻辑
        // 1.如果任务已经完成并且已经提交
        // 2.如果任务已经收集完毕但未提交
        // 3.如果目标不是被当前角色杀死的
        if (collected || !killed || data.isCompletion())
            return;
        
        // 如果打死的目标不是指定的任务目标则不处理
        String targetId = beHit.getData().getId();
        if (targets == null || !targets.contains(targetId)) {
            return;
        }
        
        // 根据机率掉落物品给当前任务执行者。
        boolean checkCollected = true; // 判断是否已经收集完
        int tempItemTotal;
        ObjectData od;
        for (ItemWrap item : items) {
            od = entityService.getObjectDataByTypeId(actor, item.itemId);
            if (od instanceof ItemData) {
                tempItemTotal = ((ItemData) od).getTotal();
            } else {
                tempItemTotal = 0;
            }
            // 如果本件物品收集完了则路过,不再掉落。
            if (tempItemTotal >= item.total) {
                continue;
            }
            // 按机率掉落
            if (dropFactor >= FastMath.nextRandomFloat()) {
                entityNetwork.addObjectData(actor, Loader.loadData(item.itemId), 1);
                tempItemTotal++;
            }
            // 只要有一类物品还没有收集完，则要标记为“未完成”
            if (tempItemTotal < item.total) {
                checkCollected = false;
            }
        }
        
        // 收集完毕给予提示
        collected = checkCollected;
    }
    
    // ignore
    @Override
    public void onActorHitByTarget(Entity beHit, Entity hitter, Attribute hitAttribute, Object hitValue, Object oldValue, boolean killed) {}
    
    // ignore
    @Override
    public void onActorTargetLocked(Entity source, Entity other) {}
    
    // ignore
    @Override
    public void onActorTargetReleased(Entity source, Entity other) {}
    
    private class ItemWrap {
        /** 需要收集的物品的ID */
        String itemId;
        
        /** 需要收集的物品总量 */
        int total;
    }
   
}
