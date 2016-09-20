/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.action;

import com.jme3.ai.navmesh.NavMeshPathfinder;
import com.jme3.ai.navmesh.Path;
import com.jme3.ai.navmesh.Path.Waypoint;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ActionData;
import name.huliqing.core.mvc.network.SkillNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.object.module.SkillModule;
import name.huliqing.core.object.skill.Skill;
import name.huliqing.core.utils.DebugDynamicUtils;
import name.huliqing.core.utils.ThreadHelper;

/**
 *
 * @author huliqing
 */
public class RunPathAction extends AbstractAction implements RunAction{
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
//    private final SkillService skillService = Factory.get(SkillService.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private SkillModule skillModule;

    // 需要走到的目标地址
    private final Vector3f position = new Vector3f();
    // 允许的最近距离，不能太小，太小会产生两个现象,特别是在侦率较低时容易出现。
    // 1.当角色瞬间太快穿过目标路径点时，可能一直走下去。
    // 2.角色可能在路径起点时的ViewDirection方向不正确，表现为瞬间前后转向。
    // 主要是由于新路径的第一个点可能产生在了角色身后而导致方向错误。
    private float nearest = 0.5f;
    
    // debug path 
    private boolean debug = false;
     
    // 避障
    private final Detour rayDetour = new RayDetour(this);
    private final Detour timeDetour = new TimeDetour(this);
    
    // 寻路
    private NavMeshPathfinder finder;
    private Path path;
    private Future<Boolean> future;
    // 角色当前正在走向的路径点
    private Waypoint current;
    
    // 路径方向修正的时间间隔，单位秒,每经过一定时间修正一下路径。
    // 因为角色可能在行走过程中被打断或影响了方向。
    private float fixInterval = 2;
    private float fixTimeUsed;
    
    // 缓存技能id
    private Skill runSkill;
    private Skill waitSkill;
    
    public RunPathAction() {
        super();
        finder = playService.createPathfinder();
    }
    
    @Override
    public void setData(ActionData data) {
        super.setData(data);
        debug = data.getAsBoolean("debug", debug);
        finder = playService.createPathfinder();
    }

    @Override
    public void initialize() {
        super.initialize();
        skillModule = actor.getModule(SkillModule.class);
        
        path = null;
        future = null;
        rayDetour.setActor(actor);
        rayDetour.setAutoFacing(true);
        
        timeDetour.setActor(actor);
        timeDetour.setAutoFacing(true);
        
        List<Skill> waitSkills = skillModule.getSkillWait(null);
        if (waitSkills != null && !waitSkills.isEmpty()) {
            waitSkill = waitSkills.get(0);
        }
        List<Skill> runSkills = skillModule.getSkillRun(null);
        if (runSkills != null && !runSkills.isEmpty()) {
            runSkill = runSkills.get(0);
        }
    }
    
    @Override
    public void cleanup() {
        if (finder != null) {
            finder.clearPath();
        }
        path = null;
        future = null;
        rayDetour.cleanup();
        timeDetour.cleanup(); 
        super.cleanup();
    }

    public void setFinder(NavMeshPathfinder finder) {
        this.finder = finder;
    }
    
    @Override
    public void doLogic(float tpf) {
        // 如果角色是不可移动的，则直接返回不处理逻辑
        if (!actorService.isMoveable(actor) || runSkill == null) {
            end();
            return;
        }
        
        // == 确定已经走到目标位置
        // .走近目标点
        // .或者走过头(该情况与目标会成大于90度的夹角）
        // 当前距离
        float distance = actor.getSpatial().getWorldTranslation().distance(position);
        if (distance <= nearest) {
            if (waitSkill != null) {
                skillNetwork.playSkill(actor, waitSkill, false);
            }
            end();
            return;
        }
        
        // 1.通过Ray偿试避开障碍，如果发现障碍，则让它偿试避开后再进行走动。
        if (rayDetour.detouring(tpf)) {
            return;
        }
        
        // 2.通过时间检测偿试避开障碍
        if (timeDetour.detouring(tpf)) {            
            return;
        }
        
        // find path;
        if (path == null && finder != null && future == null) {
            // 一开始不需要warp,只有找不到时才warp
            finder.clearPath();
            future = findPath(actor.getSpatial().getWorldTranslation(), position, false);
        }
        
        if (future != null) {
            checkAndFindPath();
        }
        
        if (path != null && path.getWaypoints().size() > 0) {
            runByPath();
        } else {
            // 无寻路信息的情况下走直线
            runByStraight();
        }
        
        // 修正路径,角色可能在走动过程中被移动了方向或被打断之类，如Detour这时需要修正方向,否则可能由于
        // 方向错误而导致永远走不到下一个路径点。
        fixTimeUsed += tpf;
        if (fixTimeUsed > fixInterval) {
            fixTimeUsed = 0;
            TempVars tv = TempVars.get();
            if (path != null && current != null) {
                skillNetwork.playWalk(actor
                        , runSkill.getData().getId()
                        , current.getPosition().subtract(actor.getSpatial().getWorldTranslation(), tv.vect1), true, false);
                
            } else {
                skillNetwork.playWalk(actor
                        , runSkill.getData().getId()
                        , position.subtract(actor.getSpatial().getWorldTranslation(), tv.vect1), true, false);
            }
            tv.release();
        }
        
    }
    
