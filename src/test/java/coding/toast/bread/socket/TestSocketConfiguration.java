package coding.toast.bread.socket;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface TestSocketConfiguration {
	
	Integer PORT_NUM = 9000;
	
	String SERVER_HOST = "127.0.0.1";
	
	Charset COMMUNICATE_CHARSET = StandardCharsets.UTF_8;
}
