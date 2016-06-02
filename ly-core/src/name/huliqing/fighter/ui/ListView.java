/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.ui;

import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author huliqing
 */
public abstract class ListView<T> extends AbstractUI implements ScrollListener {
    
    // 滚动条
    private VerticalFingerScroll scroll;
    
    // 每页显示多少条记录.
    protected int pageSize = 8;
    
    // 行数据,size与pageSize一致
    protected final List<Row> rows = new ArrayList<Row>();
    
    // 当前从哪一行记录开始显示数据.
    private int currentStartRowIndex;
    
    public ListView(float width, float height) {
        this(width, height, "");
    }
    
    public ListView(float width, float height, String name) {
        super(width, height);
        setName(name);
        this.width = width;
        this.height = height;
        this.scroll = new VerticalFingerScroll(this, 10);
        this.scroll.setScrollListener(this);
        
        this.attachChild(this.scroll);
    }

    @Override
    public final int attachChild(Spatial child) {
        scroll.removeFromParent();
        
        super.attachChild(child); 
        
        return super.attachChild(scroll);
    }

    @Override
    public void updateView() {
        super.updateView();
        float rowHeight = getRowHeight();
        float rowTotal = getRowTotal();
        float contentHeight = rowHeight * rowTotal;
        
        // Auto show scroll
        if (contentHeight > height) {
            scroll.setVisible(true);
            scroll.updateScroll(width, height, contentHeight);
        } else {
            scroll.setVisible(false);
        }
        
        // 更新rows layout
        boolean sVisible = scroll.isVisible();
        checkRows();
        Row row;
        for (int i = 0; i < rows.size(); i++) {
            row = rows.get(i);
            row.setWidth(sVisible ? width - scroll.getScrollWidth() : width);
            row.setHeight(rowHeight);
            row.updateView();
        }
        
        // 更新rows位置
        updateRowsPosition();
        
    }
    
    // 确保rows足够大于pageSize
    private void checkRows() {
        // 如果row UI少于pageSize(可能调整了pageSize),则添加新的rowUI
        for (int i = rows.size(); i < pageSize; i++) {
            Row row = createEmptyRow();
            if (row != null) {
                rows.add(row);
                attachChild(row);
            }
        }
    }
    
    private void updateRowsPosition() {
        super.updateView();
        if (rows.isEmpty()) 
            return;
        
        // 2.再更新子组件位置
        UI child;
        float y = height;
        for (int i = 0; i < pageSize; i++) {
            child = rows.get(i);
            if (!child.isVisible()) 
                continue;
            y -= child.getHeight();
            child.setPosition(0, y);
        }
    }
    
    public float getRowHeight() {
        return height / pageSize;
    }
    
    /**
     * 获取所有数据占据的行数
     * @return 
     */
    public int getRowTotal() {
        return getDatas().size();
    }
    
    public void addItem(T data) {
        getDatas().add(data);
        refreshPageData();
    }
    
    public boolean removeItem(T data) {
        boolean result = getDatas().remove(data);
        refreshPageData();
        return result;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        refreshPageData();
    }
  
    // TODO: 20160312暂不处理，因需要同时同步scroll的位置。
//    /**
//     * 设置从哪一行开始显示数据记录。
//     * @param startIndex 从0开始，最大为datas.size - 1;
//     */
//    public void setStartIndex(int startIndex) {
//        int rowTotal = getRowTotal();
//        if (rowTotal <= 0)
//            currentStartRowIndex = 0;
//        currentStartRowIndex = MathUtils.clamp(startIndex, 0, rowTotal - 1);
//    }
    
    /**
     * 判断是否要过滤某一条数据,即让某一条数据不显示在列表中。默认情况下所有
     * 数据都将按顺序逐一进行显示。
     * @param data 当前要显示的数据
     * @return 如果要过滤则返回true,否则返回false.
     */
    protected boolean filter(T data) {
        return false;
    }
    
    /**
     * 刷新页面数据,在列表中数据有变动时(添加或移除等)应该调用该方法进行刷新列表.
     * 注意：不要直接在其它线程中调用该方法，这会造成UI显示问题。　
     */
    public void refreshPageData() {
        
        // 重新载入数据
        reloadData();
        
        // 确保rows足够大于pageSize
        checkRows();
        
        // 更新数据显示
        int totalRow = getRowTotal();
        
        // fix bug: 当物品删除之后，如果数量小于或等于一页，则需要把currentStartRowIndex
        // 置为0，即从第首个物品开始显示，否则可能由于滚动条消失而导致currentStartRowIndex
        // 前面的物品不能显示出来。
        if (totalRow <= pageSize) {
            currentStartRowIndex = 0;
        }
        
        List<T> datas = getDatas();
        Row row;
        for (int i = currentStartRowIndex, rowIndex = 0; ; i++, rowIndex++) {
            if (rowIndex >= pageSize || rowIndex >= rows.size()) {
                break;
            }
            row = rows.get(rowIndex);
            if (i >= totalRow) {
                row.setVisible(false);
            } else {
                
                // 超过数据行
                if (datas == null || i >= datas.size()) {
                    continue;
                }
                
                // 过滤
                T data = datas.get(i);
                if (filter(data)) {
                    rowIndex--;
                    continue;
                }
                
                // 显示数据
                row.setVisible(true);
                row.displayRow(data);
            }
        }
        // 设置更新layout标志
        setNeedUpdate();
    }
    
    @Override
    public void onScroll(float length) {
        float rowHeight = getRowHeight();
        int startRowIndex = (int) ((length + 3) / rowHeight); // length + 3 以避免float值在极小的误差之下显示不到最后的一行记录。
//        Logger.get(getClass()).log(Level.INFO, "length={0}, rowHeight={1}, index={2}", new Object[] {length, rowHeight, startRowIndex});
        if (startRowIndex == currentStartRowIndex) {
            return;
        }
        currentStartRowIndex = startRowIndex;
        refreshPageData();
    }
    
    /**
     * 重新加载数据，该方法在刷新页面时调用。
     */
    protected void reloadData() {}
    
    /**
     * 生成空的row
     * @return 
     */
    protected abstract Row<T> createEmptyRow();
    
    /**
     * 获取数据
     */
    public abstract List<T> getDatas();
}
