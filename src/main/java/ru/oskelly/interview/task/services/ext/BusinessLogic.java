package ru.oskelly.interview.task.services.ext;

public class BusinessLogic {
    public static void sleepAndRandomThrowRuntimeException(int seconds, int exceptionProbabilityProc) {
        try {
            Thread.sleep((long) (seconds * 1000 * Math.random()));
        } catch (InterruptedException ignored) {
        }
        int randomProc = (int) (100 * Math.random());
        if (exceptionProbabilityProc > randomProc) throw new RuntimeException();
    }

    public static void doSomeWorkOnNotification() {
        sleepAndRandomThrowRuntimeException(2, 10);
    }

    public static void doSomeWorkOnCommentCreation() {
        sleepAndRandomThrowRuntimeException(1, 30);
    }
}
