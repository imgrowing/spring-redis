package my.domain.newcar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Random;

@Getter
@Setter
@ToString
@RedisHash(value = "NEW_CAR")
public class NewCar {

    public static final String[] COLORS = new String[] {"red", "blue", "gray", "white", "purple"};

    @Id
    private String name;
    private String color;

    public NewCar() {
    }

    public NewCar(String name) {
        this.name = name;
    }

    public NewCar(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static NewCar newCar(String name) {
        NewCar car = new NewCar(name);
        return randomCarColor(car);
    }

    private static NewCar randomCarColor(NewCar car) {
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(COLORS.length);
            car.setColor(COLORS[index]);
        }

        return car;
    }

}
