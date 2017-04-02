/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.editor.ui.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.tools.terrain.TexLayer;
import name.huliqing.editor.tools.terrain.TexLayerTool;
import name.huliqing.editor.ui.utils.JfxUtils;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class JfxTerrainTexLayerTool extends JfxAbstractTool<TexLayerTool> implements TexLayerTool.LayerChangedListener {

    private final VBox view = new VBox();
    private final ToolBar btnPanel = new ToolBar();
    private final Button addLayer = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_ADD));
    private final Button removeLayer = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_SUBTRACT));
    private final Button removeNormal = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_TOOL_TERRAIN_TEXLAYER_REMOVE_NORMAL));
    
    private final TableView<TexLayer> content = new TableView();
    private int lastSelectRowIndex;
    
    public JfxTerrainTexLayerTool() {
        addLayer.setTooltip(new Tooltip(Manager.getRes(ResConstants.TOOL_TERRAIN_TEXLAYER_ADD_LAYER_TIP)));
        removeLayer.setTooltip(new Tooltip(Manager.getRes(ResConstants.TOOL_TERRAIN_TEXLAYER_REMOVE_LAYER_TIP)));
        removeNormal.setTooltip(new Tooltip(Manager.getRes(ResConstants.TOOL_TERRAIN_TEXLAYER_REMOVE_NORMAL_TIP)));
        btnPanel.getItems().addAll(addLayer, removeLayer, removeNormal);
        view.getChildren().addAll(btnPanel, content);
        
        addLayer.setOnAction(e -> {
            Jfx.runOnJme(() -> {
                int count = tool.getLayerCounts();
                tool.addTextureLayer(count);
                Jfx.runOnJfx(() -> {
                    content.getSelectionModel().select(count);
                });
                if (count >= 1) {
                    Jfx.runOnJfx(() -> removeLayer.setDisable(false));
                }
            });
        });
        removeLayer.setOnAction(e -> {
            int layerSize = content.getItems().size();
            if (layerSize <= 1) {
                removeLayer.setDisable(true);
                return;
            }
            Jfx.runOnJme(() -> {
                tool.removeTextureLayer(layerSize - 1);
                if (layerSize - 1 <= 1) {
                    Jfx.runOnJfx(() -> removeLayer.setDisable(true));
                }
            });
        });
        removeNormal.setOnAction(e -> {
            int layerIndex = content.getSelectionModel().getSelectedIndex();
            if (layerIndex < 0) {
                return;
            }
            Jfx.runOnJme(() -> {
                tool.setNormalTexture(layerIndex, null);
            });
        });
        
        content.prefWidthProperty().bind(view.widthProperty());
        content.prefHeightProperty().bind(view.heightProperty().subtract(btnPanel.heightProperty()));
        view.setMinHeight(250);
        
        initTableModel();
    }
    
    private void initTableModel() {
        TableColumn<TexLayer, String> select = new TableColumn(" ");
        select.setSortable(false);
        
        TableColumn<TexLayer, String> tex = new TableColumn(Manager.getRes(ResConstants.TOOL_TERRAIN_TEXLAYER_DIFFUSE));
        tex.setSortable(false);
        tex.setCellValueFactory(new PropertyValueFactory<>("diffuseMap"));
        tex.setCellFactory(c -> {
            return new TableCell<TexLayer, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(null);
                    setGraphic(null);
                    int rowIndex = getTableRow().getIndex();
                    if (rowIndex >= content.getItems().size()) 
                        return;
                    Labeled icon = (item == null || item.isEmpty()) ? new Button() : new Label("", createImage(item));
                    icon.setOnMouseClicked(e -> {
                        if (e.getButton() != MouseButton.PRIMARY) return;
                        File abstractFilePath = selectTexture(item);
                        if (abstractFilePath != null) {
                            Jfx.runOnJme(() -> {
                                tool.setDiffuseTexture(rowIndex, toAssetsFilePath(abstractFilePath.getAbsolutePath()));
                            });
                        }
                    });
                    icon.setPrefWidth(32);
                    icon.setPrefHeight(32);
                    setGraphic(icon);
                    setAlignment(Pos.CENTER);
                }
            };
        });
        
        TableColumn<TexLayer, String> nor = new TableColumn(Manager.getRes(ResConstants.TOOL_TERRAIN_TEXLAYER_NORMAL));
        nor.setSortable(false);
        nor.setCellValueFactory(new PropertyValueFactory<>("normalMap"));
        nor.setCellFactory(c -> {
            return new TableCell<TexLayer, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(null);
                    setGraphic(null);
                    int rowIndex = getTableRow().getIndex();
                    if (rowIndex >= content.getItems().size()) 
                        return;
                    Labeled icon = (item == null || item.isEmpty()) ? new Button() : new Label("", createImage(item));
                    icon.setOnMouseClicked(e -> {
                        if (e.getButton() != MouseButton.PRIMARY) return;
                        File abstractFilePath = selectTexture(item);
                        if (abstractFilePath != null) {
                            Jfx.runOnJme(() -> {
                                tool.setNormalTexture(rowIndex, toAssetsFilePath(abstractFilePath.getAbsolutePath()));
                            });
                        }
                    });
                    icon.setAlignment(Pos.CENTER);
                    icon.setPrefWidth(32);
                    icon.setPrefHeight(32);
                    setGraphic(icon);
                    setAlignment(Pos.CENTER);
                }
            };
        });
        
        TableColumn<TexLayer, Float> scale = new TableColumn(Manager.getRes(ResConstants.TOOL_TERRAIN_TEXLAYER_SCALE));
        scale.setSortable(false);
        scale.setCellValueFactory(new PropertyValueFactory<>("scale"));
        scale.setCellFactory(c -> {
            return new TableCell<TexLayer, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty); 
                    setText(null);
                    setGraphic(null);
                    if (!empty) {
                        int rowIndex = getTableRow().getIndex();
                        TextField scaleField = new TextField();
                        scaleField.setText(item + "");
                        scaleField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                            if (!newValue) {
                                updateTextureScale(rowIndex, scaleField.getText());
                            }
                        });
                        scaleField.setOnKeyPressed((KeyEvent event) -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                updateTextureScale(rowIndex, scaleField.getText());
                            }
                        });
                        scaleField.setPrefWidth(64);
                        setGraphic(scaleField);
                        setAlignment(Pos.CENTER);
                    }
                }
            };
        });
        select.setPrefWidth(20);
        tex.setPrefWidth(32);
        nor.setPrefWidth(32);
        scale.setPrefWidth(64);
        content.getColumns().addAll(select, tex, nor, scale);
        content.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        content.setPlaceholder(new Label("No Textures"));
        content.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable
                , Number oldValue, Number newValue) -> {
            if (newValue.intValue() < 0 || newValue.intValue() >= content.getItems().size() || newValue.intValue() == lastSelectRowIndex) {
                return;
            }
            lastSelectRowIndex = newValue.intValue();
            Jfx.runOnJme(() -> {
                tool.setSelectLayerIndex(newValue.intValue());
            });
        });
        content.getSelectionModel().select(0);
    }
    
    @Override
    public Region createView() {
        return view;
    }

    @Override
    public void initialize() {
        super.initialize();
        tool.addLayerChangedListener(this);
        reloadLayers(tool.getLayers());
    }

    @Override
    public void onLayerChanged(TexLayerTool tlt) {
        reloadLayers(tool.getLayers());
    }
    
    private void reloadLayers(final List<TexLayer> layers) {
        Jfx.runOnJfx(() -> {
            content.getItems().clear();
            if (layers != null && !layers.isEmpty()) {
                content.getItems().addAll(layers);
            }
            removeLayer.setDisable(content.getItems().size() <= 1);
            if (lastSelectRowIndex >= content.getItems().size()) {
                lastSelectRowIndex = content.getItems().size() -1;
            }
            if (lastSelectRowIndex < 0) {
                lastSelectRowIndex = 0;
            }
            content.getSelectionModel().select(lastSelectRowIndex);
        });
    }
    
    private void updateTextureScale(int layer, String scaleText) {
        Jfx.runOnJme(() -> {
            try {
                float scaleFloat = Float.parseFloat(scaleText);
                tool.setTextureScale(layer, scaleFloat);
            } catch (NumberFormatException nfe) {
                // ignore
            }
        });
    }
    
    private File selectTexture(String originTexturePathInAssets) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("image", "*.jpg", "*.png", "*.bmp"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("All File", "*.*"));
        String assetDir = Manager.getConfigManager().getMainAssetDir();
        if (originTexturePathInAssets != null) {
           File originFile = new File(assetDir, originTexturePathInAssets);
           if (originFile.exists()) {
               fc.setInitialDirectory(originFile.getParentFile());
               return fc.showOpenDialog(this.getView().getScene().getWindow());
           }
        }
        fc.setInitialDirectory(new File(assetDir));
        
        return fc.showOpenDialog(this.getView().getScene().getWindow());
    }
    
    private String toAssetsFilePath(String abstractFilePath) {
        String mainAssetDir = Manager.getConfigManager().getMainAssetDir();
        String fileOnAssets  = abstractFilePath.replace(mainAssetDir, "").replace("\\", "/");
        if (fileOnAssets.startsWith("/")) {
            fileOnAssets = fileOnAssets.substring(1);
        }
        return fileOnAssets;
    }

    private ImageView createImage(String texPath) {
        if (texPath == null || texPath.isEmpty())
            return null;
        File file = new File(Manager.getConfigManager().getMainAssetDir(), texPath);
        ImageView imageView;
        try {
            imageView = JfxUtils.createImage(new FileInputStream(file));
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            return imageView;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JfxTerrainTexLayerTool.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
