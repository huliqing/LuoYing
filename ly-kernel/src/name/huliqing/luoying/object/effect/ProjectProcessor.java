/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.effect;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Texture;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.shape.QuadXYC;
import name.huliqing.luoying.utils.GeometryUtils;

/**
 * @author huliqing
 */
public class ProjectProcessor implements SceneProcessor {
    
    private RenderManager rm;
    private boolean initialized;
    private final Camera castCam;
    private final Material mat;
    
    private final Texture tex;
    private final float width = 100;
    private final float height = 100;
    // project location
    private final Vector3f pos = new Vector3f(50, 50, -50);

    private final Vector3f projPos = new Vector3f(50, 0, -50);
    
    private final List<Geometry> receives = new ArrayList<Geometry>();
    
    private final Geometry projGeo;
    private float tempTpf;
    private final boolean debug = false;
    
    private final Quaternion rot = new Quaternion();
    
    public ProjectProcessor(Node root, AssetManager assetManager) {
        castCam = new Camera((int)128, (int)128);
        castCam.setParallelProjection(true);
        castCam.setFrustum(-1, 1, -1, 1, 1, -1);
        
        //Textures\tex\magic\magic.jpg
        // Textures\tex\sky\default\east.jpg
        tex = assetManager.loadTexture("Textures/tex/magic/magic.jpg");
//        tex = assetManager.loadTexture("Textures/tex/sky/default/east.jpg");
//        tex = assetManager.loadTexture("Interface/item/face/female5.jpg");
        mat = new Material(assetManager, "MatDefs/Projection/Projection.j3md");
        
        QuadXYC quad = new QuadXYC(1,1);
        projGeo = new Geometry("ProjGeo", quad);
        projGeo.setLocalScale(width, height, 1);
        Material debugMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        debugMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        projGeo.setMaterial(debugMat);
        projGeo.setCullHint(Spatial.CullHint.Always);
        root.attachChild(projGeo);
        
    }

    @Override
    public void initialize(RenderManager rm, ViewPort vp) {
        this.rm = rm;
        initialized = true;
    }
    
    public void addProjectReceives(Spatial spatial) {
        if (spatial instanceof Geometry) {
            Geometry geo = (Geometry) spatial;
            if (!receives.contains(geo)) {
                receives.add(geo);
            }
        } else if (spatial instanceof Node) {
            List<Geometry> geos = GeometryUtils.findAllGeometry(spatial);
            for (Geometry geo : geos) {
                if (!receives.contains(geo)) {
                    receives.add(geo);
                }
            }
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void preFrame(float tpf) {
        this.tempTpf += tpf;
    }

    @Override
    public void postQueue(RenderQueue rq) {
       
    }

    @Override
    public void postFrame(FrameBuffer out) {
        if (receives.isEmpty())
            return;
                
        if (debug) {
            projGeo.setCullHint(Spatial.CullHint.Never);
        }
        projGeo.setLocalTranslation(pos);
        projGeo.lookAt(projPos, Vector3f.UNIT_Y);
        
        TempVars tv0 = TempVars.get();
        Quaternion projRot = projGeo.getLocalRotation();
        projRot.mult(Vector3f.UNIT_Z, tv0.vect1).normalizeLocal();
        rot.fromAngleAxis(-tempTpf, tv0.vect1);
        rot.mult(projRot, projRot);
        tv0.release();
        
        castCam.setLocation(projGeo.getWorldTranslation());
        castCam.setRotation(projGeo.getWorldRotation());
        castCam.update();
        castCam.updateViewProjection();

        
        TempVars tv = TempVars.get();
        Vector3f projCorner = tv.vect1.set(pos);
//        Vector3f wVec = tv.vect2.set(width, 0, 0);
//        Vector3f hVec = tv.vect3.set(height, 0, 0);
        
        castCam.getViewProjectionMatrix().mult(projCorner, projCorner);
//        castCam.getViewProjectionMatrix().mult(wVec, wVec);
//        castCam.getViewProjectionMatrix().mult(hVec, hVec);
//        float tw = wVec.length();
//        float th = hVec.length();

        float tw = width;
        float th = height;
        
        mat.setFloat("ProjLeftX", projCorner.x - tw * 0.5f);
        mat.setFloat("ProjLeftY", projCorner.y - th * 0.5f);
        mat.setFloat("ProjWidth", tw);
        mat.setFloat("ProjHeight", th);
        mat.setTexture("Texture", tex);
        mat.setColor("Color", new ColorRGBA(1, 1, 1, 1));
        mat.setMatrix4("CastViewProjectionMatrix", castCam.getViewProjectionMatrix());
        tv.release();
     
        rm.setForcedMaterial(mat);
        rm.setForcedTechnique("Default");
        for (Geometry geo : receives) {
            rm.renderGeometry(geo);
        }
        
        rm.setForcedTechnique(null);
        rm.setForcedMaterial(null);
        
    }

    @Override
    public void reshape(ViewPort vp, int w, int h) {}
    
    @Override
    public void cleanup() {}
    
}
