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
package name.huliqing.luoying.object.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.data.define.CountObject;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.luoying.utils.ConvertUtils;

/**
 * 商店逻辑，该逻辑会每隔一段时间给角色进货。以补充商店类角色的货源。
 * @author huliqing
 */
public class ShopLogic extends AbstractLogic {
    private static final Logger LOG = Logger.getLogger(ShopLogic.class.getName());
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    
    private List<Product> products;
    // 进货速度，如：1.0 表示每秒进货一件（每件未达到maxCount的商品各进货一件）
    // 如:0.1 则表示10秒进货一件，依此类推
    private float stockSpeed;
    // 是否初始化时把货物进满
    private boolean initStock;

    // ---- inner
    // remainCount为进货是不足1件的进货量，将累计到下一次足以达到1件以上的进货量
    private float remainCount;
    
    @Override
    public void setData(LogicData data) {
        super.setData(data); 

        // format: "item|maxCount,item|maxCount,item|maxCount,..."
        String[] items = data.getAsArray("items");
        products = new ArrayList<Product>(items.length);
        String[] temp;
        for (int i = 0; i < items.length; i++) {
            temp = items[i].split("\\|");
            Product pro = new Product();
            pro.itemId = temp[0];
            if (temp.length > 1) {
                pro.maxCount = ConvertUtils.toInteger(temp[1], 1);
            } else {
                pro.maxCount = 1;
            }
            products.add(pro);
        }
        stockSpeed = data.getAsFloat("stockSpeed", 10);
        initStock = data.getAsBoolean("initStock", true);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        
        if (initStock) {
            initStockProduct();
        }
    }
    
    // 初始时进满货
    private void initStockProduct() {
        // 进货
        ObjectData temp;
        int currentCount;
        int actualStock;
        for (Product p : products) {
            if (p.maxCount <= 0) 
                continue;
            
            temp = actor.getData().getObjectData(p.itemId);
            currentCount = 0;
            if (temp instanceof CountObject) {
                currentCount = ((CountObject) temp).getTotal();
            } else {
                if (temp != null) {
                    LOG.log(Level.WARNING, "Unsupported object data, ShopLogic only supported CountObject."
                            + " objectData={0}, actorId={1}, logicId={2} "
                            , new Object[] {temp.getId(), actor.getData().getId(), data.getId()});
                }
            }
            actualStock = p.maxCount - currentCount;
            if (actualStock > 0) {
                entityNetwork.addObjectData(actor, Loader.loadData(p.itemId), actualStock);
            }
        }
    }
    
    @Override
    protected void doLogic(float tpf) {
        if (stockSpeed <= 0 || products == null) // 进货速度小于0就没有意义了
            return;
       
        // 计算出要进货多少件商品
        float fullCount = interval * stockSpeed + remainCount;
        int stockCount = (int) fullCount;
        remainCount = fullCount - stockCount;
        
        // 进货
        ObjectData temp;
        int currentCount;
        int actualStock;
        for (Product p : products) {
            if (p.maxCount <= 0) 
                continue;
            
            actualStock = stockCount;
            temp = actor.getData().getObjectData(p.itemId);

            currentCount = 0;
            if (temp instanceof CountObject) {
                currentCount = ((CountObject) temp).getTotal();
            } else {
                if (temp != null) {
                    LOG.log(Level.WARNING, "Unsupported object data, ShopLogic only supported CountObject."
                            + " objectData={0}, actorId={1}, logicId={2} "
                            , new Object[] {temp.getId(), actor.getData().getId(), data.getId()});
                }
            }
            
            if (currentCount + actualStock > p.maxCount) {
                actualStock = p.maxCount - currentCount;
            }
            if (actualStock > 0) {
                entityNetwork.addObjectData(actor, Loader.loadData(p.itemId), actualStock);
            }
        }
    }
    
    private class Product {
        String itemId;  // 物品ID
        int maxCount;   // 最高进货量，达到这个数达则不再进货
    }
}
