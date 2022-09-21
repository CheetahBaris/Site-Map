import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.RecursiveTask;



public class LinkCalculator extends RecursiveTask<String> {
    private String way;
    private final String extension;
    private int random = new Random().nextInt(100, 150);
    private boolean isLikeInTheExample;
     private Vector<String> vector = new Vector<>();


    public LinkCalculator(String way, String extension, boolean isLikeInTheExample) {
        this.isLikeInTheExample = isLikeInTheExample;
        this.way = way;
        this.extension = extension;
    }


    @Override
    protected String compute() {
        String list = way;
        List<LinkCalculator> tasks = new ArrayList<>();


        try {

            Thread.sleep(random);
            String newLink = way;


            Document document = Jsoup.connect(newLink)
                    .userAgent("Yowser/2.5 Safari/537.36")
                    .referrer("https://Yandex.ru").maxBodySize(0).get();

            Elements element = document.select("a[href~=/.]");
             ArrayList<String> newLinks = new ArrayList<>(element.eachAttr("href"));
            List<String> nextLink = new ArrayList<>();




            if (isLikeInTheExample) {
                boolean isFirstIteration;

                if (newLink.equals(extension)) {
                    isFirstIteration = true;
                    newLinks.removeIf(l -> !l.startsWith(extension));
//                    System.out.println(newLinks);

                } else {

                    isFirstIteration = false;
                    newLinks.removeIf(l -> l.contains("http"));




                }



                for (int i = 0; i < newLinks.size(); i++) {

                    String currLink = newLink.replace(extension, "").replaceAll("\\w", "")
                            .replaceAll("[:.-]+", "").trim();

                    String fromNewLinks = newLinks.get(i).replaceAll("\\w", "")
                            .replaceAll("[:.-]+", "").trim();
                    if (fromNewLinks.length() > currLink.length()
                            && newLinks.get(i).startsWith(newLink.replace(extension, ""))) {
                        if(!nextLink.contains(newLinks.get(i))){
                            nextLink.add(newLinks.get(i));
                        }

                    }


                }


                 for(String links:nextLink) {
                    if (links.equals(newLinks.get(newLinks.size() - 1))) {

                        getPool().shutdown();

                    } else {

                        if (!links.endsWith(".pdf")) {
                            if (links.contains(extension)) {
                                LinkCalculator task = new LinkCalculator(links, extension, isLikeInTheExample);
                                task.fork();
                                tasks.add(task);
                            } else {
                                LinkCalculator task = new LinkCalculator(extension + links, extension, isLikeInTheExample);
                                task.fork();
                                tasks.add(task);
                            }
                        }

                    }
                }
            }

            for (LinkCalculator item : tasks) {

                if(!item.join().isEmpty()){
                    list += "\n"+ ("\t".repeat(item.join().replaceAll("\\w", "")
                            .replaceAll("[:.\\-=?]+", "").trim().length()-2)) + item.join();
                }


              }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
           return list;
    }

    public String getWay() {
        return way;
    }

    public String getExtension() {
        return extension;
    }


}

