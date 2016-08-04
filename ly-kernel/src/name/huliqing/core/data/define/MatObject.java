/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data.define;

import name.huliqing.core.enums.Mat;

/**
 * 拥有"材质"属性的物体,这里的材质与jme中的material不同， 这里的材质主要用来定义不同物体的质地，从而让材质在碰撞的
 * 时候可以产生不同的声音。
 * @author huliqing
 */
public interface MatObject {
    
    /**
     * 获取物体的材质
     * @return 
     */
    Mat getMat();

}
