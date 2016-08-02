/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import com.jme3.math.FastMath;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.data.DropData;
import name.huliqing.core.data.DropItem;
import name.huliqing.core.data.ProtoData;
import name.huliqing.core.object.DataFactory;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public class DropServiceImpl implements DropService {
    private static final Logger LOG = Logger.getLogger(DropServiceImpl.class.getName());
    

    private ConfigService configService;
    
    @Override
    public void inject() {
        configService = Factory.get(ConfigService.class);
    }

    @Override
    public DropData createDrop(String objectId) {
        return DataFactory.createData(objectId);
    }

    @Override
    public List<ProtoData> getBaseDrop(Actor actor, List<ProtoData> store) {
        DropData data = actor.getData().getDrop();
        if (data == null) 
            return null;
        
        List<DropItem> baseItems = data.getBaseItems();
        if (baseItems == null) 
            return store;
        
        if (store == null) {
            store = new ArrayList<ProtoData>(baseItems.size());
        }
        for (DropItem di : baseItems) {
            if (di.getCount() <= 0) {
                continue;
            }
            ProtoData dropItem = DataFactory.createData(di.getItemId());
            dropItem.setTotal(di.getCount());
            store.add(dropItem);
        }
        return store;
    }

    @Override
    public List<ProtoData> getRandomDrop(Actor actor, float dropFactor, List<ProtoData> store) {
        DropData data = actor.getData().getDrop();
        if (data == null) 
            return null;
        
        List<DropItem> randomItems = data.getRandomItems();
        if (randomItems == null)
            return store;
        
        if (store == null) {
            store = new ArrayList<ProtoData>(randomItems.size());
        }
        float factor; 
        float randomFactor;
        for (DropItem di : randomItems) {
            // 计算机率
            factor = di.getFactor() * dropFactor;
            randomFactor = FastMath.nextRandomFloat();
            
            if (factor >= randomFactor) {
                // 注：DropItem中掉落的各种物品的id可能不存在，应该允许兼容这种
                // 问题，给予一个警告就可以不要暴异常，因为
                ProtoData dropItem = DataFactory.createData(di.getItemId());
                if (dropItem != null) {
                    dropItem.setTotal(di.getCount());
                    store.add(dropItem);
                } else {
                    LOG.log(Level.WARNING
                            , "Item could not be found, check the drop configs if the item exists?! drop item id={0}"
                            , di.getItemId());
                }
            }
        }
        return store;
    }

    @Override
    public List<ProtoData> getRandomDropFull(Actor actor, List<ProtoData> store) {
        if (store == null) {
            store = new ArrayList<ProtoData>();
        }
        getBaseDrop(actor, store); // 必掉
        getRandomDrop(actor, configService.getDropFactor(), store); // 随机掉
        return store;
    }
    
    
}
