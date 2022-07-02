package io.datalake.ext;

public interface ExtTaskMapper {

    int runningCount(Long taskId);

    void resetRunnings(Long taskId);

}
