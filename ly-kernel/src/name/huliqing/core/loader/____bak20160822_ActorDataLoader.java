///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.loader;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import name.huliqing.core.data.ActorData;
//import name.huliqing.core.data.AttributeData;
//import name.huliqing.core.data.module.ModuleData;
//import name.huliqing.core.data.DropData;
//import name.huliqing.core.xml.Proto;
//import name.huliqing.core.data.ObjectData;
//import name.huliqing.core.data.ResistData;
//import name.huliqing.core.enums.Sex;
//import name.huliqing.core.manager.ResourceManager;
//import name.huliqing.core.xml.DataFactory;
//import name.huliqing.core.xml.DataLoader;
//
///**
// *
// * @author huliqing
// */
//public class ____bak20160822_ActorDataLoader implements DataLoader<ActorData> {
//
//    @Override
//    public void load(Proto proto, ActorData data) {
//        
//        // ==== 载入物品掉落设置
//        String drop = proto.getAsString("drop");
//        DropData dropData = null;
//        if (drop != null) {
//            dropData = DataFactory.createData(drop);
//        }
//        
//        data.setLevel(proto.getAsInteger("level", 1));
//        
//        // 武器插槽
//        List<String> slots = proto.getAsList("slots");
//        
//        // 角色属性
//        String[] attributeArr = proto.getAsArray("attributes");
//        String lifeAttribute = proto.getAsString("lifeAttribute");
//        String viewAttribute = proto.getAsString("viewAttribute");
//        Map<String, AttributeData> temp = new LinkedHashMap<String, AttributeData>();
//        if (attributeArr != null) {
//            for (String attrId : attributeArr) {
//                AttributeData attData = DataFactory.createData(attrId);
//                temp.put(attData.getId(), attData);
//            }
//        }
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
//        
//        // Resist
//        ResistData resistData = null;
//        String tempResist = proto.getAsString("resist");
//        if (tempResist != null) {
//            resistData = DataFactory.createData(tempResist);
//        }
//        
//        // 等级及经验值掉落设置
//        data.setLevelUpEl(proto.getAsString("levelUpEl"));
//        data.setXpDropEl(proto.getAsString("xpDropEl"));
//        data.setName(ResourceManager.getObjectName(data));
//        data.setGroup(proto.getAsInteger("group", 0));
//        data.setSex(Sex.identifyByName(proto.getAsString("sex", "2")));
//        data.setRace(proto.getAsString("race"));
//        data.setEssential(proto.getAsBoolean("essential", false));
//        data.setDrop(dropData);
//        data.setSlots(slots);
//        data.setLifeAttribute(lifeAttribute);
//        data.setViewAttribute(viewAttribute);
//        data.setTalentPoints(proto.getAsInteger("talentPoints", 0));
//        data.setTalentPointsLevelEl(proto.getAsString("talentPointsLevelEl"));
//        data.setTeam(proto.getAsInteger("team", 0));
//        data.setLiving(proto.getAsBoolean("living", false));
//        data.setFollowTarget(proto.getAsInteger("followTarget", -1));
//
//        // ---------------------------------------------------------------------- add 20160820
//
//        String[] moduleArr = proto.getAsArray("modules");
//        if (moduleArr != null) {
//            data.setModuleDatas(new ArrayList<ModuleData>(moduleArr.length));
//            for (String mid : moduleArr) {
//                data.getModuleDatas().add((ModuleData) DataFactory.createData(mid));
//            }
//        }
//        
//        // 格式示例：datas="itemMapWorld,item000|10,itemGold|10,itemTowerSnow|100,itemScrollLife|10"
//        String[] dataArr = proto.getAsArray("datas");
//        if (dataArr != null && dataArr.length > 0) {
//            data.setObjectDatas(new ArrayList<ObjectData>(dataArr.length));
//            for (String dataStr : dataArr) {
//                if (dataStr == null || dataStr.trim().equals("")) {
//                    continue;
//                }
//                String[] bArr = dataStr.split("\\|");
//                ObjectData objectData = DataFactory.createData(bArr[0]);
//                objectData.setTotal(bArr.length >= 2 ? Integer.parseInt(bArr[1]) : 1);
//                data.getObjectDatas().add(objectData);
//            }
//        }
//        
//    }
//    
//}
