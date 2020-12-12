package my;

import lombok.extern.slf4j.Slf4j;
import my.domain.car.Car;
import my.domain.car.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@EnableCaching
@SpringBootApplication
public class SpringRedisApplication implements CommandLineRunner {

    @Autowired
    private CarRepository carReposotiry;

    public static void main(String[] args) {
        SpringApplication.run(SpringRedisApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("자동차 정보");
        log.info("no1::" + carReposotiry.getByName("소나타"));
        log.info("no2::" + carReposotiry.getByName("K5"));
        log.info("no3::" + carReposotiry.getByName("BMW"));
        log.info("no4::" + carReposotiry.getByName("제네시스"));
        log.info("no5::" + carReposotiry.getByName("레이"));
//
//        log.info("no1::" + carReposotiry.getByName("소나타"));
//        log.info("no2::" + carReposotiry.getByName("K5"));

        test();
//        test();
//        testParallel();
//        testParallel();
//        testParallel();
    }

    private void test() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 200; i++) {
            carReposotiry.getByName("소나타");
            carReposotiry.getByName("K5");
            carReposotiry.getByName("BMW");
            carReposotiry.getByName("제네시스");
            carReposotiry.getByName("레이");
        }
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
    }

    private void testParallel() throws ExecutionException, InterruptedException {
        Executor executor = Executors.newFixedThreadPool(16);

        int loopCount = 200;
        List<CompletableFuture<Car>> futures = new ArrayList<>();

        for (int i = 0; i < loopCount; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> carReposotiry.getByName("소나타"), executor));
            futures.add(CompletableFuture.supplyAsync(() -> carReposotiry.getByName("K5"), executor));
            futures.add(CompletableFuture.supplyAsync(() -> carReposotiry.getByName("BMW"), executor));
            futures.add(CompletableFuture.supplyAsync(() -> carReposotiry.getByName("제네시스"), executor));
            futures.add(CompletableFuture.supplyAsync(() -> carReposotiry.getByName("레이"), executor));
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        CompletableFuture<Car>[] cfs = new CompletableFuture[futures.size()];
        futures.toArray(cfs);
        CompletableFuture.allOf(cfs).get();

        stopWatch.stop();
        log.info(stopWatch.prettyPrint());

    }
}
