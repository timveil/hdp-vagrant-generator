package veil.hdp.vagrant.generator.model;

import com.google.common.primitives.Ints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import veil.hdp.vagrant.generator.Constants;

import java.util.Formatter;

public class MemoryConfiguration {

    private final Logger log = LoggerFactory.getLogger(getClass());

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

        final int numberOfContainers = Ints.min(2 * arguments.getCores(), availableRam / arguments.getMinContainerSizeInMegabytes());

        final int ramPerContainer = Ints.max(arguments.getMinContainerSizeInMegabytes(), (availableRam / numberOfContainers));

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

        StringBuilder builder = new StringBuilder();
        Formatter formatter = new Formatter(builder);

        formatter.format(Constants.FORMAT_NEW_LINE, " ");
        formatter.format(Constants.FORMAT_NEW_LINE, "***********************************************************************");
        formatter.format(Constants.FORMAT_NEW_LINE, "*** Memory Configuration");
        formatter.format(Constants.FORMAT_NEW_LINE, "***********************************************************************");
        formatter.format(Constants.FORMAT_SPACER, "yarn.nodemanager.resource.memory-mb", yarnNodeManagerResoureMemory);
        formatter.format(Constants.FORMAT_SPACER, "yarn.scheduler.minimum-allocation-mb", yarnMinAllocation);
        formatter.format(Constants.FORMAT_SPACER, "yarn.scheduler.maximum-allocation-mb", yarnMaxAllocation);
        formatter.format(Constants.FORMAT_SPACER, "yarn.app.mapreduce.am.resource.mb", yarnMrMemory);
        formatter.format(Constants.FORMAT_SPACER, "yarn.app.mapreduce.am.command-opts", yarnMrCommandOpts);
        formatter.format(Constants.FORMAT_SPACER, "mapreduce.map.memory.mb", mrMapMemory);
        formatter.format(Constants.FORMAT_SPACER, "mapreduce.map.java.opts", mrMapOpts);
        formatter.format(Constants.FORMAT_SPACER, "mapreduce.reduce.memory.mb", mrReduceMemory);
        formatter.format(Constants.FORMAT_SPACER, "mapreduce.reduce.java.opts", mrReduceOpts);
        formatter.format(Constants.FORMAT_NEW_LINE, "***********************************************************************");
        formatter.format(Constants.FORMAT_NEW_LINE, " ");

        log.info(builder.toString());
    }
}
