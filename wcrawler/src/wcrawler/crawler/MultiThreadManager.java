/**
 * Description: This is assignment project of Software Architecture and Design
 * Course The purpose of this project is to build a web crawler to get
 * information from some specific sites. In this case, crawler will get
 * information about drug from www.drugs.com
 */
/**
 * Created at: 10:20:35 AM Jul 15, 2013
 *
 * @author TanNhat Project: wcrawler
 */
package wcrawler.crawler;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.logging.Level;
import org.apache.log4j.Logger;

public class MultiThreadManager {

    private int getFreeThreadIndex() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private enum State {

        Ready, Pausing, Running
    };
    private static State state;
    private ExecutorService executor;
    private ArrayList<Runnable> taskToRun;
    private Logger _logger = Logger.getLogger(MultiThreadManager.class);

    public MultiThreadManager(int maxThread) {
        executor = Executors.newFixedThreadPool(maxThread);
        state = State.Ready;
        taskToRun = new ArrayList<>();
    }

    synchronized public void start() {
        state = State.Running;
        for (Runnable task : taskToRun) {
            executor.execute(task);
            _logger.info("Crawler " + task.hashCode() + " started.");
        }
        
        taskToRun.clear();

    }

    synchronized public void pause() {
        state = State.Pausing;
    }

    synchronized public void resume() {
        start();
    }

    synchronized public void stop() {        
        state = State.Ready;
        executor.shutdown();
        try {
            while (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            }
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(MultiThreadManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        taskToRun.clear();
    }

    synchronized public void addTask(Runnable task) {
        if (state == State.Running) {
            executor.execute(task);
            _logger.info("task " + task.hashCode() + " started.");
        } else {
            taskToRun.add(task);
            _logger.info("task " + task.hashCode() + " queued");
        }
    }

    synchronized public boolean isExecutorTerminated() {
        return executor.isTerminated();
    }
}
