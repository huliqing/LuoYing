/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.game;

import com.jme3.app.SimpleApplication;

/**
 * 
 * @author huliqing
 */
public class SimpleGame extends AbstractGame {

    @Override
    public void gameInit() {
        if (app instanceof SimpleApplication) {
            throw new UnsupportedOperationException("The simpleGame only supported SimpleApplication, app=" 
                    + app.getClass().getName());
        }
        
        // 把场景根节点分别添加到app根节点下面
        SimpleApplication sa = (SimpleApplication) app;
        sa.getRootNode().attachChild(scene.getRoot());
        sa.getGuiNode().attachChild(guiScene.getRoot());
        
    }
}
