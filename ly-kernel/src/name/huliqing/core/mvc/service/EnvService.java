/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import name.huliqing.core.Inject;
import name.huliqing.core.data.EnvData;
import name.huliqing.core.object.env.Env;

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
     * 载入EnvData数据
     * @param envId
     * @return 
     */
    EnvData loadEnvData(String envId);
    
    /**
     * 载入Env
     * @param envData
     * @return 
     */
    Env loadEnv(EnvData envData);
    
}
