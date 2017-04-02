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

import com.jme3.input.KeyInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.KeyInputEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;

/**
 * Convert JFX key event to JME key event.
 * @author huliqing
 */
public class JfxKeyInput implements KeyInput, EventHandler<javafx.scene.input.KeyEvent>{

    private static final Logger LOG = Logger.getLogger(JfxKeyInput.class.getName());

    private final static String KEY_PRESSED = "KEY_PRESSED";
    private final static String KEY_TYPED = "KEY_TYPED";
    private final static String KEY_RELEASED = "KEY_RELEASED";
    
    private RawInputListener listener;
    private final ArrayList<KeyInputEvent> eventQueue = new ArrayList<>();
    
    // Running on jfx
    @Override
    public void handle(javafx.scene.input.KeyEvent e) {
//        LOG.log(Level.INFO, "handle jfx Key Event, e={0}", e);
        
        String et = e.getEventType().getName();
        KeyInputEvent keyEvent = null;
        if (KEY_PRESSED.equals(et)) {
            int code = convertKeyCode(e.getCode());
            char keyChar = ' ';
            if (e.getCharacter().length() > 0) {
                keyChar = e.getCharacter().charAt(0);
            }
            keyEvent = new KeyInputEvent(code, keyChar, true, false);
            
        } else if (KEY_RELEASED.equals(et)) {
            int code = convertKeyCode(e.getCode());
            char keyChar = ' ';
            if (e.getCharacter().length() > 0) {
                keyChar = e.getCharacter().charAt(0);
            }
            keyEvent = new KeyInputEvent(code, keyChar, false, false);
        }

        if (keyEvent != null) {
            keyEvent.setTime(System.currentTimeMillis());
            synchronized (eventQueue) {
                eventQueue.add(keyEvent);
            }
        }
    }
    
