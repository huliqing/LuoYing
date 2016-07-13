/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.view;

import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapFont.VAlign;
import com.jme3.math.ColorRGBA;
import name.huliqing.fighter.data.ViewData;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.SyncData;
import name.huliqing.fighter.ui.Text;
import name.huliqing.fighter.ui.UIFactory;

/**
 * 用于显示文字信息的界面组件
 * @author huliqing
 * @param <T>
 */
public class TextView<T extends ViewData> extends AbstractView<T> {
    
    protected Text textUI;

    @Override
    public void initData(T data) {
        super.initData(data); 
        String text = data.getAttribute("text");
        String textKey = data.getAttribute("textKey");
        ColorRGBA color = data.getAsColor("fontColor");
        float fontSize = data.getAsFloat("fontSize", UIFactory.getUIConfig().getBodyFontSize());
        Align align = identifyAlign(data.getAttribute("align"));
        VAlign valign = identifyVAlign(data.getAttribute("valign"));
        
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

    @Override
    public T getUpdateData() {
        T vd = super.getUpdateData();
        vd.setAttribute("text", textUI.getText());
        return vd;
    }

    @Override
    public void applySyncData(SyncData data) {
        super.applySyncData(data);
        textUI.setText(data.getAttribute("text"));
    }
    
}
