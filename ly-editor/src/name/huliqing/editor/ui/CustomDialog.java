/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;
import javafx.scene.control.Skinnable;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class CustomDialog extends HBox {
    
    // 用于支持模态窗口，不需要任何皮肤
    private final PopupControl popupControl = new PopupControl() {
        {
            setSkin(new Skin<Skinnable>() {
                @Override public Skinnable getSkinnable() {return null;}
                @Override public Node getNode() {return null;}
                @Override public void dispose() { }
            });
        }
    };
    private final Stage dialog = new Stage();
    private final Scene customScene = new Scene(this);
    
    private final Window owner;
    
    private InvalidationListener positionAdjuster = new InvalidationListener() {
        @Override
        public void invalidated(Observable ignored) {
            if (Double.isNaN(dialog.getWidth()) || Double.isNaN(dialog.getHeight())) {
                return;
            }
            dialog.widthProperty().removeListener(positionAdjuster);
            dialog.heightProperty().removeListener(positionAdjuster);
            fixPosition();
        }
    };
    
    public CustomDialog(Window owner) {
        this.owner = owner;
        dialog.initOwner(popupControl);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setResizable(true);
        if (owner != null) {
            final Scene ownerScene = owner.getScene();
            if (ownerScene != null) {
                if (ownerScene.getUserAgentStylesheet() != null) {
                    customScene.setUserAgentStylesheet(ownerScene.getUserAgentStylesheet());
                }
                customScene.getStylesheets().addAll(ownerScene.getStylesheets());
            }            
        }

        dialog.setScene(customScene);
        dialog.setOnHidden((e) -> {popupControl.setAutoHide(true);});
    }
    
    public void initModality(Modality modality) {
        dialog.initModality(modality);
    }
    
    public void setResizable(boolean resizable) {
        dialog.setResizable(resizable);
    }
    
    public void setTitle(String title) {
        dialog.setTitle(title);
    }
    
    public Stage getDialog() {
        return dialog;
    }

    /**
     * @deprecated 
     */
    public void show() {
        if (dialog.getOwner() != null) {
            // Workaround of RT-29871: Instead of just invoking fixPosition() 
            // here need to use listener that fixes dialog position once both
            // width and height are determined
            dialog.widthProperty().addListener(positionAdjuster);
            dialog.heightProperty().addListener(positionAdjuster);
            positionAdjuster.invalidated(null);
        }
        popupControl.show(owner);
        popupControl.setAutoHide(false);
        dialog.show();
    }
    
    public void showOnCenter() {
        popupControl.show(owner);
        popupControl.setAutoHide(false);
        dialog.centerOnScreen();
        dialog.show();
    }
    
    public void hide() {
        dialog.hide();
        popupControl.hide();
    }
    
    private void fixPosition() {
        Window w = dialog.getOwner();
        Screen s = com.sun.javafx.util.Utils.getScreen(w);
        Rectangle2D sb = s.getBounds();
        double xR = w.getX() + w.getWidth();
        double xL = w.getX() - dialog.getWidth();
        double x, y;
        if (sb.getMaxX() >= xR + dialog.getWidth()) {
            x = xR;
        } else if (sb.getMinX() <= xL) {
            x = xL;
        } else { 
            x = Math.max(sb.getMinX(), sb.getMaxX() - dialog.getWidth());
        }
        y = Math.max(sb.getMinY(), Math.min(sb.getMaxY() - dialog.getHeight(), w.getY()));
        dialog.setX(x);
        dialog.setY(y);
    }
    
    @Override public void layoutChildren() {
        super.layoutChildren();
        if (dialog.getMinWidth() > 0 && dialog.getMinHeight() > 0) {
            // don't recalculate min size once it's set
            return;
        }

        // Math.max(0, ...) added for RT-34704 to ensure the dialog is at least 0 x 0
        double minWidth = Math.max(0, computeMinWidth(getHeight()) + (dialog.getWidth() - customScene.getWidth()));
        double minHeight = Math.max(0, computeMinHeight(getWidth()) + (dialog.getHeight() - customScene.getHeight()));
        dialog.setMinWidth(minWidth);
        dialog.setMinHeight(minHeight);
    }
}
