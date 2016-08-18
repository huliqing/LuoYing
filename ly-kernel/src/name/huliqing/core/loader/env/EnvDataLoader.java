/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader.env;

import name.huliqing.core.data.env.EnvData;
import name.huliqing.core.xml.Proto;
import name.huliqing.core.xml.DataLoader;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class EnvDataLoader <T extends EnvData>  implements DataLoader<T>{

    @Override
    public void load(Proto proto, T store) {}
}
