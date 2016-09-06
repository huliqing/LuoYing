/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import com.jme3.scene.control.Control;
import com.jme3.util.SafeArrayList;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ActorLogicData;
import name.huliqing.core.data.ModuleData;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actorlogic.ActorLogic;
import name.huliqing.core.object.attribute.BooleanAttribute;

/**
 * 逻辑控制器，控制角色的所有逻辑的运行。
 * @author huliqing
 */
public class LogicModule extends AbstractModule {
    private final ActorService actorService = Factory.get(ActorService.class);
    private final AttributeService attributeService = Factory.get(AttributeService.class);

    private Actor actor;
    private Control updateControl;
    private final SafeArrayList<ActorLogic> logics = new SafeArrayList<ActorLogic>(ActorLogic.class);

    // 属性：控制逻辑开关
    private String bindAutoLogicAttribute;
    // 控制 : 自动侦察敌人,这只是一个开关
    private String bindAutoDetectAttribute;
    
    private BooleanAttribute autoLogicAttribute;
    private BooleanAttribute autoDetectAttribute;

    @Override
    public void setData(ModuleData data) {
        super.setData(data);
        bindAutoLogicAttribute = data.getAsString("bindAutoLogicAttribute");
        bindAutoDetectAttribute = data.getAsString("bindAutoDetectAttribute");
    }
    
    @Override
    public void initialize(Actor actor) {
        super.initialize(actor);
        this.actor = actor;
        updateControl = new AdapterControl() {
            @Override
            public void update(float tpf) {logicUpdate(tpf);}
        };
        this.actor.getSpatial().addControl(updateControl);
        
        // 载入逻辑
        List<ActorLogicData> logicDatas = actor.getData().getObjectDatas(ActorLogicData.class, null);
        if (logicDatas != null) {
            for (ActorLogicData ld : logicDatas) {
                addLogic((ActorLogic) Loader.load(ld));
            }
        }
        
        // 绑定“自动AI”属性
        autoLogicAttribute = attributeService.getAttributeByName(actor, bindAutoLogicAttribute);
        // 自动侦察敌人
        autoDetectAttribute = attributeService.getAttributeByName(actor, bindAutoDetectAttribute);
    }
    
    private void logicUpdate(float tpf) {
        if (actorService.isDead(actor)) {
            return;
        }
        if (!isAutoLogic()) {
            return;
        }
        for (ActorLogic logic : logics.getArray()) {
            logic.update(tpf);
        }
    }

    @Override
    public void cleanup() {
        for (ActorLogic logic : logics) {
            logic.cleanup();
        }
        logics.clear();
        if (updateControl != null) {
            actor.getSpatial().removeControl(updateControl);
        }
        super.cleanup(); 
    }

    public void addLogic(ActorLogic logic) {
        if (logics.contains(logic))
            return;
        
        logics.add(logic);
        actor.getData().addObjectData(logic.getData());
        
        logic.setActor(actor);
        logic.setLogicModule(this);
        logic.initialize();
    }

    public boolean removeLogic(ActorLogic logic) {
        if (!logics.contains(logic))
            return false;
        
        actor.getData().removeObjectData(logic.getData());
        logics.remove(logic);
        logic.cleanup();
        return true;
    }
    
    public List<ActorLogic> getLogics() {
        return logics;
    }
    
    /**
     * 判断是否打开角色逻辑AI功能
     * @return 
     */
    public boolean isAutoLogic() {
        if (autoLogicAttribute != null) {
            return autoLogicAttribute.booleanValue();
        }
        return false;
    }
    
    /**
     * 设置是否打开逻辑功能
     * @param autoAi 
     */
    public void setAutoLogic(boolean autoAi) {
        if (autoLogicAttribute != null) {
            autoLogicAttribute.setValue(autoAi);
        }
    }
    
    /**
     * 判断是否打开“自动侦察敌人”
     * @return 
     */
    public boolean isAutoDetect() {
        if (autoDetectAttribute != null) {
            return autoDetectAttribute.booleanValue();
        }
        return false;
    }
    
    /**
     * 设置是否自动侦察敌人。
     * @param autoDetect 
     */
    public void setAutoDetect(boolean autoDetect) {
        if (autoDetectAttribute != null) {
            autoDetectAttribute.setValue(autoDetect);
        }
    }
    
    
}
