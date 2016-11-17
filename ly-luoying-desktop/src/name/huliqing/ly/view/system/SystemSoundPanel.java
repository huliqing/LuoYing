/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.system;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.ConfigService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.state.UIState;

/**
 *
 * @author huliqing
 */
public class SystemSoundPanel extends ListView<SystemData> {
    private ConfigService configService = Factory.get(ConfigService.class);
    private PlayService playService = Factory.get(PlayService.class);
    private List<SystemData> datas = new ArrayList<SystemData>(2); 
    
    private RowCheckbox soundEnable;
    private RowSimple soundVolume;
    private SoundVolumeOper soundVolumeOper;
    
    public SystemSoundPanel(float width, float height) {
        super(width, height);
        datas.add(new SystemData(get("system.soundEnable"), get("system.soundEnable.des")));
        datas.add(new SystemData(get("system.soundVolume"), get("system.soundVolume.des")));
        
        soundEnable = new RowCheckbox(datas.get(0).getName(), datas.get(0).getDes(), configService.isSoundEnabled());
        soundEnable.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                configService.setSoundEnabled(soundEnable.isChecked());
            }
        });
        
        soundVolume = new RowSimple(this, datas.get(1).getName(), datas.get(1).getDes());
        soundVolume.setName("SystemSoundPanel_soundVolume");
        soundVolume.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                
//                playService.addObject(soundVolumeOper, true);
                UIState.getInstance().addUI(soundVolumeOper);
            }
        });
        
        soundVolumeOper = new SoundVolumeOper(width * 0.5f, height * 0.5f, soundVolume);
        
        rows.add(soundEnable);
        rows.add(soundVolume);
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
