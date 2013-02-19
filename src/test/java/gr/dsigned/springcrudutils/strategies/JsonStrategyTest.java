package gr.dsigned.springcrudutils.strategies;

import org.junit.Test;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by IntelliJ IDEA.
 * User: nk
 * Date: 2/19/13
 * Time: 11:04 PM
 */
public class JsonStrategyTest {

    @Test
    public void testRender() throws Exception {
        System.out.println("render");
        Object data = new TestDTO("test", Long.MAX_VALUE);
        RenderStrategy instance = new JsonStrategy<TestDTO>();
        String result = instance.render(data);
        System.out.println("Result: " + result);
        assertNotNull(result);
    }

    @Test
    public void testSetup() throws Exception {
        System.out.println("setup");
        HttpServletResponse response = mock(HttpServletResponse.class);
        RenderStrategy instance = new JsonStrategy<TestDTO>();
        instance.setup(response);
        verify(response).setContentType("application/json;charset=UTF-8");
    }
}
