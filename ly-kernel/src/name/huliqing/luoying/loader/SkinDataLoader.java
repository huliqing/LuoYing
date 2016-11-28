/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.loader;

import java.util.ArrayList;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.AttributeApply;
import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.luoying.layer.service.DefineService;
import name.huliqing.luoying.xml.DataLoader;
import name.huliqing.luoying.utils.ConvertUtils;

/**
 *
 * @author huliqing
 */
public class SkinDataLoader implements DataLoader<SkinData> {
    private final DefineService defineService = Factory.get(DefineService.class);

    @Override
    public void load(Proto proto, SkinData data) {
        // 交易信息
        data.setTradeInfos(TradeObjectLoaderHelper.loadTradeInfos(proto));
        // 装备属性
        String[] aaStr = proto.getAsArray("applyAttributes");
        if (aaStr != null) {
            ArrayList<AttributeApply> aas = new ArrayList<AttributeApply>(aaStr.length);
            for (int i = 0; i < aaStr.length; i++) {
                String[] aaArr = aaStr[i].split("\\|");
                aas.add(new AttributeApply(aaArr[0], ConvertUtils.toFloat(aaArr[1], 0)));
            }
            data.setApplyAttributes(aas);
        }
        // 设置质地,这里是必要的，因为需要将mat的字符串质地名称转化为int类型（索引）。
        data.setMat(defineService.getMatDefine().getMat(proto.getAsString("mat")));
    }
    
}
