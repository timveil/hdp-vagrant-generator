package generator.model;

import com.google.common.base.MoreObjects;
import org.springframework.core.env.Environment;

import java.util.Set;

public class Arguments {

    private static final String HDP_REPO_NAME="HDP-2.2";
    private static final String HDP_UTILS_REPO_NAME="HDP-UTILS-1.1.0.20";


    private String hostname;

    private String ip;

    private Integer memoryInMegabytes;

    private Integer cpus;

    private Integer disks;

    private Integer minContainerSizeInMegabytes;
    private Integer reservedSystemMemoryInMegabytes;

    private String clusterName;

    private String blueprintName;

    private boolean updateLibraries;

    private String mysqlPassword;

    private Set<View> views;

    private Set<Component> components;

    public Arguments(Environment environment) {


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

    public String getMysqlPassword() {
        return mysqlPassword;
    }

    public void setMysqlPassword(String mysqlPassword) {
        this.mysqlPassword = mysqlPassword;
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
        return "http://" + hostname + ":8080/api/v1/views/HIVE/versions/0.1.0/instances/Hive";
    }

    public String getCheckStatusUrl() {
        return "http://" + hostname + ":8080/api/v1/clusters/" + clusterName + "/requests/1";
    }

    public boolean containsHive() {
        return components != null && components.contains(Component.hive);
    }

    public boolean containsKnox() {
        return components != null && components.contains(Component.knox);
    }

    public Integer getReservedSystemMemoryInMegabytes() {
        return reservedSystemMemoryInMegabytes;
    }

    public void setReservedSystemMemoryInMegabytes(Integer reservedSystemMemoryInMegabytes) {
        this.reservedSystemMemoryInMegabytes = reservedSystemMemoryInMegabytes;
    }

    public void prettyPrint() {

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("hostname", hostname)
                .add("ip", ip)
                .add("memoryInMegabytes", memoryInMegabytes)
                .add("cpus", cpus)
                .add("disks", disks)
                .add("minContainerSizeInMegabytes", minContainerSizeInMegabytes)
                .add("reservedSystemMemoryInMegabytes", reservedSystemMemoryInMegabytes)
                .add("clusterName", clusterName)
                .add("blueprintName", blueprintName)
                .add("updateLibraries", updateLibraries)
                .add("mysqlPassword", mysqlPassword)
                .add("views", views)
                .add("components", components)
                .toString();
    }
}
