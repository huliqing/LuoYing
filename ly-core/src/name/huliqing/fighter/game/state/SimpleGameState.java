/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state;

import name.huliqing.fighter.game.view.ActorSelectView;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.util.SafeArrayList;
import com.jme3.util.TempVars;
import java.util.List;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.network.UserCommandNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.GameService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.SkillService;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.game.state.LanPlayStateUI;
import name.huliqing.fighter.game.view.ActorSelectView.SelectedListener;
import name.huliqing.fighter.game.view.TeamView;
import name.huliqing.fighter.manager.HUDManager;
import name.huliqing.fighter.manager.PickManager;
import name.huliqing.fighter.manager.PickManager.PickResult;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.manager.ShortcutManager;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.actor.ActorControl;
import name.huliqing.fighter.object.anim.Anim;
import name.huliqing.fighter.object.bullet.BulletCache;
import name.huliqing.fighter.object.effect.EffectCache;
import name.huliqing.fighter.object.game.Game.GameListener;
import name.huliqing.fighter.object.view.View;
import name.huliqing.fighter.ui.AbstractUI;
import name.huliqing.fighter.utils.MyChaseCamera;
import name.huliqing.fighter.utils.Temp;
import name.huliqing.fighter.ui.UI;
import name.huliqing.fighter.ui.UIEventListener;
import name.huliqing.fighter.ui.state.PickListener;
import name.huliqing.fighter.ui.state.UIState;
import name.huliqing.fighter.utils.SceneUtils;

/**
 *
 * @author huliqing
 */
