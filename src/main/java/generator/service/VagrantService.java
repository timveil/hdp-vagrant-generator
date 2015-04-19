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
import java.util.HashMap;
import java.util.Map;

@Service
public class VagrantService {


    public static final String VAGRANTFILE = "Vagrantfile";

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
        arguments.setBlueprintName("generated-" + System.currentTimeMillis());
        arguments.setClusterName("test-cluster");
        arguments.setComponents(Sets.newHashSet(Component.hive));
        arguments.setViews(Sets.newHashSet(View.file, View.hive));
        arguments.setDisks(1);

        System.out.println(arguments.toString());

        MemoryConfiguration memoryConfiguration = new MemoryConfiguration(arguments);

        System.out.println(memoryConfiguration.toString());


        Map<String, Object> model = new HashMap<>();
        model.put("arguments", arguments);
        model.put("memory", memoryConfiguration);

        String blueprint = cleanTemplate(VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-blueprint.vm", "UTF-8", model));
        String createCluster = cleanTemplate(VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-create-cluster.vm", "UTF-8", model));
        String hdpRepo = cleanTemplate(VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-hdp-repo.vm", "UTF-8", model));
        String hdpUtilsRepo = cleanTemplate(VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-hdp-utils-repo.vm", "UTF-8", model));

        String hiveView = cleanTemplate(VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-hive-view.vm", "UTF-8", model));
        String filesView = cleanTemplate(VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-files-view.vm", "UTF-8", model));

        model.put("blueprint", blueprint);
        model.put("createCluster", createCluster);
        model.put("hdpRepo", hdpRepo);
        model.put("hdpUtilsRepo", hdpUtilsRepo);
        model.put("hiveView", hiveView);
        model.put("filesView", filesView);
        //model.put("tezView", tezView);
        //model.put("jobsView", jobsView);


        try {
            FileUtils.writeStringToFile(new File(VAGRANTFILE), VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrantfile.vm", "UTF-8", model));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String cleanTemplate(String template) {
        template = StringUtils.remove(template, "\n");
        template = StringUtils.remove(template, "  ");
        System.out.println(template);

        return template;
    }

}
