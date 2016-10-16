/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.utils;

/**
 *
 * @author huliqing
 */
public interface AdController {
    /**
     * 显示指定类型的广告
     * @param type 
     */
    public void showAd(AdType... type);

    /**
     * 隐藏或销毁指定类型的广告，部分类型如banner可能在隐藏的时候需要销毁
     * @param types 
     */
    public void hideAd(AdType... types);
}
