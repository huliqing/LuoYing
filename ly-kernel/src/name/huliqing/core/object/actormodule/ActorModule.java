/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actormodule;

import name.huliqing.core.data.ModuleData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.Module;

/**
 * 定义角色类模块接口,用于扩展角色功能,一个角色可以拥有多个下接无穷多个的扩展模块，所有角色扩展模块都应该实现这个
 * 接口
 * @author huliqing
 * @param <T>
 */
public interface ActorModule<T extends ModuleData> extends Module<T>{

    @Override
    public void setData(T data);

    @Override
    public T getData();
    
    /**
     * 设置module所作用的角色
     * @param actor 
     */
    void setActor(Actor actor);
    
    /**
     * 获取module所作用的角色
     * @return 
     */
    Actor getActor();
    
}
