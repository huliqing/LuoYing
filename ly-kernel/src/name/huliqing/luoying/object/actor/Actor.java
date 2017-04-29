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
package name.huliqing.luoying.object.actor;

import com.jme3.animation.SkeletonControl;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.AssetLoader;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.effect.Effect;
import name.huliqing.luoying.object.entity.ModelEntity;

/**
 * 角色，角色由数据(ObjectData)和模块处理器(Module)组成。
 * @author huliqing
 */
public class Actor extends ModelEntity {
    
    private String file;
    private String icon;
    
    @Override
    public void setData(EntityData data) {
        super.setData(data); 
        file = data.getAsString("file");
        icon = data.getAsString("icon");
    }
    
    public String getIcon() {
        return icon;
    }
    
    /**
     * 载入基本模型
     * @return 
     */
    @Override
    protected Spatial loadModel() {
        return loadActorModel();
    }
    
    private Spatial loadActorModel() {
        // 需要确保最外层是Node类
        Spatial actorModel = AssetLoader.loadModel(file);
        if (actorModel instanceof Geometry) {
            Spatial temp = actorModel;
            actorModel = new Node();
            ((Node) actorModel).attachChild(temp);
        }
        
        // 6.==== 绑定特效
        String[] effects = data.getAsArray("effects");
        if (effects != null) {
            for (String eid : effects) {
                Effect ae = Loader.load(eid);
                ae.initialize();
                ((Node) actorModel).attachChild(ae);
            }
        }

        // 14.偿试激活HardwareSkining
        checkEnableHardwareSkining(actorModel);
        return actorModel;
    }
    
    /**
     * 检测并判断是否打开或关闭该模型的硬件skining加速
     */
    private static void checkEnableHardwareSkining(Spatial actorModel) {
        SkeletonControl sc = actorModel.getControl(SkeletonControl.class);
        if (sc == null) {
            return;
        }
        // 代换自定义的SkeletonControl,因为默认的SkeletonControl会把带
        // SkeletonControl的子节点也进行处理。比如弓武器，当弓武器带有动画时可能
        // 导致角色的SkeletonControl和弓的SkeletonControl存在冲突导致弓模型变形
        CustomSkeletonControl csc = new CustomSkeletonControl(sc.getSkeleton());
        actorModel.removeControl(sc);
        actorModel.addControl(csc);
        csc.setHardwareSkinningPreferred(true);
    }
}
