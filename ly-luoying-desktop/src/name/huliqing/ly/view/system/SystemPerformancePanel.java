/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.system;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.ConfigService;
import name.huliqing.luoying.manager.ResourceManager;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.UI;

/**
 * 性能选项设置
 * @author huliqing
 */
public class SystemPerformancePanel extends ListView<SystemData> {

    private static final Logger LOG = Logger.getLogger(SystemPerformancePanel.class.getName());
    
    private final ConfigService configService = Factory.get(ConfigService.class);
//    private final PlayService playService = Factory.get(PlayService.class);
    private final List<SystemData> datas = new ArrayList<SystemData>(2);
    
    private RowCheckbox hwSkinning;
    
    public SystemPerformancePanel(float width, float height) {
//        super(width, height, ResourceManager.get("system.performance"));
        super(width, height);
        
        datas.add(new SystemData(get("system.performance.hwSkinning"), get("system.performance.hwSkinning.des")));
       
        // xxx ----
//        hwSkinning = new RowCheckbox(datas.get(0).getName(), datas.get(0).getDes(), configService.isUseHardwareSkinning());
//        hwSkinning.addClickListener(new Listener() {
//            @Override
//            public void onClick(UI ui, boolean isPress) {
//                if (isPress) return;
//                configService.setUseHardwareSkining(hwSkinning.isChecked());
//            }
//        });
//        rows.add(hwSkinning);
        
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
