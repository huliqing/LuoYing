/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.tool;

import java.io.File;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

    private final ToolBar toolBar = new ToolBar();
    private final Button addLayer = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_TOOL_TERRAIN_TEXLAYER_ADD));
    private final Button removeLayer = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_TOOL_TERRAIN_TEXLAYER_SUBTRACT));
    private final Button removeNormal = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_TOOL_TERRAIN_TEXLAYER_REMOVE_NORMAL));
    
    private final VBox layout = new VBox();
    private final TableView<TexLayer> layerPanel = new TableView();
    private int lastSelectRowIndex;
    
    public JfxTerrainTexLayerTool() {
        addLayer.setTooltip(new Tooltip(Manager.getRes(ResConstants.TOOL_TERRAIN_TEXLAYER_ADD_LAYER_TIP)));
        removeLayer.setTooltip(new Tooltip(Manager.getRes(ResConstants.TOOL_TERRAIN_TEXLAYER_REMOVE_LAYER_TIP)));
        removeNormal.setTooltip(new Tooltip(Manager.getRes(ResConstants.TOOL_TERRAIN_TEXLAYER_REMOVE_NORMAL_TIP)));
        toolBar.getItems().addAll(addLayer, removeLayer, removeNormal);
        
        layout.getChildren().addAll(toolBar, layerPanel);
        addLayer.setOnAction(e -> {
            Jfx.runOnJme(() -> {
                int count = tool.getLayerCounts();
                tool.addTextureLayer(count);
                Jfx.runOnJfx(() -> {
                    layerPanel.getSelectionModel().select(count);
                });
                if (count >= 1) {
                    Jfx.runOnJfx(() -> removeLayer.setDisable(false));
                }
            });
        });
        removeLayer.setOnAction(e -> {
            int layerSize = layerPanel.getItems().size();
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
            int layerIndex = layerPanel.getSelectionModel().getSelectedIndex();
            if (layerIndex < 0) {
                return;
            }
            Jfx.runOnJme(() -> {
                tool.setNormalTexture(layerIndex, null);
            });
        });
        
        layerPanel.prefWidthProperty().bind(layout.widthProperty());
        layerPanel.prefHeightProperty().bind(layout.heightProperty());
        layout.setMinHeight(250);
        
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
                    if (rowIndex >= layerPanel.getItems().size()) 
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
                    if (rowIndex >= layerPanel.getItems().size()) 
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
        layerPanel.getColumns().addAll(select, tex, nor, scale);
        layerPanel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        layerPanel.setPlaceholder(new Label("No Textures"));
        layerPanel.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable
                , Number oldValue, Number newValue) -> {
            if (newValue.intValue() < 0 || newValue.intValue() >= layerPanel.getItems().size() || newValue.intValue() == lastSelectRowIndex) {
                return;
            }
            lastSelectRowIndex = newValue.intValue();
            Jfx.runOnJme(() -> {
                tool.setSelectLayerIndex(newValue.intValue());
            });
        });
        layerPanel.getSelectionModel().select(0);
    }
    
    @Override
    public Region createView() {
        return layout;
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
            layerPanel.getItems().clear();
            if (layers != null && !layers.isEmpty()) {
                layerPanel.getItems().addAll(layers);
            }
            removeLayer.setDisable(layerPanel.getItems().size() <= 1);
            if (lastSelectRowIndex >= layerPanel.getItems().size()) {
                lastSelectRowIndex = layerPanel.getItems().size() -1;
            }
            if (lastSelectRowIndex < 0) {
                lastSelectRowIndex = 0;
            }
            layerPanel.getSelectionModel().select(lastSelectRowIndex);
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
        ImageView imageView = JfxUtils.createImage(texPath, 30, 30);
        return imageView;
    }
}
