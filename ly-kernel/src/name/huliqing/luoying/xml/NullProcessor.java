/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.xml;

/**
 * @author huliqing
 */
public class NullProcessor implements DataProcessor<ObjectData> {

    private ObjectData data;
    
    @Override
    public void setData(ObjectData data) {
        this.data = data;
    }

    @Override
    public ObjectData getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
    }


}
