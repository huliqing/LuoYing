/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.tiles;

import java.io.File;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.converter.AbstractPropertyConverter;
import name.huliqing.editor.manager.Manager;
import name.huliqing.luoying.xml.Converter;

/**
 *
 * @author huliqing
 */
public class FileFieldConverter extends AbstractPropertyConverter {

    private final HBox layout = new HBox();
    private final TextField fileTextField = new TextField();
    private final Button btn = new Button("...");
    
    public FileFieldConverter() {
        layout.getStyleClass().add(StyleConstants.CLASS_HVBOX);
        layout.getChildren().addAll(fileTextField, btn);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openAssetsChooser();
            }
        });
        
        // 失去焦点时更新
        fileTextField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable
                , Boolean oldValue, Boolean newValue) -> {
            // 如果是获得焦点则不理睬。
            if (newValue) {
                return;
            }
            updateToJme();
        });
        // 按下Enter时更新
        fileTextField.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateToJme();
            }
        });
        
    }
    
    @Override
    protected Node createLayout() {
        return layout;
    }
    
    private void updateToJme() {
        updateAttribute(fileTextField.getText());
    }
    
    @Override
    public void updateUI(Object propertyValue) {
        String value = Converter.getAsString(propertyValue);
        if (value != null) {
            fileTextField.setText(value);
        } else {
            fileTextField.setText("");
        }
    }
    
    private void openAssetsChooser() {
        String assetsDir = Manager.getConfigManager().getMainAssetDir();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Manager.getRes(ResConstants.COMMON_CHOOSER_FILE));
        fileChooser.setInitialDirectory(new File(assetsDir));
        File file = fileChooser.showOpenDialog(null);
        if (file != null && file.exists() && file.isFile()) {
            String fileInAssets = file.getAbsolutePath().replace(assetsDir, "");
            fileInAssets = fileInAssets.replace("\\", "/").trim();
            if (fileInAssets.startsWith("/")) {
                fileInAssets = fileInAssets.substring(1);
            }
            fileTextField.setText(fileInAssets);
            updateToJme();
        }
    }
}
