package se.azza.recursivelymachine.main;

import org.apache.log4j.Logger;
import se.azza.recursivelymachine.model.CpuIntensiveTask;
import se.azza.recursivelymachine.service.RecursivelyMachineSaverImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    static Logger logger = Logger.getLogger(String.valueOf(Main.class));

    public static void main(String[] args) {
        logger.info("Recursively Machine is started!");
        int coreCount = Runtime.getRuntime().availableProcessors();
        logger.info("Core amount: " + coreCount);
        ExecutorService service = Executors.newFixedThreadPool(coreCount);
        for (int i = 0; i < coreCount; i++) {
            service.execute(new CpuIntensiveTask());
        }
        /*
        RecursivelyMachineSaverImpl recursivelyMachineSaver = new RecursivelyMachineSaverImpl();
        recursivelyMachineSaver.getOnlyUrlText("https://tretton37.com/assets/i/join.jpg");
         */
    }
}
