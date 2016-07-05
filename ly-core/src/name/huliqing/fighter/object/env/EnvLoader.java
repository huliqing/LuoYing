/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.object.DataLoader;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class EnvLoader <T extends EnvData>  implements DataLoader<T>{

    @Override
    public void load(Proto proto, T store) {}
}
