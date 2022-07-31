package com.atguigu.guli.infrastructure.filter;

import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.utils.JwtHelper;
import com.google.gson.JsonObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

// 统一鉴权过滤器
@Component
public class GlobalAuthFilter implements GlobalFilter, Ordered {
    // 过滤方法，网关的每个请求都会被该filter过滤
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // HttpServletRequest: 网关使用的是 webflux，之前的web启动器使用的是 servlet
        // HttpServletResponse: 这两个是 servlet 编程模型中表示请求报文和响应报文的类型

        // ServerHttpRequest 和 ServerHttpResponse 是 webflux 中的报文类型
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 获取请求路径，判断是否包含 auth
        String path = request.getURI().getPath();// 获取资源的路径
        AntPathMatcher matcher = new AntPathMatcher(); // ant 风格路径的比较器
        boolean match = matcher.match("/api/**/auth/**", path);
        if (!match){ // 路径中不包含auth或者是管理员访问
            // 放行请求
            return chain.filter(exchange);
        }
        // 路径中包含auth需要鉴权
        // 获取请求头中的token进行校验
        // JwHelper.checkToken(request)
        String token = request.getHeaders().getFirst("token");// 获取token请求头值列表中的第一个
        boolean b = JwtHelper.checkToken(token);
        if (b){// token解析成功，代表用户已登录token有效
            // 放行请求
            return chain.filter(exchange);
        }
        // 设置自定义响应报文的头：响应体内容和编码
        response.getHeaders().set("Content-type","application/json;charset=UTF-8");
        // 未登录获取token被篡改或者过期
        // 返回一个需要登录的R对象的JSON字符串给请求
//        JSONObject jsonObject = new JSONObject();// 创建一个JSON对象（调用toString时可以直接转为JSON字符串）
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", ResultCodeEnum.LOGIN_AUTH.getCode());
        jsonObject.addProperty("message","gateway : " + ResultCodeEnum.LOGIN_AUTH.getMessage());
        jsonObject.addProperty("success",ResultCodeEnum.LOGIN_AUTH.getSuccess());
        byte[] bytes = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer data = response.bufferFactory().wrap(bytes);// 将bytes响应报文的字节数组转为数据对象
        return response.writeWith(Mono.just(data)); // 将响应数据的缓存数据对象转为响应报文
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
