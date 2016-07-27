/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.fxjme;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.util.BufferUtils;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author huliqing
 */
public class JfxAppState extends AbstractAppState {

    private static final Logger LOG = Logger.getLogger(JfxAppState.class.getName());
    
    private Application app;
    private Processor processor;
    private ViewPort lastViewPort;
    
    private final JfxView jfxView;
    private JfxContext jfxContext;
    private JfxMouseInput mouseInput;
    private JfxKeyInput keyInput;
    
    public JfxAppState(JfxView jfxView) {
        this.jfxView = jfxView;
    }
    
    public JfxView getJmeView() {
        return jfxView;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); 
        this.app = app;
        
        // Processor frame buffer
        processor = new Processor();
        List<ViewPort> vps = app.getRenderManager().getPostViews();
        for (int i = vps.size() - 1; i >= 0; i-- ) {
            lastViewPort = vps.get(i);
            if (lastViewPort.isEnabled()) {
                break;
            }
        }
        lastViewPort.addProcessor(processor);
        
        // Add Mouse and key event.
        jfxContext = (JfxContext) app.getContext();
        mouseInput = (JfxMouseInput) jfxContext.getMouseInput();
        keyInput = (JfxKeyInput) jfxContext.getKeyInput();
        jfxView.addEventHandler(Event.ANY, mouseInput); // 这里要使用Event.ANY,因为需要用到MouseEvent和ScrollEvent
        jfxView.addEventHandler(KeyEvent.ANY, keyInput);
        
    }
    
    @Override
    public void cleanup() {
        if (lastViewPort != null && processor != null) {
            lastViewPort.removeProcessor(processor);
        }
        if (mouseInput != null) {
            jfxView.removeEventHandler(MouseEvent.ANY, mouseInput);
        }
        if (keyInput != null) {
            jfxView.removeEventHandler(KeyEvent.ANY, keyInput);
        }
        super.cleanup(); 
    }
    
    // ---- SceneProcessor to draw the framebuffer to jfxView
    private class Processor implements SceneProcessor {

        private boolean initilized = false;
        private RenderManager renderManager;
        private ByteBuffer buffer;
        private final DrawRunnable drawOnFx = new DrawRunnable();
        private Camera camera;
        private int width;
        private int height;
        
        @Override
        public void initialize(RenderManager rm, ViewPort vp) {
            initilized = true;
            renderManager = rm;
            camera = vp.getCamera();
            width = camera.getWidth();
            height = camera.getHeight();
            buffer = BufferUtils.createByteBuffer(width * height * 3); // for rgb
        }

        @Override
        public void reshape(ViewPort vp, int w, int h) {}

        @Override
        public boolean isInitialized() {
            return initilized;
        }

        @Override
        public void preFrame(float tpf) { }
        
        @Override
        public void postQueue(RenderQueue rq) {}

        @Override
        public void postFrame(FrameBuffer out) {
            
            // 格式必须和view中的图片格式匹配
            buffer.clear();
            renderManager.getRenderer().readFrameBufferWithFormat(out, buffer, Image.Format.RGB8);
            drawOnFx.buff = buffer;
            drawOnFx.width = width;
            drawOnFx.height = height;
            Platform.runLater(drawOnFx);
        }

        @Override
        public void cleanup() {
            initilized = false;
        }
        
        private class DrawRunnable implements Runnable {
            private ByteBuffer buff;
            private int width;
            private int height;
            
            @Override
            public void run() {
                jfxView.drawImage(buff, width, height);
            }
        }
    }
}
