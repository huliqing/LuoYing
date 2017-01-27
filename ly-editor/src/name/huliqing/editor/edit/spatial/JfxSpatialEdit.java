/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.spatial;

import java.util.logging.Logger;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import name.huliqing.editor.edit.JfxSimpleEdit;
import name.huliqing.editor.manager.EditManager;
import name.huliqing.editor.toolbar.EditToolbar;
import name.huliqing.editor.toolbar.JfxToolbar;
import name.huliqing.fxswing.Jfx;

/**
 * Spatial编辑器
 * @author huliqing
 */
public class JfxSpatialEdit extends JfxSimpleEdit<SpatialEdit> {
//    private static final Logger LOG = Logger.getLogger(JfxSpatialEdit.class.getName());

    private JfxToolbar jfxToolbar;
    
    public JfxSpatialEdit() {
        this.jmeEdit = new SpatialEdit();
    }

    @Override
    protected void jfxInitialize() {
        super.jfxInitialize();
        jfxToolbar = new JfxToolbar((EditToolbar)jmeEdit.getToolbar());
        jfxToolbar.initialize();
        setToolbar(jfxToolbar);
    }

    @Override
    protected void jfxCleanup() {
        setToolbar(null);
        jfxToolbar.cleanup();
        jfxToolbar = null;
        super.jfxCleanup();
    }

    @Override
    protected void onDragOver(DragEvent e) {
        Dragboard db = e.getDragboard();
        if (db.hasFiles()) {
            e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        e.consume();
    }

    @Override
    protected void onDragDropped(DragEvent e) {
        Dragboard db = e.getDragboard();
        if (db.hasFiles()) {
            EditManager.openSpatialEditor(db.getFiles().get(0).getAbsolutePath());
            e.setDropCompleted(true);
        }
        e.consume();
    }

    public void setFilePath(String abstractFilePath) {
        Jfx.runOnJme(() -> {
            jmeEdit.setFilePath(abstractFilePath);
        });
    }

    
}
