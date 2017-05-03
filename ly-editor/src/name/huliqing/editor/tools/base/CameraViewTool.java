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
package name.huliqing.editor.tools.base;

import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.toolbar.BaseEditToolbar;
import name.huliqing.editor.tools.AbstractTool;
import name.huliqing.editor.utils.BestEditCamera;

/**
 * 用于切换相机视角工具, 这个工具的目的是把cameraTool分割成两个工具，以对应Jfx界面工具
 * @author huliqing
 */
public class CameraViewTool extends AbstractTool<SimpleJmeEdit, BaseEditToolbar> {

    private CameraTool cameraTool;

    public CameraViewTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    protected void onToolEvent(Event e) {
        // ignore
    }

    @Override
    public void initialize(SimpleJmeEdit edit, BaseEditToolbar toolbar) {
        super.initialize(edit, toolbar);
        cameraTool = toolbar.getCameraTool();
    }

    /**
     * 把镜头移动到场景原点
     */
    public void doChaseOrigin() {
        cameraTool.doChaseOrigin();
    }

    /**
     * 把镜头移动场景中选择的物体
     */
    public void doChaseSelected() {
        cameraTool.doChaseSelected();
    }

    /**
     * 让相机转到指定的视角
     * @param view
     */
    public void doSwitchView(BestEditCamera.View view) {
        cameraTool.doSwitchView(view);
    }

    /**
     * 切换到正交视角
     */
    public void doOrthoView() {
        cameraTool.doOrthoView();
    }
    
    /**
     * 切换到透视视角
     */
    public void doPerspView() {
        cameraTool.doPerspView();
    }
}
