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
import com.jme3.math.FastMath;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 这个AppState的作用是负责把frameBuffer渲染到renderResult中.由其它应用去调用这个渲染结果。
 * @author huliqing
 */
public class JfxAppState extends AbstractAppState {

    private static final Logger LOG = Logger.getLogger(JfxAppState.class.getName());
    
    private Application app;
    private Processor processor;
    private ViewPort lastViewPort;

    RenderStore renderResult;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); 
        this.app = app;
        
        addProcessor();
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
    
    public void setResolution(int width, int height) {
        if (width <= 0 || height <= 0) {
            LOG.log(Level.WARNING, "width and height could not less than zero, width={0}, height={1}", new Object[] {width, height});
            return;
        }
        
        if (app instanceof LegacyApplication) {
            LegacyApplication lapp = (LegacyApplication) app;
            lapp.getContext().getSettings().setResolution(width, height);
            LOG.log(Level.INFO, "========SetResolution: width={0}, height={1}", new Object[] {width, height});
            lapp.restart();
        }
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
            
        private RenderStore renderer1;
        private RenderStore renderer2;
        private RenderStore temp;
        
        @Override
        public void initialize(RenderManager rm, ViewPort vp) {
            initilized = true;
            renderManager = rm;
            resize(vp, vp.getCamera().getWidth(), vp.getCamera().getHeight());
        }

        @Override
        public void reshape(ViewPort vp, int w, int h) {
            resize(vp, vp.getCamera().getWidth(), vp.getCamera().getHeight());
        }
        
        private void resize(ViewPort vp, int w, int h) {
            renderer1 = new RenderStore(w, h);
            renderer2 = new RenderStore(w, h);
            LOG.log(Level.INFO, "Processor reshape: w={0}, h={1}, camera size={2}, {3}"
                    , new Object[] {w, h, vp.getCamera().getWidth(), vp.getCamera().getHeight()});
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
            temp = temp !=  renderer1 ? renderer1 : renderer2;
            
//            LOG.log(Level.INFO, "Processor temp buffer, width={0}, height={1}, limit={2}, capacity={3}"
//                    , new Object[] {temp.width, temp.height, temp.buffer.limit(), temp.buffer.capacity()});
            
            temp.buffer.clear();
            renderManager.getRenderer().readFrameBufferWithFormat(out, temp.buffer, Image.Format.RGB8);
            renderResult = temp;
        }
        
        @Override
        public void cleanup() {
            initilized = false;
        }
        
    }
}
