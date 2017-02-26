/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author huliqing
 */
public class OutputForm extends VBox {
    
    private final WebView webView = new WebView();
    private final WebEngine engine = webView.getEngine();
    private final ContextMenu cm = new ContextMenu();
    
    private final String defaultContent = "<body><div id='content'></div></body>";
    
    public OutputForm() {
        webView.setContextMenuEnabled(false);
        engine.loadContent(defaultContent);
        
        MenuItem clean = new MenuItem(Manager.getRes(ResConstants.POPUP_CLEAR));
        clean.setOnAction(e -> {
            engine.loadContent(defaultContent);
        });
        cm.getItems().add(clean);
        cm.setAutoHide(true);
        
        webView.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                cm.show(webView, e.getScreenX(), e.getScreenY());
            }
        });

        getChildren().add(webView);
    }
    
    public void addText(String text, Color color) {
        append(text, color);
    }
    
    public void scrollToBottom() {
        engine.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }
    
    private void append(String msg, Color color) {
        Document doc = engine.getDocument();
        Element contentEle = doc.getElementById("content");
        Element pre = doc.createElement("pre");
        
        Element font = doc.createElement("font");
        font.setAttribute("color", toRGBCode(color));
        font.appendChild(doc.createTextNode(msg));
        pre.appendChild(font);
        contentEle.appendChild(pre);
    }
    
    private String toRGBCode(Color color) {
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ) );
    }
}
