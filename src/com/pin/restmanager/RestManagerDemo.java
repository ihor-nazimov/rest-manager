package com.pin.restmanager;

import java.util.ArrayList;
import java.util.List;

public class RestManagerDemo {
    public static void main(String[] args) {
        System.out.println("Start");

        List<Table> tables = new ArrayList<>();
        tables.add(new Table(6));
        tables.add(new Table(4));
        tables.add(new Table(5));
        tables.add(new Table(3));
//        tables.add(new Table(2));
//        tables.add(new Table(3));
//        tables.add(new Table(4));
        for (Table table : tables)
            System.out.printf("Table %d, size %d\n", table.id, table.size);

        RestManager restManager = new RestManager(tables);

        ClientsGroup[] cgs = new ClientsGroup[20];
        cgs[0] = new ClientsGroup(1);
        cgs[1] = new ClientsGroup(2);
        cgs[2] = new ClientsGroup(2);
        cgs[3] = new ClientsGroup(2);
        cgs[4] = new ClientsGroup(6);
        cgs[5] = new ClientsGroup(1);
        cgs[6] = new ClientsGroup(2);
        cgs[7] = new ClientsGroup(2);
        cgs[8] = new ClientsGroup(1);
        cgs[9] = new ClientsGroup(1);
//        cgs[10] = new ClientsGroup(2);
//        cgs[11] = new ClientsGroup(2);

        for (ClientsGroup cg : cgs)
            if (cg != null) System.out.printf(" %d, size %d\n", cg.id, cg.size);

        for (ClientsGroup cg : cgs) {
            if (cg == null) break;
            restManager.onArrive(cg);
        }

        restManager.onLeave(cgs[1]);
        restManager.onLeave(cgs[7]);
        restManager.onArrive(new ClientsGroup(2));
        restManager.onArrive(new ClientsGroup(2));
        restManager.onArrive(new ClientsGroup(5));
        restManager.onArrive(new ClientsGroup(5));
        restManager.onLeave(cgs[3]);

//        restManager.onLeave();
        System.out.println(restManager);

        Table lookupTable;
        for (int i : new int[] {9, 7} ) {
            lookupTable = restManager.lookup(cgs[i]);
            System.out.printf("Client %d at table %d\n",
                    i,
                    lookupTable == null ? -1 : lookupTable.id);
        }
    }
}
