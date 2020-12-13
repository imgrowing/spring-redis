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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OperationsForValueTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> opsForValue;

    @Resource(name = "stringRedisTemplate")
    private ListOperations<String, String> opsForList;

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
        // redisTemplate을 바로 Operations로 Inject 받을 수 있다.
        //ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();

        opsForValue.set(STR_KEY, "1");

        String value = opsForValue.get(STR_KEY);
        assertEquals("1", value);

        opsForValue.increment(STR_KEY, 10L);

        String value2 = opsForValue.get(STR_KEY);
        assertEquals("11", value2);
    }

    @Test
    public void testList() {
        // redisTemplate을 바로 Operations로 Inject 받을 수 있다.
        //ListOperations<String, String> opsForList = stringRedisTemplate.opsForList();

        opsForList.leftPush(LIST_KEY, "1");
        opsForList.leftPush(LIST_KEY, "2");

        List<String> values = opsForList.range(LIST_KEY, 0, Long.MAX_VALUE);
        assertEquals("2", values.get(0));
        assertEquals("1", values.get(1));

        opsForList.rightPush(LIST_KEY, "3");
        values = opsForList.range(LIST_KEY, 0, Long.MAX_VALUE);
        log.info("values: {}", values);
        assertEquals("3", values.get(2));

        String left = opsForList.leftPop(LIST_KEY);
        assertEquals("2", left);
        values = opsForList.range(LIST_KEY, 0, Long.MAX_VALUE);
        log.info("values: {}", values);
        assertEquals("1", values.get(0));
        assertEquals("3", values.get(1));


        String right = opsForList.rightPop(LIST_KEY);
        assertEquals("3", right);
        values = opsForList.range(LIST_KEY, 0, Long.MAX_VALUE);
        log.info("values: {}", values);
        assertEquals("1", values.get(0));
        assertEquals(1, values.size());

        assertEquals(1L, (long) opsForList.size(LIST_KEY));
    }
}
