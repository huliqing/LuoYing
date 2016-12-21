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
package name.huliqing.luoying.transfer;

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
        TransferData data = findData(pd.getObjectData().getId());
        if (data == null) {
            data = new TransferData();
            data.setObjectData(pd.getObjectData());
            data.setAmount(count);
            datas.add(data);
        } else {
            data.setAmount(data.getAmount() + count);
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
        
        TransferData data = findData(pd.getObjectData().getId());
        if (data == null) {
            return;
        }
        
        data.setAmount(data.getAmount() - count);
        if (data.getAmount() <= 0) {
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
            if (data.getObjectData().getId().equals(id)) {
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
