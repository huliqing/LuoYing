/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.ui;

import java.util.List;

/**
 * 文字信息面板
 * @author huliqing
 */
public class TextPanel extends Window {
    
    private boolean needResize;
    
    public TextPanel(String title, float width, float height) {
        super(width, height);
        setTitle(title);
        setPadding(10, 10, 10, 10);
    }

    @Override
    public void updateLogicalState(float tpf) {
        if (needResize) {
            resize();
            needResize = false;
        }
        super.updateLogicalState(tpf); 
    }

    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        float cw = getContentWidth();
        
        if (body != null && !body.getViews().isEmpty()) {
            List<UI> cvs = body.getViews();
            for (UI v : cvs) {
                v.setWidth(cw);
            }
        }
        
        if (footer != null && !footer.getViews().isEmpty()) {
            
            List<UI> cvs = footer.getViews();
            float fBtnWidth = width / cvs.size();
            for (UI v : cvs) {
                v.setWidth(fBtnWidth);
                v.setHeight(footerHeight);
            }
        }
    }
    
    public void addText(String text) {
        Text t = new Text(text);
        t.setWidth(getContentWidth());
        addView(t);
        needResize = true;
    }
    
    public void addText(Text text) {
        text.setWidth(getContentWidth());
        addView(text);
        needResize = true;
    }
    
    public void addButton(UI btn) {
        addFooter(btn);
        needResize = true;
    }
    
    public void addButton(String button, Listener listener) {
        Button btn = new Button(button);
        btn.addClickListener(listener);
        addFooter(btn);
        needResize = true;
    }

    @Override
    public void resize() {
        super.resize();
        needResize = false;
    }
    
}
