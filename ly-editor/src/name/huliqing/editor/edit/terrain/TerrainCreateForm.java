/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.terrain;

import com.jme3.asset.AssetManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;

/**
 *
 * @author huliqing
 */
public class TerrainCreateForm extends VBox {
    
    private final ToggleGroup tg = new ToggleGroup();
    private final GridPane tgPanel = new GridPane();
    private final RadioButton flat= new RadioButton(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_FLAT));
    private final RadioButton hill = new RadioButton(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_HILL));
    private final RadioButton imageBased = new RadioButton(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_IMAGE));
    
    public final BasePanel basePanel = new BasePanel();
    public final FlatPanel flatPanel = new FlatPanel();
    public final HillPanel hillPanel = new HillPanel(basePanel);
    public ImageBasedPanel imageBasedPanel; 
    
    private final GridPane btnPane = new GridPane();
    private final Button ok = new Button(Manager.getRes(ResConstants.COMMON_OK));
    private final Button cancel = new Button(Manager.getRes(ResConstants.COMMON_CANCEL));
    
    public TerrainCreateForm(AssetManager am) {
        imageBasedPanel = new ImageBasedPanel(am);
        
        tg.getToggles().addAll(flat, hill, imageBased);
        tgPanel.addRow(0, new Label(), flat, hill, imageBased); // new Label用于占位
        btnPane.addRow(0, new Label(), ok, cancel);
        getChildren().addAll(basePanel, tgPanel, flatPanel, hillPanel, imageBasedPanel, btnPane);
        
        flat.setTooltip(new Tooltip(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_FLAT_TIP)));
        hill.setTooltip(new Tooltip(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_HILL_TIP)));
        imageBased.setTooltip(new Tooltip(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_IMAGE_TIP)));
        
        tgPanel.getColumnConstraints().add(new ColumnConstraints(100));
        tgPanel.setHgap(10);
        tgPanel.setPadding(new Insets(0, 0, 10, 0));
        
        flatPanel.managedProperty().bind(flatPanel.visibleProperty());
        hillPanel.managedProperty().bind(hillPanel.visibleProperty());
        imageBasedPanel.managedProperty().bind(imageBasedPanel.visibleProperty());
        
        ok.setMinWidth(80);
        cancel.setMinWidth(80);
        btnPane.getColumnConstraints().addAll(new ColumnConstraints(110), new ColumnConstraints(100), new ColumnConstraints(100));
        btnPane.setPadding(new Insets(10, 0, 10, 0));
        
        setMinWidth(550);
        setMinHeight(320);
        
        tg.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                flatPanel.setVisible(false);
                hillPanel.setVisible(false);
                imageBasedPanel.setVisible(false);
                if (newValue == flat) {
                    flatPanel.setVisible(true);
                } else if (newValue == hill) {
                    hillPanel.setVisible(true);
                } else if (newValue == imageBased) {
                    imageBasedPanel.setVisible(true);
                }
            }
        });
        
        tg.selectToggle(flat);
        
    }
    
    public void setOnOk(EventHandler<ActionEvent> e) {
        ok.setOnAction(e);
    }
    
    public void setOnCancel(EventHandler<ActionEvent> e) {
        cancel.setOnAction(e);
    }
}
