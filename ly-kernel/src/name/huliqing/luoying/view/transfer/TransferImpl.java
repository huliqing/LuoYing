/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.view.transfer;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据传输基类.
 * @author huliqing
 */
public class TransferImpl implements Transfer {

    private List<TransferListener> listeners;
    // 需要传输的目标对象
    protected Transfer target;
    // 数据列表
    protected final List<TransferData> datas = new ArrayList<TransferData>();
    
    @Override
    public void setTarget(Transfer target) {
        this.target = target;
    }

    @Override
    public Transfer getTarget() {
        return target;
    }

    @Override
    public List<TransferData> getDatas() {
        return datas;
    }

    @Override
    public void setDatas(List<TransferData> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
    }

    @Override
    public void addData(TransferData pd, int count) {
        TransferData data = findData(pd.getObject().getData().getId());
        if (data == null) {
            data = new TransferData();
            data.setObject(pd.getObject());
            data.setCount(count);
            datas.add(data);
        } else {
            data.setCount(data.getCount() + count);
        }
        
        // fireListener
        if (listeners != null) {
            for (TransferListener tl : listeners) {
                tl.onAdded(this, data, count);
            }
        }
    }

    @Override
    public void removeData(TransferData pd, int count) {
        if (count < 0) 
            throw new IllegalArgumentException("Count could not less than ZERO! count=" + count);
        
        TransferData data = findData(pd.getObject().getData().getId());
        if (data == null) {
            return;
        }
        
        data.setCount(data.getCount() - count);
        if (data.getCount() <= 0) {
            datas.remove(data);
        }
        
        // fireListener
        if (listeners != null) {
            for (TransferListener tl : listeners) {
                tl.onRemoved(this, data, count);
            }
        }
    }

    @Override
    public TransferData findData(String id) {
        for (TransferData data : datas) {
            if (data.getObject().getData().getId().equals(id)) {
                return data;
            }
        }
        return null;
    }

    @Override
    public final void transfer(TransferData data, int count) {
        if (target == null)
            return;
        target.addData(data, count);
        this.removeData(data, count);
    }

    @Override
    public void addListener(TransferListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<TransferListener>(1);
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public boolean removeListener(TransferListener listener) {
        return listeners != null && listeners.remove(listener);
    }
    
    
}
