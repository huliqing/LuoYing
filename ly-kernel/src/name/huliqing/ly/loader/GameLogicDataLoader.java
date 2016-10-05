/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.loader;

import name.huliqing.ly.data.GameLogicData;
import name.huliqing.ly.xml.Proto;
import name.huliqing.ly.xml.DataLoader;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class GameLogicDataLoader<T extends GameLogicData> implements DataLoader<T> {

    @Override
    public void load(Proto proto, T store) {
    }
    
}
