/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.property;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.converter.AbstractPropertyConverter;
import name.huliqing.editor.manager.Manager;
import name.huliqing.luoying.xml.Converter;

/**
 *
 * @author huliqing
 */
public class FileConverter extends AbstractPropertyConverter {
    
    /**
     * 打开文件夹窗口时文件显示的过滤格式, 格式："des|filter1|filter2, des|filter2|filter3|..."
     * 示例："Model File|*.j3o|*.obj|*.mesh.xml,All Files|*.*"
     */
    public final static String FEATURE_FILTERS = FileConverter.class.getName() + ":filters";

    private final HBox layout = new HBox();
    private final TextField fileTextField = new TextField();
    private final Button btn = new Button("...");
    
    private String lastValueSaved;
    
    private FileChooser fileChooser;
    
    public FileConverter() {
        layout.getStyleClass().add(StyleConstants.CLASS_HVBOX);
        layout.getChildren().addAll(fileTextField, btn);
        
        btn.setOnAction((ActionEvent event) -> {
            openAssetsChooser();
        });
        
        // 失去焦点时更新
        fileTextField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable
                , Boolean oldValue, Boolean newValue) -> {
            // 如果是获得焦点则不理睬。
            if (newValue) {
                return;
            }
            updateChangedAndSave();
        });
        // 按下Enter时更新
        fileTextField.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateChangedAndSave();
            }
        });
        
    }
    
    @Override
    protected Node createLayout() {
        return layout;
    }
    
    private void updateChangedAndSave() {
        String newValue = fileTextField.getText();
        if (newValue == null) {
            if (lastValueSaved == null) {
                return;
            }
        } else if (newValue.equals(lastValueSaved)) {
            return;
        }
        updateAttribute(newValue);
        addUndoRedo(lastValueSaved, newValue);
        lastValueSaved = newValue;
    }
    
    @Override
    public void updateUI(Object propertyValue) {
        lastValueSaved = Converter.getAsString(propertyValue);
        if (lastValueSaved != null) {
            fileTextField.setText(lastValueSaved);
        } else {
            fileTextField.setText("");
            lastValueSaved = "";
        }
    }
    
    private void openAssetsChooser() {
        if (fileChooser == null) {
            fileChooser = new FileChooser();
            fileChooser.setTitle(Manager.getRes(ResConstants.COMMON_CHOOSER_FILE));
            fileChooser.getExtensionFilters().addAll(getFilters());
        }
        String assetsDir = Manager.getConfigManager().getMainAssetDir();
        fileChooser.setInitialDirectory(new File(assetsDir));
        File file = fileChooser.showOpenDialog(null);
        if (file != null && file.exists() && file.isFile()) {
            String fileInAssets = file.getAbsolutePath().replace(assetsDir, "");
            fileInAssets = fileInAssets.replace("\\", "/").trim();
            if (fileInAssets.startsWith("/")) {
                fileInAssets = fileInAssets.substring(1);
            }
            fileTextField.setText(fileInAssets);
            updateChangedAndSave();
        }
    }
    
    private List<ExtensionFilter> getFilters() {
        // 示例："Model File|*.j3o|*.obj|*.mesh.xml,All Files|*.*"
        String[] filters = this.featureHelper.getAsArray(FEATURE_FILTERS);
        if (filters != null && filters.length > 0) {
            List<ExtensionFilter> efs = new ArrayList(filters.length);
            for (String f : filters) {
                if (f == null || f.trim().equals("")) {
                    continue;
                }
                List<String> itemList = new ArrayList(Arrays.asList(f.split("\\|")));
                String des = itemList.remove(0);
                efs.add(new ExtensionFilter(des, itemList));
            }
            return efs;
        }
        return Collections.EMPTY_LIST;
    }
}
