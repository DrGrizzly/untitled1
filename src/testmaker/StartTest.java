package testmaker;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//Класс для проведения тестирования
public class StartTest {

    public static void start(String classNameStr) {
        try {
            start(Class.forName(classNameStr));
        } catch (ClassNotFoundException e) {
            System.out.println("Не найден указанный класс");
        }
    }

    public static void start(Class className) {
        List<Method> methodArr = Arrays.asList(className.getMethods());
        List<Method> beforeSuite = getMethodArrByName(BeforeSuite.class, methodArr);
        List<Method> afterSuite = getMethodArrByName(AfterSuite.class, methodArr);
        //проверим, что методы есть в классе и их количество равно 1
        if (beforeSuite.size() != 1 || afterSuite.size() != 1){
            throw new RuntimeException("Класс не содержит методов с аннотацией BeforeSuite и AfterSuite или их больше 1");
        }
        //Отсортируем все методы с аннотацией Тест по порядку их приоритетов
        Map<Integer, Method> testList = sortByPrior(methodArr);
        if (testList.size() ==0){
            throw new RuntimeException("Не найден ни один метод с аннотацией Test");
        }

        try {
            Object classTest = className.getConstructor().newInstance();
            //Исполняем метод с аннотацией BeforeSuite
             beforeSuite.get(0).invoke(classTest);
            //Выполняем методы Test по их порядку
            for (int currTest: testList.keySet().stream().sorted().collect(Collectors.toList())){
                testList.get(currTest).invoke(classTest);
            }
            //Исполняем метод c аннотацией AfterSuite
            afterSuite.get(0).invoke(classTest);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static Map<Integer,Method> sortByPrior(List<Method> methodArr){
        return getMethodArrByName(Test.class,methodArr).stream()
                .collect(Collectors.toMap(
                        method -> method.getAnnotation(Test.class).prior(),
                        method -> method));
    }

    private static List<Method> getMethodArrByName(Class<? extends Annotation> annotationType, List<Method> methodArr) {
        return methodArr.stream().filter(a -> Arrays.stream(a.getAnnotations())
                .anyMatch(f -> f.annotationType().getName().equals(annotationType.getName())))
                .collect(Collectors.toList());
    }

}
