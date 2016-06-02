
package name.huliqing.fighter.game.state.test2;

import com.jme3.animation.*;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.RagdollCollisionListener;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.KinematicRagdollControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.SkeletonDebugger;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;

/**
 * PHYSICS RAGDOLLS ARE NOT WORKING PROPERLY YET!
 * @author normenhansen
 */
public class TestBoneRagdoll extends SimpleApplication implements RagdollCollisionListener {

    private BulletAppState bulletAppState;
    Material matBullet;
    Node model;
    KinematicRagdollControl ragdoll;
    float bulletSize = 1f;
    Material mat;
    Material mat3;
    private Sphere bullet;
    private SphereCollisionShape bulletCollisionShape;

    public static void main(String[] args) {
        TestBoneRagdoll app = new TestBoneRagdoll();
        app.start();
    }

    public void simpleInitApp() {
//        initCrossHairs();
        matBullet = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matBullet.getAdditionalRenderState().setWireframe(true);
        
        cam.setLocation(new Vector3f(0.26924422f, 6.646658f, 22.265987f));
        cam.setRotation(new Quaternion(-2.302544E-4f, 0.99302495f, -0.117888905f, -0.0019395084f));


        bulletAppState = new BulletAppState();
        bulletAppState.setEnabled(true);
        stateManager.attach(bulletAppState);
        bullet = new Sphere(32, 32, 1.0f, true, false);
        bullet.setTextureMode(TextureMode.Projected);
        bulletCollisionShape = new SphereCollisionShape(1.0f);

//        bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        createPhysicsTestWorld(rootNode, assetManager, bulletAppState.getPhysicsSpace());
        setupLight();

//        model = (Node) assetManager.loadModel("Models/Sinbad/Sinbad.mesh.xml");
        model = (Node) assetManager.loadModel("Models/monster/gb/gb.mesh.j3o");
        model.setLocalScale(0.5f);

//        //debug view
//        AnimControl control = model.getControl(AnimControl.class);
//        SkeletonDebugger skeletonDebug = new SkeletonDebugger("skeleton", control.getSkeleton());
//        Material mat2 = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
//        mat2.getAdditionalRenderState().setWireframe(true);
//        mat2.setColor("Color", ColorRGBA.Green);
//        mat2.getAdditionalRenderState().setDepthTest(false);
//        skeletonDebug.setMaterial(mat2);
//        skeletonDebug.setLocalTranslation(model.getLocalTranslation());

        ragdoll = new KinematicRagdollControl(0.5f);
        ragdoll.addCollisionListener(this);
        model.addControl(ragdoll);

        getPhysicsSpace().add(ragdoll);
        speed = 1.3f;

        rootNode.attachChild(model);
        flyCam.setMoveSpeed(100);

        inputManager.addListener(new ActionListener() {

            public void onAction(String name, boolean isPressed, float tpf) {
                if (name.equals("toggle") && isPressed) {

                    Vector3f v = new Vector3f();
                    v.set(model.getLocalTranslation());
                    v.y = 0;
                    model.setLocalTranslation(v);
                    Quaternion q = new Quaternion();
                    float[] angles = new float[3];
                    model.getLocalRotation().toAngles(angles);
                    q.fromAngleAxis(angles[1], Vector3f.UNIT_Y);
                    model.setLocalRotation(q);
                    if (angles[0] < 0) {
//                        animChannel.setAnim("StandUpBack");
//                        ragdoll.blendToKinematicMode(0.5f);
                    } else {
//                        animChannel.setAnim("StandUpFront");
//                        ragdoll.blendToKinematicMode(0.5f);
                    }

                }
                if (name.equals("stop") && isPressed) {
                    ragdoll.setEnabled(!ragdoll.isEnabled());
                    ragdoll.setRagdollMode();
                }

                if (name.equals("shoot") && !isPressed) {
                    ragdoll.setRagdollMode();
                    
                    
                    PhysicsRigidBody r1 = ragdoll.getBoneRigidBody("root");
                    r1.setLinearVelocity(cam.getDirection().mult(100));
                    
                    PhysicsRigidBody r2 = ragdoll.getBoneRigidBody("head");
                    r2.setLinearVelocity(cam.getDirection().mult(100));
                    
                    
//                    Geometry bulletg = new Geometry("bullet", bullet);
//                    bulletg.setMaterial(matBullet);
//                    bulletg.setLocalTranslation(cam.getLocation());
//                    bulletg.setLocalScale(bulletSize);
//                    bulletCollisionShape = new SphereCollisionShape(bulletSize);
//                    RigidBodyControl bulletNode = new RigidBodyControl(bulletCollisionShape, bulletSize * 10);
//                    bulletNode.setCcdMotionThreshold(0.001f);
//                    bulletNode.setLinearVelocity(cam.getDirection().mult(100));
//                    bulletg.addControl(bulletNode);
//                    rootNode.attachChild(bulletg);
//                    getPhysicsSpace().add(bulletNode);
                }

            }
        }, "toggle", "shoot", "stop", "bullet+", "bullet-", "boom");
        inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("stop", new KeyTrigger(KeyInput.KEY_H));
    }

