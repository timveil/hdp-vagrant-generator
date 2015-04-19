package generator.service;

import com.google.common.collect.Sets;
import generator.model.Arguments;
import generator.model.Component;
import generator.model.MemoryConfiguration;
import org.apache.commons.io.FileUtils;
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


    public static final String VAGRANT_BLUEPRINT_JSON = "vagrant-blueprint.json";
    public static final String VAGRANT_CREATE_CLUSTER_JSON = "vagrant-create-cluster.json";
    public static final String VAGRANT_HDP_REPO_JSON = "vagrant-hdp-repo.json";
    public static final String VAGRANT_HDP_UTILS_REPO_JSON = "vagrant-hdp-utils-repo.json";
    public static final String VAGRANTFILE = "Vagrantfile";
    public static final String VAGRANT_HIVE_VIEW_JSON = "vagrant-hive-view.json";
    public static final String VAGRANT_FILES_VIEW_JSON = "vagrant-files-view.json";

    @Autowired
    private VelocityEngine engine;

    public void buildFile(Arguments arguments) {

        arguments.setHostname("test.hdp.local");
        arguments.setIp("192.168.66.101");
        arguments.setMemoryInMegabytes(8192);
        arguments.setReservedSystemMemoryInMegabytes(2048);
        arguments.setMinContainerSizeInMegabytes(512);
        arguments.setCpus(4);
        arguments.setUpdateLibraries(true);
        arguments.setBlueprintName("custom-" + System.currentTimeMillis());
        arguments.setClusterName("test-cluster");
        arguments.setComponents(Sets.newHashSet(Component.hive));
        arguments.setDisks(1);

        System.out.println(arguments.toString());

        MemoryConfiguration memoryConfiguration = new MemoryConfiguration(arguments);

        System.out.println(memoryConfiguration.toString());


        Map<String, Object> model = new HashMap<>();
        model.put("arguments", arguments);
        model.put("memory", memoryConfiguration);
        model.put("bluprentJsonFileName", VAGRANT_BLUEPRINT_JSON);
        model.put("createClusterJsonFileName", VAGRANT_CREATE_CLUSTER_JSON);
        model.put("hdpRepoJsonFileName", VAGRANT_HDP_REPO_JSON);
        model.put("hdpUtilsRepoJsonFileName", VAGRANT_HDP_UTILS_REPO_JSON);
        model.put("hiveViewJsonFileName", VAGRANT_HIVE_VIEW_JSON);
        model.put("filesViewJsonFileName", VAGRANT_FILES_VIEW_JSON);

        try {
            FileUtils.writeStringToFile(new File(VAGRANTFILE), VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrantfile.vm", "UTF-8", model));
            FileUtils.writeStringToFile(new File(VAGRANT_BLUEPRINT_JSON), VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-blueprint.vm", "UTF-8", model));
            FileUtils.writeStringToFile(new File(VAGRANT_CREATE_CLUSTER_JSON), VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-create-cluster.vm", "UTF-8", model));
            FileUtils.writeStringToFile(new File(VAGRANT_HDP_REPO_JSON), VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-hdp-repo.vm", "UTF-8", model));
            FileUtils.writeStringToFile(new File(VAGRANT_HDP_UTILS_REPO_JSON), VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-hdp-utils-repo.vm", "UTF-8", model));
            FileUtils.writeStringToFile(new File(VAGRANT_HIVE_VIEW_JSON), VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-hive-view.vm", "UTF-8", model));
            FileUtils.writeStringToFile(new File(VAGRANT_FILES_VIEW_JSON), VelocityEngineUtils.mergeTemplateIntoString(this.engine, "vagrant-files-view.vm", "UTF-8", model));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
