package com.romanidze.perpenanto.config.processors;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;
import org.unbescape.html.HtmlEscape;

public class ResourceAttributeTagProcessor extends AbstractAttributeTagProcessor{

    private static final String ATTR_NAME = "getlabel";
    private static final int PRECENDENCE = 10000;

    public ResourceAttributeTagProcessor(final String dialectPrefix){

        super(
                TemplateMode.HTML,
                dialectPrefix,
                null,
                false,
                ATTR_NAME,
                true,
                PRECENDENCE,
                true);

    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
                             AttributeName attributeName, String attributeValue,
                             IElementTagStructureHandler structureHandler) {
         structureHandler.setBody(HtmlEscape.escapeHtml5(attributeValue), false);
    }
}
