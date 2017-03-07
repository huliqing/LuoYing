/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.field;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import name.huliqing.editor.component.ComponentDefine;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.converter.SimpleFieldConverter;
import name.huliqing.editor.manager.ComponentManager;
import name.huliqing.editor.manager.ConverterManager;
import name.huliqing.editor.ui.ComponentSearch;
import name.huliqing.editor.ui.utils.JfxUtils;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 用于转换所有属性值为单个ObjectData的字段.
 * @author huliqing
 */
public class DataFieldConverter extends SimpleFieldConverter {
    private static final Logger LOG = Logger.getLogger(DataFieldConverter.class.getName());
    
    private final static String FEATURE_COMPONENT_TYPE = "componentType";
    private String componentType;
    
    private final HBox layout = new HBox();
    private final TextField input = new TextField();
    private final Button selectButton = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_SEARCH));
    protected ComponentSearch<ComponentDefine> componentList = new ComponentSearch();
    
    private ObjectData lastObjectData;
    private DataConverter dataConverter;
    
    public DataFieldConverter() {
        input.setStyle("-fx-background-radius:7 0 0 7;");
        selectButton.setStyle("-fx-background-radius: 0 7 7 0;");
        layout.getChildren().addAll(input, selectButton);
        
        input.focusedProperty().addListener((ObservableValue<? extends Boolean> observable
                , Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                refeshDataConverter();
                return;
            }
            updateChangedAndSave();
        });
        input.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateChangedAndSave();
            }
        });
        
        selectButton.setOnAction(e -> {
            componentList.show(input, -10, -8);
        });
        
        componentList.getListView().setOnMouseClicked(e -> {
            ComponentDefine cd = componentList.getListView().getSelectionModel().getSelectedItem();
            if (cd != null) {
                input.setText(cd.getId());
                updateChangedAndSave();
            }
            componentList.hide();
        });
    }
    
    private void refeshDataConverter() {
        if (lastObjectData != null) {
            dataConverter = ConverterManager.createDataConverter(jfxEdit, lastObjectData, this);
            dataConverter.initialize();
        }
        if (dataConverter != null && lastObjectData != null) {
//            getParent().setChildContent(lastObjectData.getId(), dataConverter.getLayout());
            getParent().setChildLayout(lastObjectData.getId(), dataConverter);
        }
    }
    
    private void updateChangedAndSave() {
        String newValue = input.getText();
        if (newValue != null && lastObjectData != null && newValue.equals(lastObjectData.getId()))
            return;
        
        ObjectData newObjectData = null;
        if (newValue != null && !newValue.trim().isEmpty()) {
            newObjectData = Loader.loadData(newValue);
        }
        updateAttribute(newObjectData);
        addUndoRedo(lastObjectData, newObjectData);
        lastObjectData = newObjectData;
        
        // 刷新
        refeshDataConverter();
    }
    
    @Override
    protected Node createLayout() {
        return layout;
    }
    
    @Override
    public void initialize() {
        super.initialize();
        
        componentType = featureHelper.getAsString(FEATURE_COMPONENT_TYPE);
        if (componentType != null) {
            componentList.setComponents(ComponentManager.getComponentsByType(componentType));
        } else {
            componentList.setComponents(ComponentManager.getComponents(null));
        }
    }

    @Override
    public void cleanup() {
        if (dataConverter != null) {
            if (dataConverter.isInitialized()) {
                dataConverter.cleanup();
            }
            dataConverter = null;
        }
        super.cleanup(); 
    }

    @Override
    protected void updateUI() {
        Object propertyValue = data.getAttribute(field);
        ObjectData objectData;
        try {
            objectData = (ObjectData) propertyValue;
        } catch (java.lang.ClassCastException e) {
            LOG.log(Level.SEVERE, "Could not convert property to ObjectData, data=" + data.getId() + ", field=" + field, e);
            return;
        }
        if (lastObjectData != objectData) {
            lastObjectData = objectData;
            if (dataConverter != null) {
                dataConverter.cleanup();
                dataConverter = null;
            }
        }
        input.setText(lastObjectData != null ? lastObjectData.getId() : "");
    }
    
}
