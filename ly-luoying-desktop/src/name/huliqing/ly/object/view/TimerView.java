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
package name.huliqing.ly.object.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.ly.data.ViewData;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.Window;

/**
 * @author huliqing
 */
public class TimerView extends AbstractView {

    private String title;
    private float startTime;
    private boolean up;
    private String format = "yyyy-MM-dd HH:mm:ss";
    
    // 同步时间间隔，单位秒
    protected float syncInterval = 10;
    
    // ---- inner
    private Window win;
    private Text timeText;
    private SimpleDateFormat sdf;
    private Date date;
    protected float syncTimeUsed;

    @Override
    public void setData(EntityData data) {
        super.setData(data);
        title = data.getAsString("title", "");
        startTime = data.getAsFloat("startTime", 0);
        up = data.getAsBoolean("up", true);
        format = data.getAsString("format", format);
        
        win = new Window(viewRoot.getWidth(), viewRoot.getHeight());
        win.setTitle(title);
        win.setCloseable(false);
        
        sdf = new SimpleDateFormat(format);
        date = new Date((long) (startTime * 1000));
        timeText = new Text(sdf.format(date));
        timeText.setMargin(10, 10, 10, 10);
        win.addView(timeText);
        
        viewRoot.addView(win);
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("title", title);
        data.setAttribute("startTime", startTime);
        data.setAttribute("up", up);
    }

    @Override
    protected void doViewInit() {
        super.doViewInit();
        updateTime();
    }
    
    private void updateTime() {
        float time = up ? startTime + timeUsed : startTime - timeUsed;
        if (time <= 0) {
            date.setTime(0);
        } else {
            date.setTime((long)(time * 1000));
        }
        timeText.setText(sdf.format(date));
    }

    @Override
    protected final void doViewLogic(float tpf) {
        super.doViewLogic(tpf);
        updateTime();
    }

    /**
     * 获取当前计时的毫秒时间
     * @return 
     */
    public long getTime() {
        return date.getTime();
    }
    
    /**
     * 获取当前已经使用的时间，单位秒
     * @return 
     */
    public float getTimeUsed() {
        return timeUsed;
    }
    
    public void setTitle(String title) {
        this.title = title;
        win.setTitle(title);
    }

    /**
     * 获取startTime，单位秒
     * @return 
     */
    public float getStartTime() {
        return startTime;
    }

    /**
     * 设置开始计时的时间单位秒
     * @param startTime 
     */
    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    /**
     * 设置是正序或是倒序
     * @param up 
     */
    public void setUp(boolean up) {
        this.up = up;
    }
}
