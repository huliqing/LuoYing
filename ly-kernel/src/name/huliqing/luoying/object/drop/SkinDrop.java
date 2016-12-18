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
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 掉落武器装备设置
 * @author huliqing
 */ 
public class SkinDrop extends AbstractDrop {
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);    
    private String skin;
    private int count;
    /** 掉落率: 取值0.0~1.0 */
    private float rate;
    
    @Override
    public void setData(DropData data) {
        super.setData(data);
        skin = data.getAsString("skin");
        count = data.getAsInteger("count", 1);
        rate = MathUtils.clamp(data.getAsFloat("rate", 1.0f), 0f, 1.0f);
    }
    
    // 注意：因为这里涉及到机率，所以要使用network版本（***Network.addData）
    // 这里使用ProtoNetwork就可以，不需要直接使用SkinNetwork
    @Override
    public void doDrop(Entity source, Entity target) {
        if (skin == null || count <= 0 || rate <= 0) {
            return;
        }
        
        // 注：如果rate>=1.0, 则忽略configService全局掉落设置(dropFactor)的影响，把物品视为始终掉落的。
        if (rate >= 1.0f) {
            entityNetwork.addObjectData(target, Loader.loadData(skin), count);
            playDropSounds(source);
            return;
        }
        
        // 按机率掉落，这个机率受全局掉落设置影响
        if (rate >= FastMath.nextRandomFloat()) {
            entityNetwork.addObjectData(target, Loader.loadData(skin), count);
            playDropSounds(source);
        }
    }


}
