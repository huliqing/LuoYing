/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.logic;

import com.jme3.math.FastMath;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.object.action.FollowAction;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.layer.service.ActionService;
import name.huliqing.luoying.object.action.Action;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.attribute.ValueChangeListener;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.utils.MathUtils;

/**
 * 跟随逻辑
 * @author huliqing
 */
public class FollowLogic extends AbstractLogic implements ValueChangeListener<Object>{
//    private final static Logger logger = Logger.getLogger(FollowLogic.class.getName());
    private final ActionService actionService = Factory.get(ActionService.class);
    
    private FollowAction followAction;
    // 距离的最近点和最远点
    private float minFollow = 3f;
    private float maxFollow = 7f;
    
    // 这个属性中的值存放当前角色的跟随对象.
    private String bindFollowAttribute;
    
    // ---- inner
    private NumberAttribute followAttribute;
    
    private Entity target;
    // 最近一次跟随到最近的距离
    private float lastFollowUsed;
    
    @Override
    public void setData(LogicData data) {
        super.setData(data); 
        followAction = (FollowAction) actionService.loadAction(data.getAsString("followAction"));
        maxFollow = data.getAsFloat("maxFollow", maxFollow);
        minFollow = data.getAsFloat("minFollow", minFollow);
        
        maxFollow = MathUtils.clamp(maxFollow, 0, maxFollow);
        minFollow = MathUtils.clamp(minFollow, 0, maxFollow);
        
        bindFollowAttribute = data.getAsString("bindFollowAttribute");
    }
    
    @Override
    public void initialize() {
        super.initialize();
        followAttribute = actor.getAttributeManager().getAttribute(bindFollowAttribute);
        // 不监听followAttribute也可以，但是因为逻辑功能可能会有间隔和延迟的可能，监听可以让跟随对象发生变化时立即
        // 触发跟随行为的变化。
        if (followAttribute != null) {
            followAttribute.addListener(this);
        }
    }
    
    @Override
    public void cleanup() {
        if (followAttribute != null) {
            followAttribute.removeListener(this);
        }
        super.cleanup();
    }

    @Override
    public void onValueChanged(Attribute attribute) {
        if (attribute == followAttribute) {
            doLogic(0); // 直接调用doLogic去改变跟随对象。
        }
    }
    
    @Override
    protected void doLogic(float tpf) {
        if (followAttribute == null) 
            return;
        
        // 如果角色没有设置跟随的目标,则停止当前的跟随行为(注:只停止当前逻辑启动的followAction).
        long ft = followAttribute.longValue();
        if (ft <= 0) {
            target = null;
            Action current = actionService.getPlayingAction(actor);
            if (current == followAction) {
                actionService.playAction(actor, null);
            }
            return;
        }
        
        // 如果跟随的目标发生变化则重新查找目标进行跟随.找不到指定目标则不处理
        if (target == null || target.getData().getUniqueId() != ft) {
            target = actor.getScene().getEntity(ft);
            if (target == null || target == actor) {
                return;
            }
        }
        
        // 如果距离超过MaxFollowDistance则直接转到跟随,不管是否在战斗
        if (actionService.isPlayingFight(actor)) {
            return;
        }
        
        // 跟随
        doFollow();
    }
    
    private void doFollow() {
        if (followAction.isEnd() && actor.getSpatial().getWorldTranslation()
                .distance(target.getSpatial().getWorldTranslation()) > maxFollow) {
            lastFollowUsed = FastMath.nextRandomFloat() * (maxFollow - minFollow) + minFollow;
            followAction.setFollow(target.getSpatial());
            followAction.setNearest(lastFollowUsed);
            actionService.playAction(actor, followAction);
        }
    }
    
    
}
