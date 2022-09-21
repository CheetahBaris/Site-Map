import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {

        try {
            String url = "https://skillbox.ru";
            String dstDirectory = "data/hmwrk2.txt";
            String forkRes = new ForkJoinPool().invoke(new LinkCalculator(url, url, true));

            if (!Files.exists(Path.of("data"))) {
                Files.createDirectory(Path.of("data"));
            }
            if (!Files.exists(Path.of("data/hmwrk2.txt"))) {
                Files.createFile(Path.of(dstDirectory));
            }
            String[] t = forkRes.split(" ");
            String finRes = "";
            int count = 0;
            for (String ok : t) {
                 count++;
//                finRes += ok + "\n" + "\t".repeat(count);
                finRes += ok;

            }

            Files.write(Path.of(dstDirectory), Collections.singleton(finRes));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
