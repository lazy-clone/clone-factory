package io.vepo.clone.aspectj;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LazyCloneableTest {

	@Test
	void test() {
		System.out.println("Creating SimplePojo");
		SimplePojo pojo = new SimplePojo();
		pojo.setId(7);
		pojo.setName("POJO-7");
		System.out.println(pojo.getId());
		System.out.println(pojo.getName());
//		System.out.println(LazyCloneable.cloneOf(pojo));
	}

}
