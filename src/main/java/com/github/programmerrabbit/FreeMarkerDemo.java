package com.github.programmerrabbit;

import com.github.programmerrabbit.test.Demo;
import com.github.programmerrabbit.test.Test;
import com.github.programmerrabbit.test.Tests;
import com.github.programmerrabbit.utils.MapUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * Created by Rabbit on 2016/12/12.
 */
@Demo
public class FreeMarkerDemo {
    @Test
    public void runDemo() throws IOException, TemplateException {
        Configuration configuration = new Configuration();
        configuration.setDirectoryForTemplateLoading(new File("src/main/resources/demo"));
        Map<String, String> map = MapUtils.newHashMap();
        map.put("object", "World");
        Template template = configuration.getTemplate("hello.ftl");
        template.process(map, new OutputStreamWriter(System.out));
    }

    public static void main(String[] args) {
        Tests.run(FreeMarkerDemo.class);
    }
}
