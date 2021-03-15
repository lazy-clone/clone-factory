# Cloning Java Objects

## Why?

In some frameworks and applications, there is a huge amount of time wasted in cloning objects. Some of these objects are never changed, they can be used as an immutable object. For that reason, there is a demand for Clone By Demand library.

## Java Object Anatomy

There some types of Java Objects:

* Immutable
* Fake Immutable
* POJO
* Record

### Immutable

The immutable is an object that cannot be changed, all fields are finals. This object doesn't need to be cloned. A common Immutable object looks like this:

```java
public class User {
    private final Long id;
    private final String username;

    public User(Long id, String username) {
        this.id = id;
        this username = username.
    }

    public Long getId() {
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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        return Objects.equals(id, other.id) && Objects.equals(username, other.username);
    }

    @Override
    public String toString() {
        return String.format("User [id=%s, username=%s]", id, username);
    }
}
```

### Fake Immutable

Fake Immutable is an Anti-Pattern where the objects are immutables, but the value of some fields can be changed outside the class.

```java
public class User {
    private final Long id;
    private final String username;
    private final List<String> roles

    public User(Long id, String username, List<String> roles) {
        this.id = id;
        this username = username.
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, roles);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        return Objects.equals(id, other.id) && Objects.equals(username, other.username) && Objects.equals(roles, other.roles);
    }

    @Override
    public String toString() {
        return String.format("User [id=%s, username=%s, roles=%s]", id, username, roles);
    }
}
```

### POJO

POJO is an acronym for Plain Old Java Object. This pattern is widely used.

```java
public class User {
    private Long id;
    private String username;
    private List<String> roles

    public User(Long id, String username, List<String> roles) {
        this.id = id;
        this username = username.
        this.roles = roles;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, roles);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        return Objects.equals(id, other.id) && Objects.equals(username, other.username) && Objects.equals(roles, other.roles);
    }

    @Override
    public String toString() {
        return String.format("User [id=%s, username=%s, roles=%s]", id, username, roles);
    }
}
```

### Record

A Record is non-verbose POJO. It was used before, but now the JCP accepts it as the main pattern and introduce it as a new kind of entity on Java Language ([JEP 395](https://openjdk.java.net/jeps/395)).

```java
class Point {
    private final int x;
    private final int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int x() { return x; }
    int y() { return y; }

    public boolean equals(Object o) {
        if (!(o instanceof Point)) return false;
        Point other = (Point) o;
        return other.x == x && other.y = y;
    }

    public int hashCode() {
        return Objects.hash(x, y);
    }

    public String toString() {
        return String.format("Point[x=%d, y=%d]", x, y);
    }
}
```

## Cloning Types

There are two basic types of cloning: shallow and deep. The Shallow Clone copies the values of an object, but do not clone the references. The Deep Clone copies the values clone the references.

In this library, we will build an different approach, the Lazy Clone. The Lazy Clone execute the Shallow clone on all primitive and immutable values and for the other only clones if they are requested by getters or setters.

# Cloning Java Values

Cloning a Java Object needs attention to the type of the value.

| Type | How to identify | Cloning Strategy |
|------|-----------------|-------------------|
| Primitive Types | Check [Class::isPrimitive](https://docs.oracle.com/javase/7/docs/api/java/lang/Class.html#isPrimitive()) | Do not clone it, use the same value. |
| Java Lang Immutable Object | Any class of `java.lang` that represents a value. | Do not clone it, use the same value. |
| Java Collections | Any class that can be assignable from List, Set or Map | Create a new instance and then for each value apply the Cloning Strategy for the type |
| Java POJO | Any class that does not match any type above | Create a Lazy Clone instance |
