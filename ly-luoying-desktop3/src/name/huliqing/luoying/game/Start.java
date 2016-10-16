/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.game;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.game.Game;
import name.huliqing.luoying.object.game.GameAppState;
import name.huliqing.luoying.object.module.ActorModule;

/**
 *
 * @author huliqing
 */
public class Start extends SimpleApplication {
    
    public static void main(String[] args) {
        
        AppSettings setting = new AppSettings(true);
        setting.setFrameRate(60);
        
        Start s = new Start();
        s.setSettings(setting);
        s.setShowSettings(false);
        s.start();
    }
    
    private void loadData() {
        
        // 载入系统数据
        LuoYing.initialize(this, settings);
        
        // 载入自定义数据
        try {
            LuoYing.loadData("/data/object/action.xml");
            LuoYing.loadData("/data/object/actor.xml");
            LuoYing.loadData("/data/object/actorAnim.xml");
            LuoYing.loadData("/data/object/anim.xml");
            LuoYing.loadData("/data/object/attribute.xml");
            LuoYing.loadData("/data/object/bullet.xml");
            LuoYing.loadData("/data/object/channel.xml");
            LuoYing.loadData("/data/object/chat.xml");
            LuoYing.loadData("/data/object/config.xml");
            LuoYing.loadData("/data/object/define.xml");
            LuoYing.loadData("/data/object/drop.xml");
            LuoYing.loadData("/data/object/effect.xml");
            LuoYing.loadData("/data/object/el.xml");
            LuoYing.loadData("/data/object/emitter.xml");
            LuoYing.loadData("/data/object/env.xml");
            LuoYing.loadData("/data/object/game.xml");
            LuoYing.loadData("/data/object/gameLogic.xml");
            LuoYing.loadData("/data/object/hitChecker.xml");
            LuoYing.loadData("/data/object/item.xml");
            LuoYing.loadData("/data/object/logic.xml");
            LuoYing.loadData("/data/object/magic.xml");
            LuoYing.loadData("/data/object/module.xml");
            LuoYing.loadData("/data/object/position.xml");
            LuoYing.loadData("/data/object/resist.xml");
            LuoYing.loadData("/data/object/scene.xml");
            LuoYing.loadData("/data/object/shape.xml");
            LuoYing.loadData("/data/object/skill.xml");
            LuoYing.loadData("/data/object/skill_monster.xml");
            LuoYing.loadData("/data/object/skill_skin.xml");
            LuoYing.loadData("/data/object/skin.xml");
            LuoYing.loadData("/data/object/skin_male.xml");
            LuoYing.loadData("/data/object/skin_weapon.xml");
            LuoYing.loadData("/data/object/slot.xml");
            LuoYing.loadData("/data/object/sound.xml");
            LuoYing.loadData("/data/object/state.xml");
            LuoYing.loadData("/data/object/talent.xml");
            LuoYing.loadData("/data/object/task.xml");
            LuoYing.loadData("/data/object/view.xml");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void simpleInitApp() {
        
        // 载入游戏数据
        loadData();
        
        // 载入游戏
        LuoYingGame game = new LuoYingGame();
        GameAppState gameState = new GameAppState(game);
        
        this.stateManager.attach(gameState); 
        this.flyCam.setMoveSpeed(100);
      
        
    }
    
}
