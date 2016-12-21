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
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.el.CustomEl;
import name.huliqing.luoying.object.el.ElFactory;
import name.huliqing.luoying.object.el.STBooleanEl;
import name.huliqing.luoying.object.el.STNumberEl;
import name.huliqing.luoying.object.el.LNumberEl;
import name.huliqing.luoying.object.el.SBooleanEl;
import name.huliqing.luoying.object.el.SkillHitNumberEl;

/**
 *
 * @author huliqing
 */
public class ElServiceImpl implements ElService {
    
    @Override
    public void inject() {
        // ignore
    }

    @Override
    public SBooleanEl createSBooleanEl(String idOrExpression) {
        if (ElFactory.isElExpression(idOrExpression)) {
            SBooleanEl el = Loader.load(IdConstants.SYS_EL_SBOOLEAN);
            el.setExpression(idOrExpression);
            return el;
        } else {
            return Loader.load(idOrExpression);
        }
    }
    
    @Override
    public STBooleanEl createSTBooleanEl(String idOrExpression) {
        if (ElFactory.isElExpression(idOrExpression)) {
            STBooleanEl el = Loader.load(IdConstants.SYS_EL_STBOOLEAN);
            el.setExpression(idOrExpression);
            return el;
        } else {
            return Loader.load(idOrExpression);
        }
    }

    @Override
    public STNumberEl createSTNumberEl(String idOrExpression) {
        if (ElFactory.isElExpression(idOrExpression)) {
            STNumberEl el = Loader.load(IdConstants.SYS_EL_STNUMBER);
            el.setExpression(idOrExpression);
            return el;
        } else {
            return Loader.load(idOrExpression);
        }
    }

    @Override
    public LNumberEl createLNumberEl(String idOrExpression) {
        if (ElFactory.isElExpression(idOrExpression)) {
            LNumberEl el = Loader.load(IdConstants.SYS_EL_LNUMBER);
            el.setExpression(idOrExpression);
            return el;
        } else {
            return Loader.load(idOrExpression);
        }
    }
    
    @Override
    public CustomEl createCustomEl(String idOrExpression) {
        if (ElFactory.isElExpression(idOrExpression)) {
            CustomEl el = Loader.load(IdConstants.SYS_EL_CUSTOM);
            el.setExpression(idOrExpression);
            return el;
        } else {
            return Loader.load(idOrExpression);
        }
    }
    
    @Override
    public SkillHitNumberEl createSkillHitNumberEl(String idOrExpression) {
        if (ElFactory.isElExpression(idOrExpression)) {
            SkillHitNumberEl el = Loader.load(IdConstants.SYS_EL_SKILLHITNUMBER);
            el.setExpression(idOrExpression);
            return el;
        } else {
            return Loader.load(idOrExpression);
        }
    }
}
