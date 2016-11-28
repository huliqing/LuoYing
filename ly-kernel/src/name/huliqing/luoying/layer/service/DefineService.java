/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.define.MatDefine;
import name.huliqing.luoying.object.define.SkillTypeDefine;
import name.huliqing.luoying.object.define.SkinPartDefine;
import name.huliqing.luoying.object.define.WeaponTypeDefine;

/**
 *
 * @author huliqing
 */
public interface DefineService extends Inject {
    
    /**
     * 获取技能类型定义配置
     * @return 
     */
    SkillTypeDefine getSkillTypeDefine();
    
    /**
     * 获取皮肤类型定义配置
     * @return 
     */
    SkinPartDefine getSkinPartDefine();
    
    /**
     * 获取武器类型的定义配置。
     * @return 
     */
    WeaponTypeDefine getWeaponTypeDefine();
    
    /**
     * 获取物体质地定义
     * @return 
     */
    MatDefine getMatDefine();
    
}
