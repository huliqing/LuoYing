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

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import name.huliqing.editor.tools.NumberValueTool;
import name.huliqing.editor.tools.ValueChangedListener;
import name.huliqing.editor.tools.ValueTool;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class JfxNumberValueTool extends JfxAbstractTool<NumberValueTool> 
        implements ValueChangedListener<Number>{

    private final GridPane view = new GridPane();
    private final Label label = new Label();
    private final TextField value = new TextField();
    
    private boolean ignoreUpdateView;
    
    public JfxNumberValueTool() {
        view.setPadding(Insets.EMPTY);
        view.addRow(0, label, value);
    
         // 失去焦点时更新
        value.focusedProperty().addListener((ObservableValue<? extends Boolean> observable
                , Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                return;
            }
            updateValueToEdit();
        });
        // 按下Enter时更新
        value.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateValueToEdit();
            }
        });
        
        value.prefHeightProperty().bind(view.heightProperty());
        label.prefHeightProperty().bind(view.heightProperty());
        label.setPrefWidth(100);
        value.setPrefWidth(64);
        view.setMaxHeight(25);
    }

    @Override
    public Region createView() {
        return view;
    }
    
    @Override
    public void initialize() {
        super.initialize();
        label.setText(tool.getName());
        updateValueToView(tool.getValue());
        tool.addValueChangeListener(this);
        
        // tooltip
        if (tool.getTips() != null) {
            label.setTooltip(new Tooltip(tool.getTips()));
        }
    }

    // 当3d场景工具值发生变化时更新到JFX组件
    @Override
    public void onValueChanged(ValueTool<Number> vt, Number oldValue, Number newValue) {
        Jfx.runOnJfx(() -> {
            updateValueToView(newValue);
        });
    }
    
    private void updateValueToEdit() {
        try {
            double v = Double.parseDouble(value.getText());
            Jfx.runOnJme(() -> {
                ignoreUpdateView = true;
                tool.setValue(v);
                // 重新获取值，因为NumberValueTool可能会有大小值限制，那么一切以NumberValueTool的值为准
                value.setText(tool.getValue().toString());
                ignoreUpdateView = false;
            });
        } catch (NumberFormatException nfe) {
            // ignore
        }
    }
    
    // 当Jfx组件值发生变化时更新到编辑场景中
    private void updateValueToView(Number newValue) {
        if (ignoreUpdateView) {
            return;
        }
        if (newValue == null) {
            value.setText("");
            return;
        }
        value.setText(newValue.doubleValue() + "");
    }

}
