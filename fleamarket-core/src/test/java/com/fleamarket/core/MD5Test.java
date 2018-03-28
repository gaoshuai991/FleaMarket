package com.fleamarket.core;

import com.sun.org.apache.xpath.internal.operations.String;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

public class MD5Test {

	@Test
	public void md5() {
		System.out.println(new Md5Hash("123","gss",2));
	}

}
