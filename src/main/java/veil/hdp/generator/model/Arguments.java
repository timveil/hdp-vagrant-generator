package veil.hdp.generator.model;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import org.springframework.core.env.Environment;
import veil.hdp.generator.Constants;

import java.util.List;
import java.util.Set;

public class Arguments {

    private static final String FORMAT = "*** %-30s %s\n";

    private String hostname;

    private String ip;

    private Integer memoryInMegabytes;

    private Integer cores;

    private Integer disks;

    private Integer minContainerSizeInMegabytes;

    private Integer reservedSystemMemoryInMegabytes;

    private Integer reservedHbaseMemoryInMegabytes;

    private String clusterName;

    private String blueprintName;

    private boolean updateLibraries;

    private String ambariRepoUrl;

    private Set<View> views;

    private Set<Component> components;

    private String mySqlConnectorVersion;

    private String stackVersion;

    public Arguments(Environment environment) {
        this.hostname =  environment.getProperty(Constants.VM_HOSTNAME, String.class);
        this.ip =  environment.getProperty(Constants.VM_IP, String.class);
        this.memoryInMegabytes =  environment.getProperty(Constants.VM_RAM, Integer.class);
        this.reservedSystemMemoryInMegabytes =  environment.getProperty(Constants.HDP_MEMORY_RESERVED_SYSTEM, Integer.class);
        this.reservedHbaseMemoryInMegabytes =  environment.getProperty(Constants.HDP_MEMORY_RESERVED_HBASE, Integer.class);
        this.minContainerSizeInMegabytes =  environment.getProperty(Constants.HDP_MIN_CONTAINER_SIZE, Integer.class);
        this.cores =  environment.getProperty(Constants.VM_CORES, Integer.class);
        this.updateLibraries =   Boolean.parseBoolean(environment.getProperty(Constants.VM_UPDATE_YUM, String.class));
        this.blueprintName =  environment.getProperty(Constants.HDP_BLUEPRINT_NAME, String.class);
        this.clusterName =  environment.getProperty(Constants.HDP_CLUSTER_NAME, String.class);
        this.disks =  environment.getProperty(Constants.VM_DISKS, Integer.class);
        this.mySqlConnectorVersion =  environment.getProperty(Constants.MYSQL_CONNECTOR_VERSION, String.class);
        this.stackVersion =  environment.getProperty(Constants.HDP_STACK_VERSION, String.class);

        String componentString = environment.getProperty(Constants.HDP_COMPONENTS);
        List<String> componentStrings = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(componentString);
        Set<Component> components = Sets.newHashSet();

        if (!componentStrings.isEmpty()) {
            for (String component : componentStrings) {
                components.add(Component.valueOf(component));
            }
        }

        this.components = components;


        String viewString = environment.getProperty(Constants.HDP_VIEWS);
        List<String> viewStrings = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(viewString);
        Set<View> views = Sets.newHashSet();

        if (!viewStrings.isEmpty()) {
            for (String view : viewStrings) {
                views.add(View.valueOf(view));
            }
        }

        this.views = views;


        this.ambariRepoUrl = environment.getProperty(Constants.HDP_AMBARI_REPO, String.class);


        this.prettyPrint();
    }

    public String getHostname() {
        return hostname;
    }

    public String getIp() {
        return ip;
    }

    public Integer getMemoryInMegabytes() {
        return memoryInMegabytes;
    }

    public Integer getCores() {
        return cores;
    }

    public Integer getDisks() {
        return disks;
    }

    public Integer getMinContainerSizeInMegabytes() {
        return minContainerSizeInMegabytes;
    }

    public Integer getReservedSystemMemoryInMegabytes() {
        return reservedSystemMemoryInMegabytes;
    }

    public Integer getReservedHbaseMemoryInMegabytes() {
        return reservedHbaseMemoryInMegabytes;
    }

    public String getClusterName() {
        return clusterName;
    }

    public String getBlueprintName() {
        return blueprintName;
    }

    public String getAmbariRepoUrl() {
        return ambariRepoUrl;
    }

    public boolean isUpdateLibraries() {
        return updateLibraries;
    }

    public Set<View> getViews() {
        return views;
    }

    public Set<Component> getComponents() {
        return components;
    }

    public String getBlueprintUrl() {
        return "http://" + hostname + ":8080/api/v1/blueprints/" + blueprintName;
    }

    public String getClusterUrl() {
        return "http://" + hostname + ":8080/api/v1/clusters/" + clusterName;
    }

    public String getFilesViewUrl() {
        return "http://" + hostname + ":8080/api/v1/views/FILES/versions/1.0.0/instances/Files";
    }

    public String getCheckStatusUrl() {
        return "http://" + hostname + ":8080/api/v1/clusters/" + clusterName + "/requests/1";
    }

    public boolean containsHiveComponent() {
        return components != null && components.contains(Component.hive);
    }

    public boolean containsFileView() {
        return views != null && views.contains(View.files);
    }

    public boolean containsKnoxComponent() {
        return components != null && components.contains(Component.knox);
    }

    public boolean containsSqoopComponent() {
        return components != null && components.contains(Component.sqoop);
    }

    public boolean containsSparkComponent() {
        return components != null && components.contains(Component.spark);
    }

    public boolean containsRangerComponent() {
        return components != null && components.contains(Component.ranger);
    }

    public String getMySqlConnectorVersion() {
        return mySqlConnectorVersion;
    }

    public String getStackVersion() {
        return stackVersion;
    }

    private void prettyPrint() {
        System.out.println(" ");
        System.out.println("***********************************************************************");
        System.out.println("*** Arguments");
        System.out.println("***********************************************************************");
        System.out.printf(FORMAT, Constants.VM_HOSTNAME, hostname);
        System.out.printf(FORMAT, Constants.VM_IP, ip);
        System.out.printf(FORMAT, Constants.VM_RAM, memoryInMegabytes);
        System.out.printf(FORMAT, Constants.VM_CORES, cores);
        System.out.printf(FORMAT, Constants.VM_DISKS, disks);
        System.out.printf(FORMAT, Constants.VM_UPDATE_YUM, updateLibraries);
        System.out.printf(FORMAT, Constants.HDP_CLUSTER_NAME, clusterName);
        System.out.printf(FORMAT, Constants.HDP_BLUEPRINT_NAME, blueprintName);
        System.out.printf(FORMAT, Constants.HDP_STACK_VERSION, stackVersion);
        System.out.printf(FORMAT, Constants.HDP_MIN_CONTAINER_SIZE, minContainerSizeInMegabytes);
        System.out.printf(FORMAT, Constants.HDP_MEMORY_RESERVED_SYSTEM, reservedSystemMemoryInMegabytes);
        System.out.printf(FORMAT, Constants.HDP_MEMORY_RESERVED_HBASE, reservedHbaseMemoryInMegabytes);
        System.out.printf(FORMAT, Constants.HDP_AMBARI_REPO, ambariRepoUrl);
        System.out.printf(FORMAT, Constants.HDP_VIEWS, views);
        System.out.printf(FORMAT, Constants.HDP_COMPONENTS, components);
        System.out.println("***********************************************************************");
        System.out.println(" ");
    }

}
