package model;

public class BanknoteAcceptor implements MoneyAcceptor {

    private int amount;

    public BanknoteAcceptor(int amount) {
        this.amount = amount;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String getName() {
        return "Купюры";
    }
}
