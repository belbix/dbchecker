package pro.belbix.dbchecker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

public class Main {
    private final static Logger log = LoggerFactory.getLogger(Main.class);
    private static Config config;
    private static DBService dbService;
    private static boolean run = true;

    public static void main(String[] args) throws InterruptedException {
        log.info("################ Start DBChecker ##################");
        if (args.length != 0) {
            Config.setPath(args[0]);
        }
        config = Config.getInstance();
        dbService = DBService.getInstance();

        Main main = new Main();
        while (run) {
            try {
                main.check();
            } catch (Exception e) {
                log.error("Error main loop", e);
            } finally {
                Thread.sleep(config.getLoopTime() * 1000);
            }
        }
    }

    private void check() {
        Instant now = Instant.now();
        Instant date = dbService.selectLastDate(config.getDateSelect());
        long sec = Duration.between(date, Instant.now()).getSeconds();
        if (sec > config.getMaxDiff()) {
            log.error("TOO OLD VALUE! " + sec);
            sendError(sec);
            try {
                Thread.sleep(config.getLoopTime() * 1000 * 5);
            } catch (InterruptedException ignored) {
            }
        }

        log.info("Check for " + Duration.between(now, Instant.now()).getSeconds());
    }

    private void sendError(long sec) {

    }

}
