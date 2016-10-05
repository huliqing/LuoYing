package name.huliqing.ly.object.skill;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.object.skill;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 技能标记
// * @author huliqing
// */
//public class SkillTagFactory {
//    
//    /**
//     * 技能标记列表,这个列表是有序的,并且在载入后不在运行时改变
//     */
//    private final static List<SkillTag> TAGS = new ArrayList<SkillTag>();
//    
//    static {
//        TAGS.add(new SkillTag(0, "wait"));
//        TAGS.add(new SkillTag(1, "sit"));
//        TAGS.add(new SkillTag(2, "walk"));
//        TAGS.add(new SkillTag(3, "run"));
//        TAGS.add(new SkillTag(4, "jump"));
//        TAGS.add(new SkillTag(5, "dance"));
//        TAGS.add(new SkillTag(6, "idle"));
//        TAGS.add(new SkillTag(7, "hurt"));
//        TAGS.add(new SkillTag(8, "dead"));
//        TAGS.add(new SkillTag(9, "reset"));
//        TAGS.add(new SkillTag(10, "duck"));
//        TAGS.add(new SkillTag(11, "defend"));
//        TAGS.add(new SkillTag(12, "skin"));
//        TAGS.add(new SkillTag(13, "attack"));
//        TAGS.add(new SkillTag(14, "trick"));
//        TAGS.add(new SkillTag(15, "magic"));
//        TAGS.add(new SkillTag(16, "common"));
//        TAGS.add(new SkillTag(17, "fight"));
//    }
//    
//    /**
//     * 注册、登记一个技能类型标记
//     * @param skillTag 
//     */
//    public synchronized static void registerSkillTag(String skillTag) {
//        // 已存在
//        if (getSkillTagInner(skillTag) != null) {
//            return;
//        }
//        TAGS.add(new SkillTag(TAGS.size(), skillTag));
//    }
//    
//    /**
//     * 通过技能标记获取SkillTag
//     * @param skillTag
//     * @return 
//     */
//    public static SkillTag getSkillTag(String skillTag) {
//        SkillTag st = getSkillTagInner(skillTag);
//        if (st != null) {
//            return st;
//        }
//        return null;
//    }
//    
////    public static int index(String skillTag) {
////        SkillTag st = getSkillTagInner(skillTag);
////        if (st != null) {
////            return st.index();
////        }
////        return -1;
////    }
//        
//    /**
//     * 转化所有tag为一个整型，所有整型中每个二进制(1)位代表一个tag类型, 如果tags为null则返回0.
//     * @param tags
//     * @return 
//     */
//    public static long convert(String... tags) {
//        long result = 0;
//        if (tags != null) {
//            SkillTag st;
//            for (String tag : tags) {
//                st = getSkillTagInner(tag);
//                if (st == null) {
//                    continue;
//                }
//                result |= st.indexAsBinary();
//            }            
//        }
//        return result;
//    }
//    
//    private static SkillTag getSkillTagInner(String skillTag) {
//        for (SkillTag st : TAGS) {
//            if (st.name().equals(skillTag)) {
//                return st;
//            }
//        }
//        return null;
//    }
//    
//    /**
//     * 查询当前已经注册的tag的数量
//     * @return 
//     */
//    public final static int size() {
//        return TAGS.size();
//    }
//    
//    /**
//     * 清理所有技能标记
//     */
//    public synchronized static void clearTags() {
//        TAGS.clear();
//    }
// 
//    /**
//     * 打印当前所有的标记，调试用。
//     * @return 
//     */
//    public static String toStringTags() {
//        return TAGS.toString();
//    }
//    
//    /**
//     * 打印出所有技能tag类型。
//     * @param tags
//     * @return 
//     */
//    public static String toStringTags(long tags) {
//        List<String> temp = new ArrayList<String>();
//        for (int i = 0; i < size(); i++) {
//            if ((tags & 1 << i) != 0) {
//                temp.add(TAGS.get(i).name());
//            }
//        }
//        return temp.toString();
//    }
//    
//    public static void main(String[] args) {
////        List<String> test  = new ArrayList<String>();
////        test.add("wait");
////        test.add("walk");
////        test.add("jump");
////        test.add("common");
////        long tags = convert(test);
////        System.out.println(tags);
////        System.out.println(Long.toBinaryString(tags));
////        System.out.println(toStringTags(tags));
//        
//        System.out.println(Long.toBinaryString(1L << 40));
//    }
//}
