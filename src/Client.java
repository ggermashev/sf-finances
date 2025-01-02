import Models.PaymentModel;
import java.util.*;

public class Client {
    static Server server;
    static Scanner scanner = new Scanner(System.in);
    static UUID accessToken = null;

    public static void main(String[] args) {
        try (Server _server = new Server()) {
            server = _server;

            while (true) {
                Client.printMenu();

                int choice = -1;
                try {
                    choice = scanner.nextInt();
                } catch (Exception e) {
                    scanner.nextLine();
                }

                System.out.println();

                switch (choice) {
                    case 1:
                        createAccountHandler();
                        break;
                    case 2:
                        loginHandler();
                        break;
                    case 3:
                        logoutHandler();
                        break;
                    case 4:
                        addIncomesHandler();
                        break;
                    case 5:
                        addExpensesHandler();
                        break;
                    case 6:
                        addBudgetHandler();
                        break;
                    case 7:
                        getIncomesHandler();
                        break;
                    case 8:
                        getExpensesHandler();
                        break;
                    case 9:
                        getBudgetHandler();
                        break;
                    case 10:
                        getRestBudgetHandler();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Введен некорректный пункт меню");
                        break;
                }
            }
        } catch (Exception e) {}


    }

    private static void printMenu() {
        System.out.println("\nВыберите действие:");
        System.out.println("    1. Создать аккаунт");
        System.out.println("    2. Войти в аккаунт");
        System.out.println("    3. Выйти из аккаунта");
        System.out.println("    4. Добавить доходы");
        System.out.println("    5. Добавить расходы");
        System.out.println("    6. Добавить бюджет");
        System.out.println("    7. Получить список доходов");
        System.out.println("    8. Получить список расходов");
        System.out.println("    9. Получить список бюджетов");
        System.out.println("    10. Получить список оставшихся лимитов");
        System.out.println("    0. Выход из приложения");
        System.out.println();
    }

    private static void createAccountHandler() {
        System.out.println("Для создания аккаунта ведите логин и пароль");
        System.out.print("Ваш логин: ");
        String login = scanner.next();
        System.out.print("Ваш пароль: ");
        String password = scanner.next();

        Map params = new HashMap();
        params.put("login", login);
        params.put("password", password);
        Boolean success = false;
        try {
            success = (Boolean) server.call("/user/create", params);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (success) {
                System.out.println("Аккаунт создан.");
            } else {
                System.out.println("Не удалось создать аккаунт.");
            }
        }
    }

    private static void loginHandler() {
        System.out.println("Для входа в аккаунт ведите логин и пароль");
        System.out.print("Ваш логин: ");
        String login = scanner.next();
        System.out.print("Ваш пароль: ");
        String password = scanner.next();

        Map params = new HashMap();
        params.put("login", login);
        params.put("password", password);
        UUID token = null;
        try {
            token = (UUID) server.call("/user/login", params);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (token != null) {
                accessToken = token;
                System.out.println("Вы вошли в аккаунт.");
            } else {
                System.out.println("Не удалось войти в аккаунт.");
            }
        }
    }

    private static void logoutHandler() {
        Map params = new HashMap();
        params.put("accessToken", accessToken);
        Boolean success = false;
        try {
            success = (Boolean) server.call("/user/logout", params);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (success) {
                accessToken = null;
                System.out.println("Вы вышли из аккаунта.");
            } else {
                System.out.println("Не удалось выйти из аккаунта");
            }
        }
    }

    private static void addIncomesHandler() {
        System.out.println("Введите категорию и сумму дохода");
        System.out.print("Категория: ");
        String category = scanner.next();
        System.out.print("Сумма дохода: ");
        int amount = scanner.nextInt();

        Map params = new HashMap();
        params.put("category", category);
        params.put("amount", amount);
        params.put("accessToken", accessToken);

        Boolean success = false;
        try {
            success = (Boolean) server.call("/wallet/incomes/add", params);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (success) {
                System.out.println("Доход добавлен.");
            } else {
                System.out.println("Не удалось добавить доход");
            }
        }
    }

    private static void addExpensesHandler() {
        System.out.println("Введите категорию и сумму расходов");
        System.out.print("Категория: ");
        String category = scanner.next();
        System.out.print("Сумма расходов: ");
        int amount = scanner.nextInt();

        Map params = new HashMap();
        params.put("category", category);
        params.put("amount", amount);
        params.put("accessToken", accessToken);

        Boolean success = false;
        Integer totalIncome = null;
        Integer restBudget = null;
        try {
            success = (Boolean) server.call("/wallet/expenses/add", params);
            totalIncome = (Integer) server.call("/wallet/incomes/total", params);
            restBudget = (Integer) server.call("/wallet/budget/rest/by_category", params);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (success) {
                System.out.println("Расход добавлен");
                if (totalIncome != null && totalIncome < 0) {
                    System.out.println("Сумма расходов превысила сумму доходов");
                }
                if (restBudget != null && restBudget < 0) {
                    System.out.println("Сумма расходов по категории " + category + " превысила установленный бюджет.");
                }
            } else {
                System.out.println("Не удалось добавить расход");
            }
        }
    }

