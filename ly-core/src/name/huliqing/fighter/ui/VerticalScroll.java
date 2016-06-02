/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.ui;

import com.jme3.math.Vector3f;
import name.huliqing.fighter.utils.MathUtils;

/**
 * 垂直滚动条,主要用于各UI组件中
 * @author huliqing
 */
public class VerticalScroll extends AbstractUI implements Scroll {
    
    // 侦听器,滚动条滚动时的侦听器.
    private ScrollListener listener;

    // 父UI的宽度和高度,如果父UI的宽度或高度发生变化,scrollLength也会发生变化.
    private float parentWidth;
    private float parentHeight;
    
    // 滚动内容的实际最大长度,即父UI的全部内容的长度
    private float contentLength;
    
    // 可滚动的区域的长度,如垂直滚动条的可滚动长度为 = 父UI的高度-当前UI的高度.
    private float scrollLength;
    // 当前的滚动位置,值在0~1之间.0表示未进行滚动,
    // 通过 contentLength,parentHeight,scrollFactor 来计算出当前已经滚动的长度
    private float scrollFactor;
    
    
    public VerticalScroll(float width) {
        super(width, 1);
        this.setDragEnabled(true);
        this.setBackground(UIFactory.getUIConfig().getBackground(), true);
        this.setBackgroundColor(UIFactory.getUIConfig().getScrollColor(), true);
    }
    
    @Override
    protected void onDragMove(float xAmount, float yAmount) {
        Vector3f pos = getLocalTranslation();
        pos.addLocal(0, yAmount, 0);
        if (pos.y < 0) {
            pos.setY(0);
        } else if (pos.y > scrollLength) {
            pos.setY(scrollLength);
        }
        scrollFactor = (scrollLength - pos.y) / scrollLength;
        this.setLocalTranslation(pos);
        
        if (listener != null) {
            listener.onScroll((contentLength - parentHeight) * scrollFactor);
        }
    }
    
    /**
     * 更新滚动条,该方法需要由支持滚动功能的父UI进行调用,当父UI的宽度,高度发生变化时,或者
     * 可滚动内容发生变化时应该调用这个方法进行更新.
     * @param parentWidth 父UI的宽度
     * @param parentHeight 父UI的高度
     * @param contentLength 滚动内容的长度
     */
    @Override
    public void updateScroll(float parentWidth, float parentHeight, float contentLength) {
        Vector3f pos = this.getLocalTranslation();
        if (!MathUtils.compareFloat(this.contentLength, contentLength) 
                || !MathUtils.compareFloat(this.parentHeight, parentHeight)) {

            this.height = parentHeight / contentLength * parentHeight;

            pos.x = parentWidth - width;
            pos.y = parentHeight - height;

            this.scrollLength = parentHeight - height;

        }
        this.setLocalTranslation(pos);
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;
        this.contentLength = contentLength;
    }

    @Override
    public void setScrollListener(ScrollListener scrollListener) {
        this.listener = scrollListener;
    }

    @Override
    public ScrollListener getScrollListener() {
        return this.listener;
    }

    @Override
    public float getScrollWidth() {
        return this.width;
    }

    @Override
    public float getScrollHeight() {
        return this.height;
    }
    
    
}
