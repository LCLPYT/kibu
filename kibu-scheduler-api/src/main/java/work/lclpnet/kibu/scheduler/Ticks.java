package work.lclpnet.kibu.scheduler;

public class Ticks {

    private Ticks() {}

    public static long ticks(long n) {
        return n;
    }

    public static int ticks(int n) {
        return n;
    }

    public static long seconds(long n) {
        return 20 * n;
    }

    public static int seconds(int n) {
        return 20 * n;
    }

    public static long minutes(long n) {
        return 20 * n * 60;
    }

    public static int minutes(int n) {
        return 20 * n * 60;
    }
}
