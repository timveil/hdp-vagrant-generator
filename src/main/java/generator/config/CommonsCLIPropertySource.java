package generator.config;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.springframework.core.env.CommandLinePropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonsCLIPropertySource extends CommandLinePropertySource<CommandLine> {


    public CommonsCLIPropertySource(CommandLine source) {
        super(source);
    }

    @Override
    protected boolean containsOption(String name) {
        return this.source.hasOption(name);
    }

    @Override
    protected List<String> getOptionValues(String name) {
        if (containsOption(name)) {
            return Arrays.asList(this.source.getOptionValues(name));
        }

        return null;
    }

    @Override
    protected List<String> getNonOptionArgs() {
        return null;
    }

    @Override
    public String[] getPropertyNames() {

        List<String> names = new ArrayList<>();

        for (Option option : this.source.getOptions()) {
            names.add(option.getArgName());
        }

       return names.toArray(new String[names.size()]);
    }
}
