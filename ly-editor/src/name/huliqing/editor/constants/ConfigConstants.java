/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.constants;

/**
 *
 * @author huliqing
 */
public class ConfigConstants {
    
    /**
     * 编辑器配置文件的路径
     */
    public final static String CONFIG_PATH = "data/config.ini";
    
    /**
     * 这个键值所有使用过的资源文件路径,全路径
     */
    public final static String KEY_ASSETS = "assets";
    
    /** 
     * 主资源文件夹,全路径, 这个键的配置指定了当前正在使用的资源文件夹的绝对路径位置，
     * 如："C:\xxx\project\assets\"
     */
    public final static String KEY_MAIN_ASSETS = "mainAssets";
    
    /** 
     * 默认的模板文件夹位置键, 这个键的配置指定了一个data目录下的文件夹，如："data/template/LuoYing-RPG"
     * 这个文件夹是落樱的默认数据配置文件，在切换资源夹的时候会把这个文件夹中的配置一同复制到目标资源文件夹下。
     */
    public final static String KEY_TEMPLATE = "template";
}
