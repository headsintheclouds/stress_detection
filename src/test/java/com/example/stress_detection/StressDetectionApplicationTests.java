package com.example.stress_detection;

import com.example.stress_detection.entity.File;
import com.example.stress_detection.entity.User;
import com.example.stress_detection.mapper.FileMapper;
import com.example.stress_detection.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class StressDetectionApplicationTests {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private FileMapper fileMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void testSelect() {
		List<User> users = userMapper.selectList(null);
		users.forEach(System.out::println);
		System.out.println("测试完成");
	}

	@Test
	void testFileMapper() {
		List<File> files = fileMapper.selectList(null);
		files.forEach(System.out::println);
		System.out.println(" 测试完成 ");
	}
}


