import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final AtomicInteger threeLetters = new AtomicInteger(0);
    private static final AtomicInteger fourLetters = new AtomicInteger(0);
    private static final AtomicInteger fiveLetters = new AtomicInteger(0);


    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        //Сгенерированное слово является палиндромом,
        //т. е. читается одинаково как слева направо, так и справа налево, например, abba;
        Runnable task1 = () -> {
            for (String text : texts) {
                if (text.equals(new StringBuilder(text).reverse().toString())) {
                    counterLetters(text.length());
                }
            }
        };

        // Сгенерированное слово состоит из одной и той же буквы, например, aaa;
        Runnable task2 = () -> {
            for (String text : texts) {
                if (isIdenticalLetters(text)) {
                    counterLetters(text.length());
                }
            }
        };

        // Буквы в слове идут по возрастанию:
        // сначала все a (при наличии), затем все b (при наличии), затем все c и т. д. Например, aaccc.
        Runnable task3 = () -> {
            for (String text : texts) {
                if (isIncreasingLetters(text) && !isIdenticalLetters(text)) {
                    counterLetters(text.length());
                }
            }
        };

        Thread thread1 = new Thread(task1);
        thread1.start();

        Thread thread2 = new Thread(task2);
        thread2.start();

        Thread thread3 = new Thread(task3);
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        //Вывод
        System.out.printf("Красивых слов с длиной 3: %s шт \n", threeLetters);
        System.out.printf("Красивых слов с длиной 4: %s шт \n", fourLetters);
        System.out.printf("Красивых слов с длиной 5: %s шт \n", fiveLetters);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void counterLetters(int lengthText) {
        if (lengthText == 3) {
            threeLetters.getAndIncrement();
        } else if (lengthText == 4) {
            fourLetters.getAndIncrement();
        } else {
            fiveLetters.getAndIncrement();
        }
    }

    public static boolean isIdenticalLetters(String text) {
        boolean flag = true;
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != text.charAt(i - 1)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static boolean isIncreasingLetters(String text) {
        //aaabbbcccddd
        //aaacccddd
        boolean flag = true;
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) < text.charAt(i - 1)) {
                flag = false;
                break;
            }
        }
        return flag;
    }
}