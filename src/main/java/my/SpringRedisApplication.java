package my;

import lombok.extern.slf4j.Slf4j;
import my.domain.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.util.StopWatch;

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

        test();
        test();
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
}
