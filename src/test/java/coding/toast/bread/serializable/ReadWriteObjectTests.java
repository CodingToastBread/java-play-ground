package coding.toast.bread.serializable;

import coding.toast.bread.serializable.model.SampleData;
import org.junit.jupiter.api.Test;

import java.io.*;

/**
 * how to Test
 * 1. just try two @Test, and see it works.
 * 2. after that remove object_file and remove serialVersionUID field inside SampleData Class
 * 3. re-run ObjectToFileWriteTest
 * 4. add any kind of field at SampleData Class
 * 5. re-run FileToObjectWriteTest ==> Error! Because
 */
public class ReadWriteObjectTests {
	
	@Test
	void ObjectToFileWriteTest() {
		
		try (FileOutputStream fs = new FileOutputStream("object_file");
		     ObjectOutputStream os = new ObjectOutputStream(fs)) {
			
			SampleData sampleData = new SampleData();
			sampleData.setName("wow");
			os.writeObject(sampleData);
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	void FileToObjectWriteTest() {
		
		try (FileInputStream fs = new FileInputStream("object_file");
		     ObjectInputStream os = new ObjectInputStream(fs)
		) {
			Object o = os.readObject();
			
			if (o instanceof SampleData sampleData) {
				System.out.println("sampleData = " + sampleData);
			}
			
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
}
