/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.tool;

import java.io.File;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.tools.terrain.TexLayerTool;
import name.huliqing.editor.tools.terrain.TexLayerTool.Layer;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class JfxTerrainTexLayerTool extends JfxAbstractTool<TexLayerTool> implements TexLayerTool.LayerChangedListener {

    private final VBox layout = new VBox();
    private final HBox btnZone = new HBox();
    private final GridPane layerPanel = new GridPane();
    
    private final Button addBtn = new Button("add layer");
    private final Button removeBtn = new Button("remove layer");
    
    public JfxTerrainTexLayerTool() {
        layout.getChildren().addAll(btnZone, layerPanel);
        btnZone.getChildren().addAll(addBtn, removeBtn);
        addBtn.setOnAction(e -> {
            Jfx.runOnJme(() -> {
                int count = tool.getLayerCounts();
                tool.addTextureLayer(count);
                if (count >= 1) {
                    Jfx.runOnJfx(() -> removeBtn.setDisable(false));
                }
            });
        });
        removeBtn.setOnAction(e -> {
            Jfx.runOnJme(() -> {
                int count = tool.getLayerCounts();
                if (count >= 1) {
                    tool.removeTextureLayer(count - 1);
                }
                if (count <= 2) {
                    Jfx.runOnJfx(() -> removeBtn.setDisable(true));
                }
            });
        });
        layerPanel.setVgap(5);
    }
    
    @Override
    public Node createView() {
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
    
    private void reloadLayers(final List<Layer> layers) {
        Jfx.runOnJfx(() -> {
            layerPanel.getChildren().clear();
            if (layers != null) {
                for (int i = 0; i < layers.size(); i++) {
                    Layer layer = layers.get(i);
                    Label select = new Label("Select");
                    Label diffuse = createImageLabel(layer.diffuseMap);
                    Label normal = createImageLabel(layer.normalMap != null ? layer.normalMap : AssetConstants.TEXTURES_MISS);
                    TextField scale = new TextField(layer.scale + "");
                    layerPanel.addRow(i, select, diffuse, normal, scale);
                    // action
                    final int selIndex = i;
                    select.setOnMouseClicked(e -> {
                        if (e.getButton() == MouseButton.PRIMARY) {
                            Jfx.runOnJme(() -> tool.setSelectLayerIndex(selIndex));
                        }
                    });
                    diffuse.setOnMouseClicked(e -> {
                        if (e.getButton() == MouseButton.PRIMARY) {
                            File abstractFilePath = selectTexture(layer.diffuseMap);
                            if (abstractFilePath != null) {
                                Jfx.runOnJme(() -> {
                                    tool.setDiffuseTexture(selIndex, toAssetsFilePath(abstractFilePath.getAbsolutePath()));
                                });
                            }
                        }
                    });
                    normal.setOnMouseClicked(e -> {
                        if (e.getButton() == MouseButton.PRIMARY) {
                            File abstractFilePath = selectTexture(layer.normalMap);
                            if (abstractFilePath != null) {
                                Jfx.runOnJme(() -> {
                                    tool.setNormalTexture(selIndex, toAssetsFilePath(abstractFilePath.getAbsolutePath()));
                                });
                            }
                        }
                    });
                    scale.setPrefWidth(64);
                    scale.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                        if (!newValue) {
                            updateTextureScale(selIndex, scale.getText());
                        }
                    });
                    scale.setOnKeyPressed((KeyEvent event) -> {
                        if (event.getCode() == KeyCode.ENTER) {
                            updateTextureScale(selIndex, scale.getText());
                        }
                    });
                }
            }
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
    
    private Label createImageLabel(String texPath) {
        Label label = new Label();
        if (texPath == null || texPath.isEmpty())
            return label;
        
        Image image = new Image(getClass().getResourceAsStream("/" + texPath));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        label.setGraphic(imageView);
        return label;
    }

}
