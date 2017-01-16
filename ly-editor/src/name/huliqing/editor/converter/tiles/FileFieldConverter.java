/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.tiles;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.converter.AbstractPropertyConverter;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.edit.JfxAbstractEdit;
import name.huliqing.editor.manager.Manager;

/**
 *
 * @author huliqing
 */
public class FileFieldConverter extends AbstractPropertyConverter {

    private final HBox layout = new HBox();
    private final TextField filePath = new TextField();
    private final Button btn = new Button("...");
    
    public FileFieldConverter() {
        layout.getStyleClass().add(StyleConstants.CLASS_HVBOX);
        layout.getChildren().addAll(filePath, btn);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openAssetsChooser();
            }
        });
        
        root.setContent(layout);
    }
    
    @Override
    public void initialize(JfxAbstractEdit editView, DataConverter parent, String property) {
        super.initialize(editView, parent, property);
    }
    
    @Override
    public void updateView(Object propertyValue) {
        if (propertyValue != null) {
            filePath.setText(propertyValue.toString());
        } else {
            filePath.setText("");
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
            filePath.setText(fileInAssets);
            parent.getData().setAttribute(property, fileInAssets);
            notifyChangedToParent();
        }
    }
}
