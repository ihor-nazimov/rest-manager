package com.pin.restmanager;

import com.sun.istack.internal.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RestManager {
    private final List<TableOccupation> tableOccupationList = new ArrayList<>();
    private final ArrayDeque<ClientsGroup> clientsGroupsQueue = new ArrayDeque<>();
    private int maxTableSize;

    public RestManager (List<Table> tables)
    {
        assert tables.size() > 0;
        maxTableSize = 0;
        for (Table table : tables) {
            tableOccupationList.add(new TableOccupation(table));
            if (maxTableSize < table.size) maxTableSize = table.size;
        }
//        tableOccupationList.sort(Comparator.comparingInt(a -> a.table.size));
    }

    private boolean placeGroup(ClientsGroup group, boolean useFreeTables) {
        // try to occupy free table
        int minFreePlaces = 0;
        TableOccupation minTableOcc = null;
        int freePlaces;
        for (TableOccupation tableOccupation : tableOccupationList) {
            if (useFreeTables && !tableOccupation.isEmpty()) continue;
            if (!useFreeTables && tableOccupation.isEmpty()) continue;

            freePlaces = tableOccupation.getFreePlacesCount();
            if (freePlaces < group.size) continue;

            if (minTableOcc == null || freePlaces < minFreePlaces) {
                minFreePlaces = freePlaces;
                minTableOcc = tableOccupation;
            }
        }
        if (minTableOcc != null) {
            minTableOcc.addGroup(group);
            return true;
        }

        return false;
    }

    private boolean placeGroup(ClientsGroup group) {
        if (placeGroup(group, true))
            return true;
        return placeGroup(group, false);
    }

    // new client(s) show up
    public void onArrive (ClientsGroup group)
    {
        // TODO
        // check for group already is queued/serving
        assert group != null;
        if (group.size > maxTableSize) return;

        if (!placeGroup(group)) {
            //add to queue, if not enough free tables
            clientsGroupsQueue.add(group);
        }
    }

    // client(s) leave, either served or simply abandoning the queue
    public void onLeave (ClientsGroup group)
    {
        assert group != null;

        if (clientsGroupsQueue.remove(group)) return;

        for (TableOccupation tableOccupation : tableOccupationList) {
            if (tableOccupation.removeGroup(group)) {
                //try to place queued groups at this table
                for (ClientsGroup queuedGroup : clientsGroupsQueue) {
                    if (tableOccupation.addGroup(queuedGroup)) {
                        clientsGroupsQueue.remove(queuedGroup);
                        return;
                    }
                }
            }
        }
    }

    // return table where a given client group is seated,
    // or null if it is still queuing or has already left
    public Table lookup (@NotNull ClientsGroup group)
    {
        for (TableOccupation tableOccupation : tableOccupationList) {
            if (tableOccupation.isAtTable(group)) {
                return tableOccupation.getTable();
            }
        }
        return null;
    }

    public String toString() {
        StringBuilder result = new StringBuilder("Restaurant tables status:\n");
        for (TableOccupation tableOccupation : tableOccupationList) {
            result.append(tableOccupation).append("\n");
        }

        result.append("Queue:\n");
        for (ClientsGroup group : clientsGroupsQueue) {
            result.append(group.id).append(", size ").append(group.size).append('\n');
        }
        return result.toString();
    }
}
