package testmaker;

public class ExampleTest1 {
    @BeforeSuite
    public void startBefore(){
        System.out.println("Запустился метод startBefore");
    }

    @Test(prior = 0)
    public void exampleStart1(){
        System.out.println("Запустился метод exampleStart1");
    }

    @Test(prior = 3)
    public void exampleStart2(){
        System.out.println("Запустился метод exampleStart2");
    }

    @Test(prior = 1)
    public void exampleStart3(){
        System.out.println("Запустился метод exampleStart3");
    }

    //спец-метод без аннотации для теста
    public void exampleStart4(){
        System.out.println("Запустился метод exampleStart4");
    }

    @AfterSuite
    public void startAfter(){
        System.out.println("Запустился метод startAfter");
    }

}
