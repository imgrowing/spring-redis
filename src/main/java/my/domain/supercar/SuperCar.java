package my.domain.supercar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SuperCar {

    private String name;
    private String color;

    public SuperCar() {
    }

    public SuperCar(String name) {
        this.name = name;
    }

    public SuperCar(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
