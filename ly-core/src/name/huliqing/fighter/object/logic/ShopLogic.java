/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.logic;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.LogicData;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.game.network.ProtoNetwork;
import name.huliqing.fighter.game.service.ProtoService;
import name.huliqing.fighter.utils.ConvertUtils;

/**
 * 商店逻辑，该逻辑会每隔一段时间给角色进货。以补充商店类角色的货源。
 * @author huliqing
 */
public class ShopLogic extends ActorLogic {
//    private final ItemService itemService = Factory.get(ItemService.class);
//    private final ItemNetwork itemNetwork = Factory.get(ItemNetwork.class);
    private final ProtoNetwork protoNetwork = Factory.get(ProtoNetwork.class);
    private final ProtoService protoService = Factory.get(ProtoService.class);
    
    private Product[] products;
    // 进货速度，如：1.0 表示每秒进货一件（每件未达到maxCount的商品各进货一件）
    // 如:0.1 则表示10秒进货一件，依此类推
    private float stockSpeed;
    // 是否初始化时把货物进满
    private boolean initStock;

    // ---- inner
    // remainCount为进货是不足1件的进货量，将累计到下一次足以达到1件以上的进货量
    private float remainCount;
    
    public ShopLogic(LogicData data) {
        super(data);
        // 这类逻辑不需要太频繁
        interval = data.getAsFloat("interval", 20);
        
        // format: "item|maxCount,item|maxCount,item|maxCount,..."
        String[] items = data.getAsArray("items");
        products = new Product[items.length];
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
            products[i] = pro;
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
        ProtoData temp;
        int currentCount;
        int actualStock;
        for (Product p : products) {
            if (p.maxCount <= 0) 
                continue;
            
//            temp = itemService.getItem(self, p.itemId);
            temp = protoService.getData(self, p.itemId);
            currentCount = temp != null ? temp.getTotal() : 0;
            actualStock = p.maxCount - currentCount;
            if (actualStock > 0) {
//                itemNetwork.addItem(self, p.itemId, actualStock);
                protoNetwork.addData(self, protoService.createData(p.itemId), actualStock);
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
        ProtoData temp;
        int currentCount;
        int actualStock;
        for (Product p : products) {
            if (p.maxCount <= 0) 
                continue;
            
            actualStock = stockCount;
//            temp = itemService.getItem(self, p.itemId);
            temp = protoService.getData(self, p.itemId);
            currentCount = temp != null ? temp.getTotal() : 0;
            
            if (currentCount + actualStock > p.maxCount) {
                actualStock = p.maxCount - currentCount;
            }
            if (actualStock > 0) {
//                itemNetwork.addItem(self, p.itemId, actualStock);
                protoNetwork.addData(self, protoService.createData(p.itemId), actualStock);
            }
        }
    }
    
    private class Product {
        String itemId;  // 物品ID
        int maxCount;   // 最高进货量，达到这个数达则不再进货
    }
}
