/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.utils.modifier;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LodControl;
import java.util.logging.Level;
import java.util.logging.Logger;
import jme3tools.optimize.LodGenerator;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.utils.ModelFileUtils;

/**
 * 为所有模型生成LOD信息
 * @author huliqing
 */
public class LodUtils {
    
    public static void makeLod(String file, AssetManager am) {
        Spatial spatial;
        try {
            spatial = am.loadModel(file);
        } catch (Exception e) {
            Logger.getLogger(LodUtils.class.getName()).log(Level.WARNING, "Could not load model:{0}, error={1}", new String[] {file, e.getMessage()});
            return;
        }
        
        SceneGraphVisitor sgv = new SceneGraphVisitor() {
            @Override
            public void visit(Spatial spatial) {
                if (spatial instanceof Geometry) {
                    Geometry geo = (Geometry) spatial;
                    try {
                        LodGenerator lod = new LodGenerator(geo);
                        lod.bakeLods(LodGenerator.TriangleReductionMethod.PROPORTIONAL, 0.2f, 0.5f, 0.85f);
                        LodControl lodControl = geo.getControl(LodControl.class);
                        if (lodControl == null) {
                            Logger.getLogger(getClass().getName()).log(Level.INFO, "create control for:{0}", geo.getName());
                            lodControl = new LodControl();
                            geo.addControl(lodControl);
                        }
                    } catch (Exception e) {
                        Logger.getLogger(getClass().getName()).log(Level.WARNING, "Could not generate Lod for:{0}", geo.getName());
                    }
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Make lod for:{0} ok!", geo.getName());
                }
            }
        };
        spatial.depthFirstTraversal(sgv);
        ModelFileUtils.saveTo(spatial, file);
    }
    
    private static void makeTree(AssetManager am) {
        makeLod("Models/trees/dry/dry389.j3o", am);
        makeLod("Models/trees/dry/dry390.j3o", am);
        makeLod("Models/trees/dry/dry410.j3o", am);
        
        makeLod("Models/trees/fir/fir473.j3o", am);
        makeLod("Models/trees/fir/fir483.j3o", am);
        makeLod("Models/trees/fir/fir527.j3o", am);
        
        makeLod("Models/trees/flower/flower1.j3o", am);
        makeLod("Models/trees/flower/flower2.j3o", am);
        makeLod("Models/trees/flower/flower3.j3o", am);
        makeLod("Models/trees/flower/flower4.j3o", am);
        makeLod("Models/trees/flower/flower5.j3o", am);
        makeLod("Models/trees/flower/flower6.j3o", am);
        makeLod("Models/trees/flower/flower7.j3o", am);
        makeLod("Models/trees/flower/flower8.j3o", am);
        makeLod("Models/trees/flower/flower9.j3o", am);
        
        makeLod("Models/trees/palm/palm111.j3o", am);
        makeLod("Models/trees/palm/palm123.j3o", am);
        makeLod("Models/trees/palm/palm225.j3o", am);
        
        makeLod("Models/trees/tree/tree381.j3o", am);
        makeLod("Models/trees/tree/tree431.j3o", am);
        makeLod("Models/trees/tree/tree432.j3o", am);
        makeLod("Models/trees/tree/tree978.j3o", am);
    }
    
    private static void makeCharacter(AssetManager am) {
        // ear
        makeLod("Models/character/female_min2/ear/ear.000.mesh.j3o", am);
        
        // eye
        makeLod("Models/character/female_min2/eye/eye.000.mesh.j3o", am);
        
        // face
        makeLod("Models/character/female_min2/face/face.000.mesh.j3o", am);
        
        // foot
        makeLod("Models/character/female_min2/foot/foot.000.mesh.j3o", am);
        makeLod("Models/character/female_min2/foot/foot.001.mesh.j3o", am);
        makeLod("Models/character/female_min2/foot/foot.002.mesh.j3o", am);
        makeLod("Models/character/female_min2/foot/foot.003.mesh.j3o", am);
        
        // hand
        makeLod("Models/character/female_min2/hand/hand.000.mesh.j3o", am);
        makeLod("Models/character/female_min2/hand/hand.001.mesh.j3o", am);
        makeLod("Models/character/female_min2/hand/hand.002.mesh.j3o", am);
        makeLod("Models/character/female_min2/hand/hand.003.mesh.j3o", am);
        
        // lowerBody
        makeLod("Models/character/female_min2/lowerBody/lowerBody.000.mesh.j3o", am);
        makeLod("Models/character/female_min2/lowerBody/lowerBody.001.mesh.j3o", am);
        makeLod("Models/character/female_min2/lowerBody/lowerBody.002.mesh.j3o", am);
        makeLod("Models/character/female_min2/lowerBody/lowerBody.003.mesh.j3o", am);
        
        // upperBody
        makeLod("Models/character/female_min2/upperBody/upperBody.000.mesh.j3o", am);
        makeLod("Models/character/female_min2/upperBody/upperBody.001.mesh.j3o", am);
        makeLod("Models/character/female_min2/upperBody/upperBody.002.mesh.j3o", am);
        makeLod("Models/character/female_min2/upperBody/upperBody.003.mesh.j3o", am);
        
        // hair
        makeLod("Models/character/female_min2/hair/hair.000.mesh.j3o", am);
        makeLod("Models/character/female_min2/hair/hair.001.mesh.j3o", am);
        makeLod("Models/character/female_min2/hair/hair.006.mesh.j3o", am);
        makeLod("Models/character/female_min2/hair/hair.010.mesh.j3o", am);
        makeLod("Models/character/female_min2/hair/hair.011.mesh.j3o", am);
        makeLod("Models/character/female_min2/hair/hair.999.mesh.j3o", am);
    }
    
    private static void makeEnv(AssetManager am) {
        makeLod("Models/env/skeleton/monster_skeleton.j3o", am);
        makeLod("Models/env/rock/rock.j3o", am);
    }
    
    public static void main(String[] args) {
        AssetManager am = LuoYing.getAssetManager();
        makeTree(am);
        makeCharacter(am);
        makeEnv(am);
    }
}
