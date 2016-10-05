/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.android;

import android.content.Context;
import java.io.File;
import name.huliqing.fighter.Global;
import name.huliqing.fighter.game.service.SaveServiceImpl;

/**
 * Android平台的存档管理器，每一个key将被保存在一个单独的文件中。
 * 如果key相同则将被覆盖
 * @author huliqing
 */
public class AndroidSaveServiceImpl extends SaveServiceImpl {

    @Override
    protected File getSaveDir() {
        File saveDir = Global.getContext().getDir("save", Context.MODE_PRIVATE);
        return saveDir;
    }
    
}
