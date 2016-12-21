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
package name.huliqing.luoying.object.progress;

import com.jme3.scene.Node;
import name.huliqing.luoying.data.ProgressData;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * Progress用于实现动画载入功能，例如进度条载入动画等。
 * @author huliqing
 */
public interface Progress extends DataProcessor<ProgressData>{
    
    /**
     * 初始化进度条.
     * @param viewRoot 用于进度条视图的父节点，比如GUI的根节点。
     */
    void initialize(Node viewRoot);
    
    /**
     * 判断是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 更新并渲染进度条，给定的参数progress取值为 0.0~1.0， 1.0表示100%载入完成, 0.5表示%50完成，依此类推。
     * @param progress 
     */
    void display(float progress);
    
    /**
     * 清理并释放资源
     */
    void cleanup();
    
}
