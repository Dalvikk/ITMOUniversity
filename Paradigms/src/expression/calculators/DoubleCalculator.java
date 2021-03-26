package expression.calculators;

public class DoubleCalculator implements Calculator<Double> {
    @Override
    public Double add(final Double x, final Double y) {
        return x + y;
    }

    @Override
    public Double sub(final Double x, final Double y) {
        return x - y;
    }

    @Override
    public Double mul(final Double x, final Double y) {
        return x * y;
    }

    @Override
    public Double div(final Double x, final Double y) {
        return x / y;
    }

    @Override
    public Double neg(final Double x) {
        return -x;
    }

    @Override
    public Double parse(final String s) {
        return Double.parseDouble(s);
    }

    @Override
    public Double mod(final Double x, final Double y) {
        return x % y;
    }

    @Override
    public Double abs(final Double x) {
        return Math.abs(x);
    }

    @Override
    public Double square(final Double x) {
        return x * x;
    }
}
