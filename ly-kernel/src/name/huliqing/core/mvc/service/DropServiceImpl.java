/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.data.DropData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.drop.Drop;
import name.huliqing.core.object.module.DropModule;

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
    public void addDrop(Actor actor, String dropId) {
        DropModule module = actor.getModule(DropModule.class);
        if (module != null) {
            module.addDrop((Drop)Loader.load(dropId));
        }
    }

    // remove20160830
//    @Override
//    public List<ObjectData> getBaseDrop(Actor actor, List<ObjectData> store) {
//        DropData data = actor.getData().getDrop();
//        if (data == null) 
//            return null;
//        
//        List<DropItem> baseItems = data.getBaseItems();
//        if (baseItems == null) 
//            return store;
//        
//        if (store == null) {
//            store = new ArrayList<ObjectData>(baseItems.size());
//        }
//        for (DropItem di : baseItems) {
//            if (di.getCount() <= 0) {
//                continue;
//            }
//            ObjectData dropItem = DataFactory.createData(di.getItemId());
//            dropItem.setTotal(di.getCount());
//            store.add(dropItem);
//        }
//        return store;
//    }
//
//    @Override
//    public List<ObjectData> getRandomDrop(Actor actor, float dropFactor, List<ObjectData> store) {
//        DropData data = actor.getData().getDrop();
//        if (data == null) 
//            return null;
//        
//        List<DropItem> randomItems = data.getRandomItems();
//        if (randomItems == null)
//            return store;
//        
//        if (store == null) {
//            store = new ArrayList<ObjectData>(randomItems.size());
//        }
//        float factor; 
//        float randomFactor;
//        for (DropItem di : randomItems) {
//            // 计算机率
//            factor = di.getFactor() * dropFactor;
//            randomFactor = FastMath.nextRandomFloat();
//            
//            if (factor >= randomFactor) {
//                // 注：DropItem中掉落的各种物品的id可能不存在，应该允许兼容这种
//                // 问题，给予一个警告就可以不要暴异常，因为
//                ObjectData dropItem = DataFactory.createData(di.getItemId());
//                if (dropItem != null) {
//                    dropItem.setTotal(di.getCount());
//                    store.add(dropItem);
//                } else {
//                    LOG.log(Level.WARNING
//                            , "Item could not be found, check the drop configs if the item exists?! drop item id={0}"
//                            , di.getItemId());
//                }
//            }
//        }
//        return store;
//    }
//
//    @Override
//    public List<ObjectData> getRandomDropFull(Actor actor, List<ObjectData> store) {
//        if (store == null) {
//            store = new ArrayList<ObjectData>();
//        }
//        getBaseDrop(actor, store); // 必掉
//        getRandomDrop(actor, configService.getDropFactor(), store); // 随机掉
//        return store;
//    }

    @Override
    public void doDrop(Actor source, Actor target) {
        DropModule module = source.getModule(DropModule.class);
        if (module != null) {
            module.doDrop(target);
        }
    }
    
    
}
