/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.tiles;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

/**
 * 目标树组件
 * @author huliqing
 */
public class FileTree extends TreeView {

    private final Callback<TreeView<File>, TreeCell<File>> cellCallback = new CellCallback();
    
    public FileTree() {}
    
    public FileTree(File rootDir) {
        setRootDir(rootDir);
    }
    
    public void setRootDir(File rootDir) {
        setCellFactory(cellCallback);
        TreeItem<File> root = createNode(rootDir);
        setRoot(root);
    }
    
    private class CellCallback implements Callback<TreeView<File>, TreeCell<File>> {

        @Override
        public TreeCell<File> call(TreeView<File> param) {
            TreeCell<File> cell = new TreeCell<File>() {
                @Override
                protected void updateItem(File item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        setText(item.getName());
                    } else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            };
            return cell;
        }
    }
    
    private TreeItem<File> createNode(final File f) {
        return new TreeItem<File>(f) {
            private boolean isLeaf;
            private boolean isFirstTimeChildren = true;
            private boolean isFirstTimeLeaf = true;

            @Override
            public ObservableList<TreeItem<File>> getChildren() {
                if (isFirstTimeChildren) {
                    isFirstTimeChildren = false;
                    super.getChildren().setAll(buildChildren(this));
                }
                return super.getChildren();
            }

            @Override
            public boolean isLeaf() {
                if (isFirstTimeLeaf) {
                    isFirstTimeLeaf = false;
                    File f = (File) getValue();
                    File[] children = f.listFiles();
                    isLeaf = f.isFile() || children == null || children.length == 0;
                }
                return isLeaf;
            }

            private ObservableList<TreeItem<File>> buildChildren(TreeItem<File> TreeItem) {
                File f = TreeItem.getValue();
                if (f != null && f.isDirectory()) {
                    File[] files = f.listFiles();
                    if (files != null) {
                        ObservableList<TreeItem<File>> children = FXCollections.observableArrayList();

                        for (File childFile : files) {
                            children.add(createNode(childFile));
                        }

                        return children;
                    }
                }
                return FXCollections.emptyObservableList();
            }
        };
    }
}
