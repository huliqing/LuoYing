/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fxjme;

import com.jme3.app.Application;
import com.jme3.app.LegacyApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.FrameBuffer;
import com.jme3.util.BufferUtils;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 这个AppState的作用是负责把frameBuffer渲染到renderResult中.由其它应用去调用这个渲染结果。
 * @author huliqing
 */
public class JfxAppState extends AbstractAppState {

    private static final Logger LOG = Logger.getLogger(JfxAppState.class.getName());
    
    public interface TransferRenderer {

        void initialize(RenderManager rm);
        boolean isInitialized();
        void render(RenderManager rm, FrameBuffer out);
        void cleanup();
    }
    
    private Application app;
    private Processor processor;
    private ViewPort lastViewPort;

    private TransferRenderer transferRenderer;
    private TransferRenderer transferRendererReset;
    private final AtomicBoolean needResetRenderer  = new AtomicBoolean(false);
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); 
        this.app = app;
        
        addProcessor();
    }

    public void setTransferRenderer(TransferRenderer newTransferRenderer) {
        this.transferRendererReset = newTransferRenderer;
        needResetRenderer.set(true);
    }
    
    private void addProcessor() {
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
    }
    
    @Override
    public void cleanup() {
        if (lastViewPort != null && processor != null) {
            lastViewPort.removeProcessor(processor);
        }
        super.cleanup(); 
    }
    
    // ---- SceneProcessor to draw the framebuffer to jfxView
    
    public class RenderStore {
        ByteBuffer buffer ;
        int width;
        int height;
        
        public RenderStore(int width, int height) {
            this.width = width;
            this.height = height;
            this.buffer = BufferUtils.createByteBuffer(width * height * 3); // Format RGB
        }
    }
    
    private class Processor implements SceneProcessor {

        private boolean initilized = false;
        private RenderManager renderManager;
        
        @Override
        public void initialize(RenderManager rm, ViewPort vp) {
            initilized = true;
            renderManager = rm;
        }

        @Override
        public void reshape(ViewPort vp, int w, int h) {
        }
        

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

            // replace transferRenderer
            if (needResetRenderer.getAndSet(false)) {
                if (transferRenderer != null) {
                    transferRenderer.cleanup();
                }
                transferRenderer = transferRendererReset;
                transferRenderer.initialize(renderManager);
                return; // 等待下一帧再进行渲染
            }
            
            if (transferRenderer == null)
                return;
            
            transferRenderer.render(renderManager, out);
        }
        
        @Override
        public void cleanup() {
            initilized = false;
        }
        
    }
}
