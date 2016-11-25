/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity.impl;

import name.huliqing.luoying.object.entity.NonModelEntity;

/**
 * ShadowEntity的抽象类，无任何方法。
 * @author huliqing
 */
public abstract class ShadowEntity extends NonModelEntity {

    /**
     * 设置阴影的强度，值[0.0, 1.0]
     * @param shadowIntensity 
     */
    public abstract void setShadowIntensity(float shadowIntensity);
}
