package com.evolutivelabs.app.counter.common.utils;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;

import java.util.Locale;

public class CronUtils {
    private static final CronDescriptor descriptor = CronDescriptor.instance(Locale.US);
    private static final CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));

    /**
     * 解析cron
     * @param cronExpression
     * @return
     */
    public static String paserCron(String cronExpression) {
        return descriptor.describe(parser.parse(cronExpression));
    }
}
