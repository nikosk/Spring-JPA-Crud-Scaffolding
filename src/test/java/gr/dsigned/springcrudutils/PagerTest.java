/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.springcrudutils;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author nk
 */
public class PagerTest {

    Pager instance = new Pager("/", 100, 2, 10);

    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test of getFirstPage method, of class Pager.
     */
    @Test
    public void testGetFirstPage() {
        System.out.println("getFirstPage");
        String expResult = "/?sizeNo=10";
        String result = instance.getFirstPage();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNextPage method, of class Pager.
     */
    @Test
    public void testGetNextPage() {
        System.out.println("getNextPage");
        String expResult = "/?page=3&sizeNo=10";
        String result = instance.getNextPage();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLastPage method, of class Pager.
     */
    @Test
    public void testGetLastPage() {
        System.out.println("getLastPage");
        String expResult = "/?page=10&sizeNo=10";
        String result = instance.getLastPage();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTotalPageNumber method, of class Pager.
     */
    @Test
    public void testGetTotalPageNumber() {
        System.out.println("getTotalPageNumber");
        int expResult = 10;
        int result = instance.getTotalPageNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of getBaseURL method, of class Pager.
     */
    @Test
    public void testGetBaseURL() {
        System.out.println("getBaseURL");
        String expResult = "/";
        String result = instance.getBaseURL();
        assertEquals(expResult, result);
    }


    /**
     * Test of getCurrentPage method, of class Pager.
     */
    @Test
    public void testGetCurrentPage() {
        System.out.println("getCurrentPage");
        int expResult = 2;
        int result = instance.getCurrentPage();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCurrentPage method, of class Pager.
     */
    @Test(expected = java.lang.IndexOutOfBoundsException.class)
    public void testSetCurrentPage() {
        System.out.println("setCurrentPage");
        int currentPage = 11;
        instance.setCurrentPage(currentPage);
    }

    /**
     * Test of getTotalItemCount method, of class Pager.
     */
    @Test
    public void testGetTotalItemCount() {
        System.out.println("getTotalItemCount");
        int expResult = 100;
        int result = instance.getTotalItemCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getUrls method, of class Pager.
     */
    @Test
    public void testGetUrls() {
        System.out.println("getUrls");
        List result = instance.getUrls();
        assertEquals(10, result.size());
        System.out.println("Urls: " + instance.toString());
    }

    /**
     * Test of getPreviousPage method, of class Pager.
     */
    @Test
    public void testGetPreviousPage() {
        System.out.println("getPreviousPage");
        String expResult = "/?page=1&sizeNo=10";
        String result = instance.getPreviousPage();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPerPage method, of class Pager.
     */
    @Test
    public void testGetPerPage() {
        System.out.println("getPerPage");
        int expResult = 10;
        int result = instance.getPerPage();
        assertEquals(expResult, result);
    }
}
