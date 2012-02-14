package gr.dsigned.springcrudutils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nk
 */
public class Pager {

    String baseURL;
    Long totalItemCount;
    Long currentPage;
    int perPage;
    private List<String> urls = new ArrayList<String>();

    public Pager(String baseURL, Long totalItemCount) {
        this.baseURL = baseURL;
        this.totalItemCount = totalItemCount;
        this.currentPage = 0L;
        this.perPage = 10;
        init();
    }

    public Pager(String baseURL, Long totalItemCount, Long currentPage) {
        this.baseURL = baseURL;
        this.totalItemCount = totalItemCount;
        this.currentPage = currentPage;
        this.perPage = 10;
        init();
    }

    public Pager(String baseURL, Long totalItemCount, Long currentPage, int perPage) {
        this.baseURL = baseURL;
        this.totalItemCount = totalItemCount;
        this.currentPage = currentPage;
        this.perPage = perPage;
        init();
    }

    private void init() {
        for (int i = 0; i < getTotalPageNumber(); i++) {
            if (i != 0) {
                String url = getBaseURL() + "?page=" + i + "&sizeNo=" + perPage;
                urls.add(url);
            } else {
                String url = getBaseURL() + "?sizeNo=" + perPage;
                urls.add(url);
            }

        }
    }

    public String getFirstPage() {
        return baseURL + "?sizeNo=" + perPage;
    }

    public String getLastPage() {
        return baseURL + "?page=" + getTotalPageNumber() + "&sizeNo=" + perPage;
    }

    public int getTotalPageNumber() {
        return (int) Math.ceil(totalItemCount / (float) perPage);
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public Long getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(Long totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

    public List<String> getUrls() {
        return urls;
    }
    
}
