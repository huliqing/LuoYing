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
package name.huliqing.luoying.appstate;

import com.jme3.app.Application;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppStateManager;

/**
 *
 * @author huliqing
 */
public class CustomStatsAppState  extends StatsAppState{

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        statsView.setLocalTranslation(statsView.getLocalTranslation().addLocal(0, 200, 0));
        fpsText.setLocalTranslation(fpsText.getLocalTranslation().addLocal(0, 200, 0));
        darkenFps.setLocalTranslation(darkenFps.getLocalTranslation().addLocal(0, 200, 0));
        darkenStats.setLocalTranslation(darkenStats.getLocalTranslation().addLocal(0, 200, 0));
    }
    
}
