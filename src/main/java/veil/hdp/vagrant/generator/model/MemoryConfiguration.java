package veil.hdp.vagrant.generator.model;

import com.google.common.primitives.Ints;

public class MemoryConfiguration {

    private static final String FORMAT = "*** %-40s %s\n";


    private int yarnNodeManagerResoureMemory;
    private int yarnMinAllocation;
    private int yarnMaxAllocation;
    private int mrMapMemory;
    private int mrReduceMemory;
    private String mrMapOpts;
    private String mrReduceOpts;
    private int yarnMrMemory;
    private String yarnMrCommandOpts;

    public MemoryConfiguration(Arguments arguments) {

        final int availableRam = arguments.getMemoryInMegabytes() - (arguments.getReservedSystemMemoryInMegabytes() + arguments.getReservedHbaseMemoryInMegabytes());
        System.out.printf(FORMAT, "Available RAM", availableRam);

        final int numberOfContainers = Ints.min(2 * arguments.getCores(), availableRam / arguments.getMinContainerSizeInMegabytes());
        System.out.printf(FORMAT, "Number of Containers", numberOfContainers);

        final int ramPerContainer = Ints.max(arguments.getMinContainerSizeInMegabytes(), (availableRam / numberOfContainers));
        System.out.printf(FORMAT, "RAM per Container", ramPerContainer);

        yarnNodeManagerResoureMemory = numberOfContainers * ramPerContainer;
        yarnMinAllocation = ramPerContainer;
        yarnMaxAllocation = numberOfContainers * ramPerContainer;
        mrMapMemory = ramPerContainer;
        mrReduceMemory = 2 * ramPerContainer;
        mrMapOpts = Integer.toString((int)Math.floor(.8 * ramPerContainer));
        mrReduceOpts = Integer.toString((int) Math.floor(.8 * (2 * ramPerContainer)));
        yarnMrMemory = 2 * ramPerContainer;
        yarnMrCommandOpts = Integer.toString((int) Math.floor(.8 * (2 * ramPerContainer)));

        this.prettyPrint();

    }


    public int getYarnNodeManagerResoureMemory() {
        return yarnNodeManagerResoureMemory;
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

    private void prettyPrint() {
        System.out.println(" ");
        System.out.println("***********************************************************************");
        System.out.println("*** Memory Configuration");
        System.out.println("***********************************************************************");
        System.out.printf(FORMAT, "yarn.nodemanager.resource.memory-mb", yarnNodeManagerResoureMemory);
        System.out.printf(FORMAT, "yarn.scheduler.minimum-allocation-mb", yarnMinAllocation);
        System.out.printf(FORMAT, "yarn.scheduler.maximum-allocation-mb", yarnMaxAllocation);
        System.out.printf(FORMAT, "yarn.app.mapreduce.am.resource.mb", yarnMrMemory);
        System.out.printf(FORMAT, "yarn.app.mapreduce.am.command-opts", yarnMrCommandOpts);
        System.out.printf(FORMAT, "mapreduce.map.memory.mb", mrMapMemory);
        System.out.printf(FORMAT, "mapreduce.map.java.opts", mrMapOpts);
        System.out.printf(FORMAT, "mapreduce.reduce.memory.mb", mrReduceMemory);
        System.out.printf(FORMAT, "mapreduce.reduce.java.opts", mrReduceOpts);
        System.out.println("***********************************************************************");
        System.out.println(" ");
    }
}
