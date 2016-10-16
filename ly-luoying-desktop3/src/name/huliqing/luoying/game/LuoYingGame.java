/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.game;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.game.SimpleGame;
import name.huliqing.luoying.object.module.ActorModule;
import name.huliqing.luoying.object.scene.Scene;

/**
 *
 * @author huliqing
 */
public class LuoYingGame extends SimpleGame {
    
    public LuoYingGame() {
        this.data = Loader.loadData(IdConstants.SYS_GAME);
    }

    @Override
    public void initialize(Application app) {
        super.initialize(app); 
        
        Scene treasureScene = Loader.load("sceneTreasure");
        setScene(treasureScene);
        
        Actor actor = Loader.load("actorSkeleton");
        scene.addEntity(actor);
        actor.getEntityModule().getModule(ActorModule.class).setLocation(new Vector3f(0, 10, 0));
        
    }
    
    
}
