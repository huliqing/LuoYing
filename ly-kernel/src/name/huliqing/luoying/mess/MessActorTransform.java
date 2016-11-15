/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.util.TempVars;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 用于动态同步角色的位置,使用各种方式来平滑同步客户端与服务端角色的位置
 * @author huliqing
 */
@Serializable
public class MessActorTransform extends MessBase {
//    private static final Logger LOG = Logger.getLogger(MessActorTransform.class.getName());
    
    private long actorId = -1;
    private Vector3f location = new Vector3f();
    private Vector3f walkDirection = new Vector3f();
    private Vector3f viewDirection = new Vector3f();
    
    public MessActorTransform() {}

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }
    
    public Vector3f getLocation() {
        return location;
    }

    public void setLocation(Vector3f location) {
        this.location.set(location);
    }

    public Vector3f getWalkDirection() {
        return walkDirection;
    }

    public void setWalkDirection(Vector3f walkDirection) {
        this.walkDirection.set(walkDirection);
    }

    public Vector3f getViewDirection() {
        return viewDirection;
    }

    public void setViewDirection(Vector3f viewDirection) {
        this.viewDirection.set(viewDirection);
    }
    
    // ------v5,测试中
    @Override
    public void applyOnClient() {
        super.applyOnClient();
        PlayService playService = Factory.get(PlayService.class);
        ActorService actorService = Factory.get(ActorService.class);
        Entity actor = playService.getEntity(actorId);
        if (actor == null) {
            return;
        }
        
        TempVars tv = TempVars.get();
//        Vector3f sourcePos = tv.vect1.set(actor.getLocation()); // remove
        Vector3f sourcePos = tv.vect1.set(actor.getSpatial().getWorldTranslation());
        
        Vector3f targetPos = tv.vect2.set(location);
        float distanceSquared = sourcePos.distanceSquared(targetPos);
        // 1.如果距离太小，在向量计算上容易出现意料之外的错误,所以不处理,直接同步位置
        if (distanceSquared < 0.0001f) {
//            if (Config.debug) {
//                LOG.log(Level.INFO, "--MATransform:........，距离太小，绝对同步:actor={0},distance={1}"
//                        , new Object[]{actor.getModel().getName(), FastMath.sqrt(distanceSquared)});
//            }
            
            actorService.setLocation(actor, location);
            actorService.setWalkDirection(actor, walkDirection);
            actorService.setViewDirection(actor, viewDirection);

            tv.release();
            return;
        }
        
        Vector3f targetDirection = tv.vect3.set(targetPos).subtractLocal(sourcePos);
        Vector3f sourceWalkDirection = tv.vect4.set(actorService.getWalkDirection(actor));
        
        // 2.如果距离大于一定程度，这个时候要判断WalkDirection的偏移程度，如果是
        // 在接近平行的情况下，即基本没有偏移的情况，则仍然可以使用拉伸的方式，
        // 这会产生比较平滑的视觉体验。如果角度偏移太大，则应该立即绝对同步，否则
        // 由于已经产生比较大的偏移的原因会越拉越大。
        // 注：这里的distanceSquared=4.0(distance=2.0)是按照角色的跑步速度进行大
        // 概的估算的，普通跑动速度6.0/perSec，2.0/6.0=0.333,即大概可以处理延迟330毫秒
        // ，超过这个延迟时:
        // A.如果WalkDirection偏移太大则直接绝对同步，这会让画面上的角色产生跳跃感。
        // B.如果基本没有偏移则仍然使用拉伸方式进行处理（由步骤3处理），这会产生平滑感。
        if (distanceSquared > 4.0f) {
            // 如果超偏移太大则直接绝对同步
            Vector3f targetDirectionNor = tv.vect5.set(targetDirection).normalizeLocal();
            Vector3f sourceWalkDirectionNor = tv.vect6.set(actorService.getWalkDirection(actor)).normalizeLocal();
            float d2 = sourceWalkDirectionNor.dot(targetDirectionNor);
            if ((d2 < 0.95f && d2 > -0.95f)) {
//                if (Config.debug) {
//                    LOG.log(Level.INFO, "--MATransform:OOOOOOOO，距离和偏移太大，直接绝对同步:actor={0},distance={1},d2={2}"
//                            , new Object[]{actor.getModel().getName(), FastMath.sqrt(distanceSquared), d2});
//                }

                actorService.setLocation(actor, location);
                actorService.setWalkDirection(actor, walkDirection);
                actorService.setViewDirection(actor, viewDirection);
                
                tv.release();
                return;
            }
        }
        
        // 3.平滑拉伸,这会有一个比较好的体验效果
        // 这里用targetDirection.setY(0)，以避免把角色拉到空中去，这经常发生在当
        // 主机FPS比较低，但是客户端FPS非常高的情况下，客户端角色走动时经常会被拉到空中
//        sourceWalkDirection.addLocal(targetDirection.setY(0));
        sourceWalkDirection.addLocal(targetDirection).setY(0);
        
        actorService.setWalkDirection(actor, sourceWalkDirection);
        actorService.setViewDirection(actor, viewDirection);
        
        tv.release();
    }
    
