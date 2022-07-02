package io.datalake.job.sechedule.strategy;

import io.datalake.commons.utils.CronUtils;
import io.datalake.job.sechedule.ScheduleManager;
import io.datalake.plugins.common.entity.GlobalTaskEntity;
import org.apache.commons.lang3.ObjectUtils;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.InitializingBean;

import java.util.Date;

public abstract class TaskHandler implements InitializingBean {

    public void addTask(ScheduleManager scheduleManager, GlobalTaskEntity taskEntity) throws Exception {
        // 1。首先看看是否过期
        Long endTime = taskEntity.getEndTime();
        removeTask(scheduleManager, taskEntity);
        if (CronUtils.taskExpire(endTime)) { // 过期了就删除任务
            return;
        }
        JobKey jobKey = new JobKey(taskEntity.getTaskId().toString());
        TriggerKey triggerKey = new TriggerKey(taskEntity.getTaskId().toString());
        Date start = new Date(taskEntity.getStartTime());
        Date end = null;
        if (ObjectUtils.isNotEmpty(taskEntity.getEndTime())) {
            new Date(taskEntity.getEndTime());
        }
        Class<? extends TaskHandler> executor = this.getClass();
        String cron = CronUtils.cron(taskEntity);
        scheduleManager.addOrUpdateCronJob(jobKey, triggerKey, executor, cron, start, end, jobDataMap(taskEntity));
    }

    protected abstract JobDataMap jobDataMap(GlobalTaskEntity taskEntity);



    public abstract void resetRunningInstance(Long taskId);



    public void removeTask(ScheduleManager scheduleManager, GlobalTaskEntity taskEntity) {
        JobKey jobKey = new JobKey(taskEntity.getTaskId().toString());
        TriggerKey triggerKey = new TriggerKey(taskEntity.getTaskId().toString());
        scheduleManager.removeJob(jobKey, triggerKey);
    }

    public void executeTask(ScheduleManager scheduleManager, GlobalTaskEntity taskEntity) throws Exception {
        JobKey jobKey = new JobKey(taskEntity.getTaskId().toString());
        scheduleManager.fireNow(jobKey);
    }



    protected abstract Boolean taskIsRunning(Long taskId);

    @Override
    public void afterPropertiesSet() throws Exception {
        String beanName = null;
        String className = this.getClass().getName();
        className = className.substring(className.lastIndexOf(".") + 1);
        if (className.length() > 1) {
            beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
        } else {
            beanName = className.toLowerCase();
        }
        TaskStrategyFactory.register(beanName, this);
    }

}
