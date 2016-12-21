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
package name.huliqing.luoying.object.effect;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import name.huliqing.luoying.data.EffectData;
import name.huliqing.luoying.shape.QuadXY;
import name.huliqing.luoying.utils.MaterialUtils;
import name.huliqing.luoying.utils.MathUtils;

/**
 * @deprecated 20160202不通用，将不再使用。
 * 光晕效果,在xz面上竖起多束光束。
 * @author huliqing
 */
public class HaloEffect extends Effect {
    
    // 光晕材质贴图
    private String texture = "Textures/effect/spark_v.jpg";
    // 半径
    private float radius = 1.5f;
    // 光晕数量
    private int haloTotal = 7;
    // 光晕图片的缩放大小
    private Vector3f haloSize = new Vector3f(5, 5, 1);
    // 倾斜值,大于0则向上倾斜，小于0则向下倾斜
    private float incline = 0.5f;
    
    // -------- 内部参数
    // 光晕的根节点
    private Node haloRoot;
    
    @Override
    public void setData(EffectData data) {
        super.setData(data); 
        this.texture = data.getProto().getAsString("texture", texture);
        this.radius = data.getProto().getAsFloat("radius", radius);
        this.haloTotal = data.getProto().getAsInteger("haloTotal", haloTotal);
        this.haloSize = data.getProto().getAsVector3f("haloSize", haloSize);
        this.incline = data.getProto().getAsFloat("incline", incline);
    }
    
    @Override
    public void initEntity() {
        super.initEntity();
        create();
    }
    
    private void create() {
        if (haloRoot != null) {
            haloRoot.removeFromParent();
        }
        
        // -- 初始化创建所有光晕
        haloRoot = new Node("CircleHalo_root");
        float angle = FastMath.TWO_PI / haloTotal;
        TempVars tv = TempVars.get();
        Vector3f lookAt = tv.vect1.set(0, incline, 0);
        Vector3f pos = tv.vect2;
        Vector3f size = tv.vect3.set(1,1,1).multLocal(haloSize);
        Quaternion rot = tv.quat1;
        for (int i = 0; i < haloTotal; i++) {
            pos.zero().setX(radius);
            if (i == 0) {
                haloRoot.attachChild(createHaloOne(texture, pos, size, lookAt));
            } else {
                MathUtils.createRotation(angle * i, Vector3f.UNIT_Y, rot);
                rot.mult(pos, pos);
                haloRoot.attachChild(createHaloOne(texture, pos, size, lookAt));
            }
        }
        tv.release();
        this.animNode.attachChild(haloRoot);
    }
    
    private Spatial createHaloOne(String texture, Vector3f pos, Vector3f size, Vector3f lookAt) {
        QuadXY shape = new QuadXY(size.x, size.y);
        Geometry geo = new Geometry("QuadShape_halo", shape);
        geo.setMaterial(MaterialUtils.createTransparent(texture));
        geo.setLocalTranslation(pos);
        geo.lookAt(lookAt, Vector3f.UNIT_Y);
//        geo.setQueueBucket(Bucket.Transparent); // remove20161004
        geo.setQueueBucket(Bucket.Translucent);

        return geo;
    }
    
}
