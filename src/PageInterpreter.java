import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class PageInterpreter {
    public static ArrayList<String> getTopics(String pageName) throws IOException{
        URL url = new URL("https://en.wikipedia.org/wiki/" + pageName);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
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

    public static void main(String[] args) throws IOException {
        getTopics("Gouda_cheese");
    }
}
