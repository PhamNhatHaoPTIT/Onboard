package com.haopn.demo;

import com.haopn.demo.service.CounterService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;


@RunWith(SpringRunner.class)
@SpringBootTest
class DemoApplicationTests {

	@Autowired
	CounterService counterService;

	@Test
	public void testGetCounter() {
		long number = counterService.getCounterRedis();
		System.out.println(number);
		counterService.increaseCounter();
	}

	@Test
	public void testPersisCounter() {
		counterService.persisCounter();
	}

}
