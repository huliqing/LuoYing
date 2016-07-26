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

/**
 *
 * @author huliqing
 */
public class JFXAppState extends AbstractAppState {

    private static final Logger LOG = Logger.getLogger(JFXAppState.class.getName());
    
    private Application app;
    private Processor processor;
    private ViewPort lastViewPort;
    
    private final JmeView view;
    
    public JFXAppState(JmeView view) {
        this.view = view;
    }
    
    public JmeView getJmeView() {
        return view;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); 
        this.app = app;
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
    
    private class Processor implements SceneProcessor {

        private boolean initilized = false;
        private RenderManager renderManager;
        private int width;
        private int height;
        private ByteBuffer buffer;
        private final DrawThread runOnFx = new DrawThread();
        
        @Override
        public void initialize(RenderManager rm, ViewPort vp) {
            initilized = true;
            renderManager = rm;
            Camera camera = vp.getCamera();
            width = camera.getWidth();
            height = camera.getHeight();
            buffer = BufferUtils.createByteBuffer(width * height * 3);
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
            runOnFx.buff = buffer;
            Platform.runLater(runOnFx);
            
        }
        

        @Override
        public void cleanup() {
            initilized = false;
        }
        
        private class DrawThread implements Runnable {
            
            private ByteBuffer buff;
            
            @Override
            public void run() {
                view.drawImage(buff, width, height);
            }
        }
    }
}
