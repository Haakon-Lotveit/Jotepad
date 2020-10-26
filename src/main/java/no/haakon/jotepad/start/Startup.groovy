package no.haakon.jotepad.start

import groovy.cli.picocli.CliBuilder
import no.haakon.jotepad.controller.JotepadConfiguration
import no.haakon.jotepad.controller.JotepadController

/**
 * This is the starting point of Jotepad, where things are loaded up and started.
 * It is written in Groovy, but that should have no bearing on the program as a whole.
 */
class Startup {
    static void main(String[] args) {
        CliBuilder cli = createCliParser()

        def options = cli.parse(args)

        // To keep things simple we just check for supported flags, and handle them.
        // Most of them just want to print helpful stuff to the screen and exit.
        if(options.help) {
            println(cli.usage())
            System.exit(0);
        }
        if(options.version) {
            def msg = "" +
                    "Jotepad, version 0.1, for Java 11(+)\n" +
                    "Copyright (C) 2020 Haakon Løtveit.\n" +
                    "License AGPLv3+: GNU Affero GPL version 3 or later <http://gnu.org/licenses/agpl-3.0.html>\n" +
                    "\n" +
                    "This is free software; you are free to change and redistribute it.\n" +
                    "There is NO WARRANTY, to the extent permitted by law."
            println(msg)
            System.exit(0);
        }
        if(options.confpath) {
            println("Configuration is currently not supported. The help message kind of lies")
            println("But it's in the works, promise!");
            System.exit(0);
        }

        // If we get this far, we're going to actually start up the application.
        JotepadConfiguration configuration = loadConfiguration();
        new JotepadController(configuration);

    }

    /**
     * This method is not actually set up to load properties and such yet.
     * but in the interest of actually creating something, an empty properties is used,
     * and an empty config is returned.
     * @return An empty configuration
     */
    private static JotepadConfiguration loadConfiguration() {
        return new JotepadConfiguration(new Properties());
    }


    static CliBuilder createCliParser() {
        CliBuilder cli = new CliBuilder(usage: 'Jotepad-run-command [OPTIONS] [FILE]', header: 'Options:');
        cli.help(longOpt: 'help', 'Print this message and exit')
        cli.version(longOpt: 'version', 'Print version and exit')
        cli.confpath(longOpt: 'confpath','Prints the path to the configuration file')
        cli;
    }
}
