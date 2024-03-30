package coding.toast.bread.converting;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

/**
 * Converting Xml to Pojo (=record) and Pojo to Xml Using Jackson Mapper.
 */
@Slf4j
public class JacksonXmlToPojoConvertWithRecordTests {
	
	@Test
	void convertXmlPojoUsingRecordTest() {
		// Read Sample XML File
		ClassPathResource xmlResource = new ClassPathResource("coding/toast/bread/xml_pojo_convert/complicate.xml");
		
		// Create Xml Mapper
		XmlMapper xmlMapper = new XmlMapper(
			new XmlFactory().configure(
				// this option will append "<?xml version='1.0' encoding='UTF-8'?>" at first line
				// while using XmlMapper writeValue method
				ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true));
		
		// just in case if the matching field is not found in pojo
		xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		// read InputStream
		try (InputStream inputStream = xmlResource.getInputStream()) {
			
			// Convert XML ==> POJO
			XmlReadPojo pojo = xmlMapper.readValue(inputStream, XmlReadPojo.class);
			
			log.info("XML => POJO Convert result ...\n");
			log.info("header = \n{}\n", pojo.header());
			
			log.info("body = \n{}\n", pojo.body());
			
			var buildingInfos = pojo.body.buildingInfos();
			buildingInfos.forEach(buildingInfo -> log.info("building infos {}", buildingInfo));
			
			// Convert POJO ==> XML (string)
			StringWriter stringWriter = new StringWriter();
			xmlMapper.writerWithDefaultPrettyPrinter()
				.writeValue(stringWriter, pojo);
			
			String string = stringWriter.toString();
			log.info("\nPOJO => XML String result ... \n{}", string);
			
		} catch (IOException e) {
			log.error("Oops!", e);
		}
		
	}
	
	record XmlReadPojo(
		@JsonProperty("HEADER")
		Header header,
		@JsonProperty("BODY")
		Body body
	){}
	
	
	record Header(
		@JsonProperty("CODE") String code,
		@JsonProperty("MESSAGE") String message
	){}
	
	
	record Body(
		@JsonProperty("BLDG_LIST") @JacksonXmlElementWrapper
		List<BuildingInfo> buildingInfos){}
	
	record BuildingInfo(
		@JsonProperty("ZIPCODE")
		String zipCode,
		@JsonProperty("BLDG_NM")
		String buildingName){}

}
