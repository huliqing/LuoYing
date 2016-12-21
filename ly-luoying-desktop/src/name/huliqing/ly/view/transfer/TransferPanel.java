/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.ly.view.transfer;

import java.util.List;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.ly.view.NumPanel;
import name.huliqing.ly.view.NumPanel.NumConfirmListener;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.transfer.TransferData;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.luoying.transfer.TransferImpl;
import name.huliqing.luoying.transfer.TransferListener;

/**
 * 数据传输界面
 * @author huliqing
 */
public abstract class TransferPanel extends LinearLayout implements TransferListener, NumConfirmListener {
    private final TransferImpl transfer = new TransferImpl();
    private static NumPanel numPanel; // 一个实例就行
    private TransferData tempData;

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
    public void setDatas(List<TransferData> datas) {
        transfer.setDatas(datas);
        setNeedUpdate();
    }
    
    /**
     * 获取传输数据
     * @return 
     */
    public List<TransferData> getDatas() {
        return transfer.getDatas();
    }
    
    /**
     * 传输数据
     * @param data 
     */
    public final void transfer(TransferData data) {
        if (data.getAmount() == 1) {
            // 如果数量只有一个，则直接传输
            transferInner(data, 1);
        } else {
            // 使用数据面板进行确认数量后再传输
//            transferByNumPanel(data, data.getTotal());
            transferByNumPanel(data, 0);
        }
    }
    
    private void transferByNumPanel(TransferData data, int count) {
        if (numPanel == null) {
            numPanel = new NumPanel(UIFactory.getUIConfig().getScreenWidth() * 0.5f
                    , UIFactory.getUIConfig().getScreenHeight() * 0.33f);
            numPanel.setToCorner(Corner.CC);
            numPanel.setDragEnabled(true);
        }
        numPanel.setTitle(ResourceManager.get(ResConstants.CHAT_SHOP_CONFIRM_COUNT)
                + "-" + ResourceManager.getObjectName(data.getObjectData()));
        numPanel.setNumConfirmListener(this);
        numPanel.setMinLimit(0);
        numPanel.setMaxLimit(data.getAmount());
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
    private void transferInner(TransferData data, int count) {
        transfer.transfer(data, count);
    }
    
}
