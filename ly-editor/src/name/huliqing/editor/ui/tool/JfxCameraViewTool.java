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
package name.huliqing.editor.ui.tool;

import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.tools.base.CameraViewTool;
import name.huliqing.editor.utils.BestEditCamera;
import name.huliqing.fxswing.Jfx;

/**
 * Jfx界面相机视角操作工具
 * @author huliqing
 */
public class JfxCameraViewTool extends JfxAbstractTool<CameraViewTool> {
    
    private final MenuButton view = new MenuButton();

    public JfxCameraViewTool() {
        view.setPrefWidth(80);
        view.setPrefHeight(25);
        view.setMaxHeight(25);
    }
    
    @Override
    public Region createView() {
        return view;
    }

    @Override
    public void initialize() {
        super.initialize();
        view.setText(tool.getName());
        
        if (tool.getTips() != null) {
            view.setTooltip(new Tooltip(tool.getTips()));
        }
        
        MenuItem focusOrigin = new MenuItem(Manager.getRes(ResConstants.VIEW_FOCUS_ORIGIN));
        MenuItem focusTarget = new MenuItem(Manager.getRes(ResConstants.VIEW_FOCUS_TARGET));
        MenuItem viewBack = new MenuItem(Manager.getRes(ResConstants.VIEW_BACK));
        MenuItem viewBottom = new MenuItem(Manager.getRes(ResConstants.VIEW_BOTTOM));
        MenuItem viewFront = new MenuItem(Manager.getRes(ResConstants.VIEW_FRONT));
        MenuItem viewLeft = new MenuItem(Manager.getRes(ResConstants.VIEW_LEFT));
        MenuItem viewRight = new MenuItem(Manager.getRes(ResConstants.VIEW_RIGHT));
        MenuItem viewTop = new MenuItem(Manager.getRes(ResConstants.VIEW_TOP));
        MenuItem viewOrtho = new MenuItem(Manager.getRes(ResConstants.VIEW_ORTHO));
        MenuItem viewPersp = new MenuItem(Manager.getRes(ResConstants.VIEW_PERSP));
        
        focusOrigin.setOnAction(t -> {Jfx.runOnJme(() -> tool.doChaseOrigin());});
        focusTarget.setOnAction(t -> {Jfx.runOnJme(() ->tool.doChaseSelected());});
        viewBack.setOnAction(t -> {Jfx.runOnJme(() ->tool.doSwitchView(BestEditCamera.View.back));});
        viewBottom.setOnAction(t -> {Jfx.runOnJme(() ->tool.doSwitchView(BestEditCamera.View.bottom));});
        viewFront.setOnAction(t -> {Jfx.runOnJme(() ->tool.doSwitchView(BestEditCamera.View.front));});
        viewLeft.setOnAction(t -> {Jfx.runOnJme(() ->tool.doSwitchView(BestEditCamera.View.left));});
        viewRight.setOnAction(t -> {Jfx.runOnJme(() ->tool.doSwitchView(BestEditCamera.View.right));});
        viewTop.setOnAction(t -> {Jfx.runOnJme(() ->tool.doSwitchView(BestEditCamera.View.top));});
        viewOrtho.setOnAction(t -> {Jfx.runOnJme(() ->tool.doOrthoView());});
        viewPersp.setOnAction(t -> {Jfx.runOnJme(() ->tool.doPerspView());});
        
        view.getItems().addAll(focusOrigin, focusTarget
                , viewBack, viewBottom, viewFront, viewLeft, viewRight, viewTop, viewOrtho, viewPersp);

    }

}
