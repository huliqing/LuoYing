/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view.system;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.mvc.service.ConfigService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.ui.ListView;
import name.huliqing.core.ui.Row;
import name.huliqing.core.ui.UI;

/**
 *
 * @author huliqing
 */
public class SystemUIPanel extends ListView<SystemData> {
    private final ConfigService configService = Factory.get(ConfigService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final List<SystemData> datas = new ArrayList<SystemData>(2); 
    
    // 快捷方式锁定
    private RowCheckbox shortcutLock;
    // 快捷方式大小
    private RowSimple shortcutSize;
    private ShortcutSizeOper shortcutSizeOper;
    // 快捷方式清理
    private RowSimple shortcutClear;
    // 谈话速度调节
    private RowSimple talkSpeed;
    private TalkSpeedOper talkSpeedOper;
    

    public SystemUIPanel(float width, float height) {
//        super(width, height, ResourceManager.get("system.ui"));
        super(width, height);
        
        datas.add(new SystemData(get("system.ui.shortcutLock"), get("system.ui.shortcutLock.des")));
        datas.add(new SystemData(get("system.ui.shortcutSize"), get("system.ui.shortcutSize.des")));
        datas.add(new SystemData(get("system.ui.shortcutClear"), get("system.ui.shortcutClear.des")));
        datas.add(new SystemData(get("system.ui.talkSpeed"), get("system.ui.talkSpeed.des")));
        
        // 快捷方式锁定
        shortcutLock = new RowCheckbox(datas.get(0).getName(), datas.get(0).getDes(), configService.isShortcutLocked());
        shortcutLock.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                configService.setShortcutLocked(shortcutLock.isChecked());
            }
        });
        
        // 快捷方式大小调节
        shortcutSize = new RowSimple(this, datas.get(1).getName(), datas.get(1).getDes());
        shortcutSize.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                playService.addObject(shortcutSizeOper, true);
            }
        });
        shortcutSizeOper = new ShortcutSizeOper(width * 0.5f, height * 0.5f, shortcutSize);
        
        // 快捷清理
        shortcutClear = new RowSimple(this, datas.get(2).getName(), datas.get(2).getDes());
        shortcutClear.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                configService.clearShortcuts();
            }
        });
        
        // 谈话速度调节
        talkSpeed = new RowSimple(this, datas.get(3).getName(), datas.get(3).getDes());
        talkSpeed.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                playService.addObject(talkSpeedOper, true);
            }
        });
        talkSpeedOper = new TalkSpeedOper(width * 0.5f, height * 0.5f, talkSpeed);
        
        rows.add(shortcutLock);
        rows.add(shortcutSize);
        rows.add(shortcutClear);
        rows.add(talkSpeed);
        pageSize = datas.size();
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
