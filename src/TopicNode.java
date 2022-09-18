public class TopicNode {
    String topic;
    TopicNode prev = null;

    public TopicNode(String topic) {
        this.topic = topic;
    }
    public TopicNode(String topic, TopicNode prev) {
        this.topic = topic;
        this.prev = prev;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TopicNode && topic.equals(((TopicNode) o).topic);
    }
}
