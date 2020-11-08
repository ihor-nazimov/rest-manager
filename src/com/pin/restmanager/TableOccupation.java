package com.pin.restmanager;

import java.util.ArrayList;
import java.util.List;

class TableOccupation {
    Table table;
    List<ClientsGroup> groups = new ArrayList<>();

    TableOccupation(Table table) {
        this.table = table;
    }

    public int getFreePlacesCount() {
        int occupied = 0;
        for (ClientsGroup cg : groups)
            occupied += cg.size;
        return table.size - occupied;
    }

    public boolean isAtTable(ClientsGroup group) {
        return groups.contains(group);
    }

    public boolean isEmpty() {
        return groups.isEmpty();
    }

    public boolean addGroup(ClientsGroup group) {
        assert group != null;
        if ( group.size > getFreePlacesCount() ) return false;
//        if ( !isAtTable(client) ) return false;
        assert !isAtTable(group);
        groups.add(group);
        return true;
    }

    public boolean removeGroup(ClientsGroup group) {
        assert group != null;
        return groups.remove(group);
    }

    public Table getTable() {
        return table;
    }

    public String toString() {
        StringBuilder result = new StringBuilder("Table " + table.id + ", size " + table.size + '\n');
        for (ClientsGroup group : groups) {
            result.append(group.id).append(", size ").append(group.size).append('\n');
        }
        return result.toString();
    }
}
