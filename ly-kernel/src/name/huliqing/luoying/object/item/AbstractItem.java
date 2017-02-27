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
package name.huliqing.luoying.object.item;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.message.StateCode;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.effect.Effect;
import name.huliqing.luoying.object.el.SBooleanEl;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.sound.SoundManager;

/**
 * @author huliqing
 */
public abstract class AbstractItem implements Item {
//    private static final Logger LOG = Logger.getLogger(AbstractItem.class.getName());
    private final ElService elService = Factory.get(ElService.class);
    
    protected ItemData data;
    
    // 使用物品时的效果
    protected String[] effects;
    
    // 使用物品时的声音
    protected String[] sounds;
    
    // checkEl用于判断角色是否可以使用这件物品
    protected SBooleanEl checkEl;
    
    @Override
    public void setData(ItemData data) {
        this.data = data;
        effects = data.getAsArray("effects");
        sounds = data.getAsArray("sounds");
        checkEl = elService.createSBooleanEl(data.getAsString("checkEl", "#{true}"));
    }
    
    @Override
    public ItemData getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
    }

    @Override
    public String getId() {
        return data.getId();
    }

    @Override
    public final void use(Entity actor) {
        // 执行特效和声效
         if (effects != null) {
            for (String eid : effects) {
                 Effect effect = Loader.load(eid);
                 effect.setTraceObject(actor.getSpatial());
                 effect.initialize();
                 actor.getScene().getRoot().attachChild(effect);
            }
        }
        if (sounds != null) {
            for (String sid : sounds) {
                SoundManager.getInstance().playSound(sid, actor.getSpatial().getWorldTranslation());
            }
        }
        
        // 使用物品
        doUse(actor);
    }
    
    @Override
    public int checkStateCode(Entity actor) {
        // 物品数量不够。
        if (data.getTotal() <= 0) {
            return StateCode.DATA_USE_FAILURE_NOT_ENOUGH;
        }
        
        checkEl.setSource(actor.getAttributeManager());
        if (!checkEl.getValue()) {
            return StateCode.DATA_USE_FAILURE_CHECK_EL;
        }
        
        return StateCode.DATA_USE;
    }

    /**
     * 实现使用物品的逻辑
     * @param actor 
     */
    protected abstract void doUse(Entity actor);
}
