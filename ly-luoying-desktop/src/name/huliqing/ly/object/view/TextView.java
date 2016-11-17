/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.view;

import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapFont.VAlign;
import com.jme3.math.ColorRGBA;
import name.huliqing.ly.data.ViewData;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.object.SyncData;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UIFactory;

/**
 * 用于显示文字信息的界面组件
 * @author huliqing
 * @param <T>
 */
public class TextView<T extends ViewData> extends AbstractView<T> {
    
    protected Text textUI;

    @Override
    public void setData(T data) {
        super.setData(data); 
        String text = data.getAsString("text");
        String textKey = data.getAsString("textKey");
        ColorRGBA color = data.getAsColor("fontColor");
        float fontSize = data.getAsFloat("fontSize", UIFactory.getUIConfig().getBodyFontSize());
        Align align = identifyAlign(data.getAsString("align"));
        VAlign valign = identifyVAlign(data.getAsString("valign"));
        
        if (text != null) {
            textUI = new Text(text);
        } else if (textKey != null) {
            textUI = new Text(ResourceManager.get(textKey));
        }  else {
            textUI = new Text("");
        }
        if (color != null) {
            textUI.setFontColor(color);
        }
        if (align != null) {
            textUI.setAlignment(align);
        }
        if (valign != null) {
            textUI.setVerticalAlignment(valign);
        }
        textUI.setFontSize(fontSize);
        
        viewRoot.addView(textUI);
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("text", textUI.getText());
    }
    
    @Override
    public void applySyncData(SyncData data) {
        super.applySyncData(data);
        textUI.setText(data.getAsString("text"));
    }
    
    @Override
    protected void doViewInit() {
        super.doViewInit();
        textUI.setWidth(viewRoot.getWidth());
        textUI.setHeight(viewRoot.getHeight());
    }
    
    private Align identifyAlign(String align) {
        if (align == null)
            return null;
        
        if (align.equals(Align.Center.name())) {
            return Align.Center;
        }
        if (align.equals(Align.Left.name())) {
            return Align.Left;
        }
        if (align.equals(Align.Right.name())) {
            return Align.Right;
        }
        return null;
    }
    
    private VAlign identifyVAlign(String valign) {
        if (valign == null) 
            return null;
        
        if (valign.equals(VAlign.Bottom.name())) {
            return VAlign.Bottom;
        }
        if (valign.equals(VAlign.Center.name())) {
            return VAlign.Center;
        }
        if (valign.equals(VAlign.Top.name())) {
            return VAlign.Top;
        }
        return null;
    }
    
    public void setText(String text) {
        textUI.setText(text);
        putSyncData("text", text);
    }

    
}
