/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data;

import com.jme3.network.serializing.Serializable;

/**
 * 属性， 属性由三个主要值构成，
 * 等级值（levelValue), 静态值（staticValue)，动态值(dynamicValue)。
 * 1.等级值: 作为属性在相应等级时的固定值，属性在每个等级都会重新计算出一个等级值（根据等级公式）
 *   等级值只受等级的影响，不受其它外部因素影响.等级值影响属性的最高值
 * 2.静态值: 受除等级以外的其它外部因系的影响，如：状态，天赋，装备等都可能改变静态值。
 *   静态值影响属性的最高值
 * 3.动态值: 即为当前值，受各种外部因素影响。动态值限制在这个最高值范围内，即[0~ (等级值 + 静态值)].
 * 4.等级值和静态值构成属性的"最高值".
 * 5.动态值和静态值都可能受状态天赋和装备等外部因素影响。
 * 
 * 示例1：比如一瓶药水（补生命值）， 那么它可能只影响生命属性的动态值，药水只补到
 *      满为止，不会增加角色的最高生命值（maxValue),所以不应该影响静态值。
 * 示例2: 一件装甲，当它穿在角色身上时它将影响角色防御属性的静态值和动态值,因为装
 *      甲会影响角色的最高防御力，所以会影响静态值staticValue,而同时应该更新
 *      角色的当前防御力dynamicValue
 * 示例3：一个提高角色“最高生命值”的状态效果，当这个状态赋加到角色身上时将影响
 *      角色生命属性的静态值staticValue。因为这个状态只提高“最高生命值”所以不应
 *      该影响动态值dynamicValue
 * 
 * @author huliqing
 */
@Serializable
public class AttributeData extends ObjectData {
    
    /**
     * 获取属性名称
     * @return 
     */
    public String getName() {
        return getAsString("name");
    }

}
