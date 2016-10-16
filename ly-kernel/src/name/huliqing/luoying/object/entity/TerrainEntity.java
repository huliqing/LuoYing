/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity;

import com.jme3.math.Vector3f;

/**
 * 定义地面物体。
 * @author huliqing
 */
public interface TerrainEntity extends Entity {
    
    /**
     * 获取地面的高度, 注：如果指定的位置范围超出，则该方法将返回null, 否则返回该位置处地面的最高点。
     * @param x
     * @param z
     * @return 
     */
    Vector3f getHeight(float x, float z);
}
