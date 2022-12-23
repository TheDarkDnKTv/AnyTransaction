package thedarkdnktv.anytransaction;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class AnyTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnyTransactionApplication.class, args);
	}

	@PostConstruct
	public void created() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
}
