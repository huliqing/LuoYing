/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;

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
public class AttributeData extends ProtoData {
    
    // 等级设置的id,如果该id存在，则这个属性值会随着等级的改变而发生变化
    private String el;
    
    // 受level影响的基本属性值,每个等级都会有不一样的值。
    private transient float levelValue;
    
    // 静态值
    private transient float staticValue;
    
    // 动态值,即当前值
    private transient float dynamicValue;
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(el, "el", null);
        // 注：20160103这些数值不保存到存档中，因为这些数值必须在actorService.load(ActorData)
        // 时重新计算，特别是staticValue.以避免在存档中读取出来的属性中存在staticValue
        // 导致在载入actorData时staticValue的值叠加的bug.最好是让这些值在载入时重新计算
//        oc.write(levelValue, "levelValue", 0);
//        oc.write(staticValue, "staticValue", 0);
//        oc.write(dynamicValue, "dynamicValue", 0);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        el = ic.readString("el", null);
        // 注：同上
//        levelValue = ic.readFloat("levelValue", 0);
//        staticValue = ic.readFloat("staticValue", 0);
//        dynamicValue = ic.readFloat("dynamicValue", 0);
    }
    
    public AttributeData(){}
    
    public AttributeData(String id) {
        super(id);
    }
    
    /**
     * 获取属性的等级设置
     * @return 
     */
    public String getEl() {
        return el;
    }

    /**
     * 设置属性的等级设置,指向一个el,该el必须是一个等级el(LevelEl)
     * @param el 
     */
    public void setEl(String el) {
        this.el = el;
    }

    /**
     * 获取属性的等级值
     * @return 
     */
    public float getLevelValue() {
        return levelValue;
    }

    /**
     * 设置属性的等级值,等级值是每个等级固定的，角色升级之后会根据属性所绑
     * 定的计算公式来算出这个值。
     * @param levelValue 
     */
    public void setLevelValue(float levelValue) {
        this.levelValue = levelValue;
    }

    /**
     * 获取静态值
     * @return 
     */
    public float getStaticValue() {
        return staticValue;
    }

    /**
     * 设置静态值
     * @param staticValue 
     */
    public void setStaticValue(float staticValue) {
        this.staticValue = staticValue;
    }
    
    /**
     * 获取属性的当前动态值。
     * @return 
     */
    public float getDynamicValue() {
        return dynamicValue;
    }

    /**
     * 设置属性的当前动态值
     * @param dynamicValue 
     */
    public void setDynamicValue(float dynamicValue) {
        this.dynamicValue = dynamicValue;
    }
    
    /**
     * 获取属性的最高值,最高值是由等级值和静态值组成的。
     * @return 
     */
    public float getMaxValue() {
        return levelValue + staticValue;
    }

}
