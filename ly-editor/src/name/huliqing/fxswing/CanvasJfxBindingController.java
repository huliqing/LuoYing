/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fxswing;

import java.awt.Canvas;
import java.awt.Container;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Region;
import javafx.scene.transform.Transform;
import javax.swing.JWindow;

/**
 * 绑定canvas和jfx组件，使用canvas随着jfx组件的大小和位置变化而变化.
 * @author huliqing
 */
public class CanvasJfxBindingController extends JfxBindingController {

//    private static final Logger LOG = Logger.getLogger(CanvasJfxBindingController.class.getName());
    
    private Canvas canvas;
    private Region jfxRegion;
    
    private final ChangeListener<Number> sizeChangedListener = (ObservableValue<? extends Number> observable
            , Number oldValue, Number newValue) -> {
        updateCanvasBinding();
    };
    private final ChangeListener<Transform> localChangedListener = (ObservableValue<? extends Transform> observable
            , Transform oldValue, Transform newValue) -> {
        updateCanvasBinding();
    };
    
    public void jfxCanvasBind(Region jfxRegion) {
        // remove old listener.
        if (this.jfxRegion != null) {
            this.jfxRegion.widthProperty().removeListener(sizeChangedListener);
            this.jfxRegion.heightProperty().removeListener(sizeChangedListener);
            this.jfxRegion.localToSceneTransformProperty().removeListener(localChangedListener);
        }
        this.canvas = Jfx.getJmeCanvas();
        this.jfxRegion = jfxRegion;
        
        jfxRegion.widthProperty().addListener(sizeChangedListener);
        jfxRegion.heightProperty().addListener(sizeChangedListener);
        jfxRegion.localToSceneTransformProperty().addListener(localChangedListener);
    }

    @Override
    protected void updateSize(JWindow win, Container parent) {
        super.updateSize(win, parent);
        updateCanvasBinding();
    }
    
    private void updateCanvasBinding() {
        if (canvas == null || jfxRegion == null)
            return;
        Jfx.runOnSwing(() -> {
            Transform transform = jfxRegion.localToSceneTransformProperty().getValue();
            int width = jfxRegion.widthProperty().intValue();
            int height = jfxRegion.heightProperty().intValue();
            int localX = (int) transform.getTx();
            int localY = (int) transform.getTy();
//            System.out.println("resizeCanvasSize, width=" + jfxRegion.widthProperty().intValue() 
//                    + ", height=" + jfxRegion.heightProperty().intValue()
//                    + ", locationX=" + transform.getTx()
//                    + ", locationY=" + transform.getTy()
//            );
            canvas.setSize(width, height);
            canvas.setLocation(localX, localY);
        });
    }
    
}
