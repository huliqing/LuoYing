/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.state.lan;

import com.jme3.math.ColorRGBA;
import name.huliqing.ly.constants.InterfaceConstants;
import name.huliqing.ly.data.GameData;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.ly.ui.Icon;
import name.huliqing.ly.ui.LinearLayout;
import name.huliqing.ly.ui.Text;
import name.huliqing.ly.ui.UIFactory;

/**
 * 游戏简介界面
 * @author huliqing
 */
public class LanGameInfoView extends LinearLayout{
    // 标题： 游戏名
    private Text title;
    // 分隔线
    private Icon separate;
    // 游戏简介
    private Text content;
    // 图片描述
    private Icon image;
    // 图片的宽高比率, rate=宽/高, 比率越大留给“游戏简介”的区域就越大，如果“游戏
    // 简介”的描述比较大，则应该加大比率，即宽度比高度要大一些。一般至少比率要
    // 大于或等于1.5。在1.5比率情况下(除标题和分隔线)大概有1/3的空间显示描述，2/3的空间显示图片。
    // 这个比例比较好看。
    private float imageRate;    
    
    public LanGameInfoView() {
        super();
        title = new Text("");
        separate = new Icon(InterfaceConstants.UI_LINE_H2);
        separate.setBackgroundColor(new ColorRGBA(1,1,1,0.75f), true);
        separate.setHeight(2);
        content = new Text("");
        content.setFontColor( UIFactory.getUIConfig().getDesColor());
        content.setFontSize(UIFactory.getUIConfig().getDesSize());
        image = new Icon(UIFactory.getUIConfig().getMissIcon());
        image.setVisible(false);
        
        addView(title);
        addView(separate);
        addView(content);
        addView(image);
        setPadding(10, 10, 10, 10);
    }

    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        float cw = getContentWidth();
        float ch = getContentHeight();
        
        title.setWidth(cw);
        separate.setWidth(width);
        separate.setHeight(2);
        separate.setMargin(0, 5, 0, 5);
        content.setWidth(cw);
        
        // 计算出实际图片可用的宽度和高度，图片不能盖住描述内容
        float separateHeight = 12;
        float imageWidth = cw;
        float imageHeight = cw / imageRate;
        float imageAvailabeHeight = ch - title.getHeight() - separateHeight - content.getHeight();
        float imageMarginLeft = 0;
        if (imageHeight > imageAvailabeHeight) {
            float scaleRate = imageAvailabeHeight / imageHeight;
            imageWidth *= scaleRate;
            imageHeight = imageAvailabeHeight;
            imageMarginLeft = (1 - scaleRate) * 0.5f * cw;
        }
        image.setWidth(imageWidth);
        image.setHeight(imageHeight); 
        image.setMargin(imageMarginLeft, 0, 0, 0); // 居中
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        // 更新“描述”内容的实际宽度和高度
        content.setWidth(getContentWidth());
        content.updateView();
        content.resize();
    }
    
    /**
     * 显示游戏信息
     * @param gameData 
     */
    public void setGameData(GameData gameData) {
        title.setText(ResourceManager.getObjectName(gameData));
        String des = ResourceManager.getObjectDes(gameData.getId());
        content.setText(des != null ? des : "No description");
        String gameIcon = gameData.getIcon();
        if (gameIcon != null) {
            image.setWidth(0);
            image.setHeight(0);
            image.setImage(gameIcon);
            image.setVisible(true);
            imageRate = image.getWidth() / image.getHeight();
        } else {
            image.setVisible(false);
        }
        setNeedUpdate();
    }
}
