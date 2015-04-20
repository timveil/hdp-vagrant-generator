package generator.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import org.springframework.core.env.Environment;

import java.util.Set;

public class Arguments {

    private static final String HDP_REPO_NAME="HDP-2.2";
    private static final String HDP_UTILS_REPO_NAME="HDP-UTILS-1.1.0.20";
    private static final String FORMAT = "*** %-30s %s\n";

    private String hostname;

    private String ip;

    private Integer memoryInMegabytes;

    private Integer cpus;

    private Integer disks;

    private Integer minContainerSizeInMegabytes;

    private Integer reservedSystemMemoryInMegabytes;

    private Integer reservedHbaseMemoryInMegabytes;

    private String clusterName;

    private String blueprintName;

    private boolean updateLibraries;

    private Set<View> views;

    private Set<Component> components;

    public Arguments(Environment environment) {
        this.hostname =  environment.getProperty("h", String.class);
        this.ip =  environment.getProperty("ip", String.class, "192.168.66.101");
        this.memoryInMegabytes =  environment.getProperty("ram", Integer.class, 8192);
        this.reservedSystemMemoryInMegabytes =  environment.getProperty("rs", Integer.class, 2048);
        this.reservedHbaseMemoryInMegabytes =  environment.getProperty("rh", Integer.class, 0);
        this.minContainerSizeInMegabytes =  environment.getProperty("min", Integer.class, 512);
        this.cpus =  environment.getProperty("cpu", Integer.class, 4);
        this.updateLibraries =  environment.containsProperty("update");
        this.blueprintName =  environment.getProperty("b", String.class, "custom-generated-blueprint");
        this.clusterName =  environment.getProperty("n", String.class);
        this.disks =  environment.getProperty("d", Integer.class, 1);

        this.components = Sets.newHashSet(Component.hive);
        this.views = Sets.newHashSet(View.hive, View.jobs, View.tez, View.files);

        this.prettyPrint();
    }


    public Integer getDisks() {
        return disks;
    }

    public void setDisks(Integer disks) {
        this.disks = disks;
    }

    public String getBlueprintName() {
        return blueprintName;
    }

    public void setBlueprintName(String blueprintName) {
        this.blueprintName = blueprintName;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getMemoryInMegabytes() {
        return memoryInMegabytes;
    }

    public void setMemoryInMegabytes(Integer memoryInMegabytes) {
        this.memoryInMegabytes = memoryInMegabytes;
    }

    public Integer getCpus() {
        return cpus;
    }

    public void setCpus(Integer cpus) {
        this.cpus = cpus;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public boolean isUpdateLibraries() {
        return updateLibraries;
    }

    public void setUpdateLibraries(boolean updateLibraries) {
        this.updateLibraries = updateLibraries;
    }

    public Set<View> getViews() {
        return views;
    }

    public void setViews(Set<View> views) {
        this.views = views;
    }

    public Set<Component> getComponents() {
        return components;
    }

    public void setComponents(Set<Component> components) {
        this.components = components;
    }

    public Integer getMinContainerSizeInMegabytes() {
        return minContainerSizeInMegabytes;
    }

    public void setMinContainerSizeInMegabytes(Integer minContainerSizeInMegabytes) {
        this.minContainerSizeInMegabytes = minContainerSizeInMegabytes;
    }

    public String getBlueprintUrl() {
        return "http://" + hostname + ":8080/api/v1/blueprints/" + blueprintName;
    }

    public String getClusterUrl() {
        return "http://" + hostname + ":8080/api/v1/clusters/" + clusterName;
    }

    public String getHdpRepoUrl() {
        return "http://" + hostname + ":8080/api/v1/stacks/HDP/versions/2.2/operating_systems/redhat6/repositories/" + HDP_REPO_NAME;
    }

    public String getHdpUtilsRepoUrl() {
        return "http://" + hostname + ":8080/api/v1/stacks/HDP/versions/2.2/operating_systems/redhat6/repositories/" + HDP_UTILS_REPO_NAME;
    }

    public String getFilesViewUrl() {
        return "http://" + hostname + ":8080/api/v1/views/FILES/versions/0.1.0/instances/Files";
    }

    public String getHiveViewUrl() {
        return "http://" + hostname + ":8080/api/v1/views/HIVE/versions/0.0.1/instances/Hive";
    }

    public String getCheckStatusUrl() {
        return "http://" + hostname + ":8080/api/v1/clusters/" + clusterName + "/requests/1";
    }

    public boolean containsHiveComponent() {
        return components != null && components.contains(Component.hive);
    }

    public boolean containsHiveView() {
        return views != null && views.contains(View.hive) && containsHiveComponent();
    }

    public boolean containsFileView() {
        return views != null && views.contains(View.files);
    }

    public boolean containsKnoxComponent() {
        return components != null && components.contains(Component.knox);
    }

    public Integer getReservedSystemMemoryInMegabytes() {
        return reservedSystemMemoryInMegabytes;
    }

    public void setReservedSystemMemoryInMegabytes(Integer reservedSystemMemoryInMegabytes) {
        this.reservedSystemMemoryInMegabytes = reservedSystemMemoryInMegabytes;
    }

    public Integer getReservedHbaseMemoryInMegabytes() {
        return reservedHbaseMemoryInMegabytes;
    }

    public void setReservedHbaseMemoryInMegabytes(Integer reservedHbaseMemoryInMegabytes) {
        this.reservedHbaseMemoryInMegabytes = reservedHbaseMemoryInMegabytes;
    }

    private void prettyPrint() {
        System.out.println(" ");
        System.out.println("***********************************************************************");
        System.out.println("*** Arguments");
        System.out.println("***********************************************************************");
        System.out.printf(FORMAT, "VM Hostname", hostname);
        System.out.printf(FORMAT, "VM IP", ip);
        System.out.printf(FORMAT, "VM Memory in MB", memoryInMegabytes);
        System.out.printf(FORMAT, "VM CPUs", cpus);
        System.out.printf(FORMAT, "VM Disks", disks);
        System.out.printf(FORMAT, "Cluster Name", clusterName);
        System.out.printf(FORMAT, "Blueprint Name", blueprintName);
        System.out.printf(FORMAT, "Min Container Size in MB", minContainerSizeInMegabytes);
        System.out.printf(FORMAT, "Reserved System Memory in MB", reservedSystemMemoryInMegabytes);
        System.out.printf(FORMAT, "Reserved HBase Memory in MB", reservedHbaseMemoryInMegabytes);
        System.out.printf(FORMAT, "Update Libraries", updateLibraries);
        System.out.printf(FORMAT, "Views", views);
        System.out.printf(FORMAT, "Components", components);
        System.out.println("***********************************************************************");
        System.out.println(" ");
    }

}
