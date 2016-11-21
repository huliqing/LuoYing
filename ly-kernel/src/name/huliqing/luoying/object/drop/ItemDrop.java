/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.drop;

import com.jme3.math.FastMath;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.DropData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.service.ConfigService;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 掉落普通物品设置 
 * @author huliqing
 */
public class ItemDrop extends AbstractDrop {
    private final ConfigService configService = Factory.get(ConfigService.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    
    private String item;
    private int count;
    private float rate;
    
    @Override
    public void setData(DropData data) {
        super.setData(data);
        item = data.getAsString("item");
        count = data.getAsInteger("count", 1);
        rate = MathUtils.clamp(data.getAsFloat("rate", 1.0f), 0f, 1.0f);
    }
    
    // 注意：因为这里涉及到机率，所以要使用network版本（***Network.addData）
    // // 这里使用ProtoNetwork就可以，不需要直接使用ItemNetwork
    @Override
    public boolean doDrop(Entity source, Entity target) {
        if (item == null || count <= 0 || rate <= 0) {
            return false;
        }
        
        // 注：如果rate>=1.0, 则忽略configService全局掉落设置(dropFactor)的影响，把物品视为始终掉落的。
        if (rate >= 1.0f) {
            entityNetwork.addObjectData(target, item, count);
            playDropSounds(source);
            return true;
        }
        
        // 按机率掉落，这个机率受全局掉落设置影响
        float trueRate = configService.getDropFactor() * rate;
        if (trueRate >= FastMath.nextRandomFloat()) {
            entityNetwork.addObjectData(target, item, count);
            playDropSounds(source);
            return true;
        }
        
        return false;
    }
    
}
