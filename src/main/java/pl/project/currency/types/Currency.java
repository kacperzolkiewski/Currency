package pl.project.currency.types;

public enum Currency {
    PLN(1.0), EUR(4.71), USD(4.46);

    private double value;

    Currency(double value) {
        this.value = value;
    }

    public double getValue() {return this.value;}
}
