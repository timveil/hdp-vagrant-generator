package veil.hdp.vagrant.generator.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import veil.hdp.vagrant.generator.model.Arguments;
import veil.hdp.vagrant.generator.model.MemoryConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class VagrantService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String VAGRANTFILE = "Vagrantfile";
    private static final String VAGRANT_CHECKSTATUS_SH = "vagrant-checkstatus.sh";
    private static final String ENCODING = "UTF-8";

    @Autowired
    private VelocityEngine engine;

    public void buildFile(Arguments arguments) {

        MemoryConfiguration memoryConfiguration = new MemoryConfiguration(arguments);

        Map<String, Object> model = new HashMap<>();
        model.put("arguments", arguments);
        model.put("memory", memoryConfiguration);
        model.put("generatedDate", SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT).format(new Date()));

        addTemplateToModel(model, "macros.vm", "macros");
        addTemplateToModel(model, "vagrant-blueprint.vm", "blueprint");
        addTemplateToModel(model, "vagrant-create-cluster.vm", "createCluster");

        final String parentDirectoryName = "out/" + arguments.getFqdn();

        try {
            FileUtils.writeStringToFile(new File(parentDirectoryName, VAGRANTFILE), VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrantfile.vm", ENCODING, model));
            FileUtils.writeStringToFile(new File(parentDirectoryName, VAGRANT_CHECKSTATUS_SH), VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-checkstatus.vm", ENCODING, model));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }

    private void addTemplateToModel(Map<String, Object> model, String templateName, String key) {
        String template = cleanTemplate(VelocityEngineUtils.mergeTemplateIntoString(this.engine, templateName, ENCODING, model));
        model.put(key, template);
    }

    private String cleanTemplate(String template) {
        template = StringUtils.remove(template, "\n");
        template = StringUtils.remove(template, "  ");
        return StringUtils.trimToNull(template);
    }

}
