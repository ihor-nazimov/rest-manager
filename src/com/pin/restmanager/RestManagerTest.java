package com.pin.restmanager;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestManagerTest {
    @Test
    void restManagerTest() {
        List<Table> tables;
        RestManager restManager;

        tables = new ArrayList<>();
        tables.add(new Table(6));
        tables.add(new Table(4));
        tables.add(new Table(5));
        tables.add(new Table(3));

        restManager = new RestManager(tables);
        assertNotNull(restManager, "Can't create restManager");
        ClientsGroup[] cgs = new ClientsGroup[3];
        int[] groupNumber = {1, 6, 6};

        List<Table> expectedLookup = new ArrayList<>();
        expectedLookup.add(tables.get(3));
        expectedLookup.add(tables.get(0));
        expectedLookup.add(null);

        for (int i = 0; i < groupNumber.length; i++) {
            cgs[i] = new ClientsGroup(groupNumber[i]);
            assertNull(restManager.lookup(cgs[i]), "lookup: Incorrect null result for client " + i);
            restManager.onArrive(cgs[i]);
            assertEquals(expectedLookup.get(i), restManager.lookup(cgs[i]),
                    "onArrive: incorrect result for client " + i);
        }

        restManager.onLeave(cgs[1]);
        expectedLookup = new ArrayList<>();
        expectedLookup.add(tables.get(3));
        expectedLookup.add(null);
        expectedLookup.add(tables.get(0));

        for (int i = 0; i < groupNumber.length; i++) {
            assertEquals(expectedLookup.get(i), restManager.lookup(cgs[i]),
                    "post onLeave: Incorrect table for client " + i);
        }
    }
}