package com.pin.restmanager;

public class Table {
    public final int size; //number of chairs
    public final int id;

    private static int nextId = 0;

    Table(int size) {
        this.size = size;
        id = nextId++;
    }
}
