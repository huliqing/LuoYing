/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.view.transfer;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.constants.InterfaceConstants;
import name.huliqing.luoying.data.ObjectData;
import name.huliqing.luoying.object.item.Item;
import name.huliqing.luoying.object.skin.Skin;
import name.huliqing.luoying.object.skin.Weapon;
import name.huliqing.luoying.view.IconPanel;
import name.huliqing.luoying.view.ItemList.RowClickListener;
import name.huliqing.luoying.view.ItemPanel;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.tiles.Tab;
import name.huliqing.luoying.xml.DataProcessor;

/**
 *
 * @author huliqing
 */
public class TabTransferPanel extends TransferPanel<DataProcessor<ObjectData>> implements RowClickListener<DataProcessor<ObjectData>> {
    
    private final List<DataProcessor<ObjectData>> itemDatas = new ArrayList<DataProcessor<ObjectData>>();
    private final List<DataProcessor<ObjectData>> armorDatas = new ArrayList<DataProcessor<ObjectData>>();
    private final List<DataProcessor<ObjectData>> weaponDatas = new ArrayList<DataProcessor<ObjectData>>();
    
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
    
    public void setItemDatas(List<DataProcessor<ObjectData>> datas) {
        itemDatas.clear();
        itemDatas.addAll(datas);
        refresh();
    }
    
    public void setWeaponDatas(List<DataProcessor<ObjectData>> datas) {
        weaponDatas.clear();
        weaponDatas.addAll(datas);
        refresh();
    }
    
    public void setArmorDatas(List<DataProcessor<ObjectData>> datas) {
        armorDatas.clear();
        armorDatas.addAll(datas);
        refresh();
    }

    @Override
    public void setDatas(List<DataProcessor<ObjectData>> datas) {
        super.setDatas(datas);
        itemDatas.clear();
        armorDatas.clear();
        weaponDatas.clear();
        
        // 载入角色的数据，注意：不要直接使用获取到的data，因为这会影响原始数据
        for (DataProcessor data : datas) {
            if (data instanceof Item) {
                itemDatas.add(data);
            } else if (data instanceof Weapon) {
                weaponDatas.add(data);
            } else if (data instanceof Skin) {
                armorDatas.add(data);
            } else {
                throw new UnsupportedOperationException("Unsupported data id=" + data.getData().getId());
            }
        }
        refresh();
    }

    @Override
    public void onClick(Row row, boolean isPressed, DataProcessor<ObjectData> data) {
        if (isPressed) return;
        transfer(data);
    }

    @Override
    public void onAdded(Transfer<DataProcessor<ObjectData>> transfer, DataProcessor<ObjectData> data, int count) {
        DataProcessor<ObjectData> temp = findLocalData(itemDatas, data.getData().getId());
        if (temp == null)
            temp = findLocalData(armorDatas, data.getData().getId());
        if (temp == null)
            temp = findLocalData(weaponDatas, data.getData().getId());
        
        // temp=null说明是新增加的数据，则需要把它分类到指定列表中。
        if (temp == null) {
            DataProcessor dp = data;
            if (dp instanceof Item) {
                itemDatas.add(data);
            } else if (dp instanceof Weapon) {
                weaponDatas.add(data);
            } else if (dp instanceof Skin) {
                armorDatas.add(data);
            } else {
                throw new UnsupportedOperationException("Unsupported data id=" + data.getData().getId());
            }
        }

        refresh();
    }

    @Override
    public void onRemoved(Transfer<DataProcessor<ObjectData>> transfer, DataProcessor<ObjectData> data, int count) {
        DataProcessor<ObjectData> temp = transfer.findData(data.getData().getId());
        if (temp == null || temp.getData().getTotal() <= 0) {
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
    
    private DataProcessor<ObjectData> findLocalData(List<DataProcessor<ObjectData>> datas, String id) {
        for (DataProcessor<ObjectData> data : datas) {
            if (data.getData().getId().equals(id)) {
                return data;
            }
        }
        return null;
    }
}
