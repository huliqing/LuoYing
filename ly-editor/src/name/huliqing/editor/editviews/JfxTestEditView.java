/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.editviews;

import javafx.application.Platform;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import name.huliqing.editor.editviews.SimpleEditView;
import name.huliqing.editor.formview.FormView;
import name.huliqing.editor.ui.ToolBarView;
import name.huliqing.fxswing.Jfx;

/**
 * 测试用
 * @author huliqing
 */
public class JfxTestEditView extends SimpleEditView {

    private final Pane root;
    private Region editPanel;
    private Region toolbarView;
    
    public JfxTestEditView(Pane root) {
        this.root = root;
    }
    
    @Override
    public void initialize(FormView formView) {
        super.initialize(formView);

        Jfx.runOnJfx(() -> {
            initializeJfx();
        });
    }

    @Override
    public void cleanup() {
        Jfx.runOnJfx(() -> {
            root.getChildren().remove(editPanel);
            root.getChildren().remove(toolbarView);
        });
        super.cleanup(); 
    }
    
    public void initializeJfx() {
        editPanel = new VBox();
        editPanel.setBackground(Background.EMPTY);
        toolbarView = new ToolBarView(formView);
        
        root.getChildren().addAll(editPanel, toolbarView);
        
        // editPanel放在splitPane中，不能绑定min宽度，否则拉大后无法缩小，让其自动大小就可以
//        editPanel.minWidthProperty().bind(root.widthProperty()); 
        editPanel.minHeightProperty().bind(root.heightProperty().subtract(toolbarView.heightProperty()));
        
        // 绑定窗口大小
//        Jfx.getBindingController().bindCanvasToJfxRegion(Jfx.getJmeCanvas(), editPanel);
        
        // 强制刷新一下UI，必须的，否则界面无法实时刷新(JFX嵌入Swing的一个BUG)
//        Jfx.forceUpdateJfxUI();
    }
    
    
}
