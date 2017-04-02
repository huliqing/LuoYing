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
package name.huliqing.editor;

import com.jme3.system.AppSettings;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import name.huliqing.editor.manager.UIManager;
import name.huliqing.editor.ui.Quit;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.utils.FileUtils;

/**
 * @author huliqing
 */
public class Starter {
    
    public static void main(String[] args) {
        new Starter().start();
    }
    
    private void start() {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1280, 720);
        settings.setFrameRate(60);
        settings.setSamples(4);
        
        Jfx.create(Editor.class.getName(), settings);
        Jfx.getMainFrame().setLocationRelativeTo(null);
        Jfx.getMainFrame().setVisible(true);
        Jfx.getMainFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Jfx.getMainFrame().setIconImages(createIcons());
        Jfx.getMainFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Quit.doQuit(); // 由Quicker负责退出
            }
        });
        
        // 开始构建JFX界面
        // 这里要等待JmeApp执行完simpleInit方法之后再开始执行Jfx UI,因为UI要依赖于JME的初始化
        Jfx.runOnJme(() -> {
            Jfx.runOnJfx(() -> {
                UIManager.initializeLayout(Jfx.getJfxRoot());
            });
        });
        
    }
    
    private static List<BufferedImage> createIcons() {
        try {
            List<BufferedImage> imgs = new ArrayList();
            imgs.add(ImageIO.read(FileUtils.readFile("/resources/ly16.png")));
            imgs.add(ImageIO.read(FileUtils.readFile("/resources/ly32.png")));
            imgs.add(ImageIO.read(FileUtils.readFile("/resources/ly128.png")));
            return imgs;
        } catch (IOException e) {
            Logger.getLogger(Starter.class.getName()).log(Level.WARNING, null, e);
        }
        return null;
    }
}
