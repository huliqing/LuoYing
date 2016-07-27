/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.fxjme;

import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import java.util.ArrayList;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Convert JFX mouse and scroll event to JME mouse event.
 * @author huliqing
 */
public class JfxMouseInput implements MouseInput, EventHandler<Event>{
    private static final Logger LOG = Logger.getLogger(JfxMouseInput.class.getName());

    private final static String MOUSE_ENTERED = "MOUSE_ENTERED";
    private final static String MOUSE_PRESSED = "MOUSE_PRESSED";
    private final static String MOUSE_RELEASED = "MOUSE_RELEASED";
    private final static String MOUSE_CLICKED = "MOUSE_CLICKED";
    private final static String MOUSE_EXITED = "MOUSE_EXITED";
    private final static String MOUSE_MOVED = "MOUSE_MOVED";
    private final static String MOUSE_DRAGGED = "MOUSE_DRAGGED";
    
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
    
//me=MouseEvent [source = JfxView@1a1102ae[styleClass=image-view], target = JfxView@1a1102ae[styleClass=image-view], eventType = MOUSE_MOVED, consumed = false, x = 282.0, y = 240.0, z = 0.0, button = NONE, pickResult = PickResult [node = JfxView@1a1102ae[styleClass=image-view], point = Point3D [x = 282.0, y = 240.0, z = 0.0], distance = 895.6921938165307]
//me=MouseEvent [source = JfxView@1a1102ae[styleClass=image-view], target = JfxView@1a1102ae[styleClass=image-view], eventType = MOUSE_MOVED, consumed = false, x = 282.0, y = 241.0, z = 0.0, button = NONE, pickResult = PickResult [node = JfxView@1a1102ae[styleClass=image-view], point = Point3D [x = 282.0, y = 241.0, z = 0.0], distance = 895.6921938165307]
//me=MouseEvent [source = JfxView@1a1102ae[styleClass=image-view], target = JfxView@1a1102ae[styleClass=image-view], eventType = MOUSE_MOVED, consumed = false, x = 283.0, y = 242.0, z = 0.0, button = NONE, pickResult = PickResult [node = JfxView@1a1102ae[styleClass=image-view], point = Point3D [x = 283.0, y = 242.0, z = 0.0], distance = 895.6921938165307]
    
//me=MouseEvent [source = JfxView@1a1102ae[styleClass=image-view], target = JfxView@1a1102ae[styleClass=image-view], eventType = MOUSE_DRAGGED, consumed = false, x = 282.0, y = 244.0, z = 0.0, button = PRIMARY, primaryButtonDown, pickResult = PickResult [node = JfxView@1a1102ae[styleClass=image-view], point = Point3D [x = 282.0, y = 244.0, z = 0.0], distance = 895.6921938165307]
//me=MouseEvent [source = JfxView@1a1102ae[styleClass=image-view], target = JfxView@1a1102ae[styleClass=image-view], eventType = MOUSE_DRAGGED, consumed = false, x = 281.0, y = 243.0, z = 0.0, button = PRIMARY, primaryButtonDown, pickResult = PickResult [node = JfxView@1a1102ae[styleClass=image-view], point = Point3D [x = 281.0, y = 243.0, z = 0.0], distance = 895.6921938165307]
//me=MouseEvent [source = JfxView@1a1102ae[styleClass=image-view], target = JfxView@1a1102ae[styleClass=image-view], eventType = MOUSE_DRAGGED, consumed = false, x = 281.0, y = 242.0, z = 0.0, button = PRIMARY, primaryButtonDown, pickResult = PickResult [node = JfxView@1a1102ae[styleClass=image-view], point = Point3D [x = 281.0, y = 242.0, z = 0.0], distance = 895.6921938165307]

    
    private double locationX;
    private double locationY;
    private double wheelPos;
    
    // 最近一次从jfx上获得的鼠标位置
    private double lastJfxX;
    private double lastJfxY;
    
    // 最近一次JME处理事件后记住的mouse的位置
    private double lastEventX;
    private double lastEventY;
    private double lastEventWheel;
    
    private boolean cursorMoved;
    
    @Override
    public void handle(Event e) {
        
//        LOG.log(Level.INFO, "event={e}", e);
        
        String et = e.getEventType().getName();
        if (e instanceof MouseEvent) {
            MouseEvent me = (MouseEvent) e;
            if (MOUSE_PRESSED.equals(et)) {
                convertMouseButtonEvent(me, true);
            } else if (MOUSE_RELEASED.equals(et)) {
                convertMouseButtonEvent(me, false);
            } else if (MOUSE_MOVED.equals(et)) {
                convertMouseMotion(me);
            } else if (MOUSE_DRAGGED.equals(et)) {
                convertMouseMotion(me);
            } else if (MOUSE_ENTERED.equals(et)) {
                // ignore
            } else if (MOUSE_EXITED.equals(et)) {
                // ignore
            } else if (MOUSE_CLICKED.equals(et)) {
                // ignore
            } 
            
        } else if (e instanceof ScrollEvent) {
            convertMouseWheel((ScrollEvent) e);
        } else {
            // other ignore
        }
        
    }
    
    private void convertMouseWheel(ScrollEvent se) {
        wheelPos += se.getDeltaY();
        cursorMoved = true;
    }
    
    private void convertMouseMotion(MouseEvent me) {
        double dx = me.getX() - lastJfxX;
        double dy = me.getY() - lastJfxY;
        locationX += dx;
        locationY += dy;
        lastJfxX = me.getX();
        lastJfxY = me.getY();
        cursorMoved = true;
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
        
        // 鼠标移动、滚轮事件
         if (cursorMoved) {
            double newX = locationX;
            double newY = locationY;
            double newWheel = wheelPos;

            MouseMotionEvent mme = new MouseMotionEvent((int)lastJfxX, (int)lastJfxY
                    , (int) (newX - lastEventX)
                    , (int) (newY - lastEventY)
                    , (int) newWheel
                    , (int)(newWheel - lastEventWheel));
            
            listener.onMouseMotionEvent(mme);
            
//            LOG.log(Level.INFO, "jme updateMouseMotion MouseMotionEvent={e}", mme);

            lastEventX = newX;
            lastEventY = newY;
            lastEventWheel = newWheel;

            cursorMoved = false;
        }
        
        // 处理鼠标点击事件
        synchronized (eventQueue) {
            eventQueueCopy.clear();
            eventQueueCopy.addAll(eventQueue);
            eventQueue.clear();
        }

        int size = eventQueueCopy.size();
        for (int i = 0; i < size; i++) {
            listener.onMouseButtonEvent(eventQueueCopy.get(i));
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
