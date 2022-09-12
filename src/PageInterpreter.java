import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageInterpreter {
    public static ArrayList<String> getTopics(String pageName) {
        StringBuilder sb;

        try {
            URL url = new URL("https://en.wikipedia.org/wiki/" + pageName);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String wikiPage = sb.toString();
        Pattern linkPattern = Pattern.compile("<a[^>]+href=[\"']?/wiki/([^\"']+)[\"']?[^>]*>(.+?)</a>");
        Matcher linkMatcher = linkPattern.matcher(wikiPage);

        ArrayList<String> topics = new ArrayList<>();
        while (linkMatcher.find()) {
            if (linkMatcher.group(1).contains(":")) continue;
            topics.add(linkMatcher.group(1));
        }
        return topics;
    }

    public static ArrayList<String> findPath(String start, String goal) {
        //TODO: better runtime (DFS) - tree?
        ArrayList<String> topics = getTopics(start);
        if (topics == null) return null;
        ArrayList<String> path = new ArrayList<>();
        path.add(start);

        if (start.equals(goal)) {
            return path;
        }

        for (String s : topics) {
            if (s.equals(goal)) {
                path.add(s);
                return path;
            }
        }

        for (String s : topics) {
            path = findPath(s, goal);
            if (path != null) {
                path.add(0, start);
                return path;
            }
        }

        return null;
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        ArrayList<String> path = findPath("Gouda_cheese", "Company");
        Instant end = Instant.now();
        if (path == null) {
            System.out.println("null");
            return;
        }
        for (String s : path) {
            System.out.print(s);
        }
        System.out.println("time: " + (Duration.between(start, end).toMillis()/1000.0) + "s");
    }
}
