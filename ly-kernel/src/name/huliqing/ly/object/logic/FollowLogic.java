/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.logic;

import com.jme3.math.FastMath;
import name.huliqing.ly.Factory;
import name.huliqing.ly.object.action.FollowAction;
import name.huliqing.ly.data.LogicData;
import name.huliqing.ly.layer.service.ActionService;
import name.huliqing.ly.layer.service.AttributeService;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.object.action.Action;
import name.huliqing.ly.object.attribute.NumberAttribute;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.utils.MathUtils;

/**
 * 跟随逻辑
 * @author huliqing
 * @param <T>
 */
public class FollowLogic<T extends LogicData> extends Logic<T> {
//    private final static Logger logger = Logger.getLogger(FollowLogic.class.getName());
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActionService actionService = Factory.get(ActionService.class);
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    
    // 距离的最近点和最远点
    private float minFollow = 3f;
    private float maxFollow = 7f;
    
    private FollowAction followAction;
    private Entity target;
    // 最近一次跟随到最近的距离
    private float lastFollowUsed;
    
    private String bindFollowTargetAttribute;
    private NumberAttribute followTargetAttribute;
    
    @Override
    public void setData(T data) {
        super.setData(data); 
        followAction = (FollowAction) actionService.loadAction(data.getAsString("followAction"));
        maxFollow = data.getAsFloat("maxFollow", maxFollow);
        minFollow = data.getAsFloat("minFollow", minFollow);
        
        maxFollow = MathUtils.clamp(maxFollow, 0, maxFollow);
        minFollow = MathUtils.clamp(minFollow, 0, maxFollow);
        
        bindFollowTargetAttribute = data.getAsString("bindFollowTargetAttribute");
                
        // remove20160310,考虑不要这个参数,这会导致角色在跟随和拔刀之间不停来回切换，很难看。
//        distanceLimit = data.getAsFloat("distanceLimit", distanceLimit);
//        distanceLimitSquared = distanceLimit * distanceLimit;
    }

    @Override
    public void initialize() {
        super.initialize();
        followTargetAttribute = attributeService.getAttributeByName(actor, bindFollowTargetAttribute);
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }
    
    @Override
    protected void doLogic(float tpf) {
        if (followTargetAttribute == null) 
            return;
        
        // 如果角色没有设置跟随的目标,则停止当前的跟随行为(注:只停止当前逻辑启动的followAction).
        long ft = followTargetAttribute.longValue();
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
        if (actionService.isPlayingFight(actor) 
                
                 // remove20160310,不要这个参数,这会导致角色在跟随和拔刀之间不停来回切换，很难看。
//                && target.getDistanceSquared(self) < distanceLimitSquared
                
                ) {
            return;
        }
        
        // 跟随
        doFollow();
    }
    
    private void doFollow() {
        if (followAction.isEnd() && actor.getSpatial().getWorldTranslation().distance(target.getSpatial().getWorldTranslation()) > maxFollow) {
            lastFollowUsed = FastMath.nextRandomFloat() * (maxFollow - minFollow) + minFollow;
            followAction.setFollow(target.getSpatial());
            followAction.setNearest(lastFollowUsed);
            actionService.playAction(actor, followAction);
        }
    }
    
    
}
