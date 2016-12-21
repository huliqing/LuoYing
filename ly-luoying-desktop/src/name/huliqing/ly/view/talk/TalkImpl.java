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
package name.huliqing.ly.view.talk;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public class TalkImpl implements Talk {
    private List<TalkListener> listeners;
    private final List<TalkLogic> logics = new ArrayList<TalkLogic>();

    private boolean started = false;
    private boolean end = true;
    // 是否在network中同时显示
    private boolean network = false;
    
    // 当前正在执行的逻辑索引
    private int index;
    // 当前正在执行的逻辑
    private TalkLogic current;
    
    @Override
    public void start() {
        started = true;
        end = false;
        index = -1;
        doNext();
    }

    @Override
    public void update(float tpf) {
        if (!started) {
            return;
        }
        if (logics.isEmpty()) {
            started = false;
            return;
        }
        if (end) {
            // doListeners
            if (listeners != null) {
                for (TalkListener listener : listeners) {
                    listener.onTalkEnd();
                }
            }
            cleanup();
            return;
        }
        if (current.isEnd()) {
            doNext();
        } else {
            current.update(tpf);
        }
    }
    
    @Override
    public boolean isEnd() {
        return !started;
    }
    
    protected void doNext() {
        index++;
        if (index >= logics.size()) {
            end = true;
            return;
        }
        current = logics.get(index);
        current.setNetwork(network);
        current.start();
    }
    
    // =============================

    @Override
    public Talk speak(Entity actor, String mess) {
        TalkLogicSpeak speak = new TalkLogicSpeak(actor, mess);
        logics.add(speak);
        return this;
    }

    @Override
    public Talk delay(float delay) {
        logics.add(new TalkLogicDelay(delay));
        return this;
    }

    @Override
    public Talk face(Entity actor, Entity target, boolean force) {
        logics.add(new TalkLogicFace(actor, target, force));
        return this;
    }

    @Override
    public Talk face(Entity actor, Vector3f position, boolean force) {
        logics.add(new TalkLogicFace(actor, position, force));
        return this;
    }

    @Override
    public Talk addTalkLogic(TalkLogic talkLogic) {
        logics.add(talkLogic);
        return this;
    }
    
    @Override
    public Talk addListener(TalkListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<TalkListener>();
        }
        listeners.add(listener);
        return this;
    }

    @Override
    public void cleanup() {
        for (TalkLogic logic : logics) {
            if (!logic.isEnd()) {
                logic.cleanup();
            }
        }
        logics.clear();
        index = -1;
        started = false;
    }

    @Override
    public void setNetwork(boolean network) {
        this.network = network;
    }
    
}
