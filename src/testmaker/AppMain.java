package testmaker;

public class AppMain {
    public static void main(String[] args) {
        System.out.println("Вызов по имени");
        StartTest.start("testmaker.ExampleTest1");
        System.out.println("Вызов по объекту");
        StartTest.start(ExampleTest1.class);
    }
}
