/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity.impl;

import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.layer.service.SystemService;
import name.huliqing.luoying.object.entity.NonModelEntity;
import name.huliqing.luoying.object.scene.Scene;

/**
 * ShadowEntity的抽象类，无任何方法。
 * @author huliqing
 */
public abstract class ShadowEntity extends NonModelEntity {
    private final SystemService systemService = Factory.get(SystemService.class);

    /** 在指定的平台上关闭阴影功能 */
    protected List<String> disabledOnPlatforms;
    
    // ---- 
    /** 判断Shadow功能是否打开 */
    protected boolean enabled = true;

    @Override
    public void setData(EntityData data) {
        super.setData(data);
        disabledOnPlatforms = data.getAsStringList("disabledOnPlatforms");
    }
    
    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        String platform = systemService.getPlatformName();
        if (disabledOnPlatforms != null && disabledOnPlatforms.contains(platform)) {
            enabled = false;
        }
        setShadowEnabled(enabled);
    }

    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        setShadowEnabled(enabled);
    }
    
    /**
     * 初始化Shadow功能
     * @param enabled
     */
    protected abstract void setShadowEnabled(boolean enabled);
    
    /**
     * 获取阴影的强度,返回值为0.0~1.0f
     * @return 
     */
    public abstract float getShadowIntensity();
    
    /**
     * 设置阴影的强度，值[0.0, 1.0]
     * @param shadowIntensity 
     */
    public abstract void setShadowIntensity(float shadowIntensity);
    
}
