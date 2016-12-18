/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.progress;

import com.jme3.scene.Node;
import name.huliqing.luoying.data.ProgressData;

/**
 * 抽象基类，处理掉一些通用的基本参数
 * @author huliqing
 */
public abstract class AbstractProgress implements Progress {
    
    protected ProgressData data;
    protected boolean initialized;
    
    @Override
    public void setData(ProgressData data) {
        this.data = data;
    }

    @Override
    public ProgressData getData() {
        return data;
    }

    @Override
    public void updateDatas() {}
    
    @Override
    public void initialize(Node viewRoot) {
        if (initialized) {
            throw new IllegalStateException("Progress already is initialized! progressId=" + data.getId());
        }
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        initialized = false;
    }
    
}
