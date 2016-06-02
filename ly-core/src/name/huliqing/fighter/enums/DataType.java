/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.enums;

/**
 * 定义游戏中所有物品类型
 * @author huliqing
 */
public enum DataType {
    /** 基本物体 */
    item(10),
    
    /** 人物角色 */
    actor(11),
    
    // remove20151205,后续合并到Skin中
//    /** 武器类型 */
//    weapon(12),
    
    /** 
     * 动画动作
     */
    anim(13),
    
    /** 技能 */
    skill(14),
    
    /** 皮肤或装甲，服装等 */
    skin(15),
    
    /** 音效 */
    sound(16),
    
    /** 地面 */
    terrain(17),
    
    /** 天空 */
    sky(18),
  
    // remove20160101
//    /** AI配置 */
//    ai(19),
    
    /** 特效 */
    effect(20),
    
    /** 粒子发射器 */
    emitter(21),
    
    /** 位置:emitterShape */
    position(22),
    
    /** 角色逻辑 */
    logic(23),
    
    /** Logic相关的行为逻辑。 */
    action(24),
    
    /** 物品的使用处理 */
    handler(25),
    
    /** 物品掉落设置 */
    drop(26),
    
    /** 角色状态 */
    state(27),
    
    /** 用于角色的动画控制 */
    actorAnim(28),
    
    /** 技能、魔法作用检查器 */
    hitChecker(29),
    
    /** 子弹 */
    bullet(30),
    
    /** 抗性 */
    resist(31),
    
    /** 场景 */
    scene(32),
    
    /** 环境物体 */
    env(33),
    
    /** 游戏逻辑 */
    game(34),
    
    /** 骨骼通道的配置定义 */
    channel(35),
    
    /** 武器插槽 */
    slot(36),
    
    /** 角色的属性值定义设置 */
    attribute(37),
    
    /** EL表达式 */
    el(38),
    
    /** 天赋设置 */
    talent(39),
    
    /** 配置信息 */
    config(40),
    
    /** 魔法 */
    magic(41),
    
    /** 图形 */
    shape(42),
    
    /** 用户界面 */
    view(43),
    
    /** 对话面板 */
    chat(44),
    
    /** 任务 */
    task(45),
    
    ;
    private int value;
    
    private DataType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public static DataType identify(int value) {
       DataType[] ots = DataType.values();
       for (DataType ot : ots) {
           if (ot.getValue() == value) {
               return ot;
           }
       }
       throw new UnsupportedOperationException("Unknow object type value:" + value);
    }
    
    public static DataType identifyByName(String name) {
        DataType[] ots = DataType.values();
        for (DataType ot : ots) {
            if (ot.name().equalsIgnoreCase(name)) {
                return ot;
            }
        }
        throw new UnsupportedOperationException("Unknow object type name:" + name);
    }
    
}
