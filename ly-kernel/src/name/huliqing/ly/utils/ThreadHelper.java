/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 线程,线程池管理，所有新建的线程都由该类管理，不要在代码中其它地方手动
 * 创建纯程，否则很难管理。
 * /////////////////////
 * 关于Future在JME中的典型使用一定要注意，否则可能在android下出现一些问题,
 * 类似如线程挂起、卡住、始终没有返回的情况，正确代码参考以下示例：

 * 
 * @author huliqing
 */
public class ThreadHelper {
    
    // 线程池的大小一般为CPU核心数的2倍比较合适， 这里暂定为4.
    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(8);
    
    /**
     * 创建一个需要在新线程中运行的任务。
     * <br>
     * if (future != null) {<br>
     *      if (future.isDone()) {<br>
     *          try {<br>
     *              Object result = future.get();<br>
     *              // ..doLogic<br>
     *              future = null;<br>
     *          } catch (Exception ex) {<br>
     *              // 注意点1：当发生异常时，这里要偿试取消<br>
     *              if (future != null) {<br>
     *                  future.cancel(true);<br>
     *              }<br>
     *              future = null;<br>
     *          }<br>
     *          注意点2：这里要判断是否已经取消<br>
     *      } else if (future.isCancelled()){<br>
     *          future = null;<br>
     *      }<br>
     * }<br>
     *<br>
     * @param callable
     * @return 
     */
    public static <T> Future<T> submit(Callable<T> callable) {
        return executor.submit(callable);
    }
    
    /**
     * 在系统退出时清理线程池占用的资源。
     */
    public static void cleanup() {
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }
    }
}
