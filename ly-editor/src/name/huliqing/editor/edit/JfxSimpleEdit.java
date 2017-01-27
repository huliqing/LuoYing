/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit;

import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import name.huliqing.editor.toolbar.JfxToolbar;
import name.huliqing.editor.utils.DragResizer;
import name.huliqing.fxswing.Jfx;

/**
 * @author huliqing
 * @param <T>
 */
public abstract class JfxSimpleEdit<T extends JmeEdit> extends JfxAbstractEdit<T> {
    // editPanel不能完全透明，完全透明则响应不了事件，在响应事件时还需要设置为visible=true
    private final static String STYLE_EDIT_PANEL = "-fx-background-color:rgba(0,0,0,0.11)";
    
    protected final HBox mainPanel = new HBox();
    protected final Pane toolPanel = new HBox();
    
    protected final StackPane mainLeft = new StackPane();
    
    protected final Pane propertyPanel = new HBox();
    protected final TabPane extToolPanel = new TabPane();
    protected final Pane editPanel = new HBox();
    
    @Override
    protected void jfxInitialize() {
        editRoot.getChildren().addAll(mainPanel, toolPanel);
        
        mainPanel.getChildren().addAll(mainLeft, extToolPanel);
        mainLeft.getChildren().addAll(propertyPanel, editPanel); // editPanel放在propertyPanel上面，因为要响应拖放事件
        
        mainPanel.setAlignment(Pos.TOP_RIGHT);
        mainPanel.prefWidthProperty().bind(editRoot.widthProperty());
        mainPanel.prefHeightProperty().bind(editRoot.heightProperty().subtract(toolPanel.heightProperty()));
        
        mainLeft.prefWidthProperty().bind(mainPanel.widthProperty().subtract(extToolPanel.widthProperty()));
        mainLeft.prefHeightProperty().bind(mainPanel.heightProperty());
        
        extToolPanel.prefWidth(200);
        extToolPanel.prefHeightProperty().bind(mainPanel.heightProperty());
        extToolPanel.setSide(Side.RIGHT);
        
        editPanel.setVisible(false);
        editPanel.setStyle(STYLE_EDIT_PANEL);
        editPanel.setOnDragOver(this::onDragOver);
        editPanel.setOnDragDropped(this::onDragDropped);
        
        // 强制刷新一下UI，必须的，否则界面无法实时刷新(JFX嵌入Swing的一个BUG)
        Jfx.jfxForceUpdate();
        Jfx.jfxCanvasBind(editPanel);
    }

    @Override
    protected void jfxCleanup() {
        editRoot.getChildren().clear();
        mainLeft.getChildren().clear();
        mainPanel.prefHeightProperty().unbind();
        mainLeft.prefWidthProperty().unbind();
        extToolPanel.prefHeightProperty().unbind();
    }
    
    @Override
    public void jfxOnDragStarted() {
        super.jfxOnDragStarted();
        editPanel.setVisible(true);
    }

    @Override
    public void jfxOnDragEnded() {
        super.jfxOnDragEnded(); 
        editPanel.setVisible(false);
    }

    @Override
    public Pane getPropertyPanel() {
        return propertyPanel;
    }
    
    /**
     * 设置基本工具栏
     * @param toolbar 
     */
    @Override
    public void setToolbar(JfxToolbar toolbar) {
        toolPanel.getChildren().clear();
        if (toolbar != null) {
            toolPanel.getChildren().add(toolbar);
        }
    }

    @Override
    public void addToolPanel(String name, Region toolPanel) {
        Tab tab = new Tab();
        tab.setText(name);
        tab.setContent(toolPanel);
        tab.setClosable(false);
        extToolPanel.getTabs().add(tab);
    }

    @Override
    public boolean removeToolPanel(String name) {
        Tab found = null;
        for (Tab tab : extToolPanel.getTabs()) {
            if (name.equals(tab.getText())) {
                found = tab;
                break;
            }
        }
        if (found != null) {
            extToolPanel.getTabs().remove(found);
            return true;
        }
        return false;
    }
    
    /**
     * 当鼠标拖放到编辑面板上面时该方法被调用
     * @param e 
     */
    protected abstract void onDragOver(DragEvent e);
    
    /**
     * 当鼠标从拖放到释放时该方法被调用。
     * @param e 
     */
    protected abstract void onDragDropped(DragEvent e);
}
