package com.fleamarket.core;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

public class MD5Test {

	@Test
	public void md5() {
		System.out.println(new Md5Hash("hello","admin",2));
	}

}
