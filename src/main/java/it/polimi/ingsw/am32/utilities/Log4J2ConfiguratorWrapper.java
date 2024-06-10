package it.polimi.ingsw.am32.utilities;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

/**
 * This class is a wrapper for configuring Log4J2.
 * It provides a method to set the log level and configure the logger.
 */
public class Log4J2ConfiguratorWrapper {

    /**
     * Sets the log level and configures the logger.
     *
     * @param level The log level to set.
     */
    public static void setLogLevelAndConfigure(Level level) {
        // Create a new configuration builder.
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

        // Create a console appender with the given pattern layout.
        AppenderComponentBuilder appenderBuilder = builder.newAppender("Console", "CONSOLE")
                .addAttribute("target", "SYSTEM_OUT");
        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} %X{objectId} - %msg%n");
        appenderBuilder.add(layoutBuilder);
        builder.add(appenderBuilder);

        // Create the root logger with the given log level.
        builder.add(builder.newRootLogger(level).add(builder.newAppenderRef("Console")));

        // Build the configuration and start the logger context.
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        ctx.start(builder.build());
    }
}
