/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.property;

import com.jme3.math.ColorRGBA;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import name.huliqing.editor.converter.AbstractPropertyConverter;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.luoying.xml.Converter;

/**
 *
 * @author huliqing
 */
public class ColorConverter extends AbstractPropertyConverter {

    private static final Logger LOG = Logger.getLogger(ColorConverter.class.getName());

    private final ColorPicker layout = new ColorPicker();
    private Color lastColorSaved;
    
    public ColorConverter() {
        // 宽度要减少一些，不然会导致宽度BUG。
        layout.prefWidthProperty().bind(root.widthProperty().subtract(10));
        
        // 颜色改变时持续更新，但不保存，只有在明确点击后(OnAction)的时候才保存
        // 这可避免用户在颜色面板上滑动改变颜色的时候产生很多历史记录存档。
        layout.valueProperty().addListener((ObservableValue<? extends Color> observable, Color oldValue, Color newValue) -> {
            updateColor(newValue);
        });
        
        // 当在color palette面板上点击选择了颜色及Custom面板上点击“保存”"确定“之后才会确发这个方法
        // 这里要明确保存并记录历史。
        layout.setOnAction((ActionEvent event) -> {
            updateColorAndSave(layout.getValue());
        });
        
        // 当ColorPicker界面失去焦点后检查并保存颜色设置
        // 这是因为可能用户在自定义颜色面板（滑动选择）颜色后直接关闭了面板，即无选择保存和取消行为，
        // 所在这里要特别保存一下.(另外用户可能在自定义颜色面板滑动选择颜色后直接跳转到3D场景，切换选择，这需要在cleanup中处理保存)
        layout.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//             LOG.log(Level.INFO, "ColorPicker, focused listener, oldValue={0}, newValue={1}", new Object[] {oldValue, newValue});
            if (!newValue) {
                updateColorAndSave(layout.getValue());
            }
        });
    }
    
    @Override
    protected Node createLayout() {
        return layout;
    }

    @Override
    public void initialize(DataConverter parent) {
        super.initialize(parent); 
    }

    @Override
    public void cleanup() {
        // 用户可能在调节了颜色之后，切换选择了其它物体，这时颜色选择窗口可能还没有保存颜色即被关闭.
        // 这里需要判断颜色是否发生了变化，并在清理的时候保存一次。(注：颜色选择器中的自定义颜色面板虽然是模式的
        // , 但是因为整合了JME（Swing)窗口，所以JFX的模式窗口无法阻止JME窗口的操作。)
        updateColorAndSave(layout.getValue());
        super.cleanup();
    }
    
    private void updateColor(Color color) {
        updateAttribute(toJmeColor(color));
    }
    
    private void updateColorAndSave(Color color) {
        // 颜色没有变化则不需要更新和保存
        if (!checkColorDiff(color, lastColorSaved)) {
            return;
        }
        // 更新属性
        ColorRGBA newColor = toJmeColor(color);
        updateAttribute(newColor);
        // 保存历史记录
        ColorRGBA before = toJmeColor(lastColorSaved);
        ColorRGBA after = new ColorRGBA(newColor);
        addUndoRedo(toJmeColor(lastColorSaved), after);
//        LOG.log(Level.INFO, "颜色变化(ColorPicker)，保存颜色修改, before={0}, after={1}", new Object[] {before, after});
        // 注：尽量让lastColorUsed不引用到一个特定的实例
        lastColorSaved = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
    }
    
    @Override
    protected void updateUI(Object propertyValue) {
        ColorRGBA jmeColor = Converter.getAsColor(propertyValue);
//        LOG.log(Level.INFO, "颜色更新(从3D)，color={0}", jmeColor);
        Color jfxColor = toJfxColor(jmeColor);
        layout.setValue(jfxColor);
        lastColorSaved = new Color(jfxColor.getRed(), jfxColor.getGreen(), jfxColor.getBlue(), jfxColor.getOpacity());
    }
    
    private boolean checkColorDiff(Color color1, Color color2) {
        if (color1 == color2) {
            return false;
        }
        if (color1 == null && color2 != null) {
            return true;
        }
        if (color1 != null && color2 == null) {
            return true;
        }
        return Double.compare(color1.getRed(), color2.getRed()) != 0 
                || Double.compare(color1.getGreen(), color2.getGreen()) != 0 
                || Double.compare(color1.getBlue(), color2.getBlue()) != 0 
                || Double.compare(color1.getOpacity(), color2.getOpacity()) != 0;
    }
    
    private Color toJfxColor(ColorRGBA jmeColor) {
        if (jmeColor == null) {
            return new Color(1,1,1,1);
        }
        return new Color(jmeColor.r, jmeColor.g, jmeColor.b, jmeColor.a);
    }
    
    private ColorRGBA toJmeColor(Color color) {
        if (color == null) {
            return null;
        }
        return new ColorRGBA((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getOpacity());
    }
}
