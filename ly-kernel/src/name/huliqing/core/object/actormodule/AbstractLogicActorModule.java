/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actormodule;

import name.huliqing.core.data.ModuleData;
import name.huliqing.core.object.module.LogicModule;

/**
 * 一个带有“逻辑更新”功能的角色模块基类，所有需要有持续<b>更新</b>功能的角色扩展模块都可以继承这个基类。
 * 继承这个类主要需要实现 {@link #update(float) }方法。 
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractLogicActorModule<T extends ModuleData> extends AbstractSimpleActorModule<T> 
        implements LogicModule<T> {

    // nothing
    
}
