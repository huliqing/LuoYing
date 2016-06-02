///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.game.state.test2;
//
//import com.jme3.app.Application;
//import com.jme3.app.state.AppStateManager;
//import com.jme3.math.FastMath;
//import com.jme3.math.Vector3f;
//import java.util.ArrayList;
//import java.util.List;
//import name.huliqing.fighter.game.service.Service;
//import name.huliqing.fighter.utils.SceneUtils;
//import name.huliqing.fighter.loader.Loader;
//import name.huliqing.fighter.object.actor.Actor;
//import name.huliqing.fighter.constants.IdConstants;
//import name.huliqing.fighter.enums.MessageType;
//import name.huliqing.fighter.enums.SkillType;
//import name.huliqing.fighter.enums.WeaponType;
//import name.huliqing.fighter.game.service.ActorService;
//import name.huliqing.fighter.game.service.SkillService;
//import name.huliqing.fighter.game.service.StateService;
//import name.huliqing.fighter.game.state.BasePlayStateUI;
//import name.huliqing.fighter.game.state.SimplePlayState;
//import name.huliqing.fighter.game.view.actor.ActorPanelControl;
//import name.huliqing.fighter.logic.scene.ActorBuildSimpleLogic;
//import name.huliqing.fighter.manager.PickManager;
//import name.huliqing.fighter.manager.ResourceManager;
//import name.huliqing.fighter.object.skill.Skill;
//import name.huliqing.fighter.scene.Scene;
//import name.huliqing.fighter.scene.TestScene;
////import name.huliqing.fighter.object.skill.SkillChecker;
////import name.huliqing.fighter.object.skill.SkillChecker.CheckResult;
//import name.huliqing.fighter.ui.UI;
//
///**
// *
// * @author huliqing
// */
//public class TestState extends SimplePlayState {
//    private ActorService actorService = Factory.get(ActorService.class);
//    private StateService stateService = Factory.get(StateService.class);
//    
//    private BasePlayStateUI ui;
//    private Actor player;
//    
//    @Override
//    public void initialize(AppStateManager stateManager, final Application app) {
//        super.initialize(stateManager, app); 
//        
//        // 载入UI状态
//        ui = new BasePlayStateUI(this);
//        ui.getAttack().setOnClick(new UI.Listener() {
//            @Override
//            public void onClick(UI ui, boolean isPress) {
//                if (!isPress) {
//                    attack();
//                }
//            }
//        });
//        ui.getAttack().setDragEnabled(true);
//        
//        player = Loader.loadActor("actorTest");
//        player.getModel().setName("test");
//        player.setLocation(new Vector3f(0, 10, 0));
//        player.setAutoAi(false);
//        player.setPlayer(true);
//        player.setLevel(2);
//        addObject(player.getModel());
//        setChase(player.getModel());
//        ui.setPlayerFace(player);
//        
//        ActorBuildSimpleLogic refresh = new ActorBuildSimpleLogic();
//        refresh.addBuilder(new Vector3f(30, 0, 30), new String[]{IdConstants.ACTOR_NINJA}, 0);
//        this.addLogic(refresh);
//        
//        
//    }
//
//    @Override
//    protected boolean onPicked(PickManager.PickResult pr) {
//        // 选择地面进行行走
//        if (pr.spatial == getTerrain()) {
//            player.playActionRun(pr.result.getContactPoint());
//            player.setAutoAi(false);
//            return true;
//        }
//        // 选择角色
//        Actor actor = actorService.getActor(pr.spatial);
//        if (actor != null && actor != player) {
//            setTarget(actor);
//            
//            if (actor.getOwner() == player.getData().getUniqueId()) {
//                ui.getUserPanel().setActor(actor);
//            }
//            return true;
//        }
//        return false;
//    }
//    
//    @Override
//    protected boolean onPickedActor(Actor actor) {
//        return false;
//    }
//    
//    public void attack() {
//        WeaponType wt = player.getWeaponData().getWeaponType();
//        Skill skill = player.getData().getSkillStore().getAttackSkillRandom(wt);
//        attackBySkill(skill);
//    }
//    
//    // 魔法
//    public void attackTrick() {
//        List<Skill> store = new ArrayList<Skill>();
//        WeaponType wt = player.getWeaponData().getWeaponType();
//        player.getData().getSkillStore().getSkills(SkillType.attack_shot, wt, store);
//        if (store.isEmpty()) return;
//        Skill skill = store.get(FastMath.nextRandomInt(0, store.size() - 1));
//        
//        SkillService skillService = Factory.get(SkillService.class);
//        if (!skillService.isPlayable(player, skill, true)) {
//            return;
//        }
//        attackBySkill(skill);
//        
////        CheckResult cr = SkillChecker.check(player, skill);
////        if (cr != CheckResult.ok) {
////            // 技能不可用
////            addMessage(ResourceManager.get(cr.getErrorMessageId()), MessageType.warn);
////            return;
////        }
//    }
//    
//    private void attackBySkill(Skill skill) {
//        Actor temp = getTarget();
//        // 没有目标，或者目标已经不在战场，则重新查找
//        if (temp == null 
//                || temp.getModel().getParent() == null 
//                || temp.isDead() 
//                || !temp.isEnemy(player)) {
//            temp = actorService.findNearestEnemyExcept(player, stateService.getAiViewDistance(player) * 2, null);
//            setTarget(temp);
//        }
//        if (temp != null) {
//            player.playActionFight(temp, skill);
//            player.setAutoAi(true);
//        } else {
//            addMessage(ResourceManager.get("common.noTarget"), MessageType.warn);
//        }
//    }
//    
//    public void displayUserPanel(Actor actor) {
//        ActorPanelControl userPanel = ui.getUserPanel();
//        userPanel.setVisible(!userPanel.isVisible());
//        if (userPanel.isVisible()) {
//            userPanel.setActor(actor != null ? actor : player);
//        }
//    }
//    
//    @Override
//    public void update(float tpf) {
//        super.update(tpf);
//    }
//
//    @Override
//    public Actor getPlayer() {
//        return player;
//    }
//
//    @Override
//    protected Scene loadScene() {
//        return new TestScene();
//    }
//
//    @Override
//    public void setTarget(Actor target) {
//        super.setTarget(target);
//        if (target != null) {
//            ui.setTargetFace(target);
//        }
//    }
//
//
//
//    
//}
