/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.view;

import name.huliqing.fighter.data.ViewData;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.SyncData;
import name.huliqing.fighter.ui.Window;

/**
 * 用于显示文字信息的界面组件
 * @author huliqing
 */
public class TextPanelView extends TextView {
    
    private String title;
    private Window win;
    
    public TextPanelView(ViewData data) {
        super(data);
        title = data.getAttribute("title");
        if (title == null) {
            title = ResourceManager.getObjectName(data);
        }
        
        win = new Window(viewRoot.getWidth(), viewRoot.getHeight());
        win.setTitle(title);
        win.setCloseable(false);
        win.setPadding(10, 10, 10, 10);
        
        viewRoot.removeView(textUI);
        win.addView(textUI);
        viewRoot.addView(win);
    }

    @Override
    protected void doViewInit() {
        super.doViewInit();
        textUI.setWidth(win.getContentWidth());
        textUI.setHeight(win.getContentHeight());
        
        // remove20160420,会导致拖动不了
        // FIX bug，当存在win时不能再拖动viewRoot,应该只拖动win就可以,否则会有一些
        // 拖动时的跳跃问题，影响操作体验
//        viewRoot.setDragEnabled(false);
//        if (dragEnabled) {
//            win.setDragEnabled(dragEnabled);
//        }
        
        viewRoot.setDragEnabled(dragEnabled);
        
    }
    
    public void setTitle(String title) {
        this.title = title;
        win.setTitle(title);
        putSyncData("title", title);
    }
    
    @Override
    public ViewData getUpdateData() {
        ViewData vd = super.getUpdateData();
        vd.putAttribute("title", title);
        return vd;
    }

    @Override
    public void applySyncData(SyncData data) {
        super.applySyncData(data);
        win.setTitle(data.getAttribute("title"));
    }
    
}
