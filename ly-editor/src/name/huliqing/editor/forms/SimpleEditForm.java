/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.forms;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import name.huliqing.editor.Editor;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 *
 * @author huliqing
 */
public class SimpleEditForm extends EditForm {

//    private final CollisionResults pickResults = new CollisionResults();
    
    private final Node root = new Node();
    
    @Override
    public void initialize(Editor editor) {
        super.initialize(editor);
        localRoot.attachChild(root);
        
        root.setLocalTranslation(
                FastMath.nextRandomInt(-5, 5)
                ,FastMath.nextRandomInt(0, 5)
                ,FastMath.nextRandomInt(-5, 5)
        );
        root.setLocalScale(
                FastMath.nextRandomInt(-5, 5)
                ,FastMath.nextRandomInt(-5, 5)
                ,FastMath.nextRandomInt(-5, 5)
        );
//        root.rotate(FastMath.nextRandomFloat() * FastMath.TWO_PI
//                    , FastMath.nextRandomFloat() * FastMath.TWO_PI
//                    , FastMath.nextRandomFloat() * FastMath.TWO_PI);
        
        
        Spatial sinbad = editor.getAssetManager().loadModel("Models/test/Sinbad.mesh.j3o");
        root.attachChild(sinbad);
        root.setLocalScale(0.2f);
        
        // test
        for (int i = 0; i < 10; i++) {
            Box box = new Box(0.5f, 0.5f, 0.5f);
//            Sphere box = new Sphere(20, 20, 1);
            Geometry ge = new Geometry("", box);
            ge.setMaterial(MaterialUtils.createLighting(ColorRGBA.Gray));
            
            ge.setLocalTranslation(FastMath.nextRandomInt(-5, 5)
                    , FastMath.nextRandomInt(-5, 5)
                    , FastMath.nextRandomInt(-5, 5));
            
            ge.rotate(0, FastMath.PI / 2f, 0);
            
            ge.rotate(FastMath.nextRandomFloat() * FastMath.TWO_PI
                    , FastMath.nextRandomFloat() * FastMath.TWO_PI
                    , FastMath.nextRandomFloat() * FastMath.TWO_PI);
            
            root.attachChild(ge);
        }
        localRoot.addLight(new DirectionalLight(new Vector3f(0,-1,0), new ColorRGBA(1,1,1,1).multLocal(1.5f)));
        localRoot.addLight(new AmbientLight(new ColorRGBA(1,1,1,1).multLocal(2)));
    }

//    @Override
//    public void onPick() {
//        pickResults.clear();
//        PickManager.pick(editor.getCamera(), editor.getInputManager().getCursorPosition(), root, pickResults);
//        if (pickResults.size() > 0) {
//            Spatial picked = pickResults.getClosestCollision().getGeometry();
//            setSelected(new SpatialSelectObj(picked));
//        }
//    }

    
}
