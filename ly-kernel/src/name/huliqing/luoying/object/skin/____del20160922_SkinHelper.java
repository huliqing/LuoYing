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
