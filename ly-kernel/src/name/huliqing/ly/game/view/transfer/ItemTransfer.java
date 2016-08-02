/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.game.view.transfer;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.ProtoData;
import name.huliqing.core.object.DataFactory;

/**
 * 数据传输基类
 * @author huliqing
 */
public class ItemTransfer implements Transfer {

    private List<TransferListener> listeners;
    // 需要传输的目标对象
    protected Transfer target;
    protected final List<ProtoData> datas = new ArrayList<ProtoData>();
    
    @Override
    public void setTarget(Transfer target) {
        this.target = target;
    }

    @Override
    public Transfer getTarget() {
        return target;
    }

    @Override
    public List<ProtoData> getDatas() {
        return datas;
    }

    @Override
    public void setDatas(List<ProtoData> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
    }

    @Override
    public void addData(ProtoData pd, int count) {
        ProtoData data = findData(pd.getId());
        if (data == null) {
            data = DataFactory.createData(pd.getId());
            data.setTotal(count);
            datas.add(data);
        } else {
            data.setTotal(data.getTotal() + count);
        }
        
        // fireListener
        if (listeners != null) {
            for (TransferListener tl : listeners) {
                tl.onAdded(this, data, count);
            }
        }
    }

    @Override
    public void removeData(ProtoData pd, int count) {
        if (count < 0) 
            throw new IllegalArgumentException("Count could not less than ZERO! count=" + count);
        
        ProtoData data = findData(pd.getId());
        if (data == null) {
            return;
        }
        
        data.setTotal(data.getTotal() - count);
        if (data.getTotal() <= 0) {
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
    public ProtoData findData(String id) {
        for (ProtoData data : datas) {
            if (data.getId().equals(id)) {
                return data;
            }
        }
        return null;
    }

    @Override
    public final void transfer(ProtoData data, int count) {
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
