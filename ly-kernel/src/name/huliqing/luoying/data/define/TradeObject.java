/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data.define;

import java.util.List;

/**
 * TradeObject定义了一类可以进行交易的物品,这类物品存在相应的价值，可以通过另外一些物品进行交易获得。
 * 例如：一件武器需要一些其它物品（如金币）来交换获得，那么武器就可以定义为TradeObject.
 * @author huliqing
 */
public interface TradeObject extends CountObject {
    
    /**
     * 获取物品的交易信息列表，返回的这个列表表示了物品的价值.如果返回null,则说明这件物品毫无价值.
     * @return 
     */
    List<TradeInfo> getTradeInfos();
    
    /**
     *  设置物品的交易信息列表.
     * @param tradeInfos 
     */
    void setTradeInfos(List<TradeInfo> tradeInfos);
}
