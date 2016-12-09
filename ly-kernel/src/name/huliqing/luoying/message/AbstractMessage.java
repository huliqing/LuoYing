/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.message;

/**
 * Message抽象基类
 * @author huliqing
 */
public class AbstractMessage implements Message {
    
    private final String message;
    private final int stateCode;
    
    public AbstractMessage(int stateCode, String message) {
        this.stateCode = stateCode;
        this.message = message;
    }

    @Override
    public int getStateCode() {
        return stateCode;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
}