public  class SimpleGameState extends GameState implements UIEventListener {
//    private final static Logger logger = Logger.getLogger(StoryState.class.getName());
    private final UserCommandNetwork userCommandNetwork = Factory.get(UserCommandNetwork.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final GameService gameService = Factory.get(GameService.class);
    
    protected Node localRoot;
    
    // ----场景动态对象
    // 当前场景中的所有角色,包含所有玩家,需要同步到客户端
    protected final SafeArrayList<Actor> actors = new SafeArrayList<Actor>(Actor.class);
    // 当前场景中的所有视图组件,与UI不同，这些组件需要同步到客户端
    protected final SafeArrayList<View> views = new SafeArrayList<View>(View.class);
    // 动画对象，update逻辑中不负责清理，需要由Animation2内部结束后自行清理
    protected final SafeArrayList<Anim> animations = new SafeArrayList<Anim>(Anim.class);
    
    // 基本界面
    protected LanPlayStateUI ui;
    // 场景跟随相机
    protected MyChaseCamera chaseCamera;

    // 角色选择面板
    protected ActorSelectView actorPanel;
    // 玩家当前玩家
    protected Actor player;
    
    // 最近一个选中的角色和最近一次选择的时间，主要用于处理双击选择攻击目标。
    private Actor lastPicked;
    private long lastPickTime;

    public SimpleGameState(GameData gameData) {
        super(gameData);
    }
    
    public SimpleGameState(String gameId) {
        super(gameId);
    }
    
    @Override
    public void initialize(AppStateManager stateManager, final Application app) {
        super.initialize(stateManager, app); 
        
        // initialize all node;
        localRoot = new Node("LPS_localRoot");
        this.app.getRootNode().attachChild(localRoot);
        
        // TODO: UI在这里进行了手动初始化，有些不合理，后续考虑重构
        ui = new LanPlayStateUI(this);
        ui.initialize();
        addObject(ui, false);
        
        // ==== 3.初始化事件绑定
        bindPickListener();
        
        // ==== 4.子弹、特效缓存器
        addListener(BulletCache.getInstance());
        addObject(BulletCache.getInstance(), false);
        
        addListener(EffectCache.getInstance());
        addObject(EffectCache.getInstance(), false);
        
        // ==== 5.UI全局事件监听器，主要处理当UI被点击或拖动时不要让镜头跟着转动。
        UIState.getInstance().addEventListener(this);
        
         // ==== 6.快捷方式和HUD初始化
         ShortcutManager.init();
         HUDManager.init(this.app.getGuiNode());
         
         // ==== 7.载入游戏
        game.addListener(new GameListener() {
            @Override
            public void onSceneLoaded() {
                // remove,resetCollision在手机上莫明奇妙的无效。 所以统一为重新创建chaseCamera
                //chaseCamera.resetCollision(bulletAppState.getPhysicsSpace(), scene.getTerrain());
                
                if (chaseCamera != null) {
                    chaseCamera.cleanup();
                    chaseCamera = null;
                }
                
                chaseCamera = SceneUtils.createChaseCam(app.getCamera()
                        , app.getInputManager()
                        , game.getScene().getPhysicsSpace());
                
                // 如果玩家角色已经提前载入则跟随主角
                Actor player = getPlayer();
                if (player != null) {
                    chaseCamera.setChase(player.getModel());
                } else if (actorPanel != null) {
                    chaseCamera.setChase(actorPanel.getActorView());
                    chaseCamera.setDefaultDistance(5f);
                }
            }
        });
    }
    
    /**
     * 显示角色选择面板
     * @param selectableActors 
     */
    @Override
    public void showSelectPanel(List<String> selectableActors) {
        if (actorPanel == null) {
            actorPanel = new ActorSelectView(Common.getSettings().getWidth()
                    , Common.getSettings().getHeight()
                    , app.getGuiNode()
                    );
            actorPanel.setSelectedListener(new SelectedListener() {
                @Override
                public void onSelected(String actorId, String actorName) {
                    onSelectPlayer(actorId, actorName);
                    actorPanel.removeFromParent();
                    actorPanel.getActorView().removeFromParent();
                }
            });
        }
        actorPanel.setModels(selectableActors);
        
        UIState.getInstance().addUI(actorPanel);
        
        localRoot.attachChild(actorPanel.getActorView());
        
        if (chaseCamera != null) {
            chaseCamera.setChase(actorPanel.getActorView());
            chaseCamera.setDefaultDistance(5f);
        }
    }
    
    protected void onSelectPlayer(String actorId, String actorName) {  
        userCommandNetwork.selectPlayer(actorId, actorName);
        // 在选择完角色之后要重新显示所有UI
        setUIVisiable(true);
    }
    
    /**
     * 绑定鼠标点击事件
     */
    protected void bindPickListener() {
        UIState.getInstance().putPickListener("ObjectPick", new PickListener() {
            @Override
            public boolean pick(boolean isPressed, float tpf) {
                if (!isPressed) {
                    
                    // 优化点选角色
                    Actor actorPicked = pickActor();
                    if (actorPicked != null && onPickedActor(actorPicked)) {
                        return true;
                    }
                    
                    // 场景可能还未载入
                    if (game.getScene() == null || !game.getScene().isInitialized()) {
                        return false;
                    }
                    
                    // 不使用localRoot节为根节点进行ray检测, localRoot里面包含了所有actor,而actors已经在onPickedActor中处理
                    PickManager.PickResult pr = PickManager.pick(
                            app.getInputManager(), app.getCamera(), game.getScene().getRoot());
                    if (pr != null && onPicked(pr)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }
    
    private Actor pickActor() {
        
        TempVars tv = TempVars.get();
        Temp tp = Temp.get();
        
        Vector2f v2d = tv.vect2d.set(getApp().getInputManager().getCursorPosition());
        Vector3f click3d = getApp().getCamera().getWorldCoordinates(v2d, 0, tv.vect1);
        Vector3f dir = getApp().getCamera().getWorldCoordinates(v2d, 1, tv.vect2)
                .subtractLocal(click3d).normalizeLocal();
        
        Ray ray = tp.ray;
        ray.setOrigin(click3d);
        ray.setDirection(dir);
        
        List<Actor> cList = tp.list1;
        cList.clear();
        
        // 获取所有被射线选择到的角色．
        boolean playerPickable = canPickedPlayer();
        for (Actor a : actors) {
            // 这可以防止选择到玩家自身控制的角色。
            if (!playerPickable && a == getPlayer()) {
                continue;
            }
            if (a.getModel().getWorldBound().intersects(ray)) {
                cList.add(a);
            }
        }
        
        // 查找离相机最近的目标
        float minDistance = Float.MAX_VALUE;
        Vector3f camLoc = getApp().getCamera().getLocation();
        float tempDistance;
        Actor result = null;
        for (Actor a : cList) {
            tempDistance = a.getModel().getWorldBound().getCenter().distanceSquared(camLoc);
            if (tempDistance < minDistance) {
                minDistance = tempDistance;
                result = a;
            }
        }
        
        tp.release();
        tv.release();
        return result;
    }
    
    @Override
    public void cleanup() {
        // 清理Spatial
        if (localRoot != null) {
            localRoot.removeFromParent();
            localRoot = null;
        }
        
        // 清理actors
        for (Actor a : actors) {
            a.cleanup();
        }
        actors.clear();
        
        // 清理View
        views.clear();
        
        // 清理animations
        animations.clear();
        
        // 重要：清理相机数据
        if (chaseCamera != null) {
            chaseCamera.cleanup();
            chaseCamera = null;
        }
        ShortcutManager.cleanup();
        HUDManager.cleanup();
        super.cleanup();
    }
    
    /**
     * 注：该方法不判断目标spatial是否存在于GUI中, 也就是说：对GUI无效。
     * @param spatial
     * @return 
     */
    @Override
    public boolean isInScene(Spatial spatial) {
        Spatial p = spatial.getParent();
        if (p == null) {
            return false;
        } else {
            if (p == localRoot || p == UIState.getInstance().getUIRoot()) {
                return true;
            } else {
                return isInScene(p);
            }
        }
    }

    @Override
    public List<Actor> getActors() {
        return actors;
    }

    @Override
    public List<View> getViews() {
        return views;
    }
    
    @Override
    public void addObject(Object object, boolean gui) {
        super.addObject(object, gui);
        // 作为角色添加
        if (object instanceof Actor) {
            addActor((Actor) object, gui);
            return;
        }
        
        // 作为活动角色添加
        if (object instanceof View) {
            View view = (View) object;
            if (!views.contains(view)) {
                views.add(view);
            }
            return;
        }
        
        // 作为UI添加
        if (object instanceof UI) {
            addUI((UI) object, gui);
            return;
        }
        
        if (object instanceof Anim) {
            Anim anim = (Anim) object;
            if (!animations.contains(anim)) {
                animations.add(anim);
            }
        }
        
        // ================= 如果不是spatial物体，则不再处理 ===================
        if (!(object instanceof Spatial)) {
            return;
        }
        Spatial spatialObject = (Spatial) object;
        
        // 因为actor也可以使用spatial来添加，所以这里要兼容
        if (spatialObject.getControl(ActorControl.class) != null) {
            addActor(spatialObject.getControl(ActorControl.class), gui);
            return;
        } 
        
        // 作为其它物体添加,如：效果，子弹
        addSimpleObj(spatialObject, gui);
        
    }
    
    @Override
    public final void removeObject(Object object) {
        super.removeObject(object);
        
        PhysicsControl physicsControl = null;
        Actor actor = null;
        Spatial spatial = null;
        if (object instanceof Actor) {
            actor = (Actor) object;
            physicsControl = actor.getModel().getControl(PhysicsControl.class);
        } else if (object instanceof Spatial) {
            spatial = (Spatial) object;
            actor = spatial.getControl(ActorControl.class);
            physicsControl = spatial.getControl(PhysicsControl.class);
        } 
        
        // 移出bulletAppState
        if (physicsControl != null) {
            if (game.getScene().getPhysicsSpace() != null) {
                game.getScene().getPhysicsSpace().remove(object);
            }
        }
        
        // 根据类型移出场景
        if (actor != null) {
            // 把角色从队伍移除（如果存在队伍中）
            ui.getTeamView().removeActor(actor);
            // 移出场景
            actor.getModel().removeFromParent();
            // 销毁角色，释放资源
            actor.cleanup();
            // 移出列表
            actors.remove(actor);
            return;
        }
        
        if (object instanceof View) {
            // add20160217
            // 清理View并移出列表,以后其它Object都参考View的运行方式进行处理.
            // 所有待移除的Object的清理(cleanup)都应该放在PlayState这里进行。
            View view = (View) object;
//            view.cleanup(); // remove20160218
            views.remove(view);
            return;
        }
        
        if (object instanceof Anim) {
            Anim anim = (Anim) object;
            anim.cleanup();
            animations.remove(anim);
            return;
        }
        
        if (spatial != null) {
            // 其它类型，如UI,effect,bullet,magic等,不需要处理其它额外逻辑。
            spatial.removeFromParent();
        }
        
    }
    
    private void addUI(UI ui, boolean gui) {
        if (gui) {
//            UIState.getInstance().addUI(ui.getDisplay());
            UIState.getInstance().addUI((AbstractUI)ui);
        } else {
            this.localRoot.attachChild(ui.getDisplay());
        }
    }
    
    private boolean addActor(Actor actor, boolean gui) {
        // 防止添加的角色掉到地面以下。
        Vector3f pos = actor.getModel().getWorldTranslation();
        pos.setY(playService.getTerrainHeight(pos.x, pos.z));
        
        // 让动态角色产生时的Y坐标值稍微增加一点，可避免角色加入场景时与地面
        // 发生碰撞而产生的弹跳现象
        if (actor.getMass() > 0) {
            pos.addLocal(0, 0.2f, 0);
        }
        // 设置位置应该放在加入场景之后
        actor.setLocation(pos);
        if (!actors.contains(actor)) {
            actors.add(actor);
        }
        // 不要将角色或object添加到scene中，因为scene会进行缓存，以免忘记清理的时候
        // 在再次进入state时还残留角色。
        if (gui) {
            UIState.getInstance().addUI(actor.getModel());
        } else {
            localRoot.attachChild(actor.getModel());
        }
        
        // 把model添加到physics中
        if (game.getScene().getPhysicsSpace() != null) {
            game.getScene().getPhysicsSpace().add(actor.getModel());
        }
        
        // 如果角色有指定队伍，则应该处理是否在当前队伍列表中。
        if (actor.getData().getTeam() > 0) {
            ui.getTeamView().checkAddOrRemove(actor);
        }
        
        return true;
    }

    private void addSimpleObj(Spatial spatial, boolean gui) {
        if (spatial.getControl(PhysicsControl.class) != null) {
            if (game.getScene().getPhysicsSpace() != null) {
                game.getScene().getPhysicsSpace().add(spatial);
            }
        }
        // 不要将角色或object添加到scene中，因为scene会进行缓存，以免忘记清理的时候
        // 在再次进入state时还残留角色。
        if (gui) {
            UIState.getInstance().addUI(spatial);
        } else {
            localRoot.attachChild(spatial);
        }
    }
    
    @Override
    public void addMessage(String message, MessageType messageType) {
        HUDManager.showMessage(message, messageType.getColor());
    }
    
    @Override
    public Actor getTarget() {
        return ui.getTargetFace().getActor();
    }

    @Override
    public void setTarget(Actor target) {
        if (target != null) {
            ui.setTargetFace(target);
        }
    }
    
    @Override
    public void setPlayer(Actor actor) {
        if (player != null) {
            player.setPlayer(false);
        }
        player = actor;
        player.setPlayer(true);
        ui.getTeamView().clearPartners();
        ui.getTeamView().setMainActor(player);
        ui.getTeamView().setTeamId(player.getData().getTeam());
        List<Actor> _actors = getActors();
        if (_actors != null) {
            for (Actor a : _actors) {
                ui.getTeamView().checkAddOrRemove(a);
            }
        }
        
        // 相机跟随
        setChase(player.getModel());
    }
    
    @Override
    public Actor getPlayer() {
        return player;
    }
    
    /**
     * 让相机跟随目标
     * @param spatial 
     */
    protected void setChase(Spatial spatial) {
        if (chaseCamera != null) {
            chaseCamera.setChase(spatial);
        }
    }
    
    /**
     * 是否允许选择玩家角色，如果不允许的话，则当点击场景时player角色将会被
     * 过滤掉，也就是说可以穿过player选择到其后面的其它角色．
     * @return 
     */
    protected boolean canPickedPlayer() {
        return false;
    }
    
    /**
     * 当角色被选中时的操作,如果方法返回true,则阻止后续的选择。如果略过当前
     * 角色的选择，则可返回false.
     * @param actor 被选中的角色。
     * @return 
     */
    protected boolean onPickedActor(Actor actor) {
        if (actor != null) {
            // 界面选择目标
            setTarget(actor);
            
            // 允许角色面板显示宠物的包裹
            // 注：只有debug时才允许显示其它角色的面板，否则会导致可以控制他人使用物品的问题
            if (Config.debug || 
                    (player != null && actorService.getOwner(actor) == player.getData().getUniqueId())) {
                ui.getUserPanel().setActor(actor);
            }
            
            // 判断是否双击,如果是双击，则调用攻击
            if (lastPicked == actor && Common.getGameTime() - lastPickTime <= 400) {
                attack();
            }
            lastPicked = actor;
            lastPickTime = Common.getGameTime();
            
            return true;
        }
        return false;
    }
    
    /**
     * 覆盖方法来监听当场景中某些物品被选中时的操作。
     * 如果当前已经处理了“选择”操作，可返回true来阻止后续其它“选择”监听的操
     * 作，如果当前不做任何处理，则直接返回false来超过该监听。
     * 注: 只有pickable的物体才可能被选择。
     * @param pr
     * @return 
     */
    protected boolean onPicked(PickResult pr) {
        // 选择地面进行行走
        if (pr.spatial == game.getScene().getTerrain() && player != null) {
            userCommandNetwork.playRunToPos(player, pr.result.getContactPoint());
            return true;
        }
        return false;
    }
    
    @Override
    public void attack() {
        if (player == null) 
            return;
        
        Actor temp = getTarget();
        
        // 没有目标，或者目标已经不在战场，则重新查找
        if (temp == null 
                || temp == player
                || temp.isDead() 
                || !isInScene(temp.getModel())
                || !temp.isEnemy(player)
                ) {
            float distance = actorService.getViewDistance(player) * 2;
            temp = actorService.findNearestEnemyExcept(player, distance, null);
            
            // 需要
            setTarget(temp);
        }
        
        // 即使temp为null也可以攻击，这允许角色转入自动攻击（等待）状态
        if (temp == null) {
            addMessage(ResourceManager.get("common.noTarget"), MessageType.notice);
        }
        
        userCommandNetwork.attack(player, temp);
    }

    @Override
    public void setUIVisiable(boolean visiable) {
        CullHint ch = visiable ? CullHint.Never : CullHint.Always;
        List<Spatial> children = UIState.getInstance().getUIRoot().getChildren();
        for (Spatial child : children) {
            child.setCullHint(ch);
        }
        // 快捷管理器中的“回收站”始终是关闭的，只有在拖动快捷方式时才可见
        ShortcutManager.setBucketVisible(false);
    }

    @Override
    public TeamView getTeamView() {
        return ui.getTeamView();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        
        // 更新动画
        if (!animations.isEmpty()) {
            for (Anim anim : animations.getArray()) {
                anim.update(tpf);
            }
        }
        
    }

    @Override
    public void UIClick(UI ui, boolean isPressed, boolean dbclick) {
        setChaseEnabled(false);
    }

    @Override
    public void UIDragStart(UI ui) {
        setChaseEnabled(false);
//        app.getInputManager().setCursorVisible(true);
    }
    
    @Override
    public void UIDragEnd(UI ui) {
        setChaseEnabled(true);
    }

    @Override
    public void UIRelease(UI ui) {
        setChaseEnabled(true);
    }
    
    private void setChaseEnabled(boolean enabled) {
        // 注意：这里只让相机停止旋转就可以,因为目前当UI打开时还不支持暂停(即打开
        // 用户界面时角色可能还在走动,镜头也可能还在跟随和旋转).
        // 为了避免在打开用户面板拖动一些UI组件时3D镜头跟着旋转的不舒服现象提供了这个方法．用于在打
        // 开UI面板时可以暂时关闭镜头跟随和旋转,在关闭UI后再重新开启，目前只要关闭旋转就可以，
        // 不能同时关闭跟随．
        // 因为如果人在走动，这时候如果按下鼠标不放,在关闭跟随的情况下会发现人
        // 一直远去(向前走动)，当相机重新跟随的时候，会突然移到角色旁边，过渡太过不自然．
        // 如果只关闭旋转，而保持跟随，就不会出现该现象，也就是该功能只是为了避免
        // 在按下鼠标拖动(UI)的时候同时出现3D镜头在旋转的问题．
        if (chaseCamera != null) {
            chaseCamera.setEnabledRotation(enabled);
        }
    }

//    @Override
//    public void changeGame() {
//        // 如果已有游戏在运行则要清理
//        // 1.清理旧场景信息
//        // 2.清理场景角色
//        // 3.重新给玩家类角色初始化物理特性
//        final List<Actor> players = new ArrayList<Actor>();
//        if (game != null) {
//            // 清理场景
//            removeObject(game);
//            // 清理角色
//            for (Actor actor : actors.getArray()) {
//                removeObject(actor);
//                if (actor.isPlayer()) {
//                    players.add(actor);
//                }
//            }
//        }
//        
//        game = gameService.loadGame(gameData);
//        game.addListener(new GameListener() {
//            @Override
//            public void onSceneLoaded() {
//                // remove,resetCollision在手机上莫明奇妙的无效。 所以统一为重新创建chaseCamera
//                //chaseCamera.resetCollision(bulletAppState.getPhysicsSpace(), scene.getTerrain());
//                
//                // 1.重新加入玩家角色
//                for (Actor actor : players) {
//                    addActor(actor, false);
//                }
//                
//                // 2.重新生成镜头跟随
//                if (chaseCamera != null) {
//                    chaseCamera.cleanup();
//                    chaseCamera = null;
//                }
//                chaseCamera = SceneUtils.createChaseCam(app.getCamera()
//                        , app.getInputManager()
//                        , game.getScene().getPhysicsSpace());
//                
//                // 3.因为chaseCamera进行了重建，所以必须使它重新跟随玩家
//                Actor player = getPlayer();
//                if (player != null) {
//                    chaseCamera.setChase(player.getModel());
//                }
//            }
//        });
//        addObject(game, false);
//    }
    
//    /**
//     * 获取当前连接的所有客户端(该列表中包含主机)，如果没有则返回null.
//     * @return 
//     */
//    public abstract List<MessPlayClientData> getClients();
//    
//    /**
//     * 把一个客户端踢出游戏。
//     * @param connId 
//     */
//    public abstract void kickClient(int connId);

    @Override
    public MenuTool getMenuTool() {
        return ui.getMenuTool();
    }
}
