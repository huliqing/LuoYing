/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.select;

import com.jme3.math.Ray;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 空的操作物体，相关的Entity不能被直接操作，该类主要用于所有那些不能直接可视化操作的实体Entity
 * @author huliqing
 * @param <T>
 */
public class EmptyEntitySelectObj <T extends Entity> extends EntitySelectObj<T> {

    @Override
    public Float distanceOfPick(Ray ray) {
        return null;
    }
}
