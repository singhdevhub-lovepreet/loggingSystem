package logger.data;

import logger.pojo.Log;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.concurrent.TimeoutException;

public interface Datastore {

    void appendLog(Collection<Log> logs) throws TimeoutException;

    void deleteLog();

}
