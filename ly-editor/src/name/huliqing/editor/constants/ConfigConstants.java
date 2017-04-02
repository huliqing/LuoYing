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
