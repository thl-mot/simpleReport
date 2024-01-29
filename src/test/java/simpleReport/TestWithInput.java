package simpleReport;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lauerbach.pdf.SimpleReport;

class TestWithInput {

	Gson gson= new GsonBuilder().create();
	
	@Test
	void test() throws Exception {
		FileReader fis= new FileReader("src/main/resources/sampleData.json");
		HashMap<String, Object> data= gson.fromJson( fis, HashMap.class);
		fis.close();
		
		SimpleReport r = new SimpleReport(new File("src/main/resources/example.template.xml"));
		r.print( new File("target/test.pdf"), data);
	}

}
