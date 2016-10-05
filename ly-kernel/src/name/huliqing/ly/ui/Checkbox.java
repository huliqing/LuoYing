/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.ui;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.constants.InterfaceConstants;


/**
 *
 * @author huliqing
 */
public class Checkbox extends AbstractUI {

    public interface ChangeListener {
        /**
         * 当checkbox的值发生变化时调用
         * @param ui 
         */
        void onChange(Checkbox ui);
    } 
    
    private boolean checked;
    
    private Icon flag;
    private String flagOff = InterfaceConstants.UI_CHECKBOX_OFF;
    private String flagOn = InterfaceConstants.UI_CHECKBOX_ON;
    private List<ChangeListener> listeners;

    public Checkbox() {
        this(false);
    }
    
    public Checkbox(boolean checked) {
        super(32, 32);
        this.flag = new Icon(flagOff);
        this.attachChild(flag);
        
        addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                
                // remove
//                checked = !checked;
//                updateFlag();
                
                setChecked(!isChecked());
            }
        });
        
        setChecked(checked);
    }
    
    public final boolean isChecked() {
        return checked;
    }
    
    public final void setChecked(boolean checked) {
        boolean changed = (this.checked != checked);
        this.checked = checked;
        updateFlag();
        // 触发侦听器
        if (changed && listeners != null) {
            for (ChangeListener cl : listeners) {
                cl.onChange(this);
            }
        }
    }
    
    public void addChangeListener(ChangeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<ChangeListener>(1);
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public boolean removeChangeListener(ChangeListener listener) {
        return listeners != null && listeners.remove(listener);
    }
    
    private void updateFlag() {
        flag.setImage(checked ? flagOn : flagOff);
        setNeedUpdate();
    }

    @Override
    public void updateView() {
        super.updateView();
        flag.setWidth(width);
        flag.setHeight(height);
        flag.updateView();
    }

    
}
