package com.networknt.codegen;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import io.swagger.util.Json;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

/**
 * Created by steve on 24/04/17.
 */
public class Cli {
    public static ObjectMapper mapper = new ObjectMapper();

    @Parameter(names={"--framework", "-f"})
    String framework;
    @Parameter(names={"--model", "-m"})
    String model;
    @Parameter(names={"--config", "-c"})
    String config;
    @Parameter(names={"--output", "-o"})
    String output;


    public static void main(String ... argv) {
        Cli cli = new Cli();
        JCommander.newBuilder()
                .addObject(cli)
                .build()
                .parse(argv);
        cli.run();
    }

    public void run() {
        System.out.printf("%s %s %s %s", framework, model, config, output);
        FrameworkRegistry registry = FrameworkRegistry.getInstance();
        Set<String> frameworks = registry.getFrameworks();
        if(frameworks.contains(framework)) {
            Generator generator = registry.getGenerator(framework);
            try {
                Any anyModel = null;
                // model can be empty in some cases.
                if(model != null) {
                    if(isUrl(model)) {
                        anyModel = JsonIterator.deserialize(urlToByteArray(new URL(model)));
                    } else {
                        anyModel = JsonIterator.deserialize(Files.readAllBytes(Paths.get(model)));
                    }
                }

                Any anyConfig = null;
                if(isUrl(config)) {
                    anyConfig = JsonIterator.deserialize(urlToByteArray(new URL(config)));
                } else {
                    anyConfig = JsonIterator.deserialize(Files.readAllBytes(Paths.get(config)));
                }
               generator.generate(output, anyModel, anyConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.printf("Invalid framework %s", framework);
        }
    }

    private boolean isUrl(String location) {
        return location.startsWith("http://") || location.startsWith("https://");
    }

    private byte[] urlToByteArray(URL url) throws IOException{
        try (BufferedInputStream in = new BufferedInputStream(url.openStream()); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                out.write(data, 0, count);
            }
            return out.toByteArray();
        }
    }
}
