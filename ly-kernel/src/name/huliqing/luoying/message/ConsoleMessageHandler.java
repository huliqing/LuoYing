/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.message;

/**
 * 控制台消息输出，用于简单的将游戏消息输出到控制台。
 * @author huliqing
 */
public class ConsoleMessageHandler implements MessageHandler {

    private static ConsoleMessageHandler instance;
    
    private ConsoleMessageHandler() {}
    
    public static synchronized ConsoleMessageHandler getInstance() {
        if (instance == null) {
            instance = new ConsoleMessageHandler();
        }
        return instance;
    }
    
    @Override
    public void handle(Message message) {
        
        if (message instanceof EntityDataUseMessage) {
            EntityDataUseMessage mess = (EntityDataUseMessage) message;
            System.out.println(getClass().getSimpleName() + ":" + mess.getClass().getSimpleName() + "[stateCode=" + mess.getStateCode() + "]"
                    + mess.getEntity().getData().getId() + " use data " + mess.getObjectData().getId());
        } else {
            System.out.println(getClass().getSimpleName() + ":" + message.getClass().getSimpleName() + "[stateCode=" + message.getStateCode() + "]" 
                    + message.getMessage());
        }
        
    }
    
}
