/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.utils;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author huliqing
 */
public class JfxUtils {
    
    public final static ImageView createImage(String path) {
        String truePath = path.startsWith("/") ? path : "/" + path;
        Image image = new Image(JfxUtils.class.getResourceAsStream(truePath));
        ImageView imageView = new ImageView(image);
        return imageView;
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
