import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final MoneyAcceptor moneyAcceptor;

    private static boolean isExit = false;

    private AppRunner(MoneyAcceptor moneyAcceptor) {
        this.moneyAcceptor = moneyAcceptor;
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
    }

    public static void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите тип оплаты:");
        System.out.println("1 - Монеты");
        System.out.println("2 - Купюры");
        String choice = scanner.nextLine();

        MoneyAcceptor acceptor = "2".equals(choice)
                ? new BanknoteAcceptor(100)
                : new CoinAcceptor(100);

        AppRunner app = new AppRunner(acceptor);

        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        print("В автомате доступны:");
        showProducts(products);

        print(moneyAcceptor.getName() + " на сумму: " + moneyAcceptor.getAmount());

        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        print("Выберите действие: ");
        chooseAction(allowProducts);

    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (moneyAcceptor.getAmount() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }

        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        print(" a - Пополнить баланс");
        showActions(products);
        print(" h - Выйти");
        print("Введите букву для выбора товара: ");
        String action = fromConsole().substring(0, 1);

        if ("a".equalsIgnoreCase(action)) {
            moneyAcceptor.setAmount(moneyAcceptor.getAmount() + 10);
            print("Вы пополнили баланс на 10 через " + moneyAcceptor.getName());
            return;
        }


        if ("h".equalsIgnoreCase(action)) {
            isExit = true;
            return;
        }

        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    moneyAcceptor.setAmount(moneyAcceptor.getAmount() - products.get(i).getPrice());
                    print("Вы купили " + products.get(i).getName());
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            print("Недопустимая буква. Попробуйте еще раз.");
            chooseAction(products);
        }
    }


    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
