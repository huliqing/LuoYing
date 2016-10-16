/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data.define;

/**
 * 拥有“价值”的物体,有价值的物体可以出售，可以显示出价格，即用来表示物体的价值。
 * @author huliqing
 */
public interface CostObject {
    
    /**
     * 获得物体的价值。
     * @return 
     */
    float getCost();

}
