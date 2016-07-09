/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.Inject;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.object.env.Env;

/**
 * Env
 * @author huliqing
 */
public interface EnvService extends Inject {
    
    /**
     * 载入Env
     * @param envId
     * @return 
     */
    Env loadEnv(String envId);
    
    /**
     * 载入Env
     * @param envData
     * @return 
     */
    Env loadEnv(EnvData envData);
}
