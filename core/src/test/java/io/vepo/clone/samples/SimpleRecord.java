package io.vepo.clone.samples;

public class SimpleRecord {
    private int id;
    private String username;

    public SimpleRecord() {
    }

    public SimpleRecord(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int id() {
        return id;
    }

    public void id(int id) {
        this.id = id;
    }

    public String username() {
        return username;
    }

    public void username(String username) {
        this.username = username;
    }

}
