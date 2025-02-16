package com.example.backend.utils.web;

import com.example.backend.exception.GlobalException;
import com.example.backend.utils.object.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 通用http发送方法
 *
 * @author ruoyi
 */
public class HttpUtils
{
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url 发送请求的 URL
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url)
    {
        return sendGet(url, null);
    }

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url 发送请求的 URL
     * @param param 请求参数
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url, Map<String, String> param)
    {
        return sendGet(url, param, StandardCharsets.UTF_8);
    }

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url 发送请求的 URL
     * @param param 请求参数
     * @param contentType 编码类型
     * @return 所代表远程资源的响应结果，json格式响应体
     */
    public static String sendGet(String url, Map<String, String> param, Charset contentType)
    {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        StringBuilder urlNameString;
        if(CollectionUtils.isEmpty(param)) {
            urlNameString = new StringBuilder(url);
        }
        else {
            urlNameString = new StringBuilder(url + "?");
            param.forEach((k, v) -> urlNameString.append(k).append("=").append(v).append("&"));
        }
        log.info("sendGet - {}", urlNameString);
        try {
            URL realUrl = new URL(urlNameString.toString());
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), contentType));
            String line;
            while ((line = in.readLine()) != null)
            {
                result.append(line);
            }
//            log.info("result - {}", result);
            return result.toString();
        }
        catch (ConnectException e) {
            log.error("HttpUtils.sendGet ConnectException, url={}", urlNameString, e);
            throw new GlobalException(AppHttpCode.NETWORK_ERROR);
        }
        catch (SocketTimeoutException e) {
            log.error("HttpUtils.sendGet SocketTimeoutException, url={}", urlNameString, e);
            throw new GlobalException(AppHttpCode.NETWORK_ERROR);
        }
        catch (IOException e) {
            throw new GlobalException(AppHttpCode.SYSTEM_ERROR);
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            }
            catch (IOException ex) {
                log.error("HttpUtils.sendGet 中 in.close Exception, url={}", urlNameString, ex);
            }
        }
    }
}
