package com.iot.relay.consumer.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.function.Function;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.event.annotation.BeforeTestClass;

@WebMvcTest
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class IOTConsumerTest {

	public static String INPUT_TOPIC = "iotdatain";
	public static String OUTPUT_TOPIC = "iotdataout";
	
	@Bean
	public Function<KStream<String, String>, KStream<String, String>> toUpperCase (){
		return in -> in.map((key, val) -> KeyValue.pair(key, val.toUpperCase()));
	}

	public static EmbeddedKafkaRule embeddedKafkaRule = new EmbeddedKafkaRule(1, true, INPUT_TOPIC, OUTPUT_TOPIC);

	public static EmbeddedKafkaBroker embeddedKafka = embeddedKafkaRule.getEmbeddedKafka();

	private static KafkaTemplate<String, String> template;	

	@Autowired
	private KafkaProperties properties;

	private static Consumer<String, String> consumer;

	@BeforeTestClass
	public static void setup() {
		System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getBrokersAsString());

		Map<String, Object> senderProps = KafkaTestUtils.producerProps(embeddedKafka);
		senderProps.put("key.serializer", StringSerializer.class);
		senderProps.put("value.serializer", StringSerializer.class);
		DefaultKafkaProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(senderProps);
		template = new KafkaTemplate<>(pf, true);

		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("group-id", "false", embeddedKafka);
		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		DefaultKafkaConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
		consumer = cf.createConsumer();
		embeddedKafka.consumeFromAnEmbeddedTopic(consumer, OUTPUT_TOPIC);
	}

	@AfterEach
	public void tearDown() {
		if (consumer != null){
			consumer.close();
		}
	}
	//@Test // TO DO - need to fix an issue
	public void testSendReceive() {
		template.send(INPUT_TOPIC, "foo");
		Map<String, Object> configs = properties.buildConsumerProperties();
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, "test0544");
		configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		ConsumerRecord<String, String> cr = KafkaTestUtils.getSingleRecord(consumer, OUTPUT_TOPIC);
		assertEquals(cr.value(), "FOO");
	}

}
