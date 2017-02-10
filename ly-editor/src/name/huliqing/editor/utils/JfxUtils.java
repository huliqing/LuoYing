/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Window;

/**
 *
 * @author huliqing
 */
public class JfxUtils {
    
    public final static ImageView createImage(String path) {
        Image image = new Image(JfxUtils.class.getResourceAsStream("/" + path));
        ImageView imageView = new ImageView(image);
        return imageView;
    }
    public final static ImageView createImage(String path, float width, float height) {
        ImageView imageView = createImage(path);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }
    
}
