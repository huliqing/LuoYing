/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.talent;

import name.huliqing.core.data.TalentData;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractTalent<T extends TalentData> implements Talent<T> {
//    private final static ElService elService = Factory.get(ElService.class);
    
    protected T data;
    protected Actor actor;
    private float timeUsed;

    @Override
    public void setData(T data) {
        this.data = data;
    }
    
    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setActor(Actor actor) {
        this.actor = actor;
    }

    @Override
    public void update(float tpf) {
        timeUsed += tpf;
        if (timeUsed >= data.getInterval()) {
            timeUsed = 0;
            doLogic(tpf);
        }
    }
    
    protected abstract void doLogic(float tpf);

}
