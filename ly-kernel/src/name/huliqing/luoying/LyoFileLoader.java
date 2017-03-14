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
package name.huliqing.luoying;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import com.jme3.export.binary.BinaryImporter;
import java.io.IOException;
import name.huliqing.luoying.utils.FileUtils;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 载入Lyo物体文件作为ObjectData
 * @author huliqing
 */
public class LyoFileLoader implements AssetLoader {

    @Override
    public ObjectData load(AssetInfo assetInfo) throws IOException {
        if (assetInfo == null) {
            return null;
        }
        byte[] datas = FileUtils.readFile(assetInfo.openStream());
        return (ObjectData) BinaryImporter.getInstance().load(datas);
    }
    
}
