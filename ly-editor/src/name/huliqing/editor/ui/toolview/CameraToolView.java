/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.toolview;

import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.tools.CameraTool;
import name.huliqing.editor.tools.Tool;
import name.huliqing.editor.utils.BestEditCamera;

/**
 *
 * @author huliqing
 */
public class CameraToolView extends AbstractToolView {
    
    private final MenuButton view = new MenuButton();
    private CameraTool cameraTool;

    @Override
    protected void setViewActivated(boolean activated) {
        // ignore
    }

    @Override
    protected void setViewEnabled(boolean enabled) {
        // ignore
    }

    @Override
    public Node getView() {
        return view;
    }

    @Override
    public void initialize(Tool tool, Toolbar toolbar, String name, String tooltip, String icon) {
        super.initialize(tool, toolbar, name, tooltip, icon);
        cameraTool = (CameraTool) tool;
        view.setText(name);
        
        if (tooltip != null && !tooltip.isEmpty()) {
            view.setTooltip(new Tooltip(tooltip));
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
        
        focusOrigin.setOnAction(t -> cameraTool.doChaseOrigin());
        focusTarget.setOnAction(t -> cameraTool.doChaseSelected());
        viewBack.setOnAction(t -> cameraTool.doSwitchView(BestEditCamera.View.back));
        viewBottom.setOnAction(t -> cameraTool.doSwitchView(BestEditCamera.View.bottom));
        viewFront.setOnAction(t -> cameraTool.doSwitchView(BestEditCamera.View.front));
        viewLeft.setOnAction(t -> cameraTool.doSwitchView(BestEditCamera.View.left));
        viewRight.setOnAction(t -> cameraTool.doSwitchView(BestEditCamera.View.right));
        viewTop.setOnAction(t -> cameraTool.doSwitchView(BestEditCamera.View.top));
        viewOrtho.setOnAction(t -> cameraTool.doOrthoView());
        viewPersp.setOnAction(t -> cameraTool.doPerspView());
        
        view.getItems().addAll(focusOrigin, focusTarget
                , viewBack, viewBottom, viewFront, viewLeft, viewRight, viewTop, viewOrtho, viewPersp);

    }
}
