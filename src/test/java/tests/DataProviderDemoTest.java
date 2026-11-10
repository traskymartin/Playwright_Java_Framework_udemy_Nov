package tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class DataProviderDemoTest {
    @DataProvider(name = "basicData") // multi Dimensional Array of objects
    public Object[][] basicData() {

        return new Object[][]{{"user1@yahoo.com", "Password1"}, {"user2@yahoo.com", "Password2"}};

    }

    @Test(dataProvider = "basicData")
    public void testFillForm(String email, String password) {
        System.out.println("Fill Form : " + email + " " + password);
    }

    @DataProvider(name = "hashMapData") // multi Dimensional Array of objects
    public Object[][] hashMapData() {
        HashMap<String, String> user1 = new HashMap<>();
        user1.put("email", "user1@yahoo.com");
        user1.put("password", "Password1");

        HashMap<String, String> user2 = new HashMap<>();
        user2.put("email", "user2@yahoo.com");
        user2.put("password", "Password2");
        return new Object[][]{{user1}, {user2}};

    }

    @Test(dataProvider = "jsonData")
    public void testWithHashMap(HashMap<String, String> user) {
        System.out.println(user.get("email"));
        System.out.println(user.get("password"));

    }

    @DataProvider(name = "jsonData")
    public Object[][] jsonData() throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/test/resources/testDataTC1.json")));
        Type type = new TypeToken<List<HashMap<String, String>>>() {
        }.getType();
        List<HashMap<String, String>> list = new Gson().fromJson(jsonContent, type);
        Object[][] data = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            data[i][0] = list.get(i);
        }
        return data;
    }
}
