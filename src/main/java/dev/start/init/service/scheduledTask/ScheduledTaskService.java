package dev.start.init.service.scheduledTask;

import dev.start.init.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Every user that does not verify email after a certain time is deleted from the database.
 *
 * <pre>
 * Format is:
 * 'second minute hour day-of-month month day-of-week year'
 * # 1. Entry: Minute when the process will be started [0-60]
 * # 2. Entry: Hour when the process will be started [0-23]
 * # 3. Entry: Day of the month when the process will be started [1-28/29/30/31]
 * # 4. Entry: Month of the year when the process will be started [1-12]
 * # 5. Entry: Weekday when the process will be started [0-6] [0 is Sunday]
 * #
 * # all x min = *\/x.
 * '0 0 0 ? * SUN' for every Sunday.
 * '0 * * * * *' for every minute.
 * </pre>
 */

@Service
public class ScheduledTaskService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskService.class);

    private final CompanyService companyService;
    @Value("${cron.expression.dailyTask}")
    private String dailyTaskCronExpression;

    @Autowired
    public ScheduledTaskService(CompanyService companyService, @Value("${cron.expression.dailyTask}") String dailyTaskCronExpression) {
        this.companyService = companyService;
        this.dailyTaskCronExpression = dailyTaskCronExpression;
        logger.info("Cron Expression Loaded: {}", dailyTaskCronExpression);
    }

    /**
     * This service are called at each minute (60,000 millis or 60 seconds)
     * The service initialize the company table with taking
     * starting parameter and insert some data after that integer
     * Used <b>Async</b> to running them asynchronously to prevent blocking when multiple task is running
     * long time running process
     */
    @Scheduled(fixedRate = 60000)
    @Async
    public void executeTaskWithFixedRate() {
        try{
        logger.info("Executing task at fixed rate - {}", System.currentTimeMillis());
        companyService.initCompanyTable(Integer.valueOf((int) (System.currentTimeMillis()/500)));
        }catch (Exception e){
            logger.error("Error occurred during scheduled task execution", e);
        }

    }

    /**
     * This is scheduled task each night 11:20
     * insert six records in the company table
     */

    @Scheduled(cron = "${cron.expression.dailyTask}")
    public void executeTaskWithCronExpression() {
        try{
        logger.info("Executing task with cron expression - {}", System.currentTimeMillis());
        companyService.initCompanyTable(550);
        }
        catch (Exception e){
            logger.error("Error occurred during scheduled task execution", e);
        }
    }
}

