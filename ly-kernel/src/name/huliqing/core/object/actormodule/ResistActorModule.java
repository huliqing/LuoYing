/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actormodule;

import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.data.ModuleData;
import name.huliqing.core.data.ResistData;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.object.resist.Resist;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class ResistActorModule<T extends ModuleData> extends AbstractSimpleActorModule<T> {

    private Resist resist;
    
    /**
     * 获取指定状态的抗性值,如果不存在指定状态的抗性设置，则返回0.
     * @param data
     * @param stateId
     * @return 抗性值[0.0~1.0]
     */
    public float getResist(ResistData data, String stateId) {
        if (resist == null || resist.getData() != data) {
            resist = Loader.loadResist(data);
        }
        if (resist == null) {
            Logger.getLogger(ResistActorModule.class.getName()).log(Level.WARNING, "No Resist found for ResistData={0}, stateId={1}"
                    , new Object[] {data.getId(), stateId});
            return 0;
        }
        
        return resist.getResist(stateId);
    }
    
}
