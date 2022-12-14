import java.io.*;
import java.util.*;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static ArrayList<TopicNode> getTopics(TopicNode page) {
        ArrayList<TopicNode> topics = new ArrayList<>();
        Pattern linkPattern = Pattern.compile("<a[^>]+href=[\"']?/wiki/([^\"']+)[\"']?[^>]*>(.+?)</a>");
        Matcher linkMatcher;

        try {
            URL url = new URL("https://en.wikipedia.org/wiki/" + page.topic);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = br.readLine()) != null) {
                linkMatcher = linkPattern.matcher(line);
                while (linkMatcher.find()) {
                    if (linkMatcher.group(1).contains(":")) continue;
                    topics.add(new TopicNode(linkMatcher.group(1), page));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return topics;
    }

    public static ArrayList<String> bfs(String start, String goal) {
        //TODO: take care of redirect links
        if (start.equals(goal)) {
            ArrayList<String> path = new ArrayList<>();
            path.add(start);
            return path;
        }

        Queue<TopicNode> unvisited = new ArrayDeque<>();
        HashSet<String> visited = new HashSet<>();

        unvisited.add(new TopicNode(start));
        while (!unvisited.isEmpty()) {
            TopicNode current = unvisited.poll();

            visited.add(current.topic);
            ArrayList<TopicNode> topics = getTopics(current);
            if (topics == null) continue;

            for (TopicNode t : topics) {
                if (t.topic.equals(goal)) {
                    ArrayList<String> path = new ArrayList<>();
                    path.add(t.topic);
                    TopicNode iterate = current;

                    while (iterate != null) {
                        path.add(0, iterate.topic);
                        iterate = iterate.prev;
                    }

                    return path;
                }

                if (!visited.contains(t.topic) && !unvisited.contains(t)) {
                    unvisited.add(t);
                }
            }
        }

        return null;
    }

    public static void run() {
        Instant start = Instant.now();
        ArrayList<String> path = bfs("Gouda_cheese", "Java_(programming_language)");
        Instant end = Instant.now();
        if (path == null) {
            System.out.println("null");
            return;
        }
        for (String s : path) {
            System.out.print(s + " ");
        }
        System.out.println();
        System.out.println("time: " + (Duration.between(start, end).toMillis()/1000.0) + "s");
    }

    public static void main(String[] args) {
        run();
    }
}
