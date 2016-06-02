/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.ui;

import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Window组件
 * @author huliqing
 */
public class Window extends LinearLayout {

    public interface CloseListener {
        void onClosed(Window win);
    }
    
    protected Title title;
    protected LinearLayout body;
    protected UILayout footer;
    
    protected float titleHeight;
    protected float footerHeight;
    protected boolean closeable;
    
    private List<CloseListener> closeListeners;
    
    public Window(float width, float height) {
        this(null, width, height);
    }
    
    public Window(String title, float width, float height) {
        super(width, height);
        
        titleHeight = UIFactory.getUIConfig().getTitleHeight();
        footerHeight = UIFactory.getUIConfig().getFooterHeight();
        setTitle(title);
        setDragEnabled(true);
        setBackgroundColor(UIFactory.getUIConfig().getBodyBgColor(), true);
        // 添加一个空的事件，以阻止事件向win背后穿透
        addClickListener(AbstractUI.EMPTY_LISTENER);
        setEffectEnabled(false);
    }
    
    /**
     * 替换标题
     * @param view 
     */
    public void setTitle(UI view) {
        checkTitle();
        this.title.setTitle(view);
        view.setToCorner(Corner.LC);
    }
    
    public final void setTitle(String title) {
        Text text = new Text(title);
        text.setFontSize(UIFactory.getUIConfig().getTitleSize());
        text.setHeight(titleHeight);
        text.setVerticalAlignment(BitmapFont.VAlign.Center);
        setTitle(text);
    }

    public float getTitleHeight() {
        return titleHeight;
    }
    
    public void setTitleHeight(float titleHeight) {
        this.titleHeight = titleHeight;
    }

    public void setFooter(UI footerView) {
        checkFooter();
        footer.clearViews();
        footer.addView(footerView);
    }
    
    public void addFooter(UI footerView) {
        checkFooter();
        footer.addView(footerView);
    }
    
    public float getFooterHeight() {
        return footerHeight;
    }
    
    public void setFooterHeight(float footerHeight) {
        this.footerHeight = footerHeight;
    }

    public boolean isCloseable() {
        return closeable;
    }
    
    /**
     * 设置“关闭”按钮是否可见。
     * @param bool 
     */
    public void setCloseable(boolean bool) {
        if (this.closeable != bool) {
            setNeedUpdate();
        }
        this.closeable = bool;
    }
    
    public void addCloseListener(CloseListener listener) {
        if (closeListeners == null) {
            closeListeners = new ArrayList<CloseListener>(1);
        }
        if (!closeListeners.contains(listener)) {
            closeListeners.add(listener);
        }
    }
    
    public boolean removeCloseListener(CloseListener listener) {
        return closeListeners != null && closeListeners.remove(listener);
    }

    @Override
    public void addView(UI view) {
        checkBody();
        body.addView(view);
    }
    
    @Override
    public void addView(UI view, int index) {
        checkBody();
        body.addView(view, index);
    }
    
    @Override
    public boolean removeView(UI view) {
        if (body == null) {
            return false;
        }
        return body.removeView(view);
    }
    
    @Override
    public List<UI> getViews() {
        if (body == null) {
            return Collections.EMPTY_LIST;
        }
        return body.getViews();
    }
    
    @Override
    public float getContentWidth() {
        checkBody();
        return body.getContentWidth();
    }
    
    @Override
    public float getContentHeight() {
        checkBody();
        float h = height;
        if (title != null) {
            h -= getTitleHeight();
        }
        if (footer != null) {
            h -= getFooterHeight();
        }
        return h - body.paddingTop - body.paddingBottom;
    }
    
    @Override
    public final void setDragEnabled(boolean enabled) {
        checkTitle();
        title.setDragEnabled(enabled);
    }
    
