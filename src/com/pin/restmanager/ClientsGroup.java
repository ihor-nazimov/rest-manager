package com.pin.restmanager;

public class ClientsGroup {
    public final int size;
    public final int id;

    private static int nextId = 0;

    ClientsGroup(int size) {
        this.size = size;
        id = nextId++;
    }
}
