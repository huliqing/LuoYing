///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.effect.project;
//
//import com.jme3.app.SimpleApplication;
//import com.jme3.asset.TextureKey;
//import com.jme3.bullet.BulletAppState;
//import com.jme3.bullet.control.RigidBodyControl;
//import com.jme3.light.DirectionalLight;
//import com.jme3.material.Material;
//import com.jme3.math.ColorRGBA;
//import com.jme3.math.Vector2f;
//import com.jme3.math.Vector3f;
//import com.jme3.renderer.queue.RenderQueue;
//import com.jme3.scene.Geometry;
//import com.jme3.scene.Spatial;
//import com.jme3.scene.shape.Box;
//import com.jme3.scene.shape.Sphere;
//import com.jme3.shadow.CompareMode;
//import com.jme3.shadow.EdgeFilteringMode;
//import com.jme3.system.AppSettings;
//import com.jme3.texture.Texture;
//
///**
// *
// * @author huliqing
// */
//public class ShadowTest extends SimpleApplication {
//
//    public static void main(String[] args) {
//        AppSettings settings = new AppSettings(true);
//        settings.setResolution(960, 540);
//        settings.setSamples(4);
//        settings.setTitle("Test");
//        settings.setFrameRate(40);
//        
//        ShadowTest app = new ShadowTest();
//        app.setSettings(settings);
//        app.setShowSettings(false);
//        app.setPauseOnLostFocus(false);
//        app.start();
//        
//    }
//    
//    @Override
//    public void simpleInitApp() {
//        this.flyCam.setMoveSpeed(20);
//        this.cam.setLocation(new Vector3f(0, 10, 10));
//        this.cam.lookAt(new Vector3f(0,0,0), Vector3f.UNIT_Y);
//        
//        MyDirectionalLightShadowProcessor sp = new MyDirectionalLightShadowProcessor(assetManager, 1024, 1);
//        
//        DirectionalLight dl = new DirectionalLight();
//        dl.setDirection(new Vector3f(-1, -0.5f, -1).normalizeLocal());
//        sp.setLight(dl);
//        sp.setLambda(0.55f);
//        sp.setShadowIntensity(0.6f);
//        sp.setShadowCompareMode(CompareMode.Hardware);
////        sp.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
//        sp.setEdgeFilteringMode(EdgeFilteringMode.Nearest);
//        
//        
//        viewPort.addProcessor(sp);
//        
//        initFloor();
//        initSphere();
//    }
//    
//    public void initSphere() {
//        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        TextureKey key = new TextureKey("Textures/test/_BrickWall.jpg");
//        key.setGenerateMips(true);
//        Texture tex = assetManager.loadTexture(key);
//        mat.setTexture("ColorMap", tex);
//        mat.getAdditionalRenderState().setWireframe(false);
//        
//        Sphere sp = new Sphere(10, 30, 2);
//        Geometry spGeo = new Geometry("MySphere", sp);
//        spGeo.setMaterial(mat);
//        spGeo.setShadowMode(RenderQueue.ShadowMode.Cast);
//        spGeo.setLocalTranslation(0, 5, 0);
//        
//        this.rootNode.attachChild(spGeo);
//        
//    }
//    
//    public void initFloor() {
//        Box floorBox = new Box(Vector3f.ZERO, 100f, 0.1f, 100f);
//        floorBox.scaleTextureCoordinates(new Vector2f(3, 6));
//
//        TextureKey key3 = new TextureKey("Textures/test/_Pond.jpg");
//        key3.setGenerateMips(true);
//        Texture tex3 = assetManager.loadTexture(key3);
//        tex3.setWrap(Texture.WrapMode.Repeat);
//        Material mat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        mat3.setTexture("ColorMap", tex3);
//        
//        Geometry floor = new Geometry("floor", floorBox);
//        floor.setMaterial(mat3);
//        floor.setShadowMode(RenderQueue.ShadowMode.Receive);
//        floor.setLocalTranslation(0, 0, 0);
//        floor.addControl(new RigidBodyControl(0));
//        this.rootNode.attachChild(floor);
//    }
//}
