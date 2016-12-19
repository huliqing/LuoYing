package test;

import com.engine.core.anim.AnimManager;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class TestRole extends SimpleApplication implements AnimEventListener,
		ActionListener, AnalogListener {
	private Node role;

	AnimManager animManager = new AnimManager();
	Node armSke,headSke,thighSke,coatSke,arm;
	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class);

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

		role = new Node();
		rootNode.attachChild(role);
 
		
		
		// 第一步加载骨骼文件
		coatSke = (Node) assetManager.loadModel("Props/models/Ninja_ske.j3o");
		coatSke.scale(0.01F);
		rootNode.attachChild(coatSke);
		
		// 播放动画
		AnimControl animControl = coatSke.getControl(AnimControl.class);
		animControl.addListener(this);
		AnimChannel anim= animControl.createChannel();
		anim.setAnim("Attack1");
		
		
		keyMapping();
	}


	public void keyMapping() {
		inputManager.addMapping("buttonLeft", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping("buttonRight", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		inputManager.addMapping("1", new KeyTrigger(KeyInput.KEY_1));
		inputManager.addMapping("2", new KeyTrigger(KeyInput.KEY_2));
		inputManager.addMapping("3", new KeyTrigger(KeyInput.KEY_3));
		inputManager.addMapping("4", new KeyTrigger(KeyInput.KEY_4));
		inputManager.addMapping("5", new KeyTrigger(KeyInput.KEY_5));
		inputManager.addMapping("6", new KeyTrigger(KeyInput.KEY_6));
		inputManager.addMapping("w", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("s", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("a", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("d", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("space", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(this, "1", "2", "3", "4", "5", "6", "w", "s",
				"a", "d", "space", "buttonLeft", "buttonRight");
	}

	@Override
	public void onAnimChange(AnimControl control, AnimChannel channel,
			String animName) {
	}

	@Override
	public void onAnimCycleDone(AnimControl control, AnimChannel channel,String animName) {
		if (animName.equals("jumo_1")) {
			channel.setAnim("idle_1");
			channel.setLoopMode(LoopMode.Loop);
			channel.setSpeed(0.3F);
		}
		if (animName.equals("atk_1")) {
			channel.setAnim("idle_1");
			channel.setLoopMode(LoopMode.Loop);
			channel.setSpeed(0.3F);
		}
		
		 
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		// 键盘按下
		if (isPressed) {
			if (name.equals("1")) {
				coatSke.detachAllChildren();
			}
			if (name.equals("2")) {
				// 添加皮肤
				coatSke.attachChild(assetManager.loadModel("Props/models/Ninja_skin.j3o"));
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
		TestRole app = new TestRole();
		app.start();
	}

}
