package io.github.lvyahui8.sdk.logging.handler;

import io.github.lvyahui8.sdk.logging.logger.LogSchema;
import org.slf4j.event.Level;

/**
 * @author feego lvyahui8@gmail.com
 * @date 2020/10/9
 */
public interface SchemaHandler {
    Level runtimeLevel(String enumLoggerName);
    LogSchema beforeOutput(LogSchema logSchema);
}
