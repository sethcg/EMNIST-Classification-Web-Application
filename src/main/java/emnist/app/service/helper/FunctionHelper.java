package emnist.app.service.helper;

public class FunctionHelper {
    
    @FunctionalInterface
    public interface Function<T1, R>{
        public R apply(T1 t1);
    }

    @FunctionalInterface
    public interface BiFunction<T1, T2, R>{
        public R apply(T1 t1, T2 t2);
    }

    public static void executeFunction(Integer size, Function<Integer, Void> function) {
        for (int i = 0; i < size; i++) {
            function.apply(i);
        }
    }

    public static void executeFunction(Integer rowLength, Integer columnLength, BiFunction<Integer, Integer, Void> function) {
        for (int x = 0; x < rowLength; x++) {
            for(int y = 0; y < columnLength; y++) {
                function.apply(x, y);
            }
        }
    }

}
