/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import name.huliqing.ly.Inject;
import name.huliqing.ly.object.actor.Actor;

/**
 * @author huliqing
 */
public interface DropNetwork extends Inject {

    /**
     * 从源角色(source)掉落物品给目标角色(target)
     * @param source
     * @param target 
     */
    void doDrop(Actor source, Actor target);

}
