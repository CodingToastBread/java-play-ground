package coding.toast.bread.xml_pojo_convert;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.fail;

/**
 * A test class for converting between XML and POJO.<br>
 * By the way, the pre-generated XML files are located in the <br>
 * <a href="../../../../../resources/coding/toast/bread/xml_pojo_convert/">xml_pojo_convert directory</a>
 */
@Slf4j
public class XmlPojoConvertTest {
	
	@Test
	@DisplayName("converting simple xml file to pojo (= unmarshalling)")
	void simpleXmlToPojoConvertingTest() {
		try (InputStream is = this.getClass().getResourceAsStream("./simple.xml")) {
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			
			Person unmarshal = (Person) unmarshaller.unmarshal(is);
			log.debug("unmarshal = " + unmarshal);
			
		} catch (IOException | JAXBException e) {
			fail(e.getMessage());
		}
		
	}
	
	/**
	 * Pojo Matching with simple.xml file
	 */
	@XmlRootElement(name = "PERSON")
	@XmlAccessorType(XmlAccessType.FIELD)
	@Data
	private static class Person {
		
		@XmlElement(name = "NAME")
		private String name;
		
		@XmlElement(name = "AGE")
		private Integer age;
	}
	
	
	
	
	@Test
	@DisplayName("converting complicating xml file to pojo (= unmarshalling)")
	void complicateXmlToPojoConvertingTest() {
		try (InputStream is = this.getClass().getResourceAsStream("./complicate.xml")) {
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Response.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			
			Response buildingInfoResponse = (Response) unmarshaller.unmarshal(is);
			log.debug("header info = {}", buildingInfoResponse.getHeader());
			log.debug("body info = {}", buildingInfoResponse.getBody());
			log.debug("building list info = {}", buildingInfoResponse.getBody().getBldgList());
			
		} catch (IOException | JAXBException e) {
			fail(e.getMessage());
		}
		
	}
	
	
	@XmlRootElement(name = "RESPONSE")
	@XmlAccessorType(XmlAccessType.FIELD)
	@Data
	private static class Response {
		
		@XmlElement(name = "HEADER")
		private Header header;
		
		@XmlElement(name = "BODY")
		private Body body;
	}
	
	@XmlRootElement(name = "HEADER")
	@XmlAccessorType(XmlAccessType.FIELD)
	@Data
	private static class Header {
		
		@XmlElement(name = "CODE")
		private String code;
		
		@XmlElement(name = "MESSAGE")
		private String message;
	}
	
	@XmlRootElement(name = "BODY")
	@XmlAccessorType(XmlAccessType.FIELD)
	@Data
	private static class Body {
		
		@XmlElementWrapper(name = "BLDG_LIST")
		@XmlElement(name = "BLDG_INFO")
		private List<BldgInfo> bldgList;
		
	}
	
	@XmlRootElement(name = "BODY")
	@XmlAccessorType(XmlAccessType.FIELD)
	@Data @NoArgsConstructor @AllArgsConstructor
	private static class BldgInfo {
		
		@XmlElement(name = "ZIPCODE")
		private String zipcode;
		
		@XmlElement(name = "BLDG_NM")
		private String bldgNm;
		
	}
	
	
	@Test
	@DisplayName("converting pojo to xml string or file (= marshalling)")
	void convertPojoToXmlTest() {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Response.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			
			Response response = new Response();
			Header header = new Header();
			Body body = new Body();
			
			List<BldgInfo> bldgInfos = List.of(
				new BldgInfo("111-222", "luxury hotel"),
				new BldgInfo("222-333", "luxury motel")
			);
			
			body.setBldgList(bldgInfos);
			
			header.setCode("200");
			header.setMessage("GET BUILDING INFO LIST - SUCCESS");
			
			response.setHeader(header);
			response.setBody(body);
			
			// The following code adds a new line to improve readability.
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			
			// read xml string from marshaller
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			marshaller.marshal(response, os);


			String xmlString = os.toString(StandardCharsets.UTF_8);
			// remark: if your jdk version is lower than 10, use code below
			//         ==> String s = new String(os.toByteArray(), StandardCharsets.UTF_8);

			log.debug("\n{}", xmlString);

			
			// if you want <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
			// to... <?xml version="1.0" encoding="UTF-8"> , then use the code below
			// ( replace this code right after the comment "read xml string from marshaller" )
			//
			// marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
			// StringWriter stringWriter = new StringWriter(); // java.io.StringWriter
			// stringWriter.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			// marshaller.marshal(response, stringWriter);
			// System.out.println("stringWriter.toString() = " + stringWriter.toString());
			
			
		} catch (JAXBException e) {
			fail(e.getMessage());
		}
		
	}
}
