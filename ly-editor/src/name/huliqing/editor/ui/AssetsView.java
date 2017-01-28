/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Modality;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.manager.ConfigManager;
import name.huliqing.editor.manager.ConfigManager.ConfigChangedListener;
import name.huliqing.editor.manager.EditManager;
import name.huliqing.fxswing.Jfx;

/**
 * 
 * @author huliqing
 */
public class AssetsView extends ScrollPane implements ConfigChangedListener {

    private static final Logger LOG = Logger.getLogger(AssetsView.class.getName());
    
    private final FileTree assetTree = new FileTree();
    private String currentAssetDir = "";
    private final ContextMenu popup = new ContextMenu();
    
    public AssetsView() {
        assetTree.minHeightProperty().bind(heightProperty());
        assetTree.minWidthProperty().bind(widthProperty());
        assetTree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setContent(assetTree);

        updateAassetDir();
        Manager.getConfigManager().addListener(this);
        
        assetTree.setOnMouseClicked((e) -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                doShowPopup(e.getScreenX(), e.getScreenY());
                e.consume();
            } else if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                doEdit();
                e.consume();
            }
        });
        
        assetTree.setOnDragDetected(this::doDragDetected);
        assetTree.setOnDragDone(this::doDragDone);
//        assetTree.setOnDragOver(this::doDragOver);
        
        MenuItem edit = new MenuItem(Manager.getRes(ResConstants.POPUP_EDIT));
        MenuItem rename = new MenuItem(Manager.getRes(ResConstants.POPUP_RENAME));
        MenuItem refresh = new MenuItem(Manager.getRes(ResConstants.POPUP_REFRESH));
        MenuItem separator = new SeparatorMenuItem();
        MenuItem delete = new MenuItem(Manager.getRes(ResConstants.POPUP_DELETE));
        popup.getItems().addAll(edit, rename, refresh, separator, delete);
        edit.setOnAction(e -> doEdit());
        rename.setOnAction(e -> doRename());
        refresh.setOnAction(e -> doRefresh());
        delete.setOnAction(e -> doDelete());
        
    }
    
    @Override
    public void onConfigChanged(String key) {
        String newMainAssets = Manager.getConfigManager().getMainAssetDir();
        if (key.equals(ConfigManager.KEY_MAIN_ASSETS) && !currentAssetDir.equals(newMainAssets)) {
            Jfx.runOnJfx(() -> updateAassetDir());
        }
    }
    
    private void updateAassetDir() {
        String mainAssetsDir = Manager.getConfigManager().getMainAssetDir();
        if (mainAssetsDir != null && !mainAssetsDir.isEmpty()) {
            File assetsDirFile = new File(mainAssetsDir);
            if (assetsDirFile.isDirectory()) {
                currentAssetDir = mainAssetsDir;
                ((FileTree)assetTree).setRootDir(assetsDirFile);
                assetTree.getRoot().setExpanded(true);
            }            
        }
    }
    
    private TreeItem<File> getSelectMainItem() {
        ObservableList<TreeItem<File>> selectedItems = assetTree.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty())
            return null;
        TreeItem<File> itemSelect = selectedItems.get(0);
        if (itemSelect == null || itemSelect.getValue() == null) {
            return null;
        }
        return itemSelect;
    }
    
    private File getSelectMainFile() {
        TreeItem<File> selectItem = getSelectMainItem();
        return selectItem != null ? selectItem.getValue() : null;
    }
    
    private void doShowPopup(double x, double y) {
        File fileSelected = getSelectMainFile();
        if (fileSelected == null)
            return;
        popup.show(this, x, y);
    }
    
    private void doEdit() {
        File fileSelected = getSelectMainFile();
        if (fileSelected == null || fileSelected.isDirectory()) 
            return;
        EditManager.openSpatialEditor(fileSelected.getAbsolutePath());
    }
    
    private void doRename() {
        ObservableList<TreeItem<File>> selectedItems = assetTree.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty())
            return;
        TreeItem<File> itemSelect = selectedItems.get(0);
        if (itemSelect == null || itemSelect.getValue() == null) {
            return;
        }
        TextInputDialog dialog = new TextInputDialog(itemSelect.getValue().getName());
        dialog.setTitle(Manager.getRes(ResConstants.ALERT_RENAME_TITLE));
        dialog.setHeaderText(Manager.getRes(ResConstants.ALERT_RENAME_HEADER
                , new Object[] {itemSelect.getValue().getName()}));
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (name == null || name.trim().isEmpty()) {
                return;
            }
            File file = itemSelect.getValue();
            File newFile = new File(file.getParent(), name);
            file.renameTo(newFile);
            itemSelect.setValue(newFile);
        });
    }
    
    private void doRefresh() {
        ObservableList<TreeItem<File>> items = assetTree.getSelectionModel().getSelectedItems();
        items.filtered(t -> t != null).forEach(t -> {
            assetTree.refreshItem(t);
        });
    }
    
    private void doDelete() {
        ObservableList<TreeItem<File>> items = assetTree.getSelectionModel().getSelectedItems();
        if (items.isEmpty())
            return;
        List<TreeItem<File>> selectList = new ArrayList<TreeItem<File>>(items);
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(this.getScene().getWindow());
        alert.setTitle(Manager.getRes(ResConstants.ALERT_DELETE_TITLE));
        if (selectList.size() == 1 && selectList.get(0).getValue() != null) {
            alert.setHeaderText(Manager.getRes(ResConstants.ALERT_DELETE_HEADER_SINGLE));
            alert.setContentText(selectList.get(0).getValue().getName());
        } else {
            alert.setHeaderText(Manager.getRes(ResConstants.ALERT_DELETE_HEADER_MULT, new Object[]{selectList.size()}));
        }
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            selectList.stream().filter(t -> t.getValue() != null).forEach(t -> {
                deleteFile(t.getValue()); // 递归删除
                if (t.getParent() != null) {
                    t.getParent().getChildren().remove(t);
                }
            });
        }
    }
    
    private void deleteFile(File file) {
        if (file.isDirectory()) {
            for (File sub : file.listFiles()) {
                deleteFile(sub);
            }
        }
        file.delete();
    }
    
    private void doDragDetected(MouseEvent e) {
        LOG.log(Level.INFO, "doDragDetected.");
        ObservableList<TreeItem<File>> items = assetTree.getSelectionModel().getSelectedItems();
        if (items.isEmpty()) {
            e.consume();
            return;
        }
        List<File> files = new ArrayList<>(items.size());
        items.filtered(t -> t.getValue() != null).forEach(t -> {files.add(t.getValue());});
        if (files.size() <= 0) {
            e.consume();
            return;
        } 
        Dragboard db = assetTree.startDragAndDrop(TransferMode.COPY_OR_MOVE);
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.put(DataFormat.FILES, files);
        db.setContent(clipboardContent);
        e.consume();
    }
    
    private void doDragDone(DragEvent e) {
        e.consume();
        LOG.log(Level.INFO, "doDragDone.");
    }

}
