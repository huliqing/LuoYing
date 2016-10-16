package name.huliqing.luoying.object.skin;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.object.skin;
//
//import com.jme3.scene.SceneGraphVisitor;
//import com.jme3.scene.Spatial;
//import java.util.ArrayList;
//import java.util.List;
//import name.huliqing.core.data.ObjectData;
//import name.huliqing.core.data.SkinData;
//
///**
// *
// * @author huliqing
// */
//public class SkinHelper {
//    
//    /**
//     * 从角色模型中获取与指定的conflictSkinType冲突的所有skin节点的SkinData.
//     * @param actorModel
//     * @param conflictSkinType 二制位表示的skinTypes
//     * @return 
//     */
//    public static List<SkinData> findConflictSkins(Spatial actorModel, final int conflictSkinType) {
//        final List<SkinData> results = new ArrayList<SkinData>(2); 
//        SceneGraphVisitor sgv = new SceneGraphVisitor() {
//            @Override
//            public void visit(Spatial spatial) {
//                ObjectData pd = spatial.getUserData(ObjectData.USER_DATA);
//                if (pd != null && (pd instanceof SkinData)) {
//                    SkinData sd = (SkinData) pd;
//                    if (((sd.getType() | sd.getConflictType()) & conflictSkinType) != 0) {
//                        if (!results.contains(sd)) {
//                            results.add(sd);
//                        }
//                    }
//                }
//            }
//        };
//        // 根据actorModel的结构，在这里使用广度优先查询的速度会快一些。
//        actorModel.breadthFirstTraversal(sgv);
//        return results;
//    }
//    
//}
