/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.actor;

import name.huliqing.luoying.ui.tiles.ColumnBody;
import name.huliqing.luoying.ui.tiles.ColumnText;
import name.huliqing.luoying.ui.tiles.ColumnIcon;
import name.huliqing.luoying.Factory;
import name.huliqing.ly.constants.InterfaceConstants;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.Row;

/**
 * @author huliqing
 * @param <T>
 */
public abstract class ItemRow<T> extends Row<T> {
    protected final PlayService playService = Factory.get(PlayService.class);
    
    protected T data;
    
    // 物品
    protected ColumnIcon icon;
    protected ColumnBody body;
    protected ColumnText num;
    protected ColumnIcon shortcut;
    
    public ItemRow() {
        super();
        this.setLayout(Layout.horizontal);
        icon = new ColumnIcon(height, height, InterfaceConstants.UI_MISS);
        body = new ColumnBody(height, height, "", "");
        num = new ColumnText(height, height, "");
        shortcut = new ColumnIcon(height, height, InterfaceConstants.UI_OPER);
        addView(icon);
        addView(body);
        addView(num);
        addView(shortcut);
        
        setBackground(UIFactory.getUIConfig().getBackground(), true);
        setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
        setBackgroundVisible(false);
    }
    
    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        float iconSize = height;

        icon.setWidth(iconSize);
        icon.setHeight(iconSize);

        num.setWidth(iconSize);
        num.setHeight(iconSize);

        shortcut.setWidth(iconSize);
        shortcut.setHeight(iconSize);
        shortcut.setPreventEvent(true);

        body.setWidth(width - iconSize * 3);
        body.setHeight(iconSize);
    }

    @Override
    public final void displayRow(T data) {
        this.data = data;
        display(this.data);
    }
    
    public T getData() {
        return this.data;
    }
    
    public void setRowClickListener(Listener listener) {
        addClickListener(listener);
    }
    
    public void setShortcutListener(Listener listener) {
        shortcut.addClickListener(listener);
    }

    @Override
    protected void clickEffect(boolean isPress) {
        if (isPress) {
            this.setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
        }
        setBackgroundVisible(isPress);
    }

    @Override
    public void onRelease() {
        setBackgroundVisible(false);
    }
    
    protected abstract void display(T data);
}
