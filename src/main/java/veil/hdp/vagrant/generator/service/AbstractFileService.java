package veil.hdp.vagrant.generator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import veil.hdp.vagrant.generator.Constants;
import veil.hdp.vagrant.generator.model.Arguments;
import veil.hdp.vagrant.generator.model.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
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

        final String blueprintName="generated";
        final String clusterName=arguments.getHostname();

        Map<String, Object> model = new HashMap<>();
        model.put("arguments", arguments);
        model.put("generatedDate", SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT).format(new Date()));
        model.put("requestedBy", "X-Requested-By: ambari");
        model.put("clusterName", clusterName);
        model.put("blueprintName", blueprintName);

        boolean hasHive = arguments.getComponents().contains(Component.hive);
        boolean hasSpark = arguments.getComponents().contains(Component.spark);
        boolean hasHbase = arguments.getComponents().contains(Component.hbase);

        if (hasSpark || hasHbase) {
            hasHive = true;
        }

        model.put("containsHive", hasHive);
        model.put("containsSpark", hasSpark);
        model.put("containsHbase", hasHbase);

        model.put("createBlueprintUrl", MessageFormat.format("http://{0}:8080/api/v1/blueprints/{1}", arguments.getFqdn(), blueprintName));
        model.put("createClusterUrl", MessageFormat.format("http://{0}:8080/api/v1/clusters/{1}", arguments.getFqdn(), clusterName));
        model.put("createClusterStatusUrl", MessageFormat.format("http://{0}:8080/api/v1/clusters/{1}/requests/1", arguments.getFqdn(), clusterName));

        if (arguments.isCustomRepoEnabled()) {
            model.put("ambariRepoFileUrl", arguments.getCustomRepoAmbariUrl());
            //todo: don't like that the repository names are hardcoded.  not sure how to get around this because i don't know where they come from
            model.put("createHdpRepositoryUrl", MessageFormat.format("http://{0}:8080/api/v1/stacks/HDP/versions/{1}/operating_systems/redhat7/repositories/HDP-2.5", arguments.getFqdn(), arguments.getStackVersion()));
            model.put("createHdpUtilsRepositoryUrl", MessageFormat.format("http://{0}:8080/api/v1/stacks/HDP/versions/{1}/operating_systems/redhat7/repositories/HDP-UTILS-1.1.0.21", arguments.getFqdn(), arguments.getStackVersion()));
        } else {
            model.put("ambariRepoFileUrl", MessageFormat.format("http://public-repo-1.hortonworks.com/ambari/centos7/2.x/updates/{0}/ambari.repo", arguments.getAmbariVersion()));
        }

        if (arguments.isKerberosEnabled()) {
            model.put("realmUpper", arguments.getKerberosRealm().toUpperCase());
            model.put("realmLower", arguments.getKerberosRealm().toLowerCase());
            model.put("kdcAdmin", Constants.KDC_ADMIN);
            model.put("kdcPassword", Constants.KDC_PASSWORD);
        }



        Mustache.Lambda logger = (frag, out) -> {
            out.write("echo \" \"\n");
            out.write("echo \"---------------------------------------------------------------------------------------------------------------\"\n");
            out.write("echo \"----- ");
            frag.execute(out);
            out.write("\"\n");
            out.write("echo \"---------------------------------------------------------------------------------------------------------------\"\n");
            out.write("echo \" \"\n");
        };

        model.put("logger", logger);

        return model;
    }

    protected void writeContentsToFile(String parentDirectoryName, String fileName, String contents) {
        try {
            FileUtils.writeStringToFile(new File(parentDirectoryName, fileName), contents, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
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
