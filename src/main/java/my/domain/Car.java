package my.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Car {

    private String name;
    private String color;

    public Car() {
    }

    public Car(String name) {
        this.name = name;
    }

    public Car(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
