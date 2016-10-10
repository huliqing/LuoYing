/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.module;

import java.util.List;
import name.huliqing.ly.data.ResistData;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.object.resist.Resist;

/**
 * @author huliqing
 */
public class ResistModule extends AbstractModule {

    private Resist resist;

    @Override
    public void initialize(Entity actor) {
        super.initialize(actor);
        List<ResistData> rds = actor.getData().getObjectDatas(ResistData.class, null);
        if (rds != null && !rds.isEmpty()) {
            setResist((Resist)Loader.load(rds.get(0)));
        }
    }

    @Override
    public void cleanup() {
        resist = null;
        super.cleanup();
    }
    
    public void setResist(Resist resist) {
        if (this.resist != null) {
            entity.getData().removeObjectData(this.resist.getData());
        }
        this.resist = resist;
        this.entity.getData().addObjectData(this.resist.getData());
    }
    
    /**
     * 获取搞性设置，如果没有则返回null.
     * @return 
     */
    public Resist getResist() {
        return resist;
    }
    
}
