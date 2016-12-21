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
