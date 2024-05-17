import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class MetroTest extends BaseTest {
    @Test
    void takeAllTimeInMetro() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        executorService.submit(() -> {
            Thread.currentThread().setName("Thread-1");
            new MetroBookPage(browser1).fillStations(73, 85);
        });

        executorService.submit(() -> {
            Thread.currentThread().setName("Thread-2");
            new MetroBookPage(browser2).fillStations(86, 158);
        });

        executorService.submit(() -> {
            Thread.currentThread().setName("Thread-3");
            new MetroBookPage(browser3).fillStations(159, 201);
        });

        executorService.submit(() -> {
            Thread.currentThread().setName("Thread-4");
            new MetroBookPage(browser4).fillStations(202, 244);
        });

        /*executorService.submit(() -> {
            Thread.currentThread().setName("Thread-5");
            new MetroBookPage(browser5).fillStations(202, 244);
        });*/

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
