/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.loader;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.AttributeData;
import name.huliqing.luoying.data.DropData;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.data.ChannelData;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.data.ResistData;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.luoying.data.TalentData;
import name.huliqing.luoying.layer.service.DefineService;
import name.huliqing.luoying.xml.DataFactory;

/**
 *
 * @author huliqing
 */
public class ActorDataLoader extends EntityDataLoader {
//    private static final Logger LOG = Logger.getLogger(ActorDataLoader.class.getName());
    private final DefineService defineService = Factory.get(DefineService.class);

    @Override
    public void load(Proto proto, EntityData data) {
        super.load(proto, data);
        
        String matStr = proto.getAsString("mat");
        if (matStr != null) {
            data.setMat(defineService.getMatDefine().getMat(matStr));
        }
        
        // items 
        String[] items = proto.getAsArray("items");
        if (items != null) {
            for (String item : items) {
                String[] itemArr = item.split("\\|");
                ItemData itemData = DataFactory.createData(itemArr[0]);
                itemData.setTotal(itemArr.length > 1 ? Integer.parseInt(itemArr[1]) : 1);
                data.addObjectData(itemData);
            }
        }
        
        // skinBase: 基本皮肤
        // 注1：可能部分角色没有基本皮肤，如不可换装备的角色类型
        // 注2: 基本皮肤不会存放在itemStore中，而skinOutfit会存放在itemStore
        String[] skinBases = proto.getAsArray("skinBase");
        if (skinBases != null) {
            for (String sbt : skinBases) {
                SkinData sdb = DataFactory.createData(sbt);
                sdb.setBaseSkin(true);
                sdb.setUsed(false);
                sdb.setTotal(1);
                data.addObjectData(sdb);
            }            
        }
        
        // skins: 包裹上的装备，没穿在身上
        String[] skins = proto.getAsArray("skins");
        if (skins != null && skins.length > 0) {
            for (String skinStr : skins) {
                String[] skinArr = skinStr.split("\\|");
                SkinData skinData = DataFactory.createData(skinArr[0]);
                skinData.setBaseSkin(false);
                skinData.setUsed(false);
                skinData.setTotal(skinArr.length > 1 ? Integer.parseInt(skinArr[1]) : 1);
                data.addObjectData(skinData);
            }
        }
        
        // skinOutfit: 穿在身上的装备
        String[] skinOutfit = proto.getAsArray("skinOutfit");
        if (skinOutfit != null) {
            for (String skinId : skinOutfit) {
                SkinData sdb = DataFactory.createData(skinId);
                sdb.setBaseSkin(false);
                sdb.setUsed(true);
                sdb.setTotal(1);
                data.addObjectData(sdb);
            }
        }
        
        // weapon: 拿在手上的武器
        String[] weaponIds = proto.getAsArray("skinWeapon");
        if (weaponIds != null) {
            for (String wid : weaponIds) {
                SkinData weaponData = (SkinData) DataFactory.createData(wid);
                weaponData.setBaseSkin(false);
                weaponData.setUsed(true);
                weaponData.setTotal(1);
                data.addObjectData(weaponData);
            }
        }
        
        // channels: 角色动画通道
        String[] channels = proto.getAsArray("channels");
        if (channels != null) {
            for (String id : channels) {
                data.addObjectData((ChannelData)DataFactory.createData(id));
            }
        }
        
        // drops: 物品掉落设置
        String[] drops = proto.getAsArray("drops");
        if (drops != null && drops.length > 0) {
            for (String id : drops) {
                data.addObjectData((DropData)DataFactory.createData(id));
            }
        }
        
        // skills: 技能
        String[] skillIds = proto.getAsArray("skills");
        if (skillIds != null && skillIds.length > 0) {
            for (String skillId : skillIds) {
                data.addObjectData((SkillData)DataFactory.createData(skillId));
            }
        }
        
        // logics: 逻辑
        String[] logics = proto.getAsArray("logics");
        if (logics != null && logics.length > 0) {
            for (String logicId : logics) {
                data.addObjectData((LogicData) DataFactory.createData(logicId));
            }
        }
        
        // attributes: 角色属性
        String[] attributeArr = proto.getAsArray("attributes");
        if (attributeArr != null) {
            for (String attrId : attributeArr) {
                data.addObjectData((AttributeData)DataFactory.createData(attrId));
            }
        }
        
        // resist
        String resist = proto.getAsString("resist");
        if (resist != null) {
            data.addObjectData((ResistData)DataFactory.createData(resist));
        }
        
        // talents
        String[] talentArr = proto.getAsArray("talents");
        if (talentArr != null) {
            for (String talent : talentArr) {
                data.addObjectData((TalentData)DataFactory.createData(talent));
            }
        }
        
    }
    
    
    
}
