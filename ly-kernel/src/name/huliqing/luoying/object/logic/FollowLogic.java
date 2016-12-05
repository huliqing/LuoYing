/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.logic;

import com.jme3.math.FastMath;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.object.action.FollowAction;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.layer.service.ActionService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.action.Action;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.attribute.ValueChangeListener;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ActionModule;
import name.huliqing.luoying.utils.MathUtils;

/**
 * 跟随逻辑
 * @author huliqing
 */
public class FollowLogic extends AbstractLogic implements ValueChangeListener {
    private final static Logger LOG = Logger.getLogger(FollowLogic.class.getName());
    private final ActionService actionService = Factory.get(ActionService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    
    private FollowAction followAction;
    // 距离的最近点和最远点
    private float minFollow = 3f;
    private float maxFollow = 7f;
    
    // 这个属性中的值存放当前角色的跟随对象.
    private String bindFollowAttribute;
    
    // ---- inner
    private NumberAttribute followAttribute;
    
    // 最近一个跟随的目标对象
    private Entity lastFollow;
    // 最近一次跟随到最近的距离
    private float lastFollowDistance;
    
    private ActionModule actionModule;
    
    @Override
    public void setData(LogicData data) {
        super.setData(data); 
        followAction = (FollowAction) Loader.load(data.getAsString("followAction"));
        maxFollow = data.getAsFloat("maxFollow", maxFollow);
        minFollow = data.getAsFloat("minFollow", minFollow);
        
        maxFollow = MathUtils.clamp(maxFollow, 0, maxFollow);
        minFollow = MathUtils.clamp(minFollow, 0, maxFollow);
        
        bindFollowAttribute = data.getAsString("bindFollowAttribute");
    }
    
    @Override
    public void initialize() {
        super.initialize();
        actionModule = actor.getModuleManager().getModule(ActionModule.class);
        followAttribute = actor.getAttributeManager().getAttribute(bindFollowAttribute);
        // 不监听followAttribute也可以，但是因为逻辑功能可能会有间隔和延迟的可能，监听可以让跟随对象发生变化时立即
        // 触发跟随行为的变化。
        if (followAttribute != null) {
            followAttribute.addListener(this);
        }
        checkToFollow();
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
        checkToFollow();
    }
    
    private void checkToFollow() {
        if (followAttribute == null) 
            return;
        
        // 获取跟随对象。
        Entity newTarget = playService.getEntity(followAttribute.longValue());
        
        // 如果跟随目标不存在或者目标是自己，则清除跟随
        if (newTarget == null || newTarget == actor) {
            endFollow();
        } else {
            startFollow(newTarget);
        }
    }
    
    @Override
    protected void doLogic(float tpf) {
        if (followAttribute == null) 
            return;
        
        if (lastFollow == null) 
            return;
        
        // 跟随
        if (followAction.isEnd() && actor.getSpatial().getWorldTranslation().distance(lastFollow.getSpatial().getWorldTranslation()) > maxFollow) {
           startFollow(lastFollow);
        }
    }
    
    private void startFollow(Entity target) {
        LOG.log(Level.INFO, "Start follow, follower={0}, target={1}"
                , new Object[] {actor.getData().getId(), target.getData().getId()});
        lastFollow = target;
        lastFollowDistance = FastMath.nextRandomFloat() * (maxFollow - minFollow) + minFollow;
        followAction.setFollow(lastFollow.getSpatial());
        followAction.setNearest(lastFollowDistance);
        actionModule.startAction(followAction);
    }
    
    private void endFollow() {
        LOG.log(Level.INFO, "End follow, follower={0}, target={1}"
                , new Object[] {actor.getData().getId(), lastFollow != null ? lastFollow.getData().getId() : null});
        lastFollow = null;
        Action current = actionService.getPlayingAction(actor);
        if (current == followAction) {
            actionModule.startAction(null);
        }
    }
    
}
