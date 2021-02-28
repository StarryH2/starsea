package com.xinghai.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
/**
 * 整个问题产生的原因是Spring框架先调用了MultipartResolver 来处理http multi-part的请求。这里http multipart的请求已经消耗掉。后面又交给ServletFileUpload ，那么ServletFileUpload 就获取不到相应的multi-part请求。
 *
 * 1.首先spring boot默认会调用MultipartResolver 来处理http multi-part的请求，须在配置文件中把spring.http.multipart.enabled=false。这样就不会默认调用MultipartResolver。
 */

/**
 * 重写一个MyMultipartResolver  重写isMultipart方法
 */
@Configuration
public class MyMultipartResolver extends CommonsMultipartResolver {


    /**
     * 这里是处理Multipart http的方法。如果这个返回值为true,那么Multipart http body就会MyMultipartResolver 消耗掉.如果这里返回false
     * 那么就会交给后面的自己写的处理函数处理例如刚才ServletFileUpload 所在的函数
     * @see org.springframework.web.multipart.commons.CommonsMultipartResolver#isMultipart(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public boolean isMultipart(HttpServletRequest request) {
        // 过滤金格生成文书保存的接口  兼容MultipartResolver 或者 ServletFileUpload
        if (request.getRequestURI().contains("mgt/document/upload")||request.getRequestURI().contains("/modules/document.html")) {
            return false;
        }
        return super.isMultipart(request);
    }

}
