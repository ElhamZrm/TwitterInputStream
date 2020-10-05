package cs523.TwitterStream;

/**
 * Hello world!
 *
 */
public  class KafkaConfiguration 
{
    public static final String SERVERS = "localhost:9090"; //have server for kafaka and topic
    public static final String TOPIC = "health-tweets"; //can change it
    public static final long  SLEEP_TIMER = 1000; //can change it to 5000
}
