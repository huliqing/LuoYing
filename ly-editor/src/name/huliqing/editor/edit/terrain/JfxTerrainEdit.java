/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.terrain;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import name.huliqing.editor.edit.JfxAbstractEdit;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class JfxTerrainEdit extends JfxAbstractEdit<TerrainEdit>{
    
    private final Pane editPanel = new VBox();
    
    public JfxTerrainEdit() {
        jmeEdit = new TerrainEdit();
    }
    
    @Override
    public Pane getPropertyPanel() {
        return editPanel;
    }

    @Override
    protected void jfxInitialize() {
        editRoot.getChildren().addAll(editPanel);
        
        editPanel.prefWidthProperty().bind(editRoot.widthProperty());
        editPanel.prefHeightProperty().bind(editRoot.heightProperty());
        editPanel.setVisible(false);
        
        Jfx.jfxForceUpdate();
        Jfx.jfxCanvasBind(editPanel);
    }

    @Override
    protected void jfxCleanup() {
        editPanel.prefWidthProperty().unbind();
        editPanel.prefHeightProperty().unbind();
        editRoot.getChildren().removeAll(editPanel);
    }
    
}
