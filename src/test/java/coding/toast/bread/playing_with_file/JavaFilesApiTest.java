package coding.toast.bread.playing_with_file;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.AssertionErrors;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.util.AssertionErrors.fail;

/**
 * This class is a test class that explores various experiments related to the java.nio.file.Files API.
 * However, for tasks that are difficult to achieve with Files alone, it also utilizes additional utility APIs
 * provided by the Spring framework.
 */

/**
 * <h2>Java Files Api Test</h2>
 * This class is a test class that performs various experiments related to 'files'.<br>
 * To conduct various experiments related to "files", this test class uses not only Java API but also API provided by Spring<br>
 * By the way, some files in this test is located in <a href="">here</a>.<br>
 * <br>
 * <hr/>
 * <strong>Note: File tests require some files to be present on the local computer for them to be possible.<br>
 * Therefore, some files are created and stored on the local computer before each test method is executed.<br>
 * The file creation location for this test code is the "{user home directory}/directory_for_java_file_test/" of the local computer user</strong>
 * <hr/>
 * @see <a href="https://docs.oracle.com/javase/tutorial/essential/io/dirs.html">The Java™ Tutorials</a>
 */
@Slf4j
public class JavaFilesApiTest {
	
	private static final String testFileDirectoryPath
		= System.getProperty("user.home") + File.separator + "directory_for_java_file_test";
	
	private static final Path testDirPath = Path.of(testFileDirectoryPath);
	
	/**
	 * Create a test folder Before Test
	 */
	@BeforeAll
	static void beforeAll() throws IOException {
		
		// Deleting previously created folders for multiple testing.
		FileSystemUtils.deleteRecursively(testDirPath);
		
		// create test folder
		createNewDirectory(testDirPath);
		
		// simple logging...
		log.debug("creating %s directory".formatted(testFileDirectoryPath));
		
		// if you want to open the folder with right after creation, use the code below.
		// java.awt.Desktop.getDesktop().open(testDirPath.toFile());
	}
	
	
	@Test
	@DisplayName("file copy test")
	void copyFileUsingStreamTest() {
		System.out.println("JavaFilesApiTest.some1");
		
		Path personInfoFilePath = testDirPath.resolve("person_info.txt");
		Path todoListFilePath = testDirPath.resolve("todo_list.txt");
		
		// Method 1: using InputStream.transferTo api
		try (InputStream is = this.getClass().getResourceAsStream("./person_info.txt");
		     OutputStream os = Files.newOutputStream(personInfoFilePath)) {
			is.transferTo(os);
		} catch (IOException e) {
			fail(e.getMessage());
			return;
		}
		
		
		// Method 2: when there was no transferTo Api we had to do like this...
		try (InputStream is = this.getClass().getResourceAsStream("./todo_list.txt");
		     OutputStream os = Files.newOutputStream(todoListFilePath)) {
			
			int bufferSize = 1024;
			int readSize;
			byte[] bufferByte = new byte[bufferSize];
			
			while ((readSize = is.read(bufferByte, 0, bufferSize)) >= 0) {
				os.write(bufferByte, 0, readSize);
			}
			
		} catch (IOException e) {
			fail(e.getMessage());
			return;
		}
		
		
		// Method 3: if you don't have to use Stream, then Why not use Files.api
		Path copyFileTestPath = testDirPath.resolve("person_info_copy.txt");
		try {
			Files.copy(
				personInfoFilePath
				, copyFileTestPath
				// , StandardCopyOption.REPLACE_EXISTING
			);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		// check if the file is successfully copied
		assertThat(Files.exists(personInfoFilePath)).isTrue();
		assertThat(Files.exists(todoListFilePath)).isTrue();
		assertThat(Files.exists(copyFileTestPath)).isTrue();
		
		// check if the copy file's content is the same with the original file
		try {
			assertThat(Files.mismatch(personInfoFilePath, copyFileTestPath)).isEqualTo(-1L);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
	}
	
	
	@Test
	void deleteAllFileInsideDirectoryTest() {
		log.debug("The test code will be written soon.");
	}
	
	@Test
	void creatingNewDirectoryTest() {
		log.debug("The test code will be written soon.");
	}
	
	
	/**
	 * a util method for creating new directory.
	 * @param path - directory path you want to create
	 */
	private static void createNewDirectory(Path path) throws IOException {
		// Operating system-aware test folder creation code
		String currentOS = System.getProperty("os.name").toLowerCase();
		
		// if the os is not window then set permission to the folder
		if (currentOS.contains("win")) {
			Files.createDirectories(path);
		} else {
			// The following two lines of code are taken from "The Java™ Tutorials".
			// Please refer to the JavaDoc of the JavaFilesApiTest class if you are curious about the link.
			Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwx---");
			FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
			Files.createDirectories(path, attr);
		}
	}
	
}
