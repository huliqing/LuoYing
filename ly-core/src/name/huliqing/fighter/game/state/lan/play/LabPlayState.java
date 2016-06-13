/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.play;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.manager.PickManager;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.constants.IdConstants;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.data.SceneData;
import name.huliqing.fighter.enums.Sex;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.SkillService;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.game.state.lan.mess.MessPlayClientData;
import name.huliqing.fighter.enums.SkillType;
import name.huliqing.fighter.game.service.SceneService;
import name.huliqing.fighter.object.DataFactory;

/**
 * 测试
 * @author huliqing
 */
public class LabPlayState extends LanPlayState {
    
    private final ActorService actorService = Factory.get(ActorService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final SceneService sceneService = Factory.get(SceneService.class);
    
    private Actor npc1;
    private Actor npc2;
    private int level = 60;
    
    private String[] actorIds = new String[] {
        IdConstants.ACTOR_HARD
        ,IdConstants.ACTOR_HARD
        ,IdConstants.ACTOR_SINBAD
    };

    public LabPlayState(GameData gameData) {
        super(gameData);
    }
    
    @Override
    public void initialize(AppStateManager stateManager, final Application app) {
        super.initialize(stateManager, app);
        
        // 载入场景
//        SceneData sceneData = DataLoaderFactory.createSceneData(IdConstants.SCENE_TREASURE);
//        SceneData sceneData = DataFactory.createData(IdConstants.SCENE_TREASURE);
//        initScene(sceneData);
        
        // 载入NPC
        npc1 = loadActor(FastMath.nextRandomInt(0, actorIds.length - 1));
        npc1.setLocation(new Vector3f(10, 1, 0));
        actorService.setGroup(npc1, 1);
        
        npc2 = loadActor(FastMath.nextRandomInt(0, actorIds.length - 1));
        npc2.setLocation(new Vector3f(-10, 1, 0));
        actorService.setGroup(npc2, 2);
        
        addObject(npc1.getModel(), false);
        addObject(npc2.getModel(), false);
        
        ui.getTeamView().setMainActor(npc1);
        ui.setTargetFace(npc2);
        setChase(npc1.getModel());
        
        throw new UnsupportedOperationException("Need refactor");
    }
    
    private Actor loadActor(int idIndex) {
        Actor actor;
        if (idIndex <= 1) {
            actor = actorService.loadActor(actorIds[idIndex]);
            actorService.setName(actor, actorService.createRandomName(Sex.female));
        } else {
            actor = actorService.loadActor(actorIds[idIndex]);
        }
        actor.setLocation(new Vector3f(-5, 2, 0));
        actorService.setLevel(actor, level);
        skillService.playSkill(actor, skillService.getSkill(actor, SkillType.wait).getId(), false);
        return actor;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (npc1.getModel().getWorldTranslation().y < - 5) {
            npc1.setLocation(Vector3f.ZERO.clone());
        }
        if (npc2.getModel().getWorldTranslation().y < - 5) {
            npc2.setLocation(Vector3f.ZERO.clone());
        }
    }
    
    @Override
    protected boolean onPicked(PickManager.PickResult pr) {
        return false;
    }
    
    @Override
    protected boolean onPickedActor(Actor actor) {
        setChase(actor.getModel());
        return true;
    }

    @Override
    public List<MessPlayClientData> getClients() {
        // ignore
        return null;
    }

    @Override
    public void kickClient(int connId) {
        // ignore
    }
}
