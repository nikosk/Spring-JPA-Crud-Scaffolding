package gr.dsigned.springcrudutils.strategies;

public class TestDTO {

    private String testStr;
    private Long testLong;

    public TestDTO() {
    }

    public TestDTO(String testStr, Long testLong) {
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