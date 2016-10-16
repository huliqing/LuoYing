/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.define;

import name.huliqing.luoying.data.DefineData;
import name.huliqing.luoying.xml.DataProcessor;

/**
 *
 * @author huliqing
 */
public abstract class Define implements DataProcessor<DefineData> {

    private DefineData data;
    
    @Override
    public void setData(DefineData data) {
        this.data = data;
    }

    @Override
    public DefineData getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
    }
    
    
}
