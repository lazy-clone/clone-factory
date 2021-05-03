package io.vepo.clone.samples;

import java.util.Objects;

public class ImmutableFakePojo {
	private final int id;
	private final SimplePojo pojo;
//	private final int[] intValues;

	public ImmutableFakePojo(int id, SimplePojo pojo/* , int[] intValues */) {
		this.id = id;
		this.pojo = pojo;
//		this.intValues = intValues;
	}

	public int getId() {
		return id;
	}

	public SimplePojo getPojo() {
		return pojo;
	}

//	public int[] getIntValues() {
//		return intValues;
//	}

	@Override
	public int hashCode() {
		return Objects.hash(id, pojo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImmutableFakePojo other = (ImmutableFakePojo) obj;
		return id == other.id && Objects.equals(pojo, other.pojo);
	}

	@Override
	public String toString() {
		return String.format("ImmutableFakePojo [id=%s, pojo=%s]", id, pojo);
	}

}
