/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.loader;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.data.AnimData;
import name.huliqing.luoying.data.DelayAnimData;
import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.DataLoader;

/**
 * @author huliqing
 * @param <T>
 */
public class StateDataLoader<T extends StateData> implements DataLoader<StateData> {

    @Override
    public void load(Proto proto, StateData store) {
        // animations="anim1|delayTime, anim2|delayTime,..."
        String[] tempAnims = proto.getAsArray("animations");
        if (tempAnims != null && tempAnims.length > 0) {
            String[] taArr;
            List<DelayAnimData> dads = new ArrayList<DelayAnimData>(tempAnims.length);
            for (String ta : tempAnims) {
                taArr = ta.split("\\|");
                DelayAnimData dad = Loader.loadData(IdConstants.SYS_CUSTOM_ANIM_DELAY);
                dad.setAnimData((AnimData) Loader.loadData(taArr[0]));
                if (taArr.length > 1) {
                    dad.setDelayTime(Float.parseFloat(taArr[1]));
                }
                dads.add(dad);
            }
            store.setDelayAnimDatas(dads);
        }
        
    }


}
