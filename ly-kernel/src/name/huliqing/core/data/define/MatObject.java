/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data.define;

import name.huliqing.core.object.define.MatDefine;


/**
 * 拥有"质的"属性的物体,这里的质地Mat主要用于指物体的构成成分，不同的质地可能产生不同的碰撞声音。
 * @author huliqing
 */
public interface MatObject {
    
    /**
     * 获取物体的质的类型。
     * @return 
     */
    int getMat();
    
    /**
     * 设置质地
     * @param mat 
     * @see MatDefine#getMat(java.lang.String) 
     */
    void setMat(int mat);

}
