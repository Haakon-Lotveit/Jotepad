package no.haakon.jotepad.start

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test

class StartupTest {

    // Put in the arguments that you want parsed, splits ONLY on space.
    private parseWithSpace(String commandLine) {
        String[] args = commandLine.split(" ");
        return Startup.createCliParser().parse(args);
    }
    // For parsing a singular argument.
    private parseSingleArgument(String arg) {
        String[] shortHelp = [arg]
        return Startup.createCliParser().parse(shortHelp);
    }

    @Test
    void checkShortHelpFlagParseCorrectly() {
        def options = parseSingleArgument('-help')
        assertTrue(options.help);
    }

    @Test
    void checkLongHelpFlagParseCorrectly() {
        def options = parseSingleArgument('--help')
        assertTrue(options.help);
    }

    @Test
    void checkShortVersionflagParseCorrectly() {
        def options = parseSingleArgument('-version')
        assertTrue(options.version)
    }

    @Test
    void checkLongVersionFlagParseCorrectly() {
        assertTrue(parseSingleArgument('--version').version)
    }

    @Test
    void checkConfpathFlagParseCorrectly() {
        def fileName = "jotepad.conf"
        def options = parseWithSpace("-confpath $fileName");

        assertTrue(options.confpath)
    }

}
