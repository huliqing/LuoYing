///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.luoying.layer.service;
//
//import name.huliqing.luoying.xml.ObjectData;
//import name.huliqing.luoying.xml.DataFactory;
//
///**
// *
// * @author huliqing
// */
//public class ObjectServiceImpl implements ObjectService {
////    private static final Logger LOG = Logger.getLogger(ObjectServiceImpl.class.getName());
//    
//
//    @Override
//    public void inject() {}
//
//    @Override
//    public ObjectData createData(String id) {
//        ObjectData data = DataFactory.createData(id);
//        return data;
//    }
//
////    @Override
////    public void addData(Entity actor, String id, int count) {
////        actor.addObjectData(Loader.loadData(id), count);
////    }
////
////    @Override
////    public void removeData(Entity actor, String id, int count) {
////        
//////        Class<?> cc = DataFactory.getDataClassById(id);
//////        if (cc == null)
//////            return;
//////        
//////        if (ItemData.class.isAssignableFrom(cc)) {
////////            itemService.removeItem(actor, id, count);
//////            
//////            throw new UnsupportedOperationException("Could not addData, actor=" + actor + ", id" + id);
//////            
//////        } else if (SkinData.class.isAssignableFrom(cc)) {
//////            skinService.removeSkin(actor, id, count);            
//////        } else if (SkillData.class.isAssignableFrom(cc)) {
//////            skillService.removeSkill(actor, id); 
//////        } else {
//////            throw new UnsupportedOperationException();
//////        }
////    }
////
////    @Override
////    public void useData(Entity actor, ObjectData data) {
////        if (data == null)
////            return;
////        
////        throw new UnsupportedOperationException("unsupported yet!");
////    }
// 
//}
