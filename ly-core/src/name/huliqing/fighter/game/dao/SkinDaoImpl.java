/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.dao;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.data.ActorData;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.data.SkinData;
import name.huliqing.fighter.enums.DataType;

/**
 *
 * @author huliqing
 */
public class SkinDaoImpl implements SkinDao {

    @Override
    public void inject() {
        // ignore
    }

    @Override
    public List<SkinData> getAll(ActorData actorData, List<SkinData> store) {
        if (store == null) {
            store = new ArrayList<SkinData>();
        }
        List<ProtoData> items = actorData.getItemStore().getAll();
        for (ProtoData od : items) {
            if (od.getProto().getDataType() == DataType.skin) {
                store.add((SkinData) od);
            }
        }
        return store;
    }
    
    @Override
    public List<SkinData> getArmorSkins(ActorData actorData, List<SkinData> store) {
        if (store == null) {
            store = new ArrayList<SkinData>();
        }
        List<ProtoData> items = actorData.getItemStore().getAll();
        for (ProtoData od : items) {
            if (od.getProto().getDataType() == DataType.skin) {
                SkinData sd = (SkinData) od;
                if (sd.getWeaponType() == 0) {
                    store.add(sd);
                }
            }
        }
        return store;
    }

    @Override
    public List<SkinData> getWeaponSkins(ActorData actorData, List<SkinData> store) {
        if (store == null) {
            store = new ArrayList<SkinData>();
        }
        List<ProtoData> items = actorData.getItemStore().getAll();
        for (ProtoData od : items) {
            if (isWeapon(od)) {
                store.add((SkinData) od);
            }
        }
        return store;
    }

    @Override
    public List<SkinData> getWeaponSkinsAll(ActorData actorData, List<SkinData> store) {
        // 1.包裹中的武器
        store = getWeaponSkins(actorData, store);
        
        // 2.加入基本皮肤中的武器
        List<SkinData> skinBase = actorData.getSkinBase();
        if (skinBase != null && !skinBase.isEmpty()) {
            for (SkinData sd : skinBase) {
                if (isWeapon(sd)) {
                    store.add(sd);
                }
            }
        }
        return store;
    }

    @Override
    public List<SkinData> getWeaponSkinsAllInUsed(ActorData actorData, List<SkinData> store) {
        if (store == null) {
            store = new ArrayList<SkinData>(2);
        }
        List<SkinData> allWeapon = getWeaponSkinsAll(actorData, null);
        for (SkinData sd : allWeapon) {
            if (sd.isUsing()) {
                store.add(sd);
            }
        }
        return store;
    }
    
    private boolean isWeapon(ProtoData protoData) {
        if (protoData.getProto().getDataType() == DataType.skin) {
            SkinData sd = (SkinData) protoData;
            
//            if (sd.getWeaponType() > 0) {
//                return true;
//            }
            
            return sd.isWeapon();
        }
        return false;
    }
    
}
