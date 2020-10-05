package cs523.TwitterStream;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class TwitterKafkaProducer {

	public TwitterKafkaProducer(){
		Authentication auth = new OAuth1(
				TwitterConfiguration.CONSUMER_KEY,
				TwitterConfiguration.CONSUMER_SECRET,
				TwitterConfiguration.ACCESS_TOKEN,
				TwitterConfiguration.TOKEN_SECRET);
		
		StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
		endpoint.trackTerms(Collections.singletonList(TwitterConfiguration.HASHTAG));
		BlockingQueue<String> queue = new LinkedBlockingQueue<>(10000);
		
		Client client = new ClientBuilder()
		.hosts(Constants.STREAM_HOST)
		.authentication(auth)
		.endpoint(endpoint)
		.processor(new StringDelimitedProcessor(queue))
		.build();
		
		Gson gson = new Gson();
		
		client.connect();
		//publishing from twitter to kafka
		try(Producer<Long, String> producer = getProducer()){
			while(true){
				Tweeter tweet = gson.fromJson(queue.take(), Tweeter.class);
				System.out.println("Tweet Id: " + tweet.toString());
				long key = tweet.getId();
				String msg = gson.toJson(tweet);
				
				ProducerRecord<Long, String> record = new ProducerRecord<>(KafkaConfiguration.TOPIC, key, msg);
				producer.send(record);
			}
		}
		catch (InterruptedException e){
			e.printStackTrace();
		}
		finally{
			client.stop();
		}
	}
	
	private Producer<Long, String> getProducer(){
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,  KafkaConfiguration.SERVERS);
		properties.put(ProducerConfig.ACKS_CONFIG,  "1");
		properties.put(ProducerConfig.LINGER_MS_CONFIG, 500);
		properties.put(ProducerConfig.RETRIES_CONFIG, 1); //can configure
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,  LongSerializer.class.getName()); //key from twitter line 52
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());//msg from twitter line 53
		
		return new KafkaProducer(properties);
	}
}
