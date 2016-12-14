/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.Inject;

/**
 *
 * @author huliqing
 */
public interface MathService extends Inject {
    
    /**
     * 获取一个随机值
     * @return 
     */
    float getRandom();
}