    private void setupLight() {
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -0.7f, -1).normalizeLocal());
        dl.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
        rootNode.addLight(dl);
    }

    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

    @Override
    public void collide(Bone bone, PhysicsCollisionObject object, PhysicsCollisionEvent event) {
//        if (object.getUserObject() != null && object.getUserObject() instanceof Geometry) {
//            Geometry geom = (Geometry) object.getUserObject();
//            if ("Floor".equals(geom.getName())) {
////                System.out.println("on floor" + System.currentTimeMillis());
//                return;
//            }
//        }
////        System.out.println("=============floor" + System.currentTimeMillis());
//        ragdoll.setRagdollMode();
    }

    public static void createPhysicsTestWorld(Node rootNode, AssetManager assetManager, PhysicsSpace space) {
        AmbientLight light = new AmbientLight();
        light.setColor(ColorRGBA.LightGray);
        rootNode.addLight(light);

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Gray);

        Box floorBox = new Box(140, 0.25f, 140);
        Geometry floorGeometry = new Geometry("Floor", floorBox);
        floorGeometry.setMaterial(material);
        floorGeometry.setLocalTranslation(0, -5, 0);
//        Plane plane = new Plane();
//        plane.setOriginNormal(new Vector3f(0, 0.25f, 0), Vector3f.UNIT_Y);
//        floorGeometry.addControl(new RigidBodyControl(new PlaneCollisionShape(plane), 0));
        floorGeometry.addControl(new RigidBodyControl(0));
        rootNode.attachChild(floorGeometry);
        space.add(floorGeometry);

        //movable boxes
        for (int i = 0; i < 12; i++) {
            Box box = new Box(0.25f, 0.25f, 0.25f);
            Geometry boxGeometry = new Geometry("Box", box);
            boxGeometry.setMaterial(material);
            boxGeometry.setLocalTranslation(i, 5, -3);
            //RigidBodyControl automatically uses box collision shapes when attached to single geometry with box mesh
            boxGeometry.addControl(new RigidBodyControl(2));
            rootNode.attachChild(boxGeometry);
            space.add(boxGeometry);
        }

        //immovable sphere with mesh collision shape
        Sphere sphere = new Sphere(8, 8, 1);
        Geometry sphereGeometry = new Geometry("Sphere", sphere);
        sphereGeometry.setMaterial(material);
        sphereGeometry.setLocalTranslation(4, -4, 2);
        sphereGeometry.addControl(new RigidBodyControl(new MeshCollisionShape(sphere), 0));
        rootNode.attachChild(sphereGeometry);
        space.add(sphereGeometry);

    }
}
