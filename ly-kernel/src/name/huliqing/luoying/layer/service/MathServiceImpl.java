/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.manager.RandomManager;

/**
 *
 * @author huliqing
 */
public class MathServiceImpl implements MathService {

    @Override
    public void inject() {
        // ignore
    }
    
    @Override
    public float getRandom() {
        return RandomManager.getNextValue();
    }

    
    
}
