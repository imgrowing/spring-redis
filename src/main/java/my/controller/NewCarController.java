package my.controller;

import lombok.extern.slf4j.Slf4j;
import my.domain.car.Car;
import my.domain.newcar.NewCar;
import my.domain.newcar.NewCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@RestController
public class NewCarController {

    @Autowired
    private NewCarRepository carRepository;

    @GetMapping("/newcar/save")
    public void save() {
        log.info("자동차 정보");
        log.info("no1::" + carRepository.save(NewCar.newCar("소나타")));
        log.info("no2::" + carRepository.save(NewCar.newCar("K5")));
        log.info("no3::" + carRepository.save(NewCar.newCar("BMW")));
        log.info("no4::" + carRepository.save(NewCar.newCar("제네시스")));
        log.info("no5::" + carRepository.save(NewCar.newCar("레이")));
    }

    @GetMapping("/newcar/one")
    public void newCar() {
        testByOne();
    }

    @GetMapping("/newcar/parallel")
    public void newCarsParallel() throws ExecutionException, InterruptedException {
        testParallel();
    }

    private void testByOne() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 200; i++) {
            carRepository.findOne("소나타");
            carRepository.findOne("K5");
            carRepository.findOne("BMW");
            carRepository.findOne("제네시스");
            carRepository.findOne("레이");
        }
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
    }

    private void testParallel() throws ExecutionException, InterruptedException {
        Executor executor = Executors.newFixedThreadPool(16);

        int loopCount = 200;
        List<CompletableFuture<NewCar>> futures = new ArrayList<>();

        for (int i = 0; i < loopCount; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> carRepository.findOne("소나타"), executor));
            futures.add(CompletableFuture.supplyAsync(() -> carRepository.findOne("K5"), executor));
            futures.add(CompletableFuture.supplyAsync(() -> carRepository.findOne("BMW"), executor));
            futures.add(CompletableFuture.supplyAsync(() -> carRepository.findOne("제네시스"), executor));
            futures.add(CompletableFuture.supplyAsync(() -> carRepository.findOne("레이"), executor));
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
