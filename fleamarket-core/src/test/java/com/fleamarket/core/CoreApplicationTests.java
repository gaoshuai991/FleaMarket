package com.fleamarket.core;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CoreApplicationTests {
	@Autowired
	private FastFileStorageClient client;

	@Test
	public void contextLoads() {
	}

	@Test
	public void upload(){
		File file = new File("/Users/jackiegao/Documents/7d6e12985dfdb526e902443a7dc9abfe_r.jpg");
		try {
			System.out.println(client.uploadFile(new FileInputStream(file), 71200, "jpg", null));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
