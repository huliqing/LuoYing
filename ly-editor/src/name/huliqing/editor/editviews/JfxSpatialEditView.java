/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.editviews;

import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import name.huliqing.editor.editforms.SpatialEditForm;
import name.huliqing.editor.manager.EditManager;
import name.huliqing.editor.ui.ToolBarView;
import name.huliqing.fxswing.Jfx;

/**
 * Spatial编辑UI界面
 * @author huliqing
 */
public class JfxSpatialEditView extends SimpleEditView<SpatialEditForm> {

    private final Pane root;
    private Region editPanel;
    private Region toolbarView;
    
    // 当进行DragAndDrop操作时editPanel不能全透明，否则editPanel显应不了事件
    private final static String STYLE_ON_DRAG_STARTED = "-fx-background-color:rgba(0,0,0,0.01)";
    // 在DragAndDrop操作结束之后要重新把editPanel设置为透明
    private final static String STYLE_ON_DRAG_ENDED = "-fx-background-color:rgba(0,0,0,0)";
    
    public JfxSpatialEditView(Pane root) {
        this.root = root;
    }
    
    @Override
    public void initialize(SpatialEditForm form) {
        super.initialize(form);
        this.form = form;
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
        toolbarView = new ToolBarView(form);
        root.getChildren().addAll(editPanel, toolbarView);
        
        // editPanel放在splitPane中，不能绑定min宽度，否则拉大后无法缩小，让其自动大小就可以
//        editPanel.minWidthProperty().bind(root.widthProperty()); 
        editPanel.minHeightProperty().bind(root.heightProperty().subtract(toolbarView.heightProperty()));

        editPanel.setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            if (db.hasFiles()) {
                e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            e.consume();
        });
        editPanel.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            if (db.hasFiles()) {
                EditManager.openSpatialEditor(db.getFiles().get(0).getAbsolutePath());
                e.setDropCompleted(true);
            }
            e.consume();
        });
        
        // 强制刷新一下UI，必须的，否则界面无法实时刷新(JFX嵌入Swing的一个BUG)
        Jfx.forceUpdateJfxUI();
        Jfx.getBindingController().bindCanvasToJfxRegion(Jfx.getJmeCanvas(), editPanel);
    }

    @Override
    public void onDragStarted() {
        super.onDragStarted();
        Jfx.runOnJfx(() -> {editPanel.setStyle(STYLE_ON_DRAG_STARTED);});
    }

    @Override
    public void onDragEnded() {
        super.onDragEnded(); 
        Jfx.runOnJfx(() -> editPanel.setStyle(STYLE_ON_DRAG_ENDED));
    }
    
}
