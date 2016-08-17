/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actormodule;

import name.huliqing.core.data.ModuleData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.SimpleModule;

/**
 * 简单的角色扩展模块，没有任何功能，相对于{@link ActorLogicModule}来说，这种模块不需要带有"更新"逻辑,所有简单的
 * 扩展模块都可以继承这个类.
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractSimpleActorModule<T extends ModuleData> extends SimpleModule<T> implements ActorModule<T> {

    protected Actor actor;
    
    @Override
    public void setActor(Actor actor) {
        this.actor = actor;
    }

    @Override
    public Actor getActor() {
        return actor;
    }
    
}
