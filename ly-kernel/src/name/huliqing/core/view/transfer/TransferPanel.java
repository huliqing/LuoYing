/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view.transfer;

import java.util.List;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.xml.ProtoData;
import name.huliqing.core.view.NumPanel;
import name.huliqing.core.view.NumPanel.NumConfirmListener;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.ui.LinearLayout;
import name.huliqing.core.ui.UIFactory;
import name.huliqing.core.ui.state.UIState;

/**
 * 数据传输界面
 * @author huliqing
 */
public abstract class TransferPanel extends LinearLayout implements TransferListener, NumConfirmListener {
    private final ItemTransfer transfer = new ItemTransfer();
    private static NumPanel numPanel; // 一个实例就行
    private ProtoData tempData;

    public TransferPanel(float width, float height) {
        super(width, height);
        transfer.addListener(this);
    }
    
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
//            transferByNumPanel(data, data.getTotal());
            transferByNumPanel(data, 0);
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
        if (numPanel != TransferPanel.numPanel) 
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
