package coding.toast.bread.playing_with_file;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import static java.nio.file.StandardOpenOption.APPEND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.fail;


/**
 * <h2>Java Files Api Test</h2>
 * This class is a test class that performs various experiments related to 'files'.<br>
 * To conduct various experiments related to "files", this test class uses not only Java API but also API provided by Spring<br>
 * By the way, some files in this test is located in <a href="https://github.com/CodingToastBread/java-playground/tree/main/src/test/resources/coding/toast/bread/playing_with_file/">here</a>.<br>
 * <br>
 * <hr/>
 * <strong>Note: File tests require some files to be present on the local computer for them to be possible.<br>
 * Therefore, some files are created and stored on the local computer before each test method is executed.<br>
 * The file creation location for this test code is the "{user home directory}/directory_for_java_file_test/" of the local computer user</strong>
 * <hr/>
 * @see <a href="https://docs.oracle.com/javase/tutorial/essential/io/dirs.html">The Java™ Tutorials</a>
 */
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
		
		
		// creating a directory structure for test
		createSampleDirectory(testDirPath);
		
		// simple logging...
		log.debug("creating %s directory".formatted(testFileDirectoryPath));
		
		// if you want to open the folder with right after creation, use the code below.
		java.awt.Desktop.getDesktop().open(testDirPath.toFile());
	}
	
	private static void createSampleDirectory(Path path) throws IOException {
		
		// We are going to create a sample directory structure as follows:
		/*
		sample_dir
		|   company_info.txt
		|
		+---person
		|       person_info.txt
		|       person_info2.txt
		|
		\---todo
		        todo_list.txt
		 */
		
		Path sampleDirPath = path.resolve("sample_dir");
		Path personPath = sampleDirPath.resolve("person");
		Path todoPath = sampleDirPath.resolve("todo");
		
		// create sub directory inside sample_dir directory
		createNewDirectory(personPath);
		createNewDirectory(todoPath);
		
		InputStream companyStream = JavaFilesApiTest.class.getResourceAsStream("./company_info.txt");
		InputStream personStream = JavaFilesApiTest.class.getResourceAsStream("./person_info.txt");
		InputStream todoStream = JavaFilesApiTest.class.getResourceAsStream("./todo_list.txt");
		
		// create sample files
		Path companyFilePath = sampleDirPath.resolve("company.txt");
		Path personFilePath = personPath.resolve("person_info.txt");
		Path todoFilePath = todoPath.resolve("todo_list.txt");
		
		Files.copy(companyStream, companyFilePath);
		Files.copy(personStream, personFilePath);
		Files.copy(todoStream, todoFilePath);
		
	}
	
	
	@Test
	@DisplayName("file copy test")
	void copyFileUsingStreamTest() {
		
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
	
	
	// Actually, Spring's FileSystemUtils.deleteRecursively method already provides a way to empty the directory.<br>
	// However, I still wanted to practice and study by writing the code myself, so I wrote the following test code.
	@Test
	@Order(2)
	@DisplayName("delete all files and sub directories")
	void deleteAllFileInsideDirectoryTest() throws IOException {
		log.debug("The test code will be written soon.");
		Path root = testDirPath.resolve("sample_dir");
		
		// this method is exactly same with FileSystemUtils.deleteRecursively!!!
		// i code it, just for practice!
		Files.walkFileTree(root, new SimpleFileVisitor<>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}
	
	
	// Actually, Spring's FileSystemUtils.copyRecursively method already provides a way to copy the directory.<br>
	// However, I still wanted to practice and study by writing the code myself, so I wrote the following test code.
	@Test
	@Order(1)
	@DisplayName("copy all files and sub directories")
	void copyDirectoryTest() throws IOException {
		
		// the code below is almost the same with FileSystemUtils.copyRecursively(); method!
		Path source = testDirPath.resolve("sample_dir");
		Path destination = testDirPath.resolve("sample_copy_dir");
		BasicFileAttributes fileAttributes = Files.readAttributes(source, BasicFileAttributes.class);
		
		if (fileAttributes.isDirectory()) {
			Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					Files.createDirectories(destination.resolve(source.relativize(dir)));
					return FileVisitResult.CONTINUE;
				}
				
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.copy(file, destination.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
					return FileVisitResult.CONTINUE;
				}
			});
		}
	}
	
	
	@Test
	void appendStringAtExistingFile() {
		
		// first create sampleFile...
		Path testFilePath = testDirPath.resolve("appendingString.txt");
		try {
			Path file = Files.createFile(testFilePath);
		} catch (IOException e) {
			fail("fail creating file in \"appendStringAtExistingFile\" test!");
		}
		
		// Method 1:
		try {
			// it will close automatically...
			Files.writeString(
				testFilePath,
				"appending Some Strings...1" + System.lineSeparator(),
				StandardCharsets.UTF_8, APPEND);
			
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		// Method 2:
		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(testFilePath, StandardCharsets.UTF_8, APPEND);
		     PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
			
			// The bufferedWriter alone is sufficient for adding content to a file,
			// but if you need to write a sentence and keep adding new lines,
			// using PrintWriter can be a bit more convenient.
			printWriter.println("appending Some Strings...2");
			
			// The above code performs the same operation as the following code.
			// bufferedWriter.append("appending Some Strings...2");
			// bufferedWriter.newLine();
			
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		
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
