package io.vepo.clone.samples;

import java.util.Objects;

public class ImmutableSimplePojo {
	private final int id;
	private final String username;

	public ImmutableSimplePojo(int id, String username) {
		this.id = id;
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImmutableSimplePojo other = (ImmutableSimplePojo) obj;
		return id == other.id && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return String.format("ImmutableSimplePojo [id=%s, username=%s]", id, username);
	}

}
