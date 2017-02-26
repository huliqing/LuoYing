/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.field;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.converter.SimpleFieldConverter;
import name.huliqing.editor.manager.ConverterManager;
import name.huliqing.luoying.xml.ObjectData;

/**
 *
 * @author huliqing
 */
public class DataFieldConverter extends SimpleFieldConverter{

    private static final Logger LOG = Logger.getLogger(DataFieldConverter.class.getName());

    private final TextField content = new TextField("");
    private ObjectData lastObjectData;
    
    private DataConverter dc;
    private boolean displayed;
    
    private final TitledPane bodyPanel = new TitledPane();
    
    public DataFieldConverter() {
        content.setOnMouseClicked((MouseEvent event) -> {
            if (displayed) {
                return;
            }
            dc.getLayout().setVisible(true);
            jfxEdit.getPropertyPanel().getChildren().add(bodyPanel);
            displayed = true;
        });
    }
    
    @Override
    protected Node createLayout() {
        return content;
    }
    
    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void cleanup() {
        jfxEdit.getPropertyPanel().getChildren().remove(bodyPanel);
        dc.cleanup();
        dc = null;
        displayed = false;
        super.cleanup(); 
    }

    @Override
    protected void updateUI() {
        Object propertyValue = data.getAttribute(field);
        ObjectData temp;
        try {
            temp = (ObjectData) propertyValue;
        } catch (java.lang.ClassCastException e) {
            LOG.log(Level.SEVERE, "Could not convert property to ObjectData, data=" + data.getId() + ", field=" + field, e);
            return;
        }
        if (temp != lastObjectData) {
            lastObjectData = temp;
            if (dc != null) {
                dc.cleanup();
            }
            dc = ConverterManager.createDataConverter(jfxEdit, lastObjectData, this);
            dc.initialize();
            bodyPanel.setText(lastObjectData.getId());
            bodyPanel.setContent(dc.getLayout());
        }
        content.setText(lastObjectData.getId());
    }
    
}
