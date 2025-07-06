package logger;


import logger.pojo.Log;
import logger.service.Logger;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Logger logger = Logger.getInstance();

        logger.addLog(new Log("Starting my main function"));
        logger.addLog(new Log("in mid"));
        logger.appendLog();
    }
}
