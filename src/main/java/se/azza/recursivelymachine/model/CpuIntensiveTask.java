package se.azza.recursivelymachine.model;

import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import se.azza.recursivelymachine.service.RecursivelyMachineCrawlImpl;

import java.io.IOException;
import java.time.Duration;

@NoArgsConstructor
public class CpuIntensiveTask  implements Runnable{

    static Logger logger = Logger.getLogger(String.valueOf(CpuIntensiveTask.class));
    RecursivelyMachineCrawlImpl recursivelyMachineCrawlImpl = new RecursivelyMachineCrawlImpl();

    @Override
    public void run() {
        try {
            recursivelyMachineCrawlImpl.searchValidLinks("https://tretton37.com/");
            Thread.sleep(Duration.ofSeconds(2).toMillis());
        } catch (InterruptedException | IOException e) {
            logger.error(e.getMessage() + e);
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        }
    }
}
