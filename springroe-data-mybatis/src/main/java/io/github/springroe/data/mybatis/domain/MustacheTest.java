package io.github.springroe.data.mybatis.domain;

import com.samskivert.mustache.Mustache;
import org.springframework.data.mybatis.mapping.model.Column;
import org.springframework.data.mybatis.repository.query.DefaultCollector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MustacheTest {

    public static Boolean isTrue(String propertyPath) {
        return false;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Mustache.Compiler compiler = Mustache.compiler().withLoader(name -> {
            FileInputStream fileInputStream = new FileInputStream(new File("D://Update.mustache"));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            return inputStreamReader;
        }).escapeHTML(false).withCollector(new DefaultCollector());
        Map<String, Column> columnMap = new HashMap<>();
        columnMap.put("test.name", new Column("asd"));
        Map<String, Object> scopes = new HashMap<String, Object>();
        scopes.put("statementName", "asd");
        scopes.put("properties", columnMap.entrySet());
        scopes.put("table", "test");
        scopes.put("byId", 1);
        String str = compiler.compile(new InputStreamReader(new FileInputStream(new File("D:\\Update.mustache")), StandardCharsets.UTF_8)).execute(scopes);
        System.out.println(str);

        String propertyPath = "role.entity.user";


    }
}
