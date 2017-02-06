/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.terrain;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;

/**
 *
 * @author huliqing
 */
public class JfxTerrainCreateForm extends VBox {
    private final GridPane grid = new GridPane();
    private final Label nameLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TERRAIN_NAME));
    private final Label totalSizeLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TOTAL_SIZE));
    private final Label patchSizeLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_PATCH_SIZE));
    // The size of one side of the texture, eg. 512. This will be used to blend several textures together into one texture for
    // the terrain. The larger the size, the more detailed the image. But it will use more memory and possibly slow 
    // down the editor. You cannot change this value after it is set!
    private final Label alphaTextureSizeLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_ALPHA_SIZE));
    
    public final TextField nameField = new TextField();
    public final TextField totalSizeField = new TextField("256");
    public final TextField patchSizeField = new TextField("64");
    public final TextField alphaTextureSizeField = new TextField("256");
    
    private final AnchorPane btnPane = new AnchorPane();
    private final Button ok = new Button(Manager.getRes(ResConstants.COMMON_OK));
    private final Button cancel = new Button(Manager.getRes(ResConstants.COMMON_CANCEL));
    
    public JfxTerrainCreateForm() {
        getChildren().addAll(grid, btnPane);
        
        grid.addRow(0, nameLabel, nameField);
        grid.addRow(1, totalSizeLabel, totalSizeField);
        grid.addRow(2, patchSizeLabel, patchSizeField);
        grid.addRow(3, alphaTextureSizeLabel, alphaTextureSizeField);
        
        GridPane.setHgrow(totalSizeField, Priority.ALWAYS);
        GridPane.setHgrow(patchSizeField, Priority.ALWAYS);
        
        btnPane.getChildren().addAll(ok, cancel);
        AnchorPane.setTopAnchor(ok, 10.0);
        AnchorPane.setLeftAnchor(ok, 120.0);
        
        AnchorPane.setTopAnchor(cancel, 10.0);
        AnchorPane.setRightAnchor(cancel, 100.0);
        btnPane.prefHeight(50);
        
        nameField.setPromptText(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TERRAIN_NAME_TIP));
        totalSizeField.setPromptText(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TOTAL_SIZE_TIP));
        patchSizeField.setPromptText(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_PATCH_SIZE_TIP));
        alphaTextureSizeField.setPromptText(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_ALPHA_SIZE_TIP));
        
        grid.setVgap(10);
        GridPane.setHalignment(nameLabel, HPos.RIGHT);
        GridPane.setHalignment(totalSizeLabel, HPos.RIGHT);
        GridPane.setHalignment(patchSizeLabel, HPos.RIGHT);
        GridPane.setHalignment(alphaTextureSizeLabel, HPos.RIGHT);
        setPadding(new Insets(10));
        setMinWidth(400);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        nameField.setText("terrain" + sdf.format(new Date()));
        
        // for debug
//        setStyle("-fx-border: solid inside;-fx-border-width:1;-fx-border-color:red;");
    }
    
    public void setOnOk(EventHandler<ActionEvent> e) {
        ok.setOnAction(e);
    }
    
    public void setOnCancel(EventHandler<ActionEvent> e) {
        cancel.setOnAction(e);
    }
}
