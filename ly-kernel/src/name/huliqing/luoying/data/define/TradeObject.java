/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data.define;

/**
 * TradeObject定义了一类可以进行交易的物品,这类物品存在相应的价值，可以通过另外一些物品进行交易获得。
 * 比如：一个角色可以通过一些金币来购买（交换）一件武器，那么金币和装备就必须是TradeObject类型的物品。
 * @author huliqing
 */
public interface TradeObject {
    
    /**
     * 获得物体的价值。
     * @return 
     */
    float getCost();

}
