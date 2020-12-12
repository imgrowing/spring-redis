package my.operations;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OperationsForValueTest {

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String STR_KEY = "MY_STRING_KEY";
    private static final String LIST_KEY = "MY_LIST_KEY";

    @Before
    @After
    public void cleanup() {
        stringRedisTemplate.delete(STR_KEY);
        stringRedisTemplate.delete(LIST_KEY);
    }

    @Test
    public void testString() {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();

        operations.set(STR_KEY, "1");

        String value = operations.get(STR_KEY);
        assertEquals("1", value);

        operations.increment(STR_KEY, 10L);

        String value2 = operations.get(STR_KEY);
        assertEquals("11", value2);
    }

    @Test
    public void testList() {
        ListOperations<String, String> operations = stringRedisTemplate.opsForList();

        operations.leftPush(LIST_KEY, "1");
        operations.leftPush(LIST_KEY, "2");

        List<String> values = operations.range(LIST_KEY, 0, Long.MAX_VALUE);
        assertEquals("2", values.get(0));
        assertEquals("1", values.get(1));

        operations.rightPush(LIST_KEY, "3");
        values = operations.range(LIST_KEY, 0, Long.MAX_VALUE);
        log.info("values: {}", values);
        assertEquals("3", values.get(2));

        String left = operations.leftPop(LIST_KEY);
        assertEquals("2", left);
        values = operations.range(LIST_KEY, 0, Long.MAX_VALUE);
        log.info("values: {}", values);
        assertEquals("1", values.get(0));
        assertEquals("3", values.get(1));


        String right = operations.rightPop(LIST_KEY);
        assertEquals("3", right);
        values = operations.range(LIST_KEY, 0, Long.MAX_VALUE);
        log.info("values: {}", values);
        assertEquals("1", values.get(0));
        assertEquals(1, values.size());

        assertEquals(1L, (long) operations.size(LIST_KEY));
    }
}
