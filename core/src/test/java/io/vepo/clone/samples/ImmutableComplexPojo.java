package io.vepo.clone.samples;

import java.util.Arrays;
import java.util.Objects;

public class ImmutableComplexPojo {
	private final int id;
	private final ImmutableSimplePojo pojo;
	private final int[] intValues;

	public ImmutableComplexPojo(int id, ImmutableSimplePojo pojo, int[] intValues) {
		this.id = id;
		this.pojo = pojo;
		this.intValues = intValues;
	}

	public int getId() {
		return id;
	}

	public ImmutableSimplePojo getPojo() {
		return pojo;
	}

	public int[] getIntValues() {
		return intValues;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(intValues);
		result = prime * result + Objects.hash(id, pojo);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImmutableComplexPojo other = (ImmutableComplexPojo) obj;
		return id == other.id && Arrays.equals(intValues, other.intValues) && Objects.equals(pojo, other.pojo);
	}

	@Override
	public String toString() {
		return String.format("ImmutableComplexPojo [id=%s, pojo=%s, intValues=%s]", id, pojo,
				Arrays.toString(intValues));
	}

}
