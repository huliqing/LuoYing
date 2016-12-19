/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.ui;

/**
 * 滚动条功能
 * @author huliqing
 */
public interface Scroll {
    
    /**
     * 更新滚动条layout及位置
     * @param parentWidth 父UI(支持滚动功能的UI)的可视宽度.
     * @param parentHeight 父UI的可视高度.
     * @param contentLength 父UI的所有内容的高度或宽度.
     */
    void updateScroll(float parentWidth, float parentHeight, float contentLength);
    
    /**
     * 设置侦听器,滚动条在滚动过程中会调用该侦听器
     * @param scrollListener 
     */
    void setScrollListener(ScrollListener scrollListener);
    
    /**
     * 获取侦听器
     * @return 
     */
    ScrollListener getScrollListener();
    
    /**
     * 获取滚动条的宽度.
     * @return 
     */
    float getScrollWidth();
    
    /**
     * 获取滚动条的高度
     * @return 
     */
    float getScrollHeight();
}
