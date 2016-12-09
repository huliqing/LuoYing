/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.message;

/**
 * 普通类态的消息
 * @author huliqing
 */
public class SimpleMessage extends AbstractMessage {

    public SimpleMessage(int stateCode, String message) {
        super(stateCode, message);
    }
}
