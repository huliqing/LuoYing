/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.fxswing.Jfx;
import name.huliqing.editor.ui.toolbar.JfxToolbar;
import name.huliqing.editor.ui.toolbar.JfxToolbarFactory;

/**
 * @author huliqing
 * @param <T>
 */
public abstract class JfxSimpleEdit<T extends JmeEdit> extends JfxAbstractEdit<T> {
    // editPanel不能完全透明，完全透明则响应不了事件，在响应事件时还需要设置为visible=true
    private final static String STYLE_EDIT_PANEL = "-fx-background-color:rgba(0,0,0,0.01)";
    
    protected final HBox mainPanel = new HBox();
    protected final Pane jfxToolbarPanel = new HBox();
    
    protected final StackPane mainLeft = new StackPane();
    
    protected final Pane propertyPanel = new HBox();
    protected final TabPane extToolbarPanel = new TabPane();
    protected final Pane editPanel = new HBox();
    
    // --
    protected JfxToolbar jfxToolbar;
    protected List<JfxToolbar> jfxExtToolbars;
    
    @Override
    protected void jfxInitialize() {
        editRoot.getChildren().addAll(mainPanel, jfxToolbarPanel);
        
        mainPanel.getChildren().addAll(mainLeft, extToolbarPanel);
        mainLeft.getChildren().addAll(propertyPanel, editPanel); // editPanel放在propertyPanel上面，因为要响应拖放事件
        
        mainPanel.setAlignment(Pos.TOP_RIGHT);
        mainPanel.prefWidthProperty().bind(editRoot.widthProperty());
        mainPanel.prefHeightProperty().bind(editRoot.heightProperty().subtract(jfxToolbarPanel.heightProperty()));
        
        mainLeft.prefWidthProperty().bind(mainPanel.widthProperty().subtract(extToolbarPanel.widthProperty()));
        mainLeft.prefHeightProperty().bind(mainPanel.heightProperty());
        
        extToolbarPanel.prefWidth(200);
        extToolbarPanel.prefHeightProperty().bind(mainPanel.heightProperty());
        extToolbarPanel.setSide(Side.RIGHT);
        extToolbarPanel.setPadding(new Insets(7));
        
        editPanel.setVisible(false);
        editPanel.setStyle(STYLE_EDIT_PANEL);
        editPanel.setOnDragOver(this::onDragOver);
        editPanel.setOnDragDropped(this::onDragDropped);
        
        // 基本工具栏
        Toolbar baseToolbar = jmeEdit.getToolbar();
        if (baseToolbar != null) {
            JfxToolbar jfxToolPanel = JfxToolbarFactory.createJfxToolbar(baseToolbar);
            setJfxToolbar(jfxToolPanel);
        }
        
        // 扩展工具栏
        List<Toolbar> extToolbars = jmeEdit.getExtToolbars();
        if (extToolbars != null && !extToolbars.isEmpty()) {
            extToolbars.stream().map((t) -> JfxToolbarFactory.createJfxToolbar(t)).forEach((jtl) -> {
                addJfxExtToolbar(jtl);
            });
        }
        
        // 强制刷新一下UI，必须的，否则界面无法实时刷新(JFX嵌入Swing的一个BUG)
        Jfx.jfxForceUpdate();
        Jfx.jfxCanvasBind(editPanel);
        
    }

    @Override
    protected void jfxCleanup() {
        if (jfxToolbar != null && jfxToolbar.isInitialized()) {
            jfxToolbar.cleanup();
            jfxToolbar = null;
        }
        if (jfxExtToolbars != null && !jfxExtToolbars.isEmpty()) {
            jfxExtToolbars.stream().filter((jtb) -> (jtb.isInitialized())).forEach((jtb) -> {
                jtb.cleanup();
            });
            jfxExtToolbars.clear();
            jfxExtToolbars = null;
        }
        editRoot.getChildren().clear();
        mainPanel.getChildren().clear();
        jfxToolbarPanel.getChildren().clear();
        mainLeft.getChildren().clear();
        propertyPanel.getChildren().clear();
        extToolbarPanel.getTabs().clear();
        editPanel.getChildren().clear();
        
        mainPanel.prefWidthProperty().unbind();
        mainPanel.prefHeightProperty().unbind();
        mainLeft.prefWidthProperty().unbind();
        mainLeft.prefHeightProperty().unbind();
        extToolbarPanel.prefHeightProperty().unbind();
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
     * @param jfxToolbar 
     */
    protected void setJfxToolbar(JfxToolbar jfxToolbar) {
        jfxToolbarPanel.getChildren().clear();
        if (this.jfxToolbar != null && this.jfxToolbar.isInitialized()) {
            this.jfxToolbar.cleanup();
        }
        this.jfxToolbar = jfxToolbar;
        if (jfxToolbar != null) {
            if (!jfxToolbar.isInitialized()) {
                jfxToolbar.initialize();
            }
            jfxToolbarPanel.getChildren().add(jfxToolbar.getView());
        }
    }

    /**
     * 添加扩展工具栏
     * @param jfxToolbar
     */
    protected void addJfxExtToolbar(JfxToolbar jfxToolbar) {
        if (jfxExtToolbars != null && jfxExtToolbars.contains(jfxToolbar)) {
            return;
        }
        if (!jfxToolbar.isInitialized()) {
            jfxToolbar.initialize();
        }
        Tab tab = new Tab();
        tab.setText(jfxToolbar.getName());
        tab.setContent(jfxToolbar.getView());
        tab.setClosable(false);
        extToolbarPanel.getTabs().add(tab);
        if (jfxExtToolbars == null) {
            jfxExtToolbars = new ArrayList();
        }
        jfxExtToolbars.add(jfxToolbar);
    }

    protected boolean removeJfxExtToolbar(JfxToolbar jfxToolbar) {
        Tab found = null;
        String name = jfxToolbar.getName();
        for (Tab tab : extToolbarPanel.getTabs()) {
            if (name.equals(tab.getText())) {
                found = tab;
                break;
            }
        }
        if (found != null) {
            extToolbarPanel.getTabs().remove(found);
            jfxExtToolbars.remove(jfxToolbar);
            if (jfxToolbar.isInitialized()) {
                jfxToolbar.cleanup();
            }
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
