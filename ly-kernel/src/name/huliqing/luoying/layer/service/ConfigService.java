/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.Inject;

/**
 *
 * @author huliqing
 */
public interface ConfigService extends Inject {

    /**
     * 获取客户端唯一ID，这个ID用来识别不同的客户端，每台安装了游戏的机器都会自动生成一个唯一的全局ID来识别这台机器。
     * 这个ID对于不同的客户端必须保证不相同。该ID允许清除，在重新获取的时候可以重新生成，但不保证多次生成的ID会相同<br >
     * 注：<br >
     * 1.但不保证同一台机器安装多个游戏的时候该ID仍然唯一。<br >
     * 2.不保证当游戏重装的时候该ID仍然与前一次安装时生成的ID相同。
     * @return 
     */
    String getClientId();
    
    /**
     * 获取存档目录
     * @return 
     */
    String getSaveDir();
    
    /**
     * 设置存档目录
     * @param saveDir 
     */
    void setSaveDir(String saveDir);
    
    /**
     * 获取日志记录的目录
     * @return 
     */
    String getLogDir();
    
    /**
     * 设置日志记录目录
     * @param logDir 
     */
    void setLogDir(String logDir);
    
    /**
     * 获取游戏全局声音的开关状态
     * @return 
     */
    boolean isSoundEnabled();
    
    /**
     * 设置是否开关全局声音。
     * @param enabled 
     */
    void setSoundEnabled(boolean enabled);
    
    /**
     * 获取声效音量大小
     * @return 返回 [0.0, 1.0]， 1.0表示声音最大
     */
    float getSoundVolume();
    
    /**
     * 设置音量，取值[0.0, 1.0], 0表示声音最小，1.0表示声音最大,但是并不关闭声音
     * @param volume 
     */
    void setSoundVolume(float volume);

}
