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
package name.huliqing.luoying.data;

import name.huliqing.luoying.xml.ObjectData;
import com.jme3.network.serializing.Serializable;

/**
 * 任务数据
 * @author huliqing
 */
@Serializable
public class TaskData extends ObjectData {
    
    /**
     * 判断任务是否执行完成,完成即表示任务达成目标，中断或放弃不算完成。
     * @return 
     */
    public boolean isCompletion() {
        return getAsBoolean("completion", false);
    }

    /**
     * 设置任务“完成”状态
     * @param completion 
     */
    public void setCompletion(boolean completion) {
        setAttribute("completion", completion);
    }
    
}
