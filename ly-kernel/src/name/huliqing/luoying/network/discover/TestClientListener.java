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
package name.huliqing.luoying.network.discover;

import java.net.DatagramPacket;

/**
 *
 * @author huliqing
 */
public class TestClientListener implements UDPListener {

//    @Override
//    public AbstractMess receive(String code, String content) {
////        System.out.println("客户端收到数据包：" + new String(packet.getData()));
//        int c = Integer.parseInt(code);
//        if (c == Code.CODE_SC_STARTED) {
//            // do something
//            System.out.println("从服务端收到消息：code=" + c + ",content=" + content);
//        }
//        return null;
//    }

    @Override
    public void receive(Object object, UDPDiscover discover, DatagramPacket packet) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
