/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.handler;

import name.huliqing.core.Factory;
import name.huliqing.core.GameException;
import name.huliqing.core.constants.DataTypeConstants;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.xml.ProtoData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.mvc.service.ItemService;
import name.huliqing.core.mvc.service.SkinService;

/**
 * 处理装备的使用,用于切换装备
 * @author huliqing
 */
public class OutfitHandler extends AbstractHandler {
    private final SkinService skinService = Factory.get(SkinService.class);
    private final ItemService itemService = Factory.get(ItemService.class);

    @Override
    public boolean canUse(Actor actor, ProtoData data) {
        if (!super.canUse(actor, data)) {
            return false;
        }
        
        // not a skin
        if (!(data instanceof SkinData)) {
            return false;
        }

        // remove20151204,现在装备可穿可脱
        // in using
//        SkinData skinData = (SkinData) data;
//        if (skinData.isUsing()) {
//            return false;
//        }
        
        return true;
    }

    @Override
    protected void useObject(Actor actor, ProtoData pd) {
        SkinData data = (SkinData) pd;
        if (data.isUsing()) {
            // 脱装备
            skinService.detachSkin(actor, data);
        } else {
            // 穿装备
            skinService.attachSkin(actor, data);
        }
    }

    @Override
    public boolean remove(Actor actor,  ProtoData data, int count) throws GameException {
        if (!(data instanceof SkinData)) {
//            logger.log(Level.WARNING, "OutfitHandler only supported Skin type objects");
            return false;
        }
        SkinData skinData = (SkinData) data;
        if (skinData.isUsing()) {
            return false;
        }
        return super.remove(actor, data, data.getTotal());
    }
    
    
}
