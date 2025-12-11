package lamdepzai.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class RestIO {
    static private ObjectMapper mapper = new ObjectMapper();

    //doc json tu client
    public static String readjson(HttpServletRequest req) throws IOException {
        req.setCharacterEncoding("UTF-8");
        BufferedReader reader = req.getReader();

        String line;
        StringBuffer buffer = new StringBuffer();

        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reader.close();
        return buffer.toString();
    }

    //gui chuoii json ve client
    public static void writejson(HttpServletResponse resp, String json) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().print(json);
        resp.flushBuffer();
    }

    //doc json tu client chuyen thang object
    public static <T> T readObject(HttpServletRequest req, Class<T> Clazz) throws IOException {
        String json = RestIO.readjson(req);
        T bean = mapper.readValue(json, Clazz);
        return bean;
    }

    //chuen java obejct thang json va gui ve client
    public static void writeObject(HttpServletResponse resp, Object data) throws IOException {
        String json = mapper.writeValueAsString(data);
        RestIO.writejson(resp, json);
    }

    // gui doi tuong rong ve client
    public static void writeEmptyObject(HttpServletResponse resp)
            throws IOException {
        RestIO.writeObject(resp, Map.of());
    }
}
