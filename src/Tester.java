import Controllers.ControllerTester;
import Database.DatabaseTester;
import Router.RouterTester;

public class Tester {
    public static void main(String[] args) {
        DatabaseTester databaseTester = new DatabaseTester();
        RouterTester routerTester = new RouterTester();
        ControllerTester controllerTester = new ControllerTester();

        databaseTester.test();
        routerTester.test();
        controllerTester.test();
    }
}
