package my.domain.supercar;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Random;

import static my.config.RedisConstants.CACHE_NAME_SUPER_CAR;

@Slf4j
@Component
public class SuperCarRepository {

    private static final String[] COLORS = new String[] {"red", "blue", "gray", "white", "purple"};

    @Cacheable(cacheNames = CACHE_NAME_SUPER_CAR, key = "#name")
    public SuperCar getByName(String name) {
        Random random = new Random();
        int index = random.nextInt(COLORS.length);

        SuperCar superCar = new SuperCar(name, COLORS[index]);
        return superCar;
    }
}
