/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.ui;

/**
 * @author huliqing
 * @param <T>
 */
public abstract class Row<T> extends LinearLayout {
    
    protected ListView parentList;
    
    /**
     * @deprecated 使用 {@link #Row(name.huliqing.fighter.ui.ListView) }代替。
     * 当row中的内容更新后可能要同时更新整个ListView,所以最好传递父ListView作为
     * 参数（因为ListView不是UILayout类型，与Row不是实际的父子关系，当Row内容
     * 更新的时候,row的setNeedUpdate()无法传递到父ListView,这些导致一些刷新延迟
     * 的问题，所以后续不再使用这个构造方法）。
     */
    public Row() {
        super();
    }
    
    public Row(ListView parentView) {
        super();
        this.parentList = parentView;
    }

    @Override
    public void setNeedUpdate() {
        super.setNeedUpdate();
        // 通知父ListView需要更新
        if (parentList != null) {
            parentList.setNeedUpdate();
        }
    }
    
    /**
     * 渲染显示行数据
     * @param data
     */
    public abstract void displayRow(T data);
}
