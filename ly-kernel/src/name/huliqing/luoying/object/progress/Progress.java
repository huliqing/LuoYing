/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.progress;

import com.jme3.scene.Node;
import name.huliqing.luoying.data.ProgressData;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * Progress用于实现动画载入功能，例如进度条载入动画等。
 * @author huliqing
 */
public interface Progress extends DataProcessor<ProgressData>{
    
    /**
     * 初始化进度条.
     * @param viewRoot 用于进度条视图的父节点，比如GUI的根节点。
     */
    void initialize(Node viewRoot);
    
    /**
     * 判断是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 更新并渲染进度条，给定的参数progress取值为 0.0~1.0， 1.0表示100%载入完成, 0.5表示%50完成，依此类推。
     * @param progress 
     */
    void display(float progress);
    
    /**
     * 清理并释放资源
     */
    void cleanup();
    
}
