/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.fxjme;

import com.jme3.renderer.RenderManager;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image.Format;
import com.jme3.util.BufferUtils;
import java.nio.ByteBuffer;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import name.huliqing.fxjme.JfxAppState.TransferRenderer;

/**
 *
 * @author huliqing
 */
public class JfxRenderer implements TransferRenderer{

    private static final Logger LOG = Logger.getLogger(JfxRenderer.class.getName());
    
    private boolean initialized;
    private final ByteBuffer byteBuffer;
    private final WritableImage renderImage;
    
    private  final int width;
    private  final int height;
    private  final int scanlineStride;
    
    private final ImageView imageView;
    
    private final FrameBuffer frameBuffer;

    public JfxRenderer(ImageView imageView, int width, int height, boolean depthBuffer) {
        this.imageView = imageView;
        this.width = width;
        this.height = height;
        this.byteBuffer = BufferUtils.createByteBuffer(width * height * 4);
        this.scanlineStride = width * 4;
        this.renderImage = new WritableImage(width, height);
        this.imageView.setImage(renderImage);
        if (depthBuffer) {
            frameBuffer = new FrameBuffer(width, height, 1);
            frameBuffer.setDepthBuffer(Format.Depth);
            frameBuffer.setColorBuffer(Format.BGRA8);
        } else {
            frameBuffer = null;
        }
    }
    
    @Override
    public void initialize(RenderManager rm) {
        if (frameBuffer != null) {
            rm.getRenderer().setMainFrameBufferOverride(frameBuffer);
        }
        rm.notifyReshape(width, height);
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public void render(RenderManager rm, FrameBuffer out) {
        
        synchronized (byteBuffer) {
            rm.getRenderer().readFrameBufferWithFormat(out, byteBuffer, Format.BGRA8);
        }
        
        Platform.runLater(() -> {
            synchronized (byteBuffer) {
                PixelWriter pw = renderImage.getPixelWriter();
                pw.setPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), byteBuffer, scanlineStride);
            }
        });

    }

    @Override
    public void cleanup() {
        if (frameBuffer != null) {
            frameBuffer.dispose();
        }
        initialized = false;
    }
    
}