//    // ------v4,测试中
//    @Override
//    public void applyOnClient() {
//        PlayService playService = Factory.get(PlayService.class);
//        Entity actor = playService.findActor(actorId);
//        if (actor == null) {
//            return;
//        }
//        
//        TempVars tv = TempVars.get();
//        Vector3f sourcePos = tv.vect1.set(actor.getLocation());
//        Vector3f targetPos = tv.vect2.set(location);
//        float distanceSquared = sourcePos.distanceSquared(targetPos);
//        // 1.如果距离太小，在向量计算上容易出现意料之外的错误,所以不处理,直接同步位置
//        if (distanceSquared < 0.0001f) {
//            if (Config.debug) {
//                LOG.log(Level.INFO, "--MATransform:........，距离太小，绝对同步:actor={0},distance={1}"
//                        , new Object[]{actor.getModel().getName(), FastMath.sqrt(distanceSquared)});
//            }
//            actor.setLocation(location);
//            actor.setWalkDirection(walkDirection);
//            actor.setViewDirection(viewDirection);
//            tv.release();
//            return;
//        }
//        
//        Vector3f targetDirection = tv.vect3.set(targetPos).subtractLocal(sourcePos);
//        Vector3f sourceWalkDirection = tv.vect4.set(actor.getWalkDirection());
//        
//        // 2.如果距离大于一定程度，这个时候要判断WalkDirection的偏移程度，如果是
//        // 在接近平行的情况下，即基本没有偏移的情况，则仍然可以使用拉伸的方式，
//        // 这会产生比较平滑的视觉体验。如果角度偏移太大，则应该立即绝对同步，否则
//        // 由于已经产生比较大的偏移的原因会越拉越大。
//        // 注：这里的distanceSquared=4.0(distance=2.0)是按照角色的跑步速度进行大
//        // 概的估算的，普通跑动速度6.0/perSec，2.0/6.0=0.333,即大概可以处理延迟330毫秒
//        // ，超过这个延迟时:
//        // A.如果WalkDirection偏移太大则直接绝对同步，这会让画面上的角色产生跳跃感。
//        // B.如果基本没有偏移则仍然使用拉伸方式进行处理（由步骤3处理），这会产生平滑感。
//        if (distanceSquared > 4.0f) {
//            // 如果超偏移太大则直接绝对同步
//            Vector3f targetDirectionNor = tv.vect5.set(targetDirection).normalizeLocal();
//            Vector3f sourceWalkDirectionNor = tv.vect6.set(actor.getWalkDirection()).normalizeLocal();
//            float d2 = sourceWalkDirectionNor.dot(targetDirectionNor);
//            if ((d2 < 0.95f && d2 > -0.95f)) {
//                if (Config.debug) {
//                    LOG.log(Level.INFO, "--MATransform:OOOOOOOO，距离和偏移太大，直接绝对同步:actor={0},distance={1},d2={2}"
//                            , new Object[]{actor.getModel().getName(), FastMath.sqrt(distanceSquared), d2});
//                }
//                actor.setLocation(location);
//                actor.setWalkDirection(walkDirection);
//                actor.setViewDirection(viewDirection);
//                tv.release();
//                return;
//            }
//        }
//        
//        // 3.平滑拉伸,这会有一个比较好的体验效果
//        sourceWalkDirection.addLocal(targetDirection);
//        actor.setWalkDirection(sourceWalkDirection);
//        actor.setViewDirection(viewDirection);
//        tv.release();
//    }
    
    
    
//    // ------v3,稍微满意一点的版本，但是在延迟太高的时候仍然会有不舒服的跳跃感。
//    @Override
//    public void applyOnClient() {
//        PlayService playService = Factory.get(PlayService.class);
//        Entity actor = playService.findActor(actorId);
//        if (actor == null) {
//            return;
//        }
//        
//        TempVars tv = TempVars.get();
//        Vector3f sourcePos = tv.vect1.set(actor.getLocation());
//        Vector3f targetPos = tv.vect2.set(location);
//        float distanceSquared = sourcePos.distanceSquared(targetPos);
//        // 1.如果距离太小，在向量计算上容易出现意料之外的错误,所以不处理,直接同步位置
//        // 2.如果距离大于一定程度，可能由于WalkDirection的角度偏移太大，拉伸后产生的问题。
//        // 这个时候也直接绝对同步，避免越拉越大。
//        if (distanceSquared < 0.0001f || distanceSquared > 1.0f) {
//            if (Config.debug) {
//                LOG.log(Level.INFO, "--MATransform:........，绝对同步:actor={0},distance={1}"
//                        , new Object[]{actor.getModel().getName(), FastMath.sqrt(distanceSquared)});
//            }
//            actor.setLocation(location);
//            actor.setWalkDirection(walkDirection);
//            actor.setViewDirection(viewDirection);
//            tv.release();
//            return;
//        }
//        
//        Vector3f targetDirection = tv.vect3.set(targetPos).subtractLocal(sourcePos);
//        Vector3f sourceWalkDirection = tv.vect5.set(actor.getWalkDirection());
//        sourceWalkDirection.addLocal(targetDirection);
//        
//        actor.setWalkDirection(sourceWalkDirection);
//        actor.setViewDirection(viewDirection);
//        
//        tv.release();
//    }
    
    
    
    
    // ------v2,这个版本在延迟稍大的时候，拉伸效果会比较难看，并且拉伸方向偏移太过大。
