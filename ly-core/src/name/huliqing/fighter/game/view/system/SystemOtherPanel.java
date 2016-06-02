/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.view.system;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.service.ConfigService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.ui.ListView;
import name.huliqing.fighter.ui.Row;
import name.huliqing.fighter.ui.UI;

/**
 *
 * @author huliqing
 */
public class SystemOtherPanel extends ListView<SystemData> {
    private final ConfigService configService = Factory.get(ConfigService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final List<SystemData> datas = new ArrayList<SystemData>(2); 
    
    private RowCheckbox debugRow;
    
    public SystemOtherPanel(float width, float height) {
//        super(width, height, ResourceManager.get("system.other"));
        super(width, height);
        
        datas.add(new SystemData(get("system.other.debug"), get("system.other.debug.des")));
        
        debugRow = new RowCheckbox(datas.get(0).getName(), datas.get(0).getDes(), configService.isDebugEnabled());
        debugRow.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                configService.setDebug(debugRow.isChecked());
            }
        });
        rows.add(debugRow);
        
        this.pageSize = datas.size();
        for (int i = 0; i < rows.size(); i++) {
            attachChild(rows.get(i));
        }
    }

    @Override
    protected Row createEmptyRow() {
        return new RowSimple(this, "", "");
    }

    @Override
    public List getDatas() {
        return datas;
    }
    
     /**
     * 获取所有数据占据的行数
     * @return 
     */
    @Override
    public int getRowTotal() {
        return rows.size();
    }
    
    @Override
    public void addItem(SystemData data) {
        // ignore
    }
    
    @Override
    public boolean removeItem(SystemData data) {
        return false;
    }
    
    private String get(String resKey) {
        return ResourceManager.get(resKey);
    }
}
