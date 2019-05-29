package org.benben.config;


import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON转义字符绑定对象失败解决方案/全局解析器
 * 参考 https://blog.csdn.net/senkoo_lau/article/details/77773093
 */
@SpringBootApplication
public class JsonParseConfig extends WebMvcConfigurationSupport {

	@Value("${benben.path.upload}")
	private String upLoadPath;
	@Value("${benben.path.webapp}")
	private String webAppPath;
	@Value("${spring.resource.static-locations}")
	private String staticLocations;

    /**
     * 配置fastJson 用于替代jackson
     */
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters){
        super.configureMessageConverters(converters);
        // 1.定义一个convert 转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        // 2 添加fastjson 的配置信息 比如 是否要格式化 返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        //解决返回json数据，属性值为null或空被省略的问题
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue);

        fastConverter.setFastJsonConfig(fastJsonConfig);
        // 解决乱码的问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        converters.add(fastConverter);
    }

    /**
     * 显示Swagger空白页面
     *
     * 原因分析: 在访问http://localhost:8080/swagger-ui.html 时，这个swagger-ui.html相关的所有前端静态文件都在springfox-swagger-ui-2.6.1.jar里面。
     * Spring Boot自动配置本身不会自动把/swagger-ui.html这个路径映射到对应的目录META-INF/resources/下面。我们加上以下映射即可
     *
     * @param registry
     */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler("/**")
				.addResourceLocations("file:" + upLoadPath + "//", "file:" + webAppPath + "//")
				.addResourceLocations(staticLocations.split(","));
    }
}
