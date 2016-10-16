package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import mygame.object.RpgGame;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.GameData;
import name.huliqing.luoying.loader.GameDataLoader;
import name.huliqing.luoying.object.env.ChaseCameraEnv;
import name.huliqing.luoying.xml.DataFactory;

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
        LuoYing.loadData(INPUT_MAPPING_EXIT);
        
        ChaseCameraEnv camEvn = (ChaseCameraEnv) scene.getEnv("envCameraThirdView");
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
    
    private void DefineData() {
        DataFactory.register("gameRpg", GameData.class, GameDataLoader.class, RpgGame.class);
    }
}