    private void runByStraight() {
        if (!skillModule.isRunning()) {
            TempVars tv = TempVars.get();
            skillNetwork.playWalk(actor, runSkill.getData().getId()
                    , position.subtract(actor.getSpatial().getWorldTranslation(), tv.vect1), true, false);
            tv.release();
        }
    }
    
    private void runByPath() {
        Waypoint tempPoint = null;
        try {
            tempPoint = getNextPoint();
        } catch (Exception e) {
            // 多线程情况下总可能会有极小的机率发生冲突。
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Could not get next way point!", e);
        }
        if (tempPoint == null) {
            return;
        }
        
        // 转换到下一个路径点
        if (tempPoint != current || !skillModule.isRunning()) {
            current = tempPoint;
            TempVars tv = TempVars.get();
            skillNetwork.playWalk(actor, runSkill.getData().getId()
                    , current.getPosition().subtract(actor.getSpatial().getWorldTranslation(), tv.vect1), true, false);
            tv.release();
        }
    }
    
    /**
     * 获取下一个与当前角色位置距离大于nearestDistance的下一个点。距离小于
     * nearestDistance的点被视为已经到达。
     * 并且由于pathFinder查到的路径点可能存在太近或者相同点的可能，
     * 这一步可以过滤掉这些相同的点。
     * @return 
     */
    private Path.Waypoint getNextPoint() {
        Path.Waypoint waypoint = finder.getNextWaypoint();
        if (waypoint == null || finder.getPath().getWaypoints().size() <= 0 || finder.isAtGoalWaypoint()) 
            return null;
        
        // 将Y值移到与角色相同的Y值再比较距离，避免漂浮在空中的路径点无法比较
        TempVars temp = TempVars.get();
        temp.vect1.set(waypoint.getPosition());
        temp.vect1.setY(actor.getSpatial().getWorldTranslation().y);
        float distance = temp.vect1.distance(actor.getSpatial().getWorldTranslation());
        temp.release();
        
        if (distance < 1f) {
            finder.goToNextWaypoint();
            return getNextPoint();
        }
        return waypoint;
    }
    
    private void checkAndFindPath() {
//        Logger.get(getClass()).log(Level.INFO, "CheckAndFindPath. actor={0}", actor.getModel().getName());
        if (future.isDone()) {
            try {
                Boolean result = future.get();
                if (!result) {

                    Vector3f newStartPos = actor.getSpatial().getWorldTranslation();
                    finder.clearPath();
                    future = findPath(newStartPos, position, true);

//                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Path not found, now warp the start position and try again. new startPos={0}", newStartPos);
                } else {
                    path = finder.getPath();
                    future = null; // 获得线路后清空
                    if (debug) {
                        DebugDynamicUtils.debugPath(actor.getSpatial().getName() + toString(), path.getWaypoints());
                    }
                }
            } catch (Exception ex) {
                // 重要：这个异常必须处理。
//                Logger.getLogger(getClass().getName()).log(Level.WARNING, "actorName={}, error={1}"
//                        , new Object[] {actor.getModel().getName(),ex.getMessage()});
                
                if (future != null) {
                    future.cancel(true);
                }
                future = null;
            }
        }
    }
    
    private Future findPath(final Vector3f start, final Vector3f end, final boolean warp) {
        Future temp = ThreadHelper.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (warp) {
                    finder.warp(start);
                } else {
                    finder.setPosition(start);
                }
                Boolean result = finder.computePath(end.clone());
                return result;
            }
        });
        return temp;
    }

    @Override
    public void setPosition(Vector3f position) {
        // 防止重复设置导致不停进行寻路
        if (this.position.distance(position) <= nearest) {
            return;
        }
        this.position.set(position);
        this.path = null;
        this.future = null;
    }

    @Override
    public void setNearest(float nearest) {
        this.nearest = nearest;
        if (this.nearest < 0) {
            this.nearest = 0;
        }
    }

    @Override
    public boolean isInPosition(Vector3f pos) {
        if (actor == null) {
            return false;
        }
        return actor.getSpatial().getWorldTranslation().distance(pos) <= nearest;
    }

    @Override
    public boolean isEndPosition() {
        return isInPosition(position);
    }

}
