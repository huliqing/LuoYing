/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.manager.ConfigManager;
import name.huliqing.editor.manager.ConfigManager.ConfigChangedListener;
import name.huliqing.editor.ui.menu.CreateSceneMenuItem;
import name.huliqing.editor.ui.menu.DeleteMenuItem;
import name.huliqing.editor.ui.menu.EditMenuItem;
import name.huliqing.editor.ui.menu.RefreshMenuItem;
import name.huliqing.editor.ui.menu.RenameMenuItem;
import name.huliqing.fxswing.Jfx;

/**
 * 
 * @author huliqing
 */
public class AssetsForm extends VBox implements ConfigChangedListener {

//    private static final Logger LOG = Logger.getLogger(AssetsForm.class.getName());
    
    private final FileTree assetTree = new FileTree();
    
    private String currentAssetDir = "";
    
    private ContextMenu filePopup;
    private ContextMenu dirPopup;
    
    private final EditMenuItem editMenu = new EditMenuItem(assetTree);
    
    public AssetsForm() {
        getChildren().add(assetTree);
        
        assetTree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        assetTree.setOnMouseClicked((e) -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                doShowPopup(e.getScreenX(), e.getScreenY());
                e.consume();
            } else if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                editMenu.fire();
                e.consume();
            }
        });
        
        assetTree.setOnDragDetected(this::doDragDetected);
        assetTree.setOnDragDone(this::doDragDone);
//        assetTree.setOnDragOver(this::doDragOver);
        assetTree.prefWidthProperty().bind(widthProperty());
        assetTree.prefHeightProperty().bind(heightProperty());

        updateAassetDir();
        Manager.getConfigManager().addListener(this);
        setPrefHeight(300);
        
        setStyle("-fx-background-radius: 7;-fx-padding:7;-fx-background-color:lightgray;");
        
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
        if (fileSelected == null || !fileSelected.exists())
            return;
        
        if (fileSelected.isFile()) {
            if (filePopup == null) {
                filePopup = new ContextMenu();
                filePopup.getItems().add(editMenu);
                filePopup.getItems().add(new RenameMenuItem(assetTree));
                filePopup.getItems().add(new RefreshMenuItem(assetTree));
                filePopup.getItems().add(new SeparatorMenuItem());
                filePopup.getItems().add(new DeleteMenuItem(assetTree));
            }
            filePopup.show(this, x, y);
        } else {
            if (dirPopup == null) {
                dirPopup = new ContextMenu();
                dirPopup.getItems().add(new CreateSceneMenuItem(assetTree));
                dirPopup.getItems().add(new RenameMenuItem(assetTree));
                dirPopup.getItems().add(new RefreshMenuItem(assetTree));
                dirPopup.getItems().add(new SeparatorMenuItem());
                dirPopup.getItems().add(new DeleteMenuItem(assetTree));
            }
            dirPopup.show(this, x, y);
        }
    }
    
    private void doDragDetected(MouseEvent e) {
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
//        LOG.log(Level.INFO, "doDragDone.");
    }

}