//    @Override
//    public void applyOnClient() {
//        PlayService playService = Factory.get(PlayService.class);
//        Entity actor = playService.findActor(actorId);
//        if (actor == null) {
//            return;
//        }
//        
//        TempVars tv = TempVars.get();
//        Vector3f sourcePos = tv.vect1.set(actor.getLocation());
//        Vector3f targetPos = tv.vect2.set(location);
//        float distanceSquared = sourcePos.distanceSquared(targetPos);
//        // 如果距离太小，在向量计算上容易出现意料之外的错误,所以不处理,直接同步
//        // 位置
//        if (distanceSquared < 0.0001f) {
//            if (Config.debug) {
//                LOG.log(Level.INFO, "--MATransform:........，绝对同步:actor={0},distance={1}"
//                        , new Object[]{actor.getModel().getName(), FastMath.sqrt(distanceSquared)});
//            }
//            actor.setLocation(location);
//            actor.setWalkDirection(walkDirection);
//            actor.setViewDirection(viewDirection);
//            tv.release();
//            return;
//        }
//        
//        Vector3f targetDirection = tv.vect3.set(targetPos).subtractLocal(sourcePos);
//        Vector3f sourceWalkDirection = tv.vect5.set(actor.getWalkDirection());
//        sourceWalkDirection.addLocal(targetDirection);
//        
//        actor.setWalkDirection(sourceWalkDirection);
//        actor.setViewDirection(viewDirection);
//        
//        tv.release();
//    }
    
    
    
    
    // ------v1,这个方法在客户端会发生比较强烈的抖动（在延迟较高的Android手机上）
//    @Override
//    public void applyOnClient() {
//        PlayService playService = Factory.get(PlayService.class);
//        Entity actor = playService.findActor(actorId);
//        if (actor == null) {
//            return;
//        }
//        
//        // source为当前客户端角色的状态,必须把位置定在同一个水平面上才能比较
//        TempVars tv = TempVars.get();
//        Vector3f sourcePos = tv.vect1.set(actor.getLocation());
//        Vector3f targetPos = tv.vect2.set(location);
//        float distanceSquared = sourcePos.distanceSquared(targetPos);
//        // 如果距离太小，在向量计算上容易出现意料之外的错误,所以不处理,直接同步
//        // 位置
//        if (distanceSquared < 0.01f) {
//            if (Config.debug) {
//                LOG.log(Level.INFO, "--MATransform:........，绝对同步:actor={0},distance={1}"
//                        , new Object[]{actor.getModel().getName(), FastMath.sqrt(distanceSquared)});
//            }
//            actor.setLocation(location);
//            actor.setWalkDirection(walkDirection);
//            actor.setViewDirection(viewDirection);
//            tv.release();
//            return;
//        }
//        
//        Vector3f targetDirection = tv.vect3.set(targetPos).subtractLocal(sourcePos);
//        Vector3f targetDirectionNor = tv.vect7.set(targetDirection).normalizeLocal();
//        Vector3f sourceWalkDirection = tv.vect5.set(actor.getWalkDirection());
//        Vector3f sourceWalkDirectionNor = tv.vect6.set(actor.getWalkDirection()).normalizeLocal();
//        float d2 = sourceWalkDirectionNor.dot(targetDirectionNor);
//        if ((d2 > 0.9f || d2 < -0.9f)) {
//            if (Config.debug) {
//                LOG.log(Level.INFO, "--MATransform:========，拉伸位置:actor={0},d2={1}"
//                        , new Object[]{actor.getModel().getName(), d2});
//            }
//            // 只有在当前角色前进方向和当前角色与目标同步位置是在接近同一水平方向上时才进行拉伸，
//            // 否则拉伸会出现不正确
//            sourceWalkDirection.addLocal(targetDirection);
//            actor.setWalkDirection(sourceWalkDirection);
//            actor.setViewDirection(viewDirection);
//        } else {
//            if (Config.debug) {
//                LOG.log(Level.INFO, "--MATransform:<<<<>>>>，同步绝对位置:actor={0},d2={2}"
//                        , new Object[]{actor.getModel().getName(), d2});
//            }
//            // 直接同步
//            actor.setLocation(location);
//            actor.setWalkDirection(walkDirection);
//            actor.setViewDirection(viewDirection);
//        }
//        tv.release();
//    }
    

    
}
