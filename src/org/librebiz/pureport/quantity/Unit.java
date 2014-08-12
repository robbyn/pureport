/*
    Pureport, a report generator for Java
    Copyright (C) 2011  Maurice Perry <maurice@perry.ch>

    Project Web Site: http://code.google.com/p/pureport/

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.librebiz.pureport.quantity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Unit implements Serializable {
    public static final Unit DEFAULT_UNIT;

    private static Map<String,Unit> UNIT_MAP = new HashMap<String,Unit>();

    public static final Unit CM = create(0, "cm");
    public static final Unit MM = create(1, "mm");
    public static final Unit IN = create(2, "in");
    public static final Unit PT = create(3, "pt");
    public static final Unit PC = create(4, "pc");
    public static final Unit PX = create(5, "px");

    private static final double CONVERSION_MATRIX[][] = {
        {   1,  10,   0,   0,   0,   0},
        {   0,   1,   0,   0,   0,   0},
        {2.54,   0,   1,  72,   0,   0},
        {   0,   0,   0,   1,   0,   0},
        {   0,   0,   0,  12,   1,   0},
        {   0,0.28,   0,   0,   0,   1},
    };

    static {
        for (int i = 0; i < CONVERSION_MATRIX.length; ++i) {
            for (int j = 0; j < CONVERSION_MATRIX.length; ++j) {
                if (CONVERSION_MATRIX[i][j] == 0) {
                    CONVERSION_MATRIX[i][j] = convert(i, j, 1);
                }
            }
        }
        DEFAULT_UNIT = 
            get(System.getProperty("org.freebiz.pureport.default-unit", "cm"));
    }

    private int id;
    private String name;

    private static Unit create(int id, String name) {
        Unit result = new Unit(id, name);
        UNIT_MAP.put(name, result);
        return result;
    }

    private Unit(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Unit get(String name) {
        return UNIT_MAP.get(name);
    }

    public static Unit[] availableUnits() {
        return new Unit[] {CM, MM, IN, PT, PC};
    }

    public final String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public double convertTo(Unit other, double value) {
        return value*CONVERSION_MATRIX[id][other.id];
    }

    private static double convert(int source, int dest, double value) {
        ArrayList stack = new ArrayList();
        stack.add(new Step(source, value));
        while (true) {
            Step s = (Step)stack.remove(0);
            value = s.value;
            source = s.id;
            if (source == dest) {
                return value;
            }
            for (int i = 0; i < CONVERSION_MATRIX.length; ++i) {
                if (i != source && CONVERSION_MATRIX[source][i] != 0) {
                    stack.add(new Step(i, value*CONVERSION_MATRIX[source][i]));
                }
            }
            for (int i = 0; i < CONVERSION_MATRIX.length; ++i) {
                if (i != source && CONVERSION_MATRIX[i][source] != 0) {
                    stack.add(new Step(i, value/CONVERSION_MATRIX[i][source]));
                }
            }
        }
    }

    private static class Step {
        private int id;
        private double value;

        Step(int id, double value) {
            this.id = id;
            this.value = value;
        }
    }
}
