package com.example.stress_detection;

import com.example.stress_detection.Entity.User;
import com.example.stress_detection.Mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class StressDetectionApplicationTests {

	@Autowired
	private UserMapper userMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void testSelect() {
		List<User> users = userMapper.selectList(null);
		users.forEach(System.out::println);
		System.out.println("测试完成");
	}
}


