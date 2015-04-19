package generator.service;

import com.google.common.collect.Sets;
import generator.model.Arguments;
import generator.model.Component;
import generator.model.MemoryConfiguration;
import generator.model.View;
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
    public static final String ENCODING = "UTF-8";

    @Autowired
    private VelocityEngine engine;

    public void buildFile(Arguments arguments) {

        arguments.setHostname("test.hdp.local");
        arguments.setIp("192.168.66.101");
        arguments.setMemoryInMegabytes(8192);
        arguments.setReservedSystemMemoryInMegabytes(2048);
        arguments.setReservedHbaseMemoryInMegabytes(1024);
        arguments.setMinContainerSizeInMegabytes(512);
        arguments.setCpus(4);
        arguments.setUpdateLibraries(true);
        arguments.setBlueprintName("custom-generated-blueprint");
        arguments.setClusterName("test-cluster");
        arguments.setComponents(Sets.newHashSet(Component.hive));
        arguments.setViews(Sets.newHashSet(View.files, View.hive, View.jobs, View.tez));
        arguments.setDisks(1);

        System.out.println(arguments.toString());

        MemoryConfiguration memoryConfiguration = new MemoryConfiguration(arguments);

        System.out.println(memoryConfiguration.toString());

        Map<String, Object> model = new HashMap<>();
        model.put("arguments", arguments);
        model.put("memory", memoryConfiguration);
        model.put("generatedDate", SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT).format(new Date()));

        String blueprint = cleanTemplate(VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-blueprint.vm", ENCODING, model));
        model.put("blueprint", blueprint);

        String createCluster = cleanTemplate(VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-create-cluster.vm", ENCODING, model));
        model.put("createCluster", createCluster);

        String hdpRepo = cleanTemplate(VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-hdp-repo.vm", ENCODING, model));
        model.put("hdpRepo", hdpRepo);

        String hdpUtilsRepo = cleanTemplate(VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-hdp-utils-repo.vm", ENCODING, model));
        model.put("hdpUtilsRepo", hdpUtilsRepo);

        if (arguments.containsHiveView()) {
            String hiveView = cleanTemplate(VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-hive-view.vm", ENCODING, model));
            model.put("hiveView", hiveView);
        }

        String filesView = cleanTemplate(VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-files-view.vm", ENCODING, model));
        model.put("filesView", filesView);

        String tezView = cleanTemplate(VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-tez-view.vm", ENCODING, model));
        model.put("tezView", tezView);

        String jobsView = cleanTemplate(VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-jobs-view.vm", ENCODING, model));
        model.put("jobsView", jobsView);

        try {
            FileUtils.writeStringToFile(new File(VAGRANTFILE), VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrantfile.vm", ENCODING, model));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String cleanTemplate(String template) {
        template = StringUtils.remove(template, "\n");
        template = StringUtils.remove(template, "  ");
        return StringUtils.trimToNull(template);
    }

}
