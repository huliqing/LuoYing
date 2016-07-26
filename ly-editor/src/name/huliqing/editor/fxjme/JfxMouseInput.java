/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.fxjme;

import com.jme3.app.Application;
import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.MouseButtonEvent;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author huliqing
 */
public class JfxMouseInput implements MouseInput, EventHandler<MouseEvent>{

    private final static String MOUSE_ENTERED = "MOUSE_ENTERED";
    private final static String MOUSE_PRESSED = "MOUSE_PRESSED";
    private final static String MOUSE_RELEASED = "MOUSE_RELEASED";
    private final static String MOUSE_CLICKED = "MOUSE_CLICKED";
    private final static String MOUSE_EXITED = "MOUSE_EXITED";
    
    private RawInputListener listener;
    private final ArrayList<MouseButtonEvent> eventQueue = new ArrayList<>();
    private final ArrayList<MouseButtonEvent> eventQueueCopy = new ArrayList<>();
    
    public JfxMouseInput() {}
    
    
    // ---- Run on jfx
//=>MouseEvent [source = JfxView@4cc24a15[styleClass=image-view], target = JfxView@4cc24a15[styleClass=image-view], eventType = MOUSE_ENTERED, consumed = false, x = 261.0, y = 186.0, z = 0.0, button = NONE, pickResult = PickResult [node = JfxView@4cc24a15[styleClass=image-view], point = Point3D [x = 261.0, y = 186.0, z = 0.0], distance = 895.6921938165307]
//=>MouseEvent [source = JfxView@4cc24a15[styleClass=image-view], target = JfxView@4cc24a15[styleClass=image-view], eventType = MOUSE_PRESSED, consumed = false, x = 261.0, y = 186.0, z = 0.0, button = PRIMARY, primaryButtonDown, pickResult = PickResult [node = JfxView@4cc24a15[styleClass=image-view], point = Point3D [x = 261.0, y = 186.0, z = 0.0], distance = 895.6921938165307]
//=>MouseEvent [source = JfxView@4cc24a15[styleClass=image-view], target = JfxView@4cc24a15[styleClass=image-view], eventType = MOUSE_RELEASED, consumed = false, x = 261.0, y = 186.0, z = 0.0, button = PRIMARY, pickResult = PickResult [node = JfxView@4cc24a15[styleClass=image-view], point = Point3D [x = 261.0, y = 186.0, z = 0.0], distance = 895.6921938165307]
//=>MouseEvent [source = JfxView@4cc24a15[styleClass=image-view], target = JfxView@4cc24a15[styleClass=image-view], eventType = MOUSE_CLICKED, consumed = false, x = 261.0, y = 186.0, z = 0.0, button = PRIMARY, pickResult = PickResult [node = JfxView@4cc24a15[styleClass=image-view], point = Point3D [x = 261.0, y = 186.0, z = 0.0], distance = 895.6921938165307]
//=>MouseEvent [source = JfxView@4cc24a15[styleClass=image-view], target = JfxView@4cc24a15[styleClass=image-view], eventType = MOUSE_EXITED, consumed = false, x = 261.0, y = 186.0, z = 0.0, button = NONE, pickResult = PickResult [node = JfxView@4cc24a15[styleClass=image-view], point = Point3D [x = 261.0, y = 186.0, z = 0.0], distance = 895.6921938165307]
    @Override
    public void handle(MouseEvent me) {
         String et = me.getEventType().getName();
        if (MOUSE_PRESSED.equals(et)) {
            convertMouseButtonEvent(me, true);
        } else if (MOUSE_RELEASED.equals(et)) {
            convertMouseButtonEvent(me, false);
        } else if (MOUSE_ENTERED.equals(et)) {
            // ignore
        } else if (MOUSE_EXITED.equals(et)) {
            // ignore
        } else if (MOUSE_CLICKED.equals(et)) {
            // ignore
        }
    }
    
    private void convertMouseButtonEvent(MouseEvent me, boolean isPressed) {
        int button = MouseInput.BUTTON_LEFT;
        switch (me.getButton()) {
            case NONE:
                return;
            case MIDDLE:
                button = MouseInput.BUTTON_MIDDLE;
                break;
            case PRIMARY:
                button = MouseInput.BUTTON_LEFT;
                break;
            case SECONDARY:
                button = MouseInput.BUTTON_RIGHT;
        }
        MouseButtonEvent mbe = new MouseButtonEvent(button, isPressed, (int)me.getX(), (int)me.getY());
        
        synchronized (eventQueue) {
            eventQueue.add(mbe);
        }
    }


    // ---- Run on jme
    
    @Override
    public void setCursorVisible(boolean visible) {
    }

    @Override
    public int getButtonCount() {
        return 3;
    }

    @Override
    public void setNativeCursor(JmeCursor cursor) {
        // ignore
    }

    @Override
    public void initialize() {
        // ignore
    }

    @Override
    public void update() {
        synchronized (eventQueue) {
            eventQueueCopy.clear();
            eventQueueCopy.addAll(eventQueue);
            eventQueue.clear();
        }

        int size = eventQueueCopy.size();
        for (int i = 0; i < size; i++) {
            listener.onMouseButtonEvent(eventQueueCopy.get(i));
            System.out.println("eventQueue on jme: event=" + eventQueueCopy.get(i));
        }
    }

    @Override
    public void destroy() {
        // ignore
    }

    @Override
    public boolean isInitialized() {
        return true;
    }

    @Override
    public void setInputListener(RawInputListener listener) {
        this.listener = listener;
    }

    @Override
    public long getInputTimeNanos() {
        return System.nanoTime();
    }
    
}
