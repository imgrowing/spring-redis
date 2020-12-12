package my.domain.car;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Random;

import static my.config.RedisConstants.CACHE_NAME_CAR;

@Slf4j
@Component
public class CarRepository {

    private static final String[] COLORS = new String[] {"red", "blue", "gray", "white", "purple"};
    private static final long SLEEP_MS = 500L;

    @Cacheable(cacheNames = CACHE_NAME_CAR, key = "#name")
    public Car getByName(String name) {
        //makeSlowService();
        Car car = new Car(name);
        return randomCarColor(car);
    }

    private Car randomCarColor(Car car) {
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(COLORS.length);
            car.setColor(COLORS[index]);
        }

        return car;
    }

    private void makeSlowService() {
        log.info("start sleep");
        try {
            Thread.sleep(SLEEP_MS);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        log.info("end sleep");
    }
}
