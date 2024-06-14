package it.polimi.ingsw.am32.model.match;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

/**
 * This class is a wrapper for configuring Log4J2.
 */
public class Log4J2ConfiguratorWrapperTest {

    /**
     * Creates a file logger with the specified level and file path.
     *
     * @param level The level of the logger.
     * @param filePath The path of the file where the logs will be written.
     * @return The created logger.
     */
    protected static Logger createFileLogger(Level level, String filePath) {
        // Create a new configuration builder
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

        // Create a File appender with the given pattern layout
        AppenderComponentBuilder appenderBuilder = builder.newAppender("File", "File")
                .addAttribute("fileName", filePath);
        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} %X{objectId} - %msg%n");
        appenderBuilder.add(layoutBuilder);
        builder.add(appenderBuilder);

        // Build the configuration
        Configuration config = builder.build();
        // Get the current logger context
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        // Start the configuration
        ctx.start(config);

        // Create and return the logger
        return LogManager.getLogger("MatchSimulationLoggerFile");
    }

}
