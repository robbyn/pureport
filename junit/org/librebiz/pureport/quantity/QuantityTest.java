package org.librebiz.pureport.quantity;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuantityTest {
    private Random random = new Random();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test of getValue method, of class org.freebiz.pureport.quantity.Quantity.
     */
    @Test
    public void testGetValue() {
        assertEquals(new Quantity(10, "cm").getValue(), 10.0, 0.00001);
    }

    /**
     * Test of getUnit method, of class org.freebiz.pureport.quantity.Quantity.
     */
    @Test
    public void testGetUnit() {
        assertSame(new Quantity(10, "cm").getUnit(), Unit.CM);
    }

    /**
     * Test of getUnitName method, of class org.freebiz.pureport.quantity.Quantity.
     */
    @Test
    public void testGetUnitName() {
        assertEquals(new Quantity(1, "cm").getUnitName(), "cm");
    }

    /**
     * Test of toString method, of class org.freebiz.pureport.quantity.Quantity.
     */
    @Test
    public void testToString() {
        String s = new Quantity(10, "cm").toString();
        assertEquals(s, "10cm");
        s = new Quantity(10.2, "cm").toString();
        assertEquals(s, "10.2cm");
        s = new Quantity(0, "cm").toString();
        assertEquals(s, "0cm");
    }

    private double cvt(double value, String unit1,
            String unit2) {
        Quantity q = new Quantity(value, unit1);
        q = q.convertTo(unit2);
        assertEquals(q.getUnitName(), unit2);
        return q.getValue();
    }

    /**
     * Test of convertTo method, of class org.freebiz.pureport.quantity.Quantity.
     */
    @Test
    public void testConvertTo() {
        double eps = 0.000001;
        // TODO add your test code below by replacing the default call to fail.
        assertEquals(cvt(1, "cm", "cm"), 1, eps);
        assertEquals(cvt(1, "mm", "mm"), 1, eps);
        assertEquals(cvt(1, "in", "in"), 1, eps);
        assertEquals(cvt(1, "pt", "pt"), 1, eps);
        assertEquals(cvt(1, "pc", "pc"), 1, eps);
        assertEquals(cvt(1, "px", "px"), 1, eps);
        assertEquals(cvt(10, "mm", "cm"), 1, eps);
        assertEquals(cvt(1, "cm", "mm"), 10, eps);
        assertEquals(cvt(2.54, "cm", "in"), 1, eps);
        assertEquals(cvt(25.4, "mm", "in"), 1, eps);
        assertEquals(cvt(0.28, "mm", "px"), 1, eps);
        Unit units[] = Unit.availableUnits();
        for (int i = 0; i < 10000; ++i) {
            double value = random.nextDouble();
            String u1 = units[random.nextInt(units.length)].getName();
            String u2 = units[random.nextInt(units.length)].getName();
            assertEquals(cvt(cvt(value, u1, u2), u2,  u1), value, eps*value);
        }
    }

    /**
     * Test of parse method, of class org.freebiz.pureport.quantity.Quantity.
     */
    @Test
    public void testParse() {
        assertEquals(Quantity.parse(".cm"), new Quantity(0, "cm"));
        assertEquals(Quantity.parse("0cm"), new Quantity(0, "cm"));
        assertEquals(Quantity.parse("10cm"), new Quantity(10, "cm"));
    }    
}
