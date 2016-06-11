/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.handler;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.GameException;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.data.SkinData;
import name.huliqing.fighter.enums.DataType;
import name.huliqing.fighter.game.service.ItemService;
import name.huliqing.fighter.game.service.SkinService;

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
        if (data.getDataType() != DataType.skin) {
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
        if (data.getDataType() != DataType.skin) {
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
