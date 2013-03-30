
import org.junit.Test;
import static org.junit.Assert.*;
import play.mvc.Result;
import play.libs.F.*;
import static play.test.Helpers.*;

public class AddReferenceTest {

    @Test
    public void pageTypeIsCorrect() {
        running(fakeApplication(), new Runnable() {
           public void run() {
               Result result = routeAndCall(fakeRequest(GET, "/add"));
               assertEquals(contentType(result), "text/html");
               assertEquals(status(result), OK);
           }
        });
    }
}
