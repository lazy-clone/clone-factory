package io.vepo.clone;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Data {
    public static class User {
        private Long id;
        private String username;
        private String firstName;
        private String lastName;
        private boolean admin;
        private Set<String> roles;
        private List<String> groups;
        private Map<String, String> properties;
        private byte[] bytePermissions;
        private short[] shortPermissions;
        private int[] intPermissions;
        private long[] longPermissions;
        private float[] floatPermissions;
        private double[] doublePermissions;
        private char[] charPermissions;
        private boolean[] booleanPermissions;

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

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public boolean isAdmin() {
            return admin;
        }

        public void setAdmin(boolean admin) {
            this.admin = admin;
        }

        public Set<String> getRoles() {
            return roles;
        }

        public void setRoles(Set<String> roles) {
            this.roles = roles;
        }

        public List<String> getGroups() {
            return groups;
        }

        public void setGroups(List<String> groups) {
            this.groups = groups;
        }

        public Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }

        public byte[] getBytePermissions() {
            return bytePermissions;
        }

        public void setBytePermissions(byte[] bytePermissions) {
            this.bytePermissions = bytePermissions;
        }

        public short[] getShortPermissions() {
            return shortPermissions;
        }

        public void setShortPermissions(short[] shortPermissions) {
            this.shortPermissions = shortPermissions;
        }

        public int[] getIntPermissions() {
            return intPermissions;
        }

        public void setIntPermissions(int[] intPermissions) {
            this.intPermissions = intPermissions;
        }

        public long[] getLongPermissions() {
            return longPermissions;
        }

        public void setLongPermissions(long[] longPermissions) {
            this.longPermissions = longPermissions;
        }

        public float[] getFloatPermissions() {
            return floatPermissions;
        }

        public void setFloatPermissions(float[] floatPermissions) {
            this.floatPermissions = floatPermissions;
        }

        public double[] getDoublePermissions() {
            return doublePermissions;
        }

        public void setDoublePermissions(double[] doublePermissions) {
            this.doublePermissions = doublePermissions;
        }

        public char[] getCharPermissions() {
            return charPermissions;
        }

        public void setCharPermissions(char[] charPermissions) {
            this.charPermissions = charPermissions;
        }

        public boolean[] getBooleanPermissions() {
            return booleanPermissions;
        }

        public void setBooleanPermissions(boolean[] booleanPermissions) {
            this.booleanPermissions = booleanPermissions;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + Arrays.hashCode(booleanPermissions);
            result = prime * result + Arrays.hashCode(bytePermissions);
            result = prime * result + Arrays.hashCode(charPermissions);
            result = prime * result + Arrays.hashCode(doublePermissions);
            result = prime * result + Arrays.hashCode(floatPermissions);
            result = prime * result + Arrays.hashCode(intPermissions);
            result = prime * result + Arrays.hashCode(longPermissions);
            result = prime * result + Arrays.hashCode(shortPermissions);
            result = prime * result + Objects.hash(admin, firstName, groups, id, lastName, properties, roles, username);
            return result;
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
            return admin == other.admin && Arrays.equals(booleanPermissions, other.booleanPermissions)
                    && Arrays.equals(bytePermissions, other.bytePermissions)
                    && Arrays.equals(charPermissions, other.charPermissions)
                    && Arrays.equals(doublePermissions, other.doublePermissions)
                    && Objects.equals(firstName, other.firstName)
                    && Arrays.equals(floatPermissions, other.floatPermissions) && Objects.equals(groups, other.groups)
                    && Objects.equals(id, other.id) && Arrays.equals(intPermissions, other.intPermissions)
                    && Objects.equals(lastName, other.lastName) && Arrays.equals(longPermissions, other.longPermissions)
                    && Objects.equals(properties, other.properties) && Objects.equals(roles, other.roles)
                    && Arrays.equals(shortPermissions, other.shortPermissions)
                    && Objects.equals(username, other.username);
        }

        @Override
        public String toString() {
            return String.format("User [id=%s, username=%s, firstName=%s, lastName=%s, admin=%s, roles=%s, groups=%s, properties=%s, bytePermissions=%s, shortPermissions=%s, intPermissions=%s, longPermissions=%s, floatPermissions=%s, doublePermissions=%s, charPermissions=%s, booleanPermissions=%s]",
                                 id, username, firstName, lastName, admin, roles, groups, properties,
                                 Arrays.toString(bytePermissions), Arrays.toString(shortPermissions),
                                 Arrays.toString(intPermissions), Arrays.toString(longPermissions),
                                 Arrays.toString(floatPermissions), Arrays.toString(doublePermissions),
                                 Arrays.toString(charPermissions), Arrays.toString(booleanPermissions));
        }

    }

    private static final Random random = new Random();

    public static User givenRandomUser() {
        User user = new User();
        user.setId(random.nextLong());
        user.setAdmin(random.nextBoolean());
        user.setUsername(randomString(5));
        user.setFirstName(randomString(10));
        user.setLastName(randomString(10));
        user.setRoles(IntStream.range(0, random.nextInt(5) + 3)
                               .mapToObj(index -> randomString(index + 5))
                               .collect(toSet()));

        user.setGroups(IntStream.range(0, random.nextInt(5) + 3)
                                .mapToObj(index -> randomString(index + 2))
                                .collect(toList()));
        user.setProperties(IntStream.range(0, random.nextInt(5) + 3)
                                    .mapToObj(Integer::toString)
                                    .collect(Collectors.toMap(index -> randomString(5),
                                                              Function.identity())));
        user.setShortPermissions(new short[] {
            (short) random.nextInt(255),
            (short) random.nextInt(255),
            (short) random.nextInt(255),
            (short) random.nextInt(255) });
        user.setBooleanPermissions(new boolean[] {
            random.nextBoolean(),
            random.nextBoolean(),
            random.nextBoolean()
        });
        user.setBytePermissions(new byte[] {
            (byte) (random.nextInt(255) - 128),
            (byte) (random.nextInt(255) - 128),
            (byte) (random.nextInt(255) - 128)
        });
        user.setCharPermissions(new char[] {
            (char) (random.nextInt('z' - 'a') + 'a'),
            (char) (random.nextInt('z' - 'a') + 'a'),
            (char) (random.nextInt('z' - 'a') + 'a'),
            (char) (random.nextInt('z' - 'a') + 'a'),
            (char) (random.nextInt('z' - 'a') + 'a')
        });
        user.setDoublePermissions(new double[] {
            random.nextDouble(),
            random.nextDouble(),
            random.nextDouble(),
            random.nextDouble()
        });
        user.setFloatPermissions(new float[] {
            random.nextFloat(),
            random.nextFloat(),
            random.nextFloat(),
            random.nextFloat()
        });
        user.setLongPermissions(new long[] {
            random.nextLong(),
            random.nextLong(),
            random.nextLong() });
        user.setIntPermissions(IntStream.of(0, random.nextInt(10) + 5)
                                        .map(index -> random.nextInt(1_000 + (500 * index)))
                                        .toArray());
        return user;
    }

    private static String randomString(int size) {
        return random.ints('a', 'z')
                     .limit(size)
                     .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                     .toString();
    }

}
