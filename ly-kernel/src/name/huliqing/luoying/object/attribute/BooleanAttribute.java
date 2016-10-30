/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

import name.huliqing.luoying.data.AttributeData;

/**
 *
 * @author huliqing
 */
public class BooleanAttribute extends AbstractAttribute<Boolean> {

    @Override
    public void setData(AttributeData data) {
        super.setData(data); 
        value = data.getAsBoolean("value", false);
    }

    // remove20161029
//    /**
//     * 判断与另一个属性是否匹配，这里要注意：BooleanAttribute会把数值: 1或字符串"1"视为true,
//     * @param other
//     * @return 
//     */
//    @Override
//    public boolean match(Object other) {
//        Boolean bOther = convertToBoolean(other);
//        return bOther != null && bOther == this.value;
//    }
//    
//    private Boolean convertToBoolean(Object other) {
//        if (other instanceof Boolean) {
//            return (Boolean) other;
//        }
//        // 注：对于整数字来说,只有 0或1可以转化为Boolean
//        if (other instanceof Integer) {
//            Integer iOther = (Integer) other;
//            if (iOther == 1) {
//                return true;
//            } else if (iOther == 0) {
//                return false;
//            }
//        }
//        // 注：对于字符串来说,只有 "0", "1", "true", "false"可以转化为Boolean
//        if (other instanceof String) {
//            String sOther = (String) other;
//            if ("1".equals(sOther) || "true".equals(sOther)) {
//                return true;
//            } else if ("0".equals(sOther) || "false".equals(sOther)) {
//                return false;
//            }
//        }
//        // 其它值不能转化
//        return null;
//    }

}
