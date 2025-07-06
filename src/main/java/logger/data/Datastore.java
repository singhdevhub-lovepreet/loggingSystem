package logger.data;

import logger.pojo.Log;

import java.util.Collection;
import java.util.concurrent.TimeoutException;

public interface Datastore {

    void addLog(Log log);

    void appendLog(Collection<Log> logs) throws TimeoutException;

}
