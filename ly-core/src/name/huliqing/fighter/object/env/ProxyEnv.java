/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

/**
 * Env代理接口
 * @author huliqing
 */
public interface ProxyEnv {
    
    /**
     * 获取代理的实际Env
     * @return 
     */
    Env getProxyEnv();
}
