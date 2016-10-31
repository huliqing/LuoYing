/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.loader;

import java.util.ArrayList;
import name.huliqing.luoying.data.AttributeApply;
import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.luoying.object.define.DefineFactory;
import name.huliqing.luoying.xml.DataLoader;
import name.huliqing.luoying.utils.ConvertUtils;

/**
 *
 * @author huliqing
 */
public class SkinDataLoader implements DataLoader<SkinData> {

    @Override
    public void load(Proto proto, SkinData data) {
        data.setBaseSkin(proto.getAsBoolean("baseSkin", false));
        String[] aaStr = proto.getAsArray("applyAttributes");
        if (aaStr != null) {
            ArrayList<AttributeApply> aas = new ArrayList<AttributeApply>(aaStr.length);
            for (int i = 0; i < aaStr.length; i++) {
                String[] aaArr = aaStr[i].split("\\|");
                aas.add(new AttributeApply(aaArr[0], ConvertUtils.toFloat(aaArr[1], 0)));
            }
            data.setApplyAttributes(aas);
        }
        
        // 设置质的
        data.setMat(DefineFactory.getMatDefine().getMat(proto.getAsString("mat")));
        // 默认给一个数量
        data.setTotal(proto.getAsInteger("total", 1));
    }
    
}
