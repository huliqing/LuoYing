/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.spatial;

import java.util.logging.Logger;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import name.huliqing.editor.edit.JfxAbstractEdit;
import name.huliqing.editor.manager.EditManager;
import name.huliqing.editor.ui.ToolBarView;
import name.huliqing.fxswing.Jfx;

/**
 * Spatial编辑UI界面
 * @author huliqing
 */
public class JfxSpatialEdit extends JfxAbstractEdit<SpatialEdit> {

    private static final Logger LOG = Logger.getLogger(JfxSpatialEdit.class.getName());

    private Region editPanel;
    private Region toolbarView;
    
    // editPanel不能完全透明，完全透明则响应不了事件，在响应事件时还需要设置为visible=true
    private final static String STYLE_EDIT_PANEL = "-fx-background-color:rgba(0,0,0,0.01)";
    
    public JfxSpatialEdit() {
        this.form = new SpatialEdit();
    }

    @Override
    protected void jfxInitialize() {
        editPanel = new VBox();
        toolbarView = new ToolBarView(form);
        editRoot.getChildren().addAll(editPanel, toolbarView);
        
        editPanel.prefWidthProperty().bind(editRoot.widthProperty());
        editPanel.prefHeightProperty().bind(editRoot.heightProperty().subtract(toolbarView.heightProperty()));
        editPanel.setVisible(false);
        editPanel.setStyle(STYLE_EDIT_PANEL);
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
        
        toolbarView.prefWidthProperty().bind((editRoot.widthProperty()));
        
        // 强制刷新一下UI，必须的，否则界面无法实时刷新(JFX嵌入Swing的一个BUG)
        Jfx.jfxForceUpdate();
        Jfx.jfxCanvasBind(editPanel);
    }

    @Override
    protected void jfxCleanup() {
        toolbarView.prefWidthProperty().unbind();
        editPanel.prefWidthProperty().unbind();
        editPanel.prefHeightProperty().unbind();
        editRoot.getChildren().removeAll(editPanel, toolbarView);
    }

    @Override
    public void jfxOnDragStarted() {
        super.jfxOnDragStarted();
        editPanel.setVisible(true);
    }

    @Override
    public void jfxOnDragEnded() {
        super.jfxOnDragEnded();
        editPanel.setVisible(false);
    }

    public void setFilePath(String abstractFilePath) {
        Jfx.runOnJme(() -> {
            form.setFilePath(abstractFilePath);
        });
    }
}
