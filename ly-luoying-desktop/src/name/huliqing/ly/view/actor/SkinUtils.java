/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.actor;

import java.util.List;
import name.huliqing.luoying.data.AttributeApply;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.ly.manager.ResourceManager;

/**
 *
 * @author huliqing
 */
public class SkinUtils {
    
    /**
     * 获取Skin的描述说明
     * @param skinData
     * @return 
     */
    public static String getSkinDes(SkinData skinData) {
        List<AttributeApply> aas = skinData.getApplyAttributes();
        if (aas != null) {
            StringBuilder sb = new StringBuilder();
            for (AttributeApply aa : aas) {
                sb.append(ResourceManager.getObjectName(aa.getAttribute()))
                        .append(":")
                        .append(aa.getAmount())
                        .append("  ");
            }
            return sb.toString();
        }
        return ResourceManager.get(ResConstants.COMMON_UNKNOW);
    }
}
