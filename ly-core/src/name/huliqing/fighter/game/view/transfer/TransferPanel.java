/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.view.transfer;

import com.jme3.font.BitmapFont;
import java.util.List;
import name.huliqing.fighter.constants.ResConstants;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.game.view.tiles.NumPanel;
import name.huliqing.fighter.game.view.tiles.NumPanel.NumConfirmListener;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.ui.LinearLayout;
import name.huliqing.fighter.ui.Text;
import name.huliqing.fighter.ui.UIFactory;
import name.huliqing.fighter.ui.state.UIState;

/**
 * 数据传输界面
 * @author huliqing
 */
public abstract class TransferPanel extends LinearLayout implements TransferListener, NumConfirmListener {
    private final ItemTransfer transfer = new ItemTransfer();
    private static NumPanel numPanel; // 一个实例就行
    private ProtoData tempData;

    // remove20160311,太难看
//    // 标题栏 
//    protected LinearLayout title;
//    private Text titleText;
    
    public TransferPanel(float width, float height) {
        super(width, height);
        transfer.addListener(this);
        
//        title = new LinearLayout(width, UIFactory.getUIConfig().getListTitleHeight());
//        title.setBackground(UIFactory.getUIConfig().getBackground(), true);
//        title.setBackgroundColor(UIFactory.getUIConfig().getTitleBgColor(), false);
//        titleText = new Text("");
//        titleText.setWidth(width);
//        titleText.setHeight(title.getHeight());
//        titleText.setVerticalAlignment(BitmapFont.VAlign.Center);
//        titleText.setMargin(10, 0, 0, 0);
//        title.addView(titleText);
//        addView(title);
    }
    
//    public void setTitle(String title) {
//        this.titleText.setText(title);
//    }
    
    /**
     * 设置传输目标，即要将数据传输哪一个目标TransferPanel
     * @param target 
     */
    public void setTransfer(TransferPanel target) {
        transfer.setTarget(target.transfer);
    }
    
    /**
     * 设置初始化数据
     * @param datas 
     */
    public void setDatas(List<ProtoData> datas) {
        transfer.setDatas(datas);
        setNeedUpdate();
    }
    
    /**
     * 获取传输数据
     * @return 
     */
    public List<ProtoData> getDatas() {
        return transfer.getDatas();
    }
    
    /**
     * 传输数据
     * @param data 
     */
    public final void transfer(ProtoData data) {
        if (data.getTotal() == 1) {
            // 如果数量只有一个，则直接传输
            transferInner(data, 1);
        } else {
            // 使用数据面板进行确认数量后再传输
            transferByNumPanel(data, data.getTotal());
        }
    }
    
    private void transferByNumPanel(ProtoData data, int count) {
        if (numPanel == null) {
            numPanel = new NumPanel(UIFactory.getUIConfig().getScreenWidth() * 0.5f
                    , UIFactory.getUIConfig().getScreenHeight() * 0.33f);
            numPanel.setToCorner(Corner.CC);
            numPanel.setDragEnabled(true);
        }
        numPanel.setTitle(ResourceManager.get(ResConstants.CHAT_SHOP_CONFIRM_COUNT)
                + "-" + ResourceManager.getObjectName(data));
        numPanel.setNumConfirmListener(this);
        numPanel.setMinLimit(0);
        numPanel.setMaxLimit(data.getTotal());
        numPanel.setValue(count);
        // 记录临时数据
        tempData = data;
        UIState.getInstance().addUI(numPanel);
    }

    @Override
    public final void onConfirm(NumPanel numPanel) {
        if (numPanel != this.numPanel) 
            return;
        
        int count = numPanel.getValue();
        if (count <= 0) {
            numPanel.removeFromParent();
            return;
        }
        
        // 通过NumPanel确认数量后提交数据传输
        transferInner(tempData, count);
        // 移除numPanel
        numPanel.removeFromParent();
    }
    
    /**
     * 传输数据
     * @param data
     * @param count 
     */
    private void transferInner(ProtoData data, int count) {
        transfer.transfer(data, count);
    }
    
}
