package gr.dsigned.springcrudutils.strategies;


import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


public class CsvStrategy<T> implements RenderStrategy<T> {


    @Override
    public String render(T data) {
        StringBuilder writer = new StringBuilder();
        try {
            if (data instanceof List) {
                List dataList = (List) data;
                if (!dataList.isEmpty()) {
                    writer.append(getHeaderFields(dataList.get(0)));
                }
                for (Object obj : dataList) {
                    writer.append(checkFields(obj));
                }
            } else {
                writer.append(getHeaderFields((Object) data));
                writer.append(checkFields((Object) data));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while converting to CSV", e);
        }
        return writer.toString();
    }

    @Override
    public void setup(HttpServletResponse response) {
        response.setContentType(ContentType.CSV.toString());
        response.setHeader("Content-Disposition", "attachment;filename=file.csv");
    }

    /**
     * Dynamic checking fields
     *
     * @param obj
     */
    private static String getHeaderFields(Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        StringBuilder sb = new StringBuilder();
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (!method.getName().startsWith("get")) {
                continue;
            }
            sb.append("\"").append(method.getName().replaceFirst("get", "")).append("\"");
            sb.append(',');
        }
        sb.append('\n');
        return sb.toString();
    }

    /**
     * Dynamic checking fields
     *
     * @param obj
     */
    private static String checkFields(Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        StringBuilder sb = new StringBuilder();
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (!method.getName().startsWith("get")) {
                continue;
            }
            Object returnValueNew = method.invoke(obj);
            returnValueNew = returnValueNew == null ? "" : returnValueNew;
            sb.append("\"").append(escape(returnValueNew.toString())).append("\"");
            sb.append(',');
        }
        sb.append('\n');
        return sb.toString();
    }

    private static String escape(String value) {
        if (value != null) {
            return value.replaceAll("\"", "'");
        }
        return value;
    }
}
