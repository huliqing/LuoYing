/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import name.huliqing.core.data.ActorData;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.data.DropData;
import name.huliqing.core.data.ActorLogicData;
import name.huliqing.core.data.ChannelData;
import name.huliqing.core.data.ChatData;
import name.huliqing.core.xml.Proto;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.data.ResistData;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.data.TalentData;
import name.huliqing.core.data.module.ModuleData;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.xml.DataLoader;

/**
 *
 * @author huliqing
 */
public class ActorDataLoader implements DataLoader<ActorData> {

    @Override
    public void load(Proto proto, ActorData data) {
        // ==== 2.items 
        String[] itemsTemp = proto.getAsArray("items");
        if (itemsTemp != null && itemsTemp.length > 0) {
            for (String item : itemsTemp) {
                if (item == null || item.trim().equals("")) {
                    continue;
                } 
                String[] itemArr = item.split("\\|");
                String itemId = itemArr[0];
                int itemTotal = 1;
                if (itemArr.length >= 2) {
                    itemTotal = Integer.parseInt(itemArr[1]);
                }
                ObjectData itemData = DataFactory.createData(itemId);
                itemData.setTotal(itemTotal);
                data.addObjectData(itemData);
            }
        }
        
        // skinBase 基本皮肤,
        // 注1：可能部分角色没有基本皮肤，如不可换装备的角色类型
        // 注2: 基本皮肤不会存放在itemStore中，而skinOutfit会存放在itemStore
        String[] skinBases = proto.getAsArray("skinBase");
        if (skinBases != null) {
            for (String sbt : skinBases) {
                SkinData sdb = DataFactory.createData(sbt);
                sdb.setBaseSkin(true);
                sdb.setUsing(false);
                data.addObjectData(sdb);
            }            
        }
        
        // 包裹上的装备，没穿在身上
        String[] skinsArr = proto.getAsArray("skins");
        if (skinsArr != null && skinsArr.length > 0) {
            for (String skinStr : skinsArr) {
                String[] skinArr = skinStr.split("\\|");
                SkinData skinData = DataFactory.createData(skinArr[0]);
                skinData.setTotal(skinArr.length > 1 ? Integer.parseInt(skinArr[1]) : 1);
                skinData.setBaseSkin(false);
                skinData.setUsing(false);
                data.addObjectData(skinData);
            }
        }
        
        // skinOutfit
        String[] skinOutfit = proto.getAsArray("skinOutfit");
        if (skinOutfit != null) {
            for (String skinId : skinOutfit) {
                SkinData sdb = DataFactory.createData(skinId);
                sdb.setBaseSkin(false);
                sdb.setUsing(true);
                data.addObjectData(sdb);
            }
        }
        
        // weapon - 拿在手上的武器
        String[] weaponIds = proto.getAsArray("skinWeapon");
        if (weaponIds != null) {
            for (String wid : weaponIds) {
                SkinData weaponData = (SkinData) DataFactory.createData(wid);
                weaponData.setBaseSkin(false);
                weaponData.setUsing(true);
                data.addObjectData(weaponData);
            }
        }
        
        // 角色动画通道
        String[] channels = proto.getAsArray("channels");
        if (channels != null) {
            for (String id : channels) {
                data.addObjectData((ChannelData)DataFactory.createData(id));
            }
        }
        
        // 物品掉落设置
        String[] drops = proto.getAsArray("drops");
        if (drops != null && drops.length > 0) {
            for (String id : drops) {
                data.addObjectData((DropData)DataFactory.createData(id));
            }
        }
        
        String chat = proto.getAsString("chat");
        if (chat != null) {
            data.addObjectData((ChatData) DataFactory.createData(chat));
        }
        
        // ==== 载入技能
        String[] skillIds = proto.getAsArray("skills");
        if (skillIds != null && skillIds.length > 0) {
            for (String skillId : skillIds) {
                data.addObjectData((SkillData)DataFactory.createData(skillId));
            }
        }
        
        // ==== 载入逻辑
        String[] logics = proto.getAsArray("logics");
        if (logics != null && logics.length > 0) {
            for (String logicId : logics) {
                data.addObjectData((ActorLogicData) DataFactory.createData(logicId));
            }
        }
        
        // remove20160828
//        data.setLevel(proto.getAsInteger("level", 1));
        
        // 武器插槽
        List<String> slots = proto.getAsStringList("slots");
        
        // 角色属性
        String[] attributeArr = proto.getAsArray("attributes");
        if (attributeArr != null) {
            for (String attrId : attributeArr) {
                data.addObjectData((AttributeData)DataFactory.createData(attrId));
            }
        }
        
        String lifeAttribute = proto.getAsString("lifeAttribute");
        String viewAttribute = proto.getAsString("viewAttribute");
        
        // remove20160828
//        Map<String, AttributeData> temp = new LinkedHashMap<String, AttributeData>();
//        if (lifeAttribute != null) {
//            AttributeData ad = DataFactory.createData(lifeAttribute);
//            temp.put(lifeAttribute, ad);
//        }
//        if (viewAttribute != null) {
//            AttributeData ad = DataFactory.createData(viewAttribute);
//            temp.put(viewAttribute, ad);
//        }
//        Map<String, AttributeData> attributes = null;
//        if (temp.size() > 0) {
//            attributes = new LinkedHashMap<String, AttributeData>(temp.size());
//            attributes.putAll(temp);
//        }
        
        // Resist
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
        
        // 等级及经验值掉落设置
//        data.setLevelUpEl(proto.getAsString("levelUpEl"));
//        data.setXpDropEl(proto.getAsString("xpDropEl"));
        data.setName(ResourceManager.getObjectName(data));
//        data.setGroup(proto.getAsInteger("group", 0));
//        data.setSex(Sex.identifyByName(proto.getAsString("sex", "2")));
//        data.setRace(proto.getAsString("race"));
//        data.setEssential(proto.getAsBoolean("essential", false));
        
        // remove20160830
//        data.setDrop(dropData);

//        data.setSlots(slots);
//        data.setLifeAttribute(lifeAttribute);
//        data.setViewAttribute(viewAttribute);

            // remove20160828
//        data.setTalentPoints(proto.getAsInteger("talentPoints", 0));
//        data.setTalentPointsLevelEl(proto.getAsString("talentPointsLevelEl"));
//        data.setTeam(proto.getAsInteger("team", 0));

//        data.setLiving(proto.getAsBoolean("living", false));
//        data.setFollowTarget(proto.getAsInteger("followTarget", -1));
        
        // 载入模块配置,并根据ModuleOrder进行排序
        String[] moduleArr = proto.getAsArray("modules");
        if (moduleArr != null) {
            data.setModuleDatas(new ArrayList<ModuleData>(moduleArr.length));
            for (String mid : moduleArr) {
                data.getModuleDatas().add((ModuleData) DataFactory.createData(mid));
            }
            Collections.sort(data.getModuleDatas(), new Comparator<ModuleData>() {
                @Override
                public int compare(ModuleData o1, ModuleData o2) {
                    return o1.getModuleOrder() - o2.getModuleOrder();
                }
            });
        }
        
    }
    
    
    
}
