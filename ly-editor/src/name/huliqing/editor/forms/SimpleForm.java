/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.forms;

import com.jme3.collision.CollisionResults;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import name.huliqing.editor.Editor;
import name.huliqing.editor.select.SpatialSelectObj;
import name.huliqing.luoying.manager.PickManager;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 *
 * @author huliqing
 */
public class SimpleForm extends BaseForm {

    private final CollisionResults pickResults = new CollisionResults();
    
    private final Node root = new Node();
    
    @Override
    public void initialize(Editor editor) {
        super.initialize(editor);
        localRoot.attachChild(root);
        
        // test
        for (int i = 0; i < 10; i++) {
            Box box = new Box(0.5f, 0.5f, 0.5f);
            Geometry ge = new Geometry("", box);
            ge.setMaterial(MaterialUtils.createUnshaded(ColorRGBA.Gray));
            
            ge.setLocalTranslation(FastMath.nextRandomInt(-5, 5)
                    , FastMath.nextRandomInt(-5, 5)
                    , FastMath.nextRandomInt(-5, 5));
            
            ge.rotate(FastMath.nextRandomFloat(), FastMath.nextRandomFloat(), FastMath.nextRandomFloat());
            root.attachChild(ge);
        }
    }

    @Override
    public void onPick() {
        pickResults.clear();
        PickManager.pick(editor.getInputManager(), editor.getCamera(), root, pickResults);
        if (pickResults.size() > 0) {
            Spatial picked = pickResults.getClosestCollision().getGeometry();
            setSelectObj(new SpatialSelectObj(picked));
        }
    }
    
    
}
