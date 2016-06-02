/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.atest;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import name.huliqing.fighter.Fighter;

/**
 *
 * @author huliqing
 */
public class Demo extends SimpleApplication{

     public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(960, 540);
        settings.setSamples(4);
        settings.setTitle("落樱之剑");
        settings.setFrameRate(40);
        
        Demo app = new Demo();
        app.setSettings(settings);
        app.setShowSettings(false);
        app.setPauseOnLostFocus(false);
        app.start();
        
    }
    
    @Override
    public void simpleInitApp() {
        
        // 载入标准骨骼
        Node ske = (Node) getAssetManager().loadModel("Models/actor/ske.mesh.j3o");
        rootNode.attachChild(ske);
        rootNode.addLight(new AmbientLight());
        
        // 载入基本皮肤
        Spatial foot = getAssetManager().loadModel("Models/actor/female/foot.000.mesh.j3o");
        Spatial lowerBody = getAssetManager().loadModel("Models/actor/female/lowerBody.000.mesh.j3o");
        Spatial upperBody = getAssetManager().loadModel("Models/actor/female/upperBody.000.mesh.j3o");
        Spatial hand = getAssetManager().loadModel("Models/actor/female/hand.000.mesh.j3o");
        Spatial face = getAssetManager().loadModel("Models/actor/female/face.000.mesh.j3o");
        
        // 组装角色（基本皮肤）
        ske.attachChild(foot);
        ske.attachChild(lowerBody);
        ske.attachChild(upperBody);
        ske.attachChild(hand);
        ske.attachChild(face);
        
        // 换装示例,比如换上“脚”装备
        Spatial footOutfit = getAssetManager().loadModel("Models/actor/female/foot.001.mesh.j3o");
        ske.detachChild(foot);      // 移除基本皮肤
        ske.attachChild(footOutfit);// 换上装备
    }
    
}