    @Override
    public void setLayout(Layout layout) {
        checkBody();
        body.setLayout(layout);
    }
    
    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        
        float bodyHeight = height;
        if (title != null && title.isVisible()) {
            title.setWidth(width);
            title.setHeight(titleHeight);
            title.getExitButton().setVisible(closeable);
            bodyHeight -= titleHeight;
        }
        if (footer != null && footer.isVisible()) {
            footer.setWidth(width);
            footer.setHeight(footerHeight);
            bodyHeight -= footerHeight;
        }
        if (body != null && body.isVisible()) {
            body.setWidth(width);
            body.setHeight(bodyHeight);
        }
    }

    @Override
    public void setPadding(float left, float top, float right, float bottom) {
        checkBody();
        body.setPadding(left, top, right, bottom); 
    }

    private void checkTitle() {
        if (title == null) {
            title = createTitle(width, height);
            super.addView(title, 0);
        }
    }
    
    private void checkBody() {
        if (body == null) {
            body = createBody(width, height);
            if (title == null) {
                super.addView(body, 0);
            } else {
                super.addView(body, 1);
            }
        }
    }
    
    private void checkFooter() {
        if (footer == null) {
            footer = createFooter();
            super.addView(footer);
        }
    }
    
    /**
     * 创建标题组件
     * @return 
     */
    protected Title createTitle(float width, float height) {
        return new SimpleTitle(width, height);
    }
    
    /**
     * 创建BODY布局
     * @return 
     */
    protected LinearLayout createBody(float width, float height) {
        return new LinearLayout(width, height);
    }
    
    protected UILayout createFooter() {
        LinearLayout f = new LinearLayout();
        f.setLayout(Layout.horizontal);
        return f;
    }
    
    public void close() {
        removeFromParent();
        if (closeListeners != null) {
            for (CloseListener cl : closeListeners) {
                cl.onClosed(this);
            }
        }
    }

    // -------------------------------------------------------------------------
    
    private class SimpleTitle extends FrameLayout implements Title {

        private UI text;
        private CloseIcon closeBtn;
        
        public SimpleTitle(float width, float height) {
            super(width, height);
            setBackground(UIFactory.getUIConfig().getBackground(), false);
            setBackgroundColor(ColorRGBA.White, false);
            setPadding(10, 0, 0, 0);
            
            closeBtn = new CloseIcon();
            addView(closeBtn);
        }

        @Override
        protected void updateViewChildren() {
            super.updateViewChildren();
            float btnWidth = height * 1.1f;
            if (text != null) {
                text.setWidth(width - btnWidth - paddingLeft - paddingRight);
                text.setHeight(height);
            }
            
            closeBtn.setWidth(btnWidth);
            closeBtn.setHeight(height);
            
            // remove20160309不再使用这个FIX,这会导致“关闭”按钮始终出现在其它UI上面
            // FIX BUG，部分情况下会发生“关闭”按钮明明在标题栏之上，但是即点击
            // 无效的情况，导致Window窗口无法关闭。
            // 这里的代码确保“关闭”按钮的Z值始终大于标题栏，可FIX该问题。
//            Vector3f loc = closeBtn.getLocalTranslation();
//            loc.setZ(getLocalTranslation().z + 1);
//            closeBtn.setLocalTranslation(loc);
        }

        @Override
        protected void updateViewLayout() {
            super.updateViewLayout(); 
            if (text != null) 
                text.setToCorner(Corner.LC);
            closeBtn.setToCorner(Corner.RC);
        }
        
        @Override
        public void setTitle(UI title) {
            if (text == title) 
                return;
            
            if (text != null) {
                removeView(text);
            }
            text = title;
            // 这里要确保text不要盖住"关闭"按钮
            addView(text, 0);
        }

        @Override
        public UI getExitButton() {
            return closeBtn;
        }

        @Override
        protected void onDragMove(float xMoveAmount, float yMoveAmount) {
            Window.this.move(xMoveAmount, yMoveAmount, 0);
        }
    }
    
    private class CloseIcon extends FrameLayout {
        private Icon icon;

        public CloseIcon() {
            super();
            icon = new Icon(UIFactory.getUIConfig().getButtonClose());
            addView(icon);
            
            addClickListener(new Listener() {
                @Override
                public void onClick(UI view, boolean isPressed) {
                    if (isPressed) return;
//                    Window.this.getDisplay().removeFromParent(); // remove20160306
                    close();
                }
            });
        }

        @Override
        public void updateViewChildren() {
            super.updateViewChildren();
            icon.setWidth(height * 0.75f);
            icon.setHeight(height * 0.75f);
        }

        @Override
        protected void updateViewLayout() {
            super.updateViewLayout(); 
            icon.setToCorner(Corner.CC);
        }

        @Override
        protected void clickEffect(boolean isPressed) {
            icon.clickEffect(isPressed);
        }
        
        
    }
}
