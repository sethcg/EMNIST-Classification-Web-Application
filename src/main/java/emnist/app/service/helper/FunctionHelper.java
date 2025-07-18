package emnist.app.service.helper;

public class FunctionHelper {
    
    @FunctionalInterface
    public interface Function<T1>{
        void apply(T1 t1);
    }

    @FunctionalInterface
    public interface BiFunction<T1, T2>{
        void apply(T1 t1, T2 t2);
    }

    @FunctionalInterface
    public interface TriFunction<T1, T2, T3>{
        void apply(T1 t1, T2 t2, T3 t3);
    }

    public static void executeFunction(Integer size, Function<Integer> function) {
        for (int i = 0; i < size; i++) {
            function.apply(i);
        }
    }

    public static void executeFunction(Integer rowLength, Integer columnLength, BiFunction<Integer, Integer> function) {
        for (int x = 0; x < rowLength; x++) {
            for(int y = 0; y < columnLength; y++) {
                function.apply(x, y);
            }
        }
    }

    public static void executeFunction(Integer rowLength, Integer columnLength, Integer depthLength, TriFunction<Integer, Integer, Integer> function) {
        for (int x = 0; x < rowLength; x++) {
            for(int y = 0; y < columnLength; y++) {
                for(int k = 0; k < depthLength; k++) {
                    function.apply(x, y, k);
                }
            }
        }
    }

}
