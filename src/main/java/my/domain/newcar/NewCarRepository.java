package my.domain.newcar;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NewCarRepository extends CrudRepository<NewCar, String> {
}
