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
package name.huliqing.luoying.ui.state;

/**
 * 镜头转动操作,主要将鼠标按下超过一定时间的操作视为镜头转动。不与其它的选择
   操作混淆在一起。镜头的转动由系统另外处理,这里主要避免在镜头转动的时候仍然去触发UI事件的问题.
 * @author huliqing
 */
public class CameraRotPickListener implements PickListener {

    // 镜头转动,当鼠标按下的时间达到CRPMT(cameraRotatePressMaxTime)时，视为
    // 镜头转动操作。优先级高于游戏逻辑中的其它“选择”操作。
    private final static long CRPMT = 170L;
    private long pressTime;
    
    @Override
    public boolean pick(boolean isPressed, float tpf) {
        if (isPressed) {
            pressTime = System.currentTimeMillis();
            return true;
        }
        
        return System.currentTimeMillis() - pressTime >= CRPMT;
    }
    
}
