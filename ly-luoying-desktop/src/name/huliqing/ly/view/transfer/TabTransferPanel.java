/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.transfer;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.constants.InterfaceConstants;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.ly.view.IconPanel;
import name.huliqing.ly.view.ItemList.RowClickListener;
import name.huliqing.ly.view.ItemPanel;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.tiles.Tab;
import name.huliqing.luoying.transfer.Transfer;
import name.huliqing.luoying.transfer.TransferData;
import name.huliqing.luoying.xml.ObjectData;

/**
 *
 * @author huliqing
 */
public class TabTransferPanel extends TransferPanel implements RowClickListener {
    
    private final List<TransferData> itemDatas = new ArrayList<TransferData>();
    private final List<TransferData> armorDatas = new ArrayList<TransferData>();
    private final List<TransferData> weaponDatas = new ArrayList<TransferData>();
    
    private final Tab tab;
    private final IconPanel btnItem;      // 物品列表
    private final IconPanel btnArmor;     // 装备面板
    private final IconPanel btnWeapon;    // 武器
    
    private final ItemPanel itemPanel;
    private final ItemPanel armorPanel;
    private final ItemPanel weaponPanel;

    public TabTransferPanel(float width, float height) {
        super(width, height);
        
        btnItem = new IconPanel(1,1, InterfaceConstants.UI_ITEM_ITEM, 0.5f);
        btnArmor = new IconPanel(1,1, InterfaceConstants.UI_ITEM_ARMOR, 0.5f);
        btnWeapon = new IconPanel(1,1, InterfaceConstants.UI_ITEM_WEAPON, 0.5f);
        itemPanel = new ItemPanel(width, height, itemDatas);
        itemPanel.setRowClickListener(this);
        armorPanel = new ItemPanel(width, height, armorDatas);
        armorPanel.setRowClickListener(this);
        weaponPanel = new ItemPanel(width, height, weaponDatas);
        weaponPanel.setRowClickListener(this);
        
        tab = new Tab(width, height);
        tab.addTab(btnWeapon, weaponPanel);
        tab.addTab(btnArmor, armorPanel);
        tab.addTab(btnItem, itemPanel);
        tab.setLayout(Layout.horizontal);
        tab.setTabSizeWeight(0.15f);
        tab.showTab(2);
        addView(tab);
    }
    
    public void setItemDatas(List<TransferData> datas) {
        itemDatas.clear();
        itemDatas.addAll(datas);
        refresh();
    }
    
    public void setWeaponDatas(List<TransferData> datas) {
        weaponDatas.clear();
        weaponDatas.addAll(datas);
        refresh();
    }
    
    public void setArmorDatas(List<TransferData> datas) {
        armorDatas.clear();
        armorDatas.addAll(datas);
        refresh();
    }

    @Override
    public void setDatas(List<TransferData> datas) {
        super.setDatas(datas);
        itemDatas.clear();
        armorDatas.clear();
        weaponDatas.clear();
        
        // 载入角色的数据，注意：不要直接使用获取到的data，因为这会影响原始数据
        for (TransferData data : datas) {
            if (data.getObjectData() instanceof ItemData) {
                itemDatas.add(data);
            } else if (data.getObjectData() instanceof SkinData) {
                SkinData sd = (SkinData) data.getObjectData();
                if (sd.getWeaponType() != null) {
                    weaponDatas.add(data);
                } else {
                    armorDatas.add(data);
                }
            } else {
                throw new UnsupportedOperationException("Unsupported data id=" + data.getObjectData().getId());
            }
        }
        refresh();
    }

    @Override
    public void onClick(Row row, boolean isPressed, TransferData data) {
        if (isPressed) return;
        transfer(data);
    }

    @Override
    public void onAdded(Transfer transfer, TransferData data, int count) {
        TransferData temp = findLocalData(itemDatas, data.getObjectData().getId());
        if (temp == null)
            temp = findLocalData(armorDatas, data.getObjectData().getId());
        if (temp == null)
            temp = findLocalData(weaponDatas, data.getObjectData().getId());
        
        // temp=null说明是新增加的数据，则需要把它分类到指定列表中。
        if (temp == null) {
            ObjectData od = data.getObjectData();
            if (od instanceof ItemData) {
                itemDatas.add(data);
            } else if (od instanceof SkinData) {
                SkinData sd = (SkinData) od;
                if (sd.getWeaponType() != null) {
                    weaponDatas.add(data);
                } else {
                    armorDatas.add(data);
                }
            } else {
                throw new UnsupportedOperationException("Unsupported data id=" + data.getObjectData().getId());
            }
        }

        refresh();
    }

    @Override
    public void onRemoved(Transfer transfer, TransferData data, int count) {
        TransferData temp = transfer.findData(data.getObjectData().getId());
        if (temp == null || temp.getAmount() <= 0) {
            itemDatas.remove(data);
            weaponDatas.remove(data);
            armorDatas.remove(data);
        }
        refresh();
    }
    
    // 刷新面板
    private void refresh() {
        itemPanel.refresh();
        armorPanel.refresh();
        weaponPanel.refresh();
    }
    
    private TransferData findLocalData(List<TransferData> datas, String id) {
        for (TransferData data : datas) {
            if (data.getObjectData().getId().equals(id)) {
                return data;
            }
        }
        return null;
    }
}
