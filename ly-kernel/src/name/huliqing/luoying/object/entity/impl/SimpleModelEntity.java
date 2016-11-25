/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity.impl;

import com.jme3.scene.Spatial;
import name.huliqing.luoying.object.AssetLoader;
import name.huliqing.luoying.object.entity.ModelEntity;

/**
 * 普通模型类环境物体,所有不需要归类或不需要特别类型的模型类实体都可以直接继承这个类。
 * @author huliqing
 */
public class SimpleModelEntity extends ModelEntity {

    @Override
    protected Spatial loadModel() {
        return AssetLoader.loadModel(data.getAsString("file"));
    }
    
}
