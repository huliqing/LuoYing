package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import name.huliqing.ly.Ly;
import name.huliqing.ly.LyException;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.env.CameraChaseEnv;
import name.huliqing.ly.object.module.ActorModule;
import name.huliqing.ly.object.module.LogicModule;
import name.huliqing.ly.object.scene.Scene;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        try {
            // 1.初始化数据
            loadData();
        } catch (LyException ex) {
            throw new RuntimeException(ex);
        }
        
        Scene scene = Loader.load("sceneTreasure");
        scene.setSceneRoot(this.rootNode);
        stateManager.attach(scene);
        
        Actor actor = Loader.load("actorPlayerTest");
        actor.initialize();
        actor.getModule(LogicModule.class).setAutoLogic(false);
        actor.getModule(ActorModule.class).setLocation(new Vector3f(0, 10, 0));
        scene.addSceneObject(actor.getSpatial());
        
        
        CameraChaseEnv camEvn = (CameraChaseEnv) scene.getEnv("envCameraThirdView");
        if (camEvn != null) {
            camEvn.setChase(actor.getSpatial());
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    
    private void loadData() throws LyException {
        
        Ly.loadData("/data/object/action.xml");
        Ly.loadData("/data/object/actor.xml");
        Ly.loadData("/data/object/actorAnim.xml");
        Ly.loadData("/data/object/anim.xml");
        Ly.loadData("/data/object/attribute.xml");
        Ly.loadData("/data/object/bullet.xml");
        Ly.loadData("/data/object/channel.xml");
        Ly.loadData("/data/object/chat.xml");
        Ly.loadData("/data/object/config.xml");
        Ly.loadData("/data/object/define.xml");
        Ly.loadData("/data/object/drop.xml");
        Ly.loadData("/data/object/effect.xml");
        Ly.loadData("/data/object/el.xml");
        Ly.loadData("/data/object/emitter.xml");
        Ly.loadData("/data/object/env.xml");
        Ly.loadData("/data/object/game.xml");
        Ly.loadData("/data/object/gameLogic.xml");
        Ly.loadData("/data/object/hitChecker.xml");
        Ly.loadData("/data/object/item.xml");
        Ly.loadData("/data/object/logic.xml");
        Ly.loadData("/data/object/magic.xml");
        Ly.loadData("/data/object/module.xml");
        Ly.loadData("/data/object/position.xml");
        Ly.loadData("/data/object/resist.xml");
        Ly.loadData("/data/object/scene.xml");
        Ly.loadData("/data/object/shape.xml");

        // 技能
        Ly.loadData("/data/object/skill.xml");
        Ly.loadData("/data/object/skill_monster.xml");
        Ly.loadData("/data/object/skill_skin.xml");

        // 装备、武器
        Ly.loadData("/data/object/skin.xml");
        Ly.loadData("/data/object/skin_male.xml");
        Ly.loadData("/data/object/skin_weapon.xml");

        // 武器槽位配置
        Ly.loadData("/data/object/slot.xml");

        Ly.loadData("/data/object/sound.xml");
        Ly.loadData("/data/object/state.xml");
        Ly.loadData("/data/object/talent.xml");
        Ly.loadData("/data/object/task.xml");
        Ly.loadData("/data/object/view.xml");
        
        
        Ly.initialize(this, settings);
    }
}
