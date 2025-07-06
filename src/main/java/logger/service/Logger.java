package logger.service;

import logger.data.Datastore;
import logger.data.FileStore;
import logger.enums.Severity;
import logger.pojo.Log;
import logger.utils.DeepCopyUtil;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Logger {

    private Datastore datastore = new FileStore();

    private Set<Log> logTrackSet = new HashSet<>();

    private Queue<Set<Log>> logsProcessingQueue = new ArrayDeque<>();

    private Integer timeout;

    ExecutorService service = Executors.newFixedThreadPool(10);

    private static Logger logger = null;

    public static Logger getInstance(){
        if(logger == null){
            logger = new Logger();
        }
        return logger;
    }

    public void addLog(Log log){
        synchronized (Logger.class){
            Timestamp timestamp = new Timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
            Thread currentThread = Thread.currentThread();

            StackTraceElement[] elements = currentThread.getStackTrace();
            StringBuilder builder = new StringBuilder();

            int counter = 0;
            for(StackTraceElement element: elements){
                if(counter == 0){
                    builder.append(element.toString()).append("\n");
                    counter = 1;
                }else{
                    builder.append("\tat").append(element.toString()).append("\n");
                }
            }

            String stackTraceString = builder.toString();
            log.setStackTrace(stackTraceString);
            log.setTimestamp(timestamp);
            log.setThreadId(Long.toString(currentThread.getId()));
            log.setThreadName(currentThread.getName());
            log.setSeverity(log.getSeverity() == null ? Severity.LOW : log.getSeverity());
            put(logTrackSet, log);
        }
    }

    public void appendLog(){
        synchronized (Logger.class){
           try{
               Set<Log> logTrackSetCopied = DeepCopyUtil.deepcopy(logTrackSet);
               put(logsProcessingQueue, logTrackSetCopied); // deep copy error will come
               flushLogTrackSet();

               service.submit(() -> {
                   try{
                        datastore.appendLog(logsProcessingQueue.peek());
                        logsProcessingQueue.remove();
                    }catch (Exception ex){

                    }
               });

           }catch (Exception ex){

           }
       }
    }

    private void flushLogProcessingQueue(){
        logsProcessingQueue.clear(); // <L1, L2, L3>
    }

    private void flushLogTrackSet(){
        logTrackSet.clear(); // <L1, L2, L3>
    }

    private <T> void put(Collection<T> collection, T item){
        collection.add(item);
    }

    private void deleteLogs(){
        // if timeout occurs toh delete few logs from the file
    }



}
