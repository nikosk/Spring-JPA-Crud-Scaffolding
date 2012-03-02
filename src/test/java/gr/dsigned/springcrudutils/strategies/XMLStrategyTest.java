/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.springcrudutils.strategies;

import static org.mockito.Mockito.*;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;

/**
 *
 * @author nk
 */
public class XMLStrategyTest {

    /**
     * Test of render method, of class XMLStrategy.
     */
    @Test
    public void testRender() {
        System.out.println("render");
        Object data = new TestClass("test", Long.MAX_VALUE);
        XMLStrategy instance = new XMLStrategy();
        String result = instance.render(data);
        System.out.println("Result: " + result);
        assertNotNull(result);
    }

    /**
     * Test of setup method, of class XMLStrategy.
     */
    @Test
    public void testSetup() {
        System.out.println("setup");
        HttpServletResponse response = mock(HttpServletResponse.class);        
        XMLStrategy instance = new XMLStrategy();
        instance.setup(response);
        verify(response).setContentType("application/xml");
    }
    public static final class TestClass {

        private String testStr;
        private Long testLong;

        public TestClass() {
        }

        public TestClass(String testStr, Long testLong) {
            this.testStr = testStr;
            this.testLong = testLong;
        }

        public Long getTestLong() {
            return testLong;
        }
        
        public void setTestLong(Long testLong) {
            this.testLong = testLong;
        }

        public String getTestStr() {
            return testStr;
        }
        
        public void setTestStr(String testStr) {
            this.testStr = testStr;
        }
    }
}
