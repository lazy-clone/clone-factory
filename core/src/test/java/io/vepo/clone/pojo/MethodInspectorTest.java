package io.vepo.clone.pojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.vepo.clone.samples.ImmutableComplexPojo;
import io.vepo.clone.samples.ImmutableFakePojo;
import io.vepo.clone.samples.ImmutableSimplePojo;
import io.vepo.clone.samples.InheritancePojo;
import io.vepo.clone.samples.SimplePojo;
import io.vepo.clone.samples.SimpleRecord;

@DisplayName("Method Inpector")
class MethodInspectorTest {

	@Test
	@DisplayName("Setter")
	void getSetterTest() throws NoSuchFieldException, SecurityException, NoSuchMethodException {
		assertEquals(SimplePojo.class.getMethod("setId", int.class),
				ClassInspector.getSetter(SimplePojo.class.getDeclaredField("id")));
		assertEquals(SimpleRecord.class.getMethod("id", int.class),
				ClassInspector.getSetter(SimpleRecord.class.getDeclaredField("id")));
	}

	@Test
	@DisplayName("Get Field by Setter")
	void getFieldBySetterTest() throws NoSuchFieldException, SecurityException, NoSuchMethodException {
		assertEquals(SimplePojo.class.getDeclaredField("id"),
				ClassInspector.getFieldBySetter(SimplePojo.class.getMethod("setId", int.class)));
		assertEquals(SimpleRecord.class.getDeclaredField("id"),
				ClassInspector.getFieldBySetter(SimpleRecord.class.getMethod("id", int.class)));
	}

	@Test
	@DisplayName("Getter")
	void getGetterTest() throws NoSuchMethodException, SecurityException, NoSuchFieldException {
		assertEquals(SimplePojo.class.getMethod("getId"),
				ClassInspector.getGetter(SimplePojo.class.getDeclaredField("id")));
		assertEquals(SimpleRecord.class.getMethod("id"),
				ClassInspector.getGetter(SimpleRecord.class.getDeclaredField("id")));
	}

	@Test
	@DisplayName("Get Field by Getter")
	void getFieldByGetterTest() throws NoSuchMethodException, SecurityException, NoSuchFieldException {
		assertEquals(SimplePojo.class.getDeclaredField("id"),
				ClassInspector.getFieldByGetter(SimplePojo.class.getMethod("getId")));
		assertEquals(SimpleRecord.class.getDeclaredField("id"),
				ClassInspector.getFieldByGetter(SimpleRecord.class.getMethod("id")));
	}

	@Test
	@DisplayName("Get default constructor")
	void defaultConstructorTest() throws NoSuchMethodException, SecurityException {
		assertEquals(SimplePojo.class.getConstructor(), ClassInspector.getDefaultContructor(SimplePojo.class));
	}

	@Test
	@DisplayName("Get all fields")
	void allFieldsTest() throws NoSuchFieldException, SecurityException {
		assertEquals(
				Arrays.asList(SimplePojo.class.getDeclaredField("id"), SimplePojo.class.getDeclaredField("username"),
						InheritancePojo.class.getDeclaredField("roles")).stream().collect(Collectors.toSet()),
				ClassInspector.getAllFields(InheritancePojo.class));
	}

	@Test
	@DisplayName("Is Immutable")
	void isImmutableTest() throws NoSuchFieldException, SecurityException {
		assertTrue(ClassInspector.isImmutable(ImmutableSimplePojo.class));
		assertFalse(ClassInspector.isImmutable(SimplePojo.class));
		assertTrue(ClassInspector.isImmutable(ImmutableComplexPojo.class));
		assertFalse(ClassInspector.isImmutable(ImmutableFakePojo.class));
		assertTrue(ClassInspector.isImmutable(ImmutableFakePojo.class.getDeclaredField("id")));
		assertFalse(ClassInspector.isImmutable(ImmutableFakePojo.class.getDeclaredField("pojo")));
	}

	@Test
	@DisplayName("Is Primitive")
	void isPrimitiveTest() throws NoSuchFieldException, SecurityException {
		assertTrue(ClassInspector.isPrimitive(int.class));
		assertTrue(ClassInspector.isPrimitive(Integer.class));
		assertTrue(ClassInspector.isPrimitive(ImmutableFakePojo.class.getDeclaredField("id")));
		assertFalse(ClassInspector.isPrimitive(ImmutableFakePojo.class.getDeclaredField("pojo")));
	}

}
