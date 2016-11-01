///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.luoying.object.hitchecker;
//
//import name.huliqing.luoying.data.HitCheckerData;
//import name.huliqing.luoying.xml.DataProcessor;
//import name.huliqing.luoying.object.entity.Entity;
//
///**
// * HitChecker主要用来检查一个角色source是否可以与另一个角色target进行交互,
// * 如果可以进行交互则返回true,否则返回false. 示如：
// * 1.当作用于技能(Skill)时可以用来判断source的技能是否可以作用于另一个角色。
// * 2.当作用于对话(Chat)时可用来判断source的某些对话(Chat)是否对目标(target)可见。
// * ...
// * @author huliqing
// * @param <T>
// */
//public interface HitChecker<T extends HitCheckerData> extends DataProcessor<T> {
//    
//    /**
//     * @param source
//     * @param target
//     * @return 
//     */
//    boolean canHit(Entity source, Entity target);
//}
