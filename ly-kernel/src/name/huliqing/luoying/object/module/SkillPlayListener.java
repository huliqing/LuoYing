/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.module;

import name.huliqing.luoying.object.skill.Skill;

/**
 * 技能侦听器，用于侦听技能的执行
 * @author huliqing
 */
public interface SkillPlayListener {
    
    /**
     * 如果角色可以执行这个技能则该方法返回true,否则返回false.这个方法主要
     * 用于支持一些状态效果，比如一些状态效果可能不允许角色去执行某些特定的
     * 技能，则这时可以返回false,以阻止技能的执行。默认情况下应该始终返回true.
     * @param skill 指定要执行的技能
     * @return 
     */
    boolean onSkillHookCheck(Skill skill);
    
    /**
     * 当角色开始执行某个技能时该方法被调用，这个调用发生在技能
     * 开始之后,也即已经调用了技能的start方法和init方法之后。
     * @param skill 执行的技能
     */
    void onSkillStart(Skill skill);
 
    /**
     * 当技能结束时调用,这个方法发生在技能完成执行之后，即表示技能已经cleanup
     * 完毕，目标技能已经完全退出的情况。<br>
     * 注：不管技能是否正常结束或被其它技能打断而结束，该方法都会在结束后被调用。
     * @param skill 
     */
    void onSkillEnd(Skill skill);
}
