/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view;

import com.jme3.math.ColorRGBA;
import name.huliqing.ly.ui.FrameLayout;
import name.huliqing.ly.ui.Icon;

/**
 * 进度条
 * @author huliqing
 */
public class ProgressView extends FrameLayout {
    private String pathProgress = "Interface/ui/progress/progress.png";
    private String pathCore = "Interface/ui/progress/progressCore.png";
    
    // 进度条外框
    private Icon progress;
    // 进度条
    private Icon core;
    // 最大值
    private float maxValue;
    // 当前值
    private float value;
    
    public ProgressView() {
        this(100, 75);
    }
    
    public ProgressView(float maxValue, float value) {
        super();
        this.maxValue = maxValue;
        this.value = value;
        init();
    }
    
    private void init() {
        progress = new Icon();
        core = new Icon();
        progress.setImage(pathProgress);
        progress.setUseAlpha(true);
        core.setImage(pathCore);
        core.setUseAlpha(true);
        addView(progress);
        addView(core);
    }

    public float getMaxValue() {
        return maxValue;
    }
    
    public float getValue() {
        return value;
    }

    public void setColor(ColorRGBA color) {
        core.setColor(color);
    }

    public void setMaxValue(float maxValue) {
        if (this.maxValue != maxValue) {
            this.maxValue = maxValue;
            setNeedUpdate();
        }
    }

    public void setValue(float value) {
        if (this.value != value) {
            this.value = value;
            setNeedUpdate();
        }
    }
    
    // ==== End getter and setter 

    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        progress.setWidth(width);
        progress.setHeight(height);
        core.setHeight(height * 0.8f);
        core.setMargin(0, height * 0.1f, 0, 0);
        if (maxValue <= 0) {
            core.setWidth(0);
        } else {
            core.setWidth(value / maxValue * width);
        }
        
    }
    
    
}
