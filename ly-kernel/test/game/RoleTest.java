package game;

import java.util.Collection;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.Animation;
import com.jme3.animation.SkeletonControl;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import name.huliqing.luoying.object.actor.CustomSkeletonControl;

public class RoleTest extends SimpleApplication implements AnimEventListener,
        ActionListener, AnalogListener {

    AnimChannel anim;
    Node ske;

    @Override
    public void simpleInitApp() {
//		assetManager.registerLocator("assets/", FileLocator.class);

        // 关闭第一人称
//		flyCam.setEnabled(false);
        this.getFlyByCamera().setMoveSpeed(30);
        cam.setFrustumPerspective(45, (float) cam.getWidth() / cam.getHeight(), 0.1F, 1000);
        AmbientLight amb = new AmbientLight();
        amb.setColor(new ColorRGBA(0.6F, 0.8F, 0.8F, 1.0f));
        rootNode.addLight(amb);

        DirectionalLight light = new DirectionalLight();
        light.setColor(new ColorRGBA(1.0f, 0.9f, 0.8f, 1.0f));
        light.setDirection(new Vector3f(0F, -0.779F, 0.627F));
        rootNode.addLight(light);

        // 第一步加载骨骼文件
        ske = (Node) assetManager.loadModel("Models/actor/ske.mesh.j3o");
        SkeletonControl sc = ske.getControl(SkeletonControl.class);
        
        CustomSkeletonControl csc = new CustomSkeletonControl(sc.getSkeleton());
        ske.removeControl(sc);
        ske.addControl(csc);
//        csc.setHardwareSkinningPreferred(true);
        

        rootNode.attachChild(ske);
        addSkeletonAnim(assetManager.loadModel("Models/actor/ext_anim/idle_run.mesh.j3o"), ske);
        // 加载初始化皮肤
        ske.attachChild(assetManager.loadModel("Models/actor/female/foot.003.mesh.j3o"));

        // 播放动画
        AnimControl animControl = ske.getControl(AnimControl.class);
        System.out.println(animControl.getAnimationNames().toString());
        anim = animControl.createChannel();
        anim.setAnim("idle_run");

        keyMapping();
    }

    public static void addSkeletonAnim(Spatial from, Spatial to) {
        AnimControl acFrom = from.getControl(AnimControl.class);
        AnimControl acTo = to.getControl(AnimControl.class);
        if (acFrom == null || acTo == null) {
            return;
        }
        Collection<String> namesFrom = acFrom.getAnimationNames();
        if (namesFrom == null || namesFrom.isEmpty()) {
            return;
        }
        for (String name : namesFrom) {
            Animation anim = acFrom.getAnim(name);
            acTo.addAnim(anim);
        }
    }

    public void keyMapping() {
        inputManager.addMapping("1", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("2", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "1", "2", "3", "4", "5", "6", "w", "s",
                "a", "d", "space", "buttonLeft", "buttonRight");
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel,
            String animName) {
    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        // 键盘按下
        if (isPressed) {
            if (name.equals("1")) {
                ske.detachAllChildren();
            }
            if (name.equals("2")) {
                // 添加皮肤
                ske.attachChild(assetManager.loadModel("Models/actor/female/foot.002.mesh.j3o"));
                //anim.setAnim("idle_run");
            }
            if (name.equals("4")) {

            }
            if (name.equals("5")) {
            }
            if (name.equals("6")) {
            }
        }

    }

    @Override
    public void onAnalog(String name, float value, float tpf) {

    }

    public static void main(String[] args) {
        RoleTest app = new RoleTest();
        app.start();
    }

}