    private static void addBudgetHandler() {
        System.out.println("Введите категорию и бюджет");
        System.out.print("Категория: ");
        String category = scanner.next();
        System.out.print("Бюджет: ");
        int amount = scanner.nextInt();

        Map params = new HashMap();
        params.put("category", category);
        params.put("amount", amount);
        params.put("accessToken", accessToken);

        Boolean success = false;
        try {
            success = (Boolean) server.call("/wallet/budget/add", params);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (success) {
                System.out.println("Бюджет добавлен");
            } else {
                System.out.println("Не удалось добавить бюджет");
            }
        }
    }

    private static void getIncomesHandler() {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\\W");
        System.out.println("Введите категорию дохода или пропустите ввод, чтобы получить результат по всем категориям.");
        System.out.print("Категория: ");
        String category = scanner.next();
        if (Objects.equals(category, "")) {category = null;}

        Map params = new HashMap();
        params.put("category", category);
        params.put("accessToken", accessToken);

        try {
            var incomes = (ArrayList<PaymentModel>) server.call("/wallet/incomes/get", params);
            if (incomes.isEmpty()) {
                System.out.println("Список доходов пуст!");
                return;
            }
            if (category != null) {
                System.out.println("Доход по категории " + category + ": " + incomes.stream().map(income -> income.amount).reduce(Integer::sum).get());
            } else {
                int total = 0;
                for (var income: incomes) {
                    total += income.amount;
                }
                System.out.println("Доход по всем категориям: " + total);
                incomes.forEach(income -> System.out.println(income.getReadable()));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void getExpensesHandler() {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\\W");
        System.out.println("Введите категорию расхода или пропустите ввод, чтобы получить результат по всем категориям.");
        System.out.print("Категория: ");
        String category = scanner.next();
        if (Objects.equals(category, "")) {category = null;}

        Map params = new HashMap();
        params.put("category", category);
        params.put("accessToken", accessToken);

        try {
            var expenses = (ArrayList<PaymentModel>) server.call("/wallet/expenses/get", params);
            if (expenses.size() == 0) {
                System.out.println("Список расходов пуст!");
                return;
            }
            if (category != null) {
                System.out.println("Расход по категории " + category + ": " + expenses.stream().map(expense -> expense.amount).reduce(Integer::sum).get());
            } else {
                int total = 0;
                for (var expense: expenses) {
                    total += expense.amount;
                }
                System.out.println("Расходы по всем категориям: " + total);
                expenses.forEach(expense -> System.out.println(expense.getReadable()));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void getBudgetHandler() {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\\W");
        System.out.println("Введите категорию бюджета или пропустите ввод, чтобы получить результат по всем категориям.");
        System.out.print("Категория: ");
        String category = scanner.next();
        if (Objects.equals(category, "")) {category = null;}

        Map params = new HashMap();
        params.put("accessToken", accessToken);

        try {
            var budget = (Map<String, Integer>) server.call("/wallet/budget/get", params);
            if (category != null && budget.get(category) == null) {
                System.out.println("Бюджет пуст!");
                return;
            }
            if (category != null) {
                System.out.println("Бюджет по категории " + category + ": " + budget.get(category));
            } else {
                int total = 0;
                for (var amount: budget.values()) {
                    total += amount;
                }
                System.out.println("Бюджет по всем категориям: " + total);
                budget.keySet().forEach(key -> {
                    int amount = budget.get(key);
                    System.out.println(key + ": " + amount);
                });
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void getRestBudgetHandler() {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\\W");
        System.out.println("Введите категорию бюджета или пропустите ввод, чтобы получить результат по всем категориям.");
        System.out.print("Категория: ");
        String category = scanner.next();
        if (Objects.equals(category, "")) {category = null;}

        Map params = new HashMap();
        params.put("category", category);
        params.put("accessToken", accessToken);

        try {
            if (category != null) {
                var budget = (Map<String, Integer>) server.call("/wallet/budget/rest/by_category", params);
                System.out.println("Оставшийся лимит по категории " + category + ": " + budget.get(category));
            } else {
                var budget = (Map<String, Integer>) server.call("/wallet/budget/rest", params);
                int total = 0;
                for (var amount: budget.values()) {
                    total += amount;
                }
                System.out.println("Оставшиеся лимиты по всем категориям: " + total);
                budget.keySet().forEach(key -> {
                    int amount = budget.get(key);
                    System.out.println(key + ": " + amount);
                });
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }


}
