/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.test;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.CompareMode;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import name.huliqing.core.object.effect.ProjectProcessor;
import name.huliqing.core.utils.GeometryUtils;

/**
 *
 * @author huliqing
 */
public class ProjectTest extends SimpleApplication {

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(640, 480);
        settings.setSamples(4);
        settings.setTitle("Projection");
        settings.setFrameRate(40);
        
        ProjectTest app = new ProjectTest();
        app.setSettings(settings);
        app.setShowSettings(false);
        app.setPauseOnLostFocus(false);
        app.start();
        
    }
    
    @Override
    public void simpleInitApp() {
        this.flyCam.setMoveSpeed(100);
        this.cam.setLocation(new Vector3f(0, 300, 0));
        this.cam.lookAt(new Vector3f(0,0,0), new Vector3f(0,0,-1));
        
        ProjectProcessor pp = new ProjectProcessor(rootNode, getAssetManager());
        viewPort.addProcessor(pp);
        
        Spatial floor = initFloor();
        
        
        Geometry geo = initSphere();
        
        pp.addProjectReceives(floor);
        pp.addProjectReceives(geo);
    }
    
    public Geometry initSphere() {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key = new TextureKey("Textures/test/_BrickWall.jpg");
        key.setGenerateMips(true);
        Texture tex = assetManager.loadTexture(key);
        mat.setTexture("ColorMap", tex);
        mat.getAdditionalRenderState().setWireframe(false);
        
        Sphere sp = new Sphere(10, 30, 2);
        Geometry spGeo = new Geometry("MySphere", sp);
        spGeo.setMaterial(mat);
        spGeo.setLocalTranslation(0, 15, -50);
        spGeo.setLocalScale(10, 10, 10);
        
        this.rootNode.attachChild(spGeo);
        return spGeo;
    }
    
    public Spatial initFloor() {
        Material mat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Geometry floor = new Geometry("floor", new Box(Vector3f.ZERO, 100f, 0.1f, 100f));
        
        mat3.setTexture("ColorMap", assetManager.loadTexture("Textures/test/_Pond.jpg"));
        floor.setMaterial(mat3);
        
//        Spatial floor = assetManager.loadModel("Models/env/terrain/treasure.j3o");
        
        AmbientLight light = new AmbientLight();
        light.setColor(ColorRGBA.White);
        rootNode.addLight(light);
        rootNode.addLight(new DirectionalLight());
        
        GeometryUtils.makeUnshaded(floor);
        
        this.rootNode.attachChild(floor);
        return floor;
    }
}
