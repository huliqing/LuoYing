/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.editforms;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import name.huliqing.editor.formview.FormView;
import name.huliqing.editor.toolbar.EditToolbar;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 *
 * @author huliqing
 */
public class StartEditForm extends SimpleEditForm {

    private final Node root = new Node();
    
    @Override
    public void initialize(FormView formView) {
        super.initialize(formView);
        editRoot.attachChild(root);
        Spatial sinbad = formView.getEditor().getAssetManager().loadModel("Models/test/Sinbad.mesh.j3o");
        root.attachChild(sinbad);
        root.setLocalScale(0.2f);
        
        // test
        for (int i = 0; i < 10; i++) {
            Box box = new Box(0.5f, 0.5f, 0.5f);
//            Sphere box = new Sphere(20, 20, 1);
            Geometry ge = new Geometry("", box);
            ge.setMaterial(MaterialUtils.createLighting(ColorRGBA.Gray));
            
            ge.setLocalTranslation(FastMath.nextRandomInt(-20, 20)
                    , FastMath.nextRandomInt(0, 20)
                    , FastMath.nextRandomInt(-20, 20));
            
            ge.rotate(0, FastMath.PI / 2f, 0);
            
            ge.rotate(FastMath.nextRandomFloat() * FastMath.TWO_PI
                    , FastMath.nextRandomFloat() * FastMath.TWO_PI
                    , FastMath.nextRandomFloat() * FastMath.TWO_PI);
            
            root.attachChild(ge);
        }
        editRoot.addLight(new DirectionalLight(new Vector3f(0,-1,0), new ColorRGBA(1,1,1,1).multLocal(1.5f)));
        editRoot.addLight(new AmbientLight(new ColorRGBA(1,1,1,1).multLocal(2)));
        
         // 编辑用的工具栏
        setToolbar(new EditToolbar());
    }
    
}
