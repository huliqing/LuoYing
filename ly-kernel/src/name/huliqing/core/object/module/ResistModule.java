/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.data.module.ResistModuleData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.resist.Resist;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class ResistModule<T extends ResistModuleData> extends AbstractModule<T> {

    private Resist resist;

    @Override
    public void initialize(Actor actor) {
        super.initialize(actor); 
    }

    @Override
    public void cleanup() {
        resist = null;
        super.cleanup();
    }
    
    /**
     * 获取指定状态的抗性值,如果不存在指定状态的抗性设置，则返回0.
     * @param stateId
     * @return 抗性值[0.0~1.0]
     */
    public float getResist(String stateId) {
        if (resist == null && data.getResist() != null) {
            resist = Loader.load(data.getResist());
        }
        if (resist == null) {
            Logger.getLogger(ResistModule.class.getName()).log(Level.WARNING, "No Resist found for ResistData={0}, stateId={1}"
                    , new Object[] {data.getId(), stateId});
            return 0;
        }
        
        return resist.getResist(stateId);
    }
    
}
