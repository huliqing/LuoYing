/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.loader;

import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.xml.DataLoader;

/**
 * @author huliqing
 */
public class ItemDataLoader implements DataLoader<ItemData> {

    @Override
    public void load(Proto proto, ItemData data) {
        data.setTradeInfos(TradeObjectLoaderHelper.loadTradeInfos(proto));
    }
    
}
