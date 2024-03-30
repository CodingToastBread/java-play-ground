package coding.toast.bread.converting;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.Builder;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

/**
 * Read, Write CSV Using Jackson CSV Library
 */
public class JacksonCsvPojoConvertTests {
	@Test
	@Description("Reading CSV File and convert to Iterator which contains POJO")
	void readTest() {
		// read file
		var csvFile = new ClassPathResource("coding/toast/bread/sample_csv/customers-100.csv");
		
		// there is header row in our csv!
		CsvSchema schema = CsvSchema.emptySchema().withHeader();
		
		// create Reader For Csv File
		ObjectReader objectReader = new CsvMapper().readerFor(Customer.class).with(schema);
		
		try (InputStream inputStream = csvFile.getInputStream()) {
			
			MappingIterator<Customer> iterator = objectReader.readValues(inputStream);
			while (iterator.hasNext()) {
				System.out.println("next = " + iterator.next());
			}
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	@Description("Writing list of pojo to CSV")
	void writeTest() {
		
		// need to create schema for csv file's first row definition
		CsvSchema schema = CsvSchema.builder().
			addColumn("firstName").addColumn("lastName").build()
			.withHeader();
		
		// create writer
		ObjectWriter writer =
			new CsvMapper()
				.writerFor(new TypeReference<List<Name>>() {})
				.with(schema);
		
		// create List of Pojo to Insert into CSV File (or outputStream, Writer, etc...)
		List<Name> names = List.of(
			Name.builder().firstName("daily").lastName("code").build(),
			Name.builder().firstName("coding").lastName("toast").build(),
			Name.builder().firstName("hello").lastName("world").build()
		);
		
		// Write Csv Data
		try {
			StringWriter stringWriter = new StringWriter();
			writer.writeValue(stringWriter, names);
			System.out.println(stringWriter);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	record Customer(
		@JsonProperty("Index")
		String index,
		@JsonProperty("Customer Id")
		String customerId,
		@JsonProperty("First Name")
		String firstName,
		@JsonProperty("Last Name")
		String lastName,
		@JsonProperty("Company")
		String company,
		@JsonProperty("City")
		String city,
		@JsonProperty("Country")
		String country,
		@JsonProperty("Phone 1")
		String phone1,
		@JsonProperty("Phone 2")
		String phone2,
		@JsonProperty("Email")
		String email,
		@JsonProperty("Subscription Date")
		String subscriptionDate,
		@JsonProperty("Website")
		String website
	) {}
	
	@Builder
	record Name(
		@JsonProperty("firstName") String firstName,
		@JsonProperty("lastName") String lastName
	) {}
	
}
