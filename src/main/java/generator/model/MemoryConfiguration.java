package generator.model;

import com.google.common.base.MoreObjects;
import com.google.common.primitives.Ints;

public class MemoryConfiguration {


    private int nodeManagerResoureMemory;
    private int yarnMinAllocation;
    private int yarnMaxAllocation;
    private int mrMapMemory;
    private int mrReduceMemory;
    private String mrMapOpts;
    private String mrReduceOpts;
    private int yarnMrMemory;
    private String yarnMrCommandOpts;

    public MemoryConfiguration(Arguments arguments) {

        int availableRam = arguments.getMemoryInMegabytes() - arguments.getReservedSystemMemoryInMegabytes();
        System.out.println("Available RAM: " + availableRam);

        int numberOfContainers = Ints.min(2 * arguments.getCpus(), availableRam / arguments.getMinContainerSizeInMegabytes());
        System.out.println("Number of Containers: " + numberOfContainers);

        int ramPerContainer = Ints.max(arguments.getMinContainerSizeInMegabytes(), (availableRam / numberOfContainers));
        System.out.println("RAM per Container: " + ramPerContainer);

        nodeManagerResoureMemory = numberOfContainers * ramPerContainer;
        yarnMinAllocation = ramPerContainer;
        yarnMaxAllocation = numberOfContainers * ramPerContainer;
        mrMapMemory = ramPerContainer;
        mrReduceMemory = 2 * ramPerContainer;
        mrMapOpts = Integer.toString((int)Math.floor(.8 * ramPerContainer));
        mrReduceOpts = Integer.toString((int) Math.floor(.8 * (2 * ramPerContainer)));
        yarnMrMemory = 2 * ramPerContainer;
        yarnMrCommandOpts = Integer.toString((int) Math.floor(.8 * (2 * ramPerContainer)));

    }


    public int getNodeManagerResoureMemory() {
        return nodeManagerResoureMemory;
    }

    public int getYarnMinAllocation() {
        return yarnMinAllocation;
    }

    public int getYarnMaxAllocation() {
        return yarnMaxAllocation;
    }

    public int getMrMapMemory() {
        return mrMapMemory;
    }

    public int getMrReduceMemory() {
        return mrReduceMemory;
    }

    public String getMrMapOpts() {
        return mrMapOpts;
    }

    public String getMrReduceOpts() {
        return mrReduceOpts;
    }

    public int getYarnMrMemory() {
        return yarnMrMemory;
    }

    public String getYarnMrCommandOpts() {
        return yarnMrCommandOpts;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("nodeManagerResoureMemory", nodeManagerResoureMemory)
                .add("yarnMinAllocation", yarnMinAllocation)
                .add("yarnMaxAllocation", yarnMaxAllocation)
                .add("mrMapMemory", mrMapMemory)
                .add("mrReduceMemory", mrReduceMemory)
                .add("mrMapOpts", mrMapOpts)
                .add("mrReduceOpts", mrReduceOpts)
                .add("yarnMrMemory", yarnMrMemory)
                .add("yarnMrCommandOpts", yarnMrCommandOpts)
                .toString();
    }
}
