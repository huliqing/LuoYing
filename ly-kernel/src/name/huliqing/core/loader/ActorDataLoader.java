/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader;

import java.util.ArrayList;
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
import name.huliqing.core.enums.Sex;
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
        
        // 角色属性
        String[] channels = proto.getAsArray("channels");
        if (channels != null) {
            for (String id : channels) {
                data.addObjectData((ChannelData)DataFactory.createData(id));
            }
        }
        
//        // items - weapon
//        String[] weaponIds = proto.getAsArray("weapon");
//        if (weaponIds != null) {
//            for (String wid : weaponIds) {
//                itemStore.addItem((ObjectData) DataFactory.createData(wid), 1);
//                SkinData weaponData = (SkinData) itemStore.getItem(wid);
//                weaponData.setUsing(true);
//            }
//        }
        
        // ==== 载入物品掉落设置
        String drop = proto.getAsString("drop");
        DropData dropData = null;
        if (drop != null) {
            dropData = DataFactory.createData(drop);
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
        
        data.setLevel(proto.getAsInteger("level", 1));
        
        // remove201602xx
//        // physics
//        boolean physicsEnabled = proto.getAsBoolean("physicsEnabled", true);
//        data.setPhysicsEnabled(physicsEnabled);
        
        // 武器插槽
        List<String> slots = proto.getAsList("slots");
        
        // 角色属性
        String[] attributeArr = proto.getAsArray("attributes");
        if (attributeArr != null) {
            for (String attrId : attributeArr) {
                data.addObjectData((AttributeData)DataFactory.createData(attrId));
            }
        }
        
        String lifeAttribute = proto.getAsString("lifeAttribute");
        String viewAttribute = proto.getAsString("viewAttribute");
        
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
        data.setLevelUpEl(proto.getAsString("levelUpEl"));
        data.setXpDropEl(proto.getAsString("xpDropEl"));
        data.setName(ResourceManager.getObjectName(data));
        data.setGroup(proto.getAsInteger("group", 0));
        data.setSex(Sex.identifyByName(proto.getAsString("sex", "2")));
        data.setRace(proto.getAsString("race"));
        data.setEssential(proto.getAsBoolean("essential", false));
//        data.setSkinBase(skinBases);
//        data.setItemStore(itemStore);
//        data.setSkillStore(skillStore);
        data.setDrop(dropData);
//        data.setLogics(logics);
        data.setSlots(slots);
//        data.setAttributes(attributes);
        data.setLifeAttribute(lifeAttribute);
        data.setViewAttribute(viewAttribute);
//        data.setResist(resist);
//        data.setTalents(talents);
        data.setTalentPoints(proto.getAsInteger("talentPoints", 0));
        data.setTalentPointsLevelEl(proto.getAsString("talentPointsLevelEl"));
        data.setTeam(proto.getAsInteger("team", 0));
        data.setLiving(proto.getAsBoolean("living", false));
        data.setFollowTarget(proto.getAsInteger("followTarget", -1));
        

        // 载入模块配置
        String[] moduleArr = proto.getAsArray("modules");
        if (moduleArr != null) {
            data.setModuleDatas(new ArrayList<ModuleData>(moduleArr.length));
            for (String mid : moduleArr) {
                data.getModuleDatas().add((ModuleData) DataFactory.createData(mid));
            }
        }
    }
    
}
