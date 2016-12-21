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
package name.huliqing.ly.view.system;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.ly.LyConfig;

/**
 *
 * @author huliqing
 */
public class SystemSoundPanel extends ListView<SystemData> {
    private List<SystemData> datas = new ArrayList<SystemData>(2); 
    
    private RowCheckbox soundEnable;
    private RowSimple soundVolume;
    private SoundVolumeOper soundVolumeOper;
    
    public SystemSoundPanel(float width, float height) {
        super(width, height);
        datas.add(new SystemData(get("system.soundEnable"), get("system.soundEnable.des")));
        datas.add(new SystemData(get("system.soundVolume"), get("system.soundVolume.des")));
        
        soundEnable = new RowCheckbox(datas.get(0).getName(), datas.get(0).getDes(), LyConfig.isSoundEnabled());
        soundEnable.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                LyConfig.setSoundEnabled(soundEnable.isChecked());
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
