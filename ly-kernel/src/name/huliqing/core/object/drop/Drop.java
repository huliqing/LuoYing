/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.drop;

import name.huliqing.core.data.DropData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.xml.DataProcessor;

/**
 *
 * @author huliqing
 */
public abstract class  Drop implements  DataProcessor<DropData> {

    protected DropData data;
    
    @Override
    public void setData(DropData data) {
        this.data = data;
    }

    @Override
    public DropData getData() {
        return data;
    }
    
    /**
     * 处理掉落物品，物品从source掉落到target身上。
     * @param source 掉落源
     * @param target 接受掉落物品的角色
     */
    public abstract void doDrop(Actor source, Actor target);
}
