package veil.hdp.vagrant.generator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import veil.hdp.vagrant.generator.model.Arguments;
import veil.hdp.vagrant.generator.model.MemoryConfiguration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFileService implements FileService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected Mustache.Compiler compiler;

    protected ResourcePatternResolver resolver;

    protected AbstractFileService() {
       resolver = new PathMatchingResourcePatternResolver(this.getClass().getClassLoader());
    }

    @Override
    public void buildFile(Arguments arguments) {
        Map<String, Object> model = buildModel(arguments);

        buildFile(model, arguments);
    }

    protected abstract void buildFile(Map<String, Object> model, Arguments arguments);

    protected Map<String, Object> buildModel(Arguments arguments) {
        MemoryConfiguration memoryConfiguration = new MemoryConfiguration(arguments);

        Map<String, Object> model = new HashMap<>();
        model.put("arguments", arguments);
        model.put("memory", memoryConfiguration);
        model.put("generatedDate", SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT).format(new Date()));
        model.put("requestedBy", "X-Requested-By: ambari");

        Mustache.Lambda logger = new Mustache.Lambda() {
            public void execute(Template.Fragment frag, Writer out) throws IOException {
                out.write("echo ");
                frag.execute(out);
            }
        };


        String blueprintJson = convertTemplateToString(resolver, "templates/common/json/json-blueprint.mustache", model);
        model.put("blueprintJson", compactJSON(blueprintJson));

        String createClusterJson = convertTemplateToString(resolver, "templates/common/json/json-create-cluster.mustache", model);
        model.put("createClusterJson", compactJSON(createClusterJson));

        if (StringUtils.isNotBlank(arguments.getHdpRepoBaseUrl()) && StringUtils.isNotBlank(arguments.getAmbariApiRepositoriesHdpUrl())) {
            String hdpRepoJson = convertTemplateToString(resolver, "templates/common/json/json-create-repository.mustache", ImmutableMap.of("url", arguments.getHdpRepoBaseUrl()));
            model.put("hdpRepoJson", compactJSON(hdpRepoJson));
        }

        if (StringUtils.isNotBlank(arguments.getHdpRepoUtilsBaseUrl()) && StringUtils.isNotBlank(arguments.getAmbariApiRepositoriesHdpUtilsUrl())) {
            String hdpUtilsRepoJson = convertTemplateToString(resolver, "templates/common/json/json-create-repository.mustache", ImmutableMap.of("url", arguments.getHdpRepoUtilsBaseUrl()));
            model.put("hdpUtilsRepoJson", compactJSON(hdpUtilsRepoJson));
        }
        return model;
    }


    protected String compactJSON(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode jsonNode = mapper.readTree(json);
            return jsonNode.toString();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return json;
    }

    protected String convertTemplateToString(ResourcePatternResolver resolver, String templateLocation, Map<String, Object> model) {
        Resource resource = resolver.getResource(templateLocation);
        StringWriter out = new StringWriter();

        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
            Template template = compiler.escapeHTML(false).compile(reader);
            template.execute(model, out);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return out.toString();

    }

}
