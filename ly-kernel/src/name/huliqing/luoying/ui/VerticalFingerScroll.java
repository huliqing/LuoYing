/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.ui;

    
/**
 * 滚动组件,用于模拟手势滑动.
 * fingerScroll使用一个无形的geomery覆盖在ListPanel上面,为了响应拖动事件,必须
 * 将当前组件覆盖在listPanel的item组件的最上层,所以z值应该大于RowItem的z值,
 * 并且应该允许事件穿透(preventCross=false),否则会影响下层row item组件的事件影响.
 * @author huliqing
 */
public class VerticalFingerScroll extends AbstractUI implements Scroll {
 
    // 普通的滚动组件
    private final VerticalScroll inner;
    
    public VerticalFingerScroll(UI parent, float width) {
        super(width, 1);
        // 必须为absolute,否则在父组件中会占用宽度
        this.setDragEnabled(true);
        // 必须允许事件穿透
        this.setPreventCross(false);
        
        inner = new VerticalScroll(width);
        attachChild(inner);
    }
    
    @Override
    protected void onDragMove(float xAmount, float yAmount) {
        inner.onDragMove(xAmount * -1, yAmount * -1);
        
        // ignore
        // 不处理自身: 的拖动位置.
    }

    @Override
    public void updateScroll(float parentWidth, float parentHeight, float contentLength) {
        width = parentWidth;
        height = parentHeight;
        
        // remove20160310,不再需要，因为设置Z值不会影响事件顺序，只会影响显示的层叠顺序
        // 当前组件的z值应该大于row item的z值
//        setLocalTranslation(getLocalTranslation().set(0, 0, 10));
        
        inner.updateScroll(width, height, contentLength);
        
        updateView();
    }

    @Override
    public void updateView() {
        super.updateView();
        inner.updateView();
    }
    
    @Override
    public void setScrollListener(ScrollListener scrollListener) {
        inner.setScrollListener(scrollListener);
    }

    @Override
    public ScrollListener getScrollListener() {
        return inner.getScrollListener();
    }

    @Override
    public float getScrollWidth() {
        return inner.getScrollWidth();
    }

    @Override
    public float getScrollHeight() {
        return inner.getScrollHeight();
    }
    
}
