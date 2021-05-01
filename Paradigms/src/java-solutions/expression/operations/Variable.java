package expression.operations;

import expression.GenericExpression;
import expression.calculators.Calculator;
import expression.exceptions.ParseException;

public class Variable implements GenericExpression {
    private final String name;

    public Variable(final String name) {
        if (!name.equals("x") && !name.equals("y") && !name.equals("z")) {
            throw new ParseException("Incorrect variable name: " + name + ". Supports: x, y, z");
        }
        this.name = name;
    }

    @Override
    public <T> T evaluate(final T x, final T y, final T z, final Calculator<T> calculator) {
        switch (name) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
        }
        throw new AssertionError("Unexpected variable: " + name);
    }
}
