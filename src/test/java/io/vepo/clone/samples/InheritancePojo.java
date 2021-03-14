package io.vepo.clone.samples;

import java.util.List;
import java.util.Objects;

public class InheritancePojo extends SimplePojo {

    /**
     * 
     */
    private static final long serialVersionUID = -2360274979832888349L;

    private List<Roles> roles;

    public InheritancePojo() {
    }

    public InheritancePojo(int id, String username, List<Roles> roles) {
        super(id, username);
        this.roles = roles;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(roles);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        InheritancePojo other = (InheritancePojo) obj;
        return Objects.equals(roles, other.roles);
    }

    @Override
    public String toString() {
        return String.format("InheritancePojo [id=%s, username=%s, roles=%s]", getId(), getUsername(), roles);
    }

}
