/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.serializer;

import com.jme3.network.serializing.Serializer;
import com.jme3.scene.UserData;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author huliqing
 */
public class UserDataSerializer extends Serializer{

    @Override
    public UserData readObject(ByteBuffer data, Class c) throws IOException {
        byte type = data.get();
        Object value = Serializer.readClassAndObject(data);
        return new UserData(type, value);
    }
    
    @Override
    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        UserData ud = (UserData) object;
        Object value = ud.getValue();
        byte type = UserData.getObjectType(value);
        buffer.put(type);
        Serializer.writeClassAndObject(buffer, value);
    }
    
}
