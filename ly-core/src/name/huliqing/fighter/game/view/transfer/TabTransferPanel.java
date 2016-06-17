/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.view.transfer;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.InterfaceConstants;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.data.SkinData;
import name.huliqing.fighter.enums.DataType;
import name.huliqing.fighter.game.service.SkinService;
import name.huliqing.fighter.game.view.tiles.IconPanel;
import name.huliqing.fighter.game.view.tiles.ItemList.RowClickListener;
import name.huliqing.fighter.game.view.tiles.ItemPanel;
import name.huliqing.fighter.ui.Row;
import name.huliqing.fighter.ui.tiles.Tab;

/**
 *
 * @author huliqing
 */
public class TabTransferPanel extends TransferPanel implements RowClickListener {
    private final SkinService skinService = Factory.get(SkinService.class);
    
    private final List<ProtoData> itemDatas = new ArrayList<ProtoData>();
    private final List<ProtoData> armorDatas = new ArrayList<ProtoData>();
    private final List<ProtoData> weaponDatas = new ArrayList<ProtoData>();
    
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
    
    @Override
    public void setDatas(List<ProtoData> datas) {
        super.setDatas(datas);
        itemDatas.clear();
        armorDatas.clear();
        weaponDatas.clear();
        
        // 载入角色的数据，注意：不要直接使用获取到的data，因为这会影响原始数据
        for (ProtoData data : datas) {
            if (data.getDataType() == DataType.item) {
                itemDatas.add(data);
            } else if (data.getDataType() == DataType.skin) {
                if (skinService.isWeapon((SkinData) data)) {
                    weaponDatas.add(data);
                } else {
                    armorDatas.add(data);
                }
            } else {
                throw new UnsupportedOperationException("Unsupported dataType=" + data.getDataType());
            }
        }
        refresh();
    }

    @Override
    public void onClick(Row row, boolean isPressed, ProtoData data) {
        if (isPressed) return;
        transfer(data);
    }

    @Override
    public void onAdded(Transfer transfer, ProtoData data, int count) {
        ProtoData temp = findLocalData(itemDatas, data.getId());
        if (temp == null)
            temp = findLocalData(armorDatas, data.getId());
        if (temp == null)
            temp = findLocalData(weaponDatas, data.getId());
        
        // temp=null说明是新增加的数据，则需要把它分类到指定列表中。
        if (temp == null) {
            if (data.getDataType() == DataType.item) {
                itemDatas.add(data);
            } else if (data.getDataType() == DataType.skin) {
                if (skinService.isWeapon((SkinData) data)) {
                    weaponDatas.add(data);
                } else {
                    armorDatas.add(data);
                }
            } else {
                throw new IllegalArgumentException("Unsupported dataType, dataType=" + data.getDataType());
            }
        }

        refresh();
    }

    @Override
    public void onRemoved(Transfer transfer, ProtoData data, int count) {
        // temp == null说明该物品已经完全被移除
        ProtoData temp = transfer.findData(data.getId());
        if (temp == null || temp.getTotal() <= 0) {
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
    
    private ProtoData findLocalData(List<ProtoData> datas, String id) {
        for (ProtoData data : datas) {
            if (data.getId().equals(id)) {
                return data;
            }
        }
        return null;
    }
}
