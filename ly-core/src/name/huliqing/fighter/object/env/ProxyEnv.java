/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import java.util.List;
import name.huliqing.fighter.data.EnvData;

/**
 * 代理类ENV
 * @author huliqing
 * @param <T>
 */
public interface ProxyEnv<T extends EnvData> extends Env<T>{
    
    /**
     * 获取当前ENV代理下的所有子ENV。
     * @param store 存放结果
     * @return  
     */
    List<Env> getProxyEnvs(List<Env> store);
}
