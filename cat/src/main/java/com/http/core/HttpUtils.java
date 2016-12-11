package com.http.core;

import com.http.bean.Request;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;


/**
 * Created by Administrator on 2016/12/11.
 */

public class HttpUtils {


    public void execute(Request request){
        switch (request.method){
            case GET:
                get(request);
                break;
            case POST:
                post(request);
                break;
        }
    }

    public static String get(Request request) {
        if (request != null) {
            HttpURLConnection connection = null;
            InputStream is = null;
            ByteArrayOutputStream out = null;
            try {
                connection = (HttpURLConnection) new URL(request.url).openConnection();
                connection.setRequestMethod(request.method.name());
                connection.setConnectTimeout(15 * 3000);
                connection.setReadTimeout(15 * 3000);
                int status = connection.getResponseCode();

                if (status == HttpURLConnection.HTTP_OK) {
                    out = new ByteArrayOutputStream();
                    is = connection.getInputStream();
                    byte[] buffer = new byte[2048];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    is.close();
                    out.flush();
                    out.close();
                    return new String(out.toByteArray());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeStream(is, out);
            }
        }
        return null;
    }

    public static String post(Request request) {
        HttpURLConnection connection = null;
        InputStream is = null;
        ByteArrayOutputStream out = null;
        try {
            connection = (HttpURLConnection) new URL(request.url).openConnection();
            connection.setRequestMethod(request.method.name());
            addHeads(request.map, connection);
            connection.setConnectTimeout(15 * 3000);
            connection.setReadTimeout(15 * 3000);
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(request.content.getBytes());
            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                out = new ByteArrayOutputStream();
                is = connection.getInputStream();
                byte[] buffer = new byte[2048];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                is.close();
                out.flush();
                out.close();
                return new String(out.toByteArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(is, out);
        }

        return null;
    }

    public static void put(Request request) throws Exception {
        URL url = new URL(request.url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(request.method.name());
        String paramStr = prepareParam(request.map);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(paramStr.getBytes("utf-8"));
        os.close();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        String result = "";
        while ((line = br.readLine()) != null) {
            result += "/n" + line;
        }
        System.out.println(result);
        br.close();

    }


    public static boolean delete(Request request) throws Exception {
        //HttpURLConnection 是不支持在delete方法中加入请求体的，既不能调用connection.setRequestProperty()方法;
        //会抛出异常 java.net.ProtocolException: HTTP method DELETE doesn't support output
        String paramStr = prepareParam(request.map);
        if (paramStr == null || paramStr.trim().length() < 1) {

        } else {
            request.url += "?" + paramStr;
        }
        System.out.println(request.url);
        URL url = new URL(request.url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod(request.method.name());
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return true;
        } else {
            System.out.println(conn.getResponseCode());
            return false;
        }
    }

    private static String prepareParam(Map<String, String> paramMap) {
        StringBuffer sb = new StringBuffer();
        if (paramMap.isEmpty()) {
            return "";
        } else {
            for (String key : paramMap.keySet()) {
                String value = paramMap.get(key);
                if (sb.length() < 1) {
                    sb.append(key).append("=").append(value);
                } else {
                    sb.append("&").append(key).append("=").append(value);
                }
            }
            return sb.toString();
        }
    }

    private static void addHeads(Map<String, String> map, HttpURLConnection connection) {
        if (map != null){
            Set<Map.Entry<String, String>> entries = map.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

    }

    private static void closeStream(Closeable... closeable) {
        if (closeable != null && closeable.length > 0) {
            try {
                for (Closeable close : closeable) {
                    if (close != null) close.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
