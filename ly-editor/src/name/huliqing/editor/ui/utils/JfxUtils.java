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
package name.huliqing.editor.ui.utils;

import java.io.InputStream;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import name.huliqing.editor.constants.AssetConstants;

/**
 *
 * @author huliqing
 */
public class JfxUtils {

    public final static ImageView createImage(InputStream is) {
        Image image;
        try {
            image = new Image(is);
        } catch (Exception e) {
            image = new Image(AssetConstants.TEXTURES_MISS);
        }
        return new ImageView(image);
    }
    
    public final static ImageView createImage(String fileInClassPath) {
        String truePath = fileInClassPath.startsWith("/") ? fileInClassPath : "/" + fileInClassPath;
        return createImage(JfxUtils.class.getResourceAsStream(truePath));
    }
    
    /**
     * 创建指定大小的图片组件
     * @param path
     * @param width
     * @param height
     * @return 
     */
    public final static ImageView createImage(String path, float width, float height) {
        ImageView imageView = createImage(path);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }
    
    /**
     * 创建图片14 * 14
     * @param path
     * @return 
     */
    public final static ImageView createIcon(String path) {
        ImageView imageView = createImage(path);
        imageView.setFitWidth(14);
        imageView.setFitHeight(14);
        return imageView;
    }
    
    public final static Point2D getScreenPosition(Node node) {
        Point2D txtCoords = node.localToScene(0.0, 0.0);
        double x = txtCoords .getX() + node.getScene().getX() + node.getScene().getWindow().getX();
        double y = txtCoords .getY() + node.getScene().getY() + node.getScene().getWindow().getY();
        return new Point2D(x, y);
    }
}
