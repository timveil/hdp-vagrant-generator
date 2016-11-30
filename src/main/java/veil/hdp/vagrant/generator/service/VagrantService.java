package veil.hdp.vagrant.generator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import veil.hdp.vagrant.generator.model.Arguments;
import veil.hdp.vagrant.generator.model.MemoryConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class VagrantService implements FileService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String VAGRANTFILE = "Vagrantfile";

    @Autowired
    private Mustache.Compiler compiler;

    @Override
    public void buildFile(Arguments arguments) {

        MemoryConfiguration memoryConfiguration = new MemoryConfiguration(arguments);

        Map<String, Object> model = new HashMap<>();
        model.put("arguments", arguments);
        model.put("memory", memoryConfiguration);
        model.put("generatedDate", SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT).format(new Date()));
        model.put("requestedBy", "X-Requested-By: ambari");

        Mustache.Lambda logger = new Mustache.Lambda() {
            public void execute(Template.Fragment frag, Writer out) throws IOException {
                out.write("<b>");
                frag.execute(out);
                out.write("</b>");
            }
        };

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(this.getClass().getClassLoader());

        String blueprintJson = convertTemplateToString(resolver, "templates/common/json/json-blueprint.mustache", model);
        String createClusterJson = convertTemplateToString(resolver, "templates/common/json/json-create-cluster.mustache", model);

        //todo build this conditionally
        String hdpRepoJson = convertTemplateToString(resolver, "templates/common/json/json-create-repository.mustache", ImmutableMap.of("url", arguments.getHdpRepoBaseUrl()));
        //todo build this conditionally
        String hdpUtilsRepoJson = convertTemplateToString(resolver, "templates/common/json/json-create-repository.mustache", ImmutableMap.of("url", arguments.getHdpRepoUtilsBaseUrl()));

        model.put("blueprintJson", compactJSON(blueprintJson));
        model.put("hdpRepoJson", compactJSON(hdpRepoJson));
        model.put("hdpUtilsRepoJson", compactJSON(hdpUtilsRepoJson));
        model.put("createClusterJson", compactJSON(createClusterJson));

        String installShellScript = convertTemplateToString(resolver, "templates/common/shell/shell-install.mustache", model);
        String postInstallShellScript = convertTemplateToString(resolver, "templates/common/shell/shell-post-install.mustache", model);
        String cleanLogsShellScript = convertTemplateToString(resolver, "templates/common/shell/shell-clean-logs.mustache", model);
        String checkClusterStatusShellScript = convertTemplateToString(resolver, "templates/common/shell/shell-check-cluster-status.mustache", model);
        String vagrantFile = convertTemplateToString(resolver, "templates/vagrant/vagrantfile.mustache", model);



        final String parentDirectoryName = "out/" + arguments.getFqdn();

        try {
            FileUtils.writeStringToFile(new File(parentDirectoryName, VAGRANTFILE), vagrantFile, StandardCharsets.UTF_8);
            FileUtils.writeStringToFile(new File(parentDirectoryName, "shell-clean-logs.sh"), cleanLogsShellScript, StandardCharsets.UTF_8);
            FileUtils.writeStringToFile(new File(parentDirectoryName, "shell-check-cluster-status.sh"), checkClusterStatusShellScript, StandardCharsets.UTF_8);
            FileUtils.writeStringToFile(new File(parentDirectoryName, "shell-install.sh"), installShellScript, StandardCharsets.UTF_8);
            FileUtils.writeStringToFile(new File(parentDirectoryName, "shell-post-install.sh"), postInstallShellScript, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }

    private String compactJSON(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode jsonNode = mapper.readTree(json);
            return jsonNode.toString();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return json;
    }

    private String convertTemplateToString(ResourcePatternResolver resolver, String templateLocation, Map<String, Object> model) {
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
