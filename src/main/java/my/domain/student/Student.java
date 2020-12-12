package my.domain.student;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.Date;

@RedisHash(value = "STUDENT")
@Getter
@Setter
public class Student {

    public enum Gender {
        MALE, FEMALE
    }

    @Id
    private String id;
    private String name;
    private Gender gender;
    private int grade;
    private Address address;
    private Date birthDate;
    @TimeToLive
    private Long ttl;

    public Student() {
    }

    public Student(String id, String name, Gender gender, int grade) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.grade = grade;
        this.address = new Address("Seoul", "South");
        this.birthDate = new Date();
        this.ttl = 60L;
    }

    @Getter
    @Setter
    public static class Address {
        private String city;
        private String street;

        public Address() {
        }

        public Address(String city, String street) {
            this.city = city;
            this.street = street;
        }
    }
}
