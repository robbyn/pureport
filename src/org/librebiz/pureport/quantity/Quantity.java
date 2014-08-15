package org.librebiz.pureport.quantity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Quantity implements Comparable<Quantity>, Serializable {
    public static Quantity ZERO = new Quantity(0, Unit.DEFAULT_UNIT);

    private static final Pattern QUANTITY_PATTERN = buildPattern();

    private double value;
    private Unit unit;

    public Quantity(double value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public Quantity(double value, String unitName) {
        this(value, Unit.get(unitName));
    }

    public Quantity(double value) {
        this(value, Unit.DEFAULT_UNIT);
    }

    public double getValue() {
        return value;
    }

    public double getValue(Unit resultUnit) {
        return resultUnit == unit ? value : unit.convertTo(resultUnit, value);
    }

    public double getValue(String resultUnit) {
        return getValue(Unit.get(resultUnit));
    }

    public Unit getUnit() {
        return unit;
    }

    public String getUnitName() {
        return unit.getName();
    }

    @Override
    public int hashCode() {
        long v = Double.doubleToLongBits(getValue(Unit.MM));
        return (int)v ^ (int)(v >>> 32);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        return compareTo((Quantity)other) == 0;
    }

    @Override
    public int compareTo(Quantity other) {
        if (other == null) {
            return 1;
        }
        if (other.unit != unit) {
            other = other.convertTo(unit);
        }
        return Double.compare(value, other.value);
    }

    @Override
    public String toString() {
        return getNumberFormat().format(value) + getUnitName();
    }

    public Quantity convertTo(Unit newUnit) {
        return new Quantity(unit.convertTo(newUnit, value), newUnit);
    }

    public Quantity convertTo(String name) {
        return convertTo(Unit.get(name));
    }

    public static Quantity parse(String s, Unit unit) {
        Matcher matcher = QUANTITY_PATTERN.matcher(s);
        if (!matcher.matches()) {
            throw new NumberFormatException("Invalid quantity " + s);
        }
        String mantissa = matcher.group(1);
        if (mantissa == null || mantissa.length() == 0) {
            mantissa = "0";
        }
        String decs = matcher.group(2);
        if (decs == null) {
            decs = "";
        }
        String units = matcher.group(3);
        if (units != null && units.length() > 0) {
            unit = Unit.get(units.toLowerCase());
        }
        BigDecimal d = new BigDecimal(new BigInteger(mantissa + decs), decs.length());
        return new Quantity(d.doubleValue(), unit);
    }

    public static Quantity parse(String s, String defaultName) {
        return parse(s, Unit.get(defaultName));
    }

    public static Quantity parse(String s) {
        return parse(s, Unit.DEFAULT_UNIT);
    }

    private static Pattern buildPattern() {
        StringBuilder buf = new StringBuilder("\\s*(\\d*)(?:\\.(\\d*))?\\s*(");
        Unit units[] = Unit.availableUnits();
        buf.append(units[0].getName());
        for (int i = 1; i < units.length; ++i) {
            buf.append('|');
            buf.append(units[i].getName());
        }
        buf.append(")?\\s*");
        return Pattern.compile(buf.toString(), Pattern.CASE_INSENSITIVE);
    }

    private static DecimalFormat getNumberFormat() {
        DecimalFormat numberFormat
                = new DecimalFormat("0.########");

        numberFormat.getDecimalFormatSymbols().setDecimalSeparator('.');
        return numberFormat;
    }
}
