/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import name.huliqing.fighter.data.ViewData;
import name.huliqing.fighter.object.SyncData;
import name.huliqing.fighter.ui.Text;
import name.huliqing.fighter.ui.Window;

/**
 * @author huliqing
 * @param <T>
 */
public class TimerView<T extends ViewData> extends AbstractView<T> {

    private String title;
    private float startTime;
    private boolean up;
    private String format = "yyyy-MM-dd HH:mm:ss";
    // 是否开启与客户端的同步
    protected boolean syncAuto;
    // 同步时间间隔，单位秒
    protected float syncInterval = 10;
    
    // ---- inner
    private Window win;
    private Text timeText;
    private SimpleDateFormat sdf;
    private Date date;
    protected float syncTimeUsed;

    @Override
    public void initData(T data) {
        super.initData(data);
        title = data.getAttribute("title", "");
        startTime = data.getAsFloat("startTime", 0);
        up = data.getAsBoolean("up", true);
        format = data.getAttribute("format", format);
        syncAuto = data.getAsBoolean("syncAuto", false);
        syncInterval = data.getAsFloat("syncInterval", 10);
        
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
        
        // 自动同步
        if (syncAuto) {
            syncTimeUsed += tpf;
            if (syncTimeUsed >= syncInterval) {
                putSyncData("timeUsed", timeUsed);
                syncTimeUsed = 0;
            }
        }
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
        putSyncData("title", title);
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
        putSyncData("startTime", startTime);
    }

    /**
     * 设置是正序或是倒序
     * @param up 
     */
    public void setUp(boolean up) {
        this.up = up;
        putSyncData("up", up);
    }

    @Override
    public T getUpdateData() {
        T vd = super.getUpdateData(); //To change body of generated methods, choose Tools | Templates.
        vd.setAttribute("title", title);
        vd.setAttribute("startTime", startTime);
        vd.setAttribute("up", up);
        return vd;
    }
    
    @Override
    public void applySyncData(SyncData data) {
        super.applySyncData(data);
        setTitle(data.getAttribute("title", title));
        startTime = data.getAsFloat("startTime", startTime);
        up = data.getAsBoolean("up", up);
    }
    
}
