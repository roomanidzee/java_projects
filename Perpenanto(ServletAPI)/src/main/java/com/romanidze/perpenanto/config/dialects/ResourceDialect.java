package com.romanidze.perpenanto.config.dialects;

import com.romanidze.perpenanto.config.processors.ResourceAttributeTagProcessor;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

public class ResourceDialect extends AbstractProcessorDialect{

    public ResourceDialect(){

        super(
                "Resource Dialect",
                "resource",
                1000);

    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {

        final Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new ResourceAttributeTagProcessor(dialectPrefix));
        return processors;

    }
}
