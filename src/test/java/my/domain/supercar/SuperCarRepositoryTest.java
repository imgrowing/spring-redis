package my.domain.supercar;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SuperCarRepositoryTest {

    @Autowired
    private SuperCarRepository repository;

    @Test
    public void test() {
        SuperCar superCar = repository.getByName("소나타1");
        log.info("superCar: {}", superCar);
    }
}