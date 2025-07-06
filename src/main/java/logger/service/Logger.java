package logger.service;

import logger.data.Datastore;
import logger.data.FileStore;
import logger.pojo.Log;
import utils.DeepCopyUtil;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Logger {

    private Datastore fileStore = new FileStore();

    private Set<Log> logTrackSet = new HashSet<>();

    private Queue<Set<Log>> logsProcessingQueue = new ArrayDeque<>();

    private Integer timeout;

    private static Logger logger = null;

    ExecutorService service = Executors.newFixedThreadPool(50);

    public static Logger getInstance(){
        if(logger == null){
            logger = new Logger();
        }
        return logger;
    }

    public void addLog(Log log){
        log.setTimestamp(new Timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)));

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("Thread Stack Trace:\n");
        int elementsToSkip = 2;
        for (int i = elementsToSkip; i < stackTrace.length; i++) {
            sb.append("\tat ").append(stackTrace[i].toString()).append("\n");
        }
        String formattedStackTrace = sb.toString();
        log.setStackTrace(formattedStackTrace);

        logTrackSet.add(log);
        put(logTrackSet, log);
    }

    public void appendLog(){
        // Todo: Handle exception of append Log from datastore
        synchronized (Logger.class){
            put(logsProcessingQueue, logTrackSet);
            flushLogProcessingQueue(logTrackSet);

            service.submit(() -> {
                try{
                    synchronized (Logger.class){
                        fileStore.appendLog(logsProcessingQueue.peek());
                        logsProcessingQueue.remove();
                        System.out.println("Done with saving file");
                    }
                }catch (Exception ex){
                    deleteLogs();
                }
            });

        }
    }

    private <T> void flushLogProcessingQueue(Collection<T> logstore){
        logstore.clear();
    }

    private <T> void put(Collection<T> logStore, T log){
       synchronized (Logger.class){
           try{
               logStore.add(DeepCopyUtil.deepCopy(log));
           }catch (Exception ex){
               //
           }
       }
    }

    private void deleteLogs(){
        // Todo: access log file and delete
    }




}