    public static int convertKeyCode(KeyCode kc) {
        switch (kc) {
            case ESCAPE:
                return KEY_ESCAPE;
            case DIGIT1:
                return KEY_1;
            case DIGIT2:
                return KEY_2;
            case DIGIT3:
                return KEY_3;
            case DIGIT4:
                return KEY_4;
            case DIGIT5:
                return KEY_5;
            case DIGIT6:
                return KEY_6;
            case DIGIT7:
                return KEY_7;
            case DIGIT8:
                return KEY_8;
            case DIGIT9:
                return KEY_9;
            case DIGIT0:
                return KEY_0;
            case MINUS:
                return KEY_MINUS;
            case EQUALS:
                return KEY_EQUALS;
            case BACK_SPACE:
                return KEY_BACK;
            case TAB:
                return KEY_TAB;
            case Q:
                return KEY_Q;
            case W:
                return KEY_W;
            case E:
                return KEY_E;
            case R:
                return KEY_R;
            case T:
                return KEY_T;
            case Y:
                return KEY_Y;
            case U:
                return KEY_U;
            case I:
                return KEY_I;
            case O:
                return KEY_O;
            case P:
                return KEY_P;
            case OPEN_BRACKET:
                return KEY_LBRACKET;
            case CLOSE_BRACKET:
                return KEY_RBRACKET;
            case ENTER:
                return KEY_RETURN;
            case A:
                return KEY_A;
            case S:
                return KEY_S;
            case D:
                return KEY_D;
            case F:
                return KEY_F;
            case G:
                return KEY_G;
            case H:
                return KEY_H;
            case J:
                return KEY_J;
            case K:
                return KEY_K;
            case L:
                return KEY_L;
            case SEMICOLON:
                return KEY_SEMICOLON;
            case QUOTE:
                return KEY_APOSTROPHE;
            case BACK_QUOTE:
                return KEY_GRAVE;
            case SHIFT:
                return KEY_LSHIFT;
            case BACK_SLASH:
                return KEY_BACKSLASH;
            case Z:
                return KEY_Z;
            case X:
                return KEY_X;
            case C:
                return KEY_C;
            case V:
                return KEY_V;
            case B:
                return KEY_B;
            case N:
                return KEY_N;
            case M:
                return KEY_M;
            case COMMA:
                return KEY_COMMA;
            case PERIOD:
                return KEY_PERIOD;
            case SLASH:
                return KEY_SLASH;
            case MULTIPLY:
                return KEY_MULTIPLY;
            case SPACE:
                return KEY_SPACE;
            case CAPS:
                return KEY_CAPITAL;
            case F1:
                return KEY_F1;
            case F2:
                return KEY_F2;
            case F3:
                return KEY_F3;
            case F4:
                return KEY_F4;
            case F5:
                return KEY_F5;
            case F6:
                return KEY_F6;
            case F7:
                return KEY_F7;
            case F8:
                return KEY_F8;
            case F9:
                return KEY_F9;
            case F10:
                return KEY_F10;
            case NUM_LOCK:
                return KEY_NUMLOCK;
            case SCROLL_LOCK:
                return KEY_SCROLL;
            case NUMPAD7:
                return KEY_NUMPAD7;
            case NUMPAD8:
                return KEY_NUMPAD8;
            case NUMPAD9:
                return KEY_NUMPAD9;
            case SUBTRACT:
                return KEY_SUBTRACT;
            case NUMPAD4:
                return KEY_NUMPAD4;
            case NUMPAD5:
                return KEY_NUMPAD5;
            case NUMPAD6:
                return KEY_NUMPAD6;
            case ADD:
                return KEY_ADD;
            case NUMPAD1:
                return KEY_NUMPAD1;
            case NUMPAD2:
                return KEY_NUMPAD2;
            case NUMPAD3:
                return KEY_NUMPAD3;
            case NUMPAD0:
                return KEY_NUMPAD0;
            case DECIMAL:
                return KEY_DECIMAL;
            case F11:
                return KEY_F11;
            case F12:
                return KEY_F12;
            case F13:
                return KEY_F13;
            case F14:
                return KEY_F14;
            case F15:
                return KEY_F15;
            case KANA:
                return KEY_KANA;
            case CONVERT:
                return KEY_CONVERT;
            case NONCONVERT:
                return KEY_NOCONVERT;
            case CIRCUMFLEX:
                return KEY_CIRCUMFLEX;
            case AT:
                return KEY_AT;
            case COLON:
                return KEY_COLON;
                
//     * _ key (NEC PC98).
//            case 1111:
//                return KEY_UNDERLINE;
                
//     * stop key (NEC PC98).
//            case 1111:
//                return KEY_STOP;
                
            case DIVIDE:
                return KEY_DIVIDE;
            case HOME:
                return KEY_HOME;
            case UP:
                return KEY_UP;
            case PAGE_UP:
                return KEY_PRIOR;
            case LEFT:
                return KEY_LEFT;
            case RIGHT:
                return KEY_RIGHT;
                
            case END:
                return KEY_END;
//            case END:
//                return KEY_PAUSE;
                
            case DOWN:
                return KEY_DOWN;
            case PAGE_DOWN:
                return KEY_PGDN;
            case INSERT:
                return KEY_INSERT;
            case DELETE:
                return KEY_DELETE;
            case ALT:
                return KEY_LMENU; //Left vs. Right need to improve
            case ALT_GRAPH:
                return KEY_RMENU;
            case CONTROL:
                return KEY_LCONTROL;
//            case CONTROL:
//            	return KEY_RCONTROL;

        }
        LOG.log( Level.WARNING, "unsupported keyCode:{0}", kc);
        return 0;
    }
    
    // Running on jme
    
    @Override
    public void initialize() {
        // ignore
    }

    @Override
    public void update() {
        synchronized (eventQueue){
            for (int i = 0; i < eventQueue.size(); i++){
//                LOG.log(Level.INFO, "jme update keyEvent={0}", eventQueue.get(i));
                listener.onKeyEvent(eventQueue.get(i));
            }
            eventQueue.clear();
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
