package gr.dsigned.web.events;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: nk Date: 2/9/14 Time: 2:35 PM
 */
@Component
public class RestListEventListener implements ApplicationListener<RestListPaginationEvent> {

    private static final String NEXT_REL = "next";

    private static final String PREVIOUS_REL = "prev";
    private static final String COUNT_HEADER = "X-Rest-Count";

    public static String createLinkHeader(final String uri, final String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }

    @Override
    public void onApplicationEvent(RestListPaginationEvent event) {
        RequestMapping mapping = AnnotationUtils.findAnnotation(event.getControllerClass(), RequestMapping.class);
        if (mapping == null || mapping.value().length < 1) {
            throw new IllegalStateException("No mapping found on controller");
        }
        if (event.getSize() != null) {
            for (String path : mapping.value()) {
                final String queryString = event.getRequest().getQueryString();
                Pager pager = new Pager(path, queryString == null ? "" : queryString, event.getCount(), event.getPage(), event.getSize());
                if (pager.hasNextPage()) {
                    event.getResponse().addHeader("Link", createLinkHeader(pager.getNextPage(), NEXT_REL));
                }
                if (pager.hasPreviousPage()) {
                    event.getResponse().addHeader("Link", createLinkHeader(pager.getPreviousPage(), PREVIOUS_REL));
                }
            }
        }
        event.getResponse().addHeader(COUNT_HEADER, event.getCount().toString());
    }

    public static class Pager {

        private static final String SIZE_PARAM_NAME = "size";
        private static final String PAGE_PARAM_NAME = "page";

        String baseURL;
        String queryString;
        long totalItemCount;
        int currentPage;
        int perPage;
        private List<String> urls;

        public Pager(String baseURL, String queryString, long totalItemCount, int currentPage, int perPage) {
            this.baseURL = baseURL;
            this.queryString = normalize(queryString);
            this.totalItemCount = totalItemCount;
            this.currentPage = currentPage;
            this.perPage = perPage;
            init();
        }

        private static String removeParameter(String queryString, String name) {
            if (queryString != null) {
                queryString = queryString.replaceAll(name + "=.*?($|&)", "").replaceFirst("&$", "");
            }
            return queryString;
        }

        private String normalize(String queryString) {
            queryString = queryString.startsWith("?") ? queryString.substring(1) : queryString;
            queryString = removeParameter(queryString, SIZE_PARAM_NAME);
            queryString = removeParameter(queryString, PAGE_PARAM_NAME);
            return queryString;
        }

        private void init() {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < getTotalPageNumber(); i++) {
                if (i != 0) {
                    String
                        url =
                        String.format("%s?%s&%s%s", getBaseURL(), SIZE_PARAM_NAME + "=" + perPage, PAGE_PARAM_NAME + "=" + i,
                                      queryString.isEmpty() ? queryString : "&" + queryString);
                    list.add(url);
                } else {
                    String url = String.format("%s?%s%s", getBaseURL(), SIZE_PARAM_NAME + "=" + perPage,
                                               queryString.isEmpty() ? queryString : "&" + queryString);
                    list.add(url);
                }
            }
            urls = ImmutableList.copyOf(list);
        }

        public String getFirstPage() {
            return urls.get(0);
        }

        public String getNextPage() {
            if (currentPage + 1 >= urls.size()) {
                return getLastPage();
            } else {
                return urls.get(currentPage + 1);
            }
        }

        public String getPreviousPage() {
            if (currentPage - 1 > urls.size()) {
                return getFirstPage();
            } else {
                return urls.get(currentPage - 1);
            }
        }

        public String getLastPage() {
            return urls.get(urls.size() - 1);
        }

        public int getTotalPageNumber() {
            return (int) Math.ceil(totalItemCount / (float) perPage);
        }

        public String getBaseURL() {
            return baseURL;
        }

        public boolean hasNextPage() {
            return getCurrentPage() + 1 < getTotalPageNumber();
        }

        public boolean hasPreviousPage() {
            return getCurrentPage() != 0;
        }


        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            Preconditions.checkPositionIndex(currentPage, getTotalPageNumber(), "Page number out of bounds.");
            this.currentPage = currentPage;
        }

        public int getPerPage() {
            return perPage;
        }


        public long getTotalItemCount() {
            return totalItemCount;
        }


        public List<String> getUrls() {
            return urls;
        }

        @Override
        public String toString() {
            return "Pager{" + "baseURL=" + baseURL + ", totalItemCount=" + totalItemCount + ", currentPage=" + currentPage + ", perPage=" + perPage
                   + ", urls=" + urls + '}';
        }

    }

}
