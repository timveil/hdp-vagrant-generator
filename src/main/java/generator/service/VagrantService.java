package generator.service;

import generator.model.Arguments;
import generator.model.MemoryConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class VagrantService {


    public static final String VAGRANTFILE = "Vagrantfile";
    public static final String VAGRANT_CHECKSTATUS_SH = "vagrant-checkstatus.sh";
    public static final String ENCODING = "UTF-8";

    @Autowired
    private VelocityEngine engine;

    public void buildFile(Arguments arguments) {

        MemoryConfiguration memoryConfiguration = new MemoryConfiguration(arguments);

        Map<String, Object> model = new HashMap<>();
        model.put("arguments", arguments);
        model.put("memory", memoryConfiguration);
        model.put("generatedDate", SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT).format(new Date()));

        addTemplateToModel(model, "vagrant-blueprint.vm", "blueprint");
        addTemplateToModel(model, "vagrant-create-cluster.vm", "createCluster");
        addTemplateToModel(model, "vagrant-hdp-repo.vm", "hdpRepo");
        addTemplateToModel(model, "vagrant-hdp-utils-repo.vm", "hdpUtilsRepo");

        if (arguments.containsHiveView()) {
            addTemplateToModel(model, "vagrant-hive-view.vm", "hiveView");
        }

        addTemplateToModel(model, "vagrant-files-view.vm", "filesView");
        addTemplateToModel(model, "vagrant-tez-view.vm", "tezView");
        addTemplateToModel(model, "vagrant-jobs-view.vm", "jobsView");

        try {
            FileUtils.writeStringToFile(new File(VAGRANTFILE), VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrantfile.vm", ENCODING, model));
            FileUtils.writeStringToFile(new File(VAGRANT_CHECKSTATUS_SH), VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-checkstatus.vm", ENCODING, model));
        } catch (IOException e) {
            e.printStackTrace();
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
