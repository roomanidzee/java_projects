package com.romanidze.perpenanto.config;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import org.thymeleaf.TemplateEngine;

@WebListener
public class TemplateEngineUtil {

    private static final String TEMPLATE_ENGINE_ATTR = "com.romanidze.perpenanto.config.TemplateEngineInstance";

    public static void storeTemplateEngine(ServletContext context, TemplateEngine engine){

        context.setAttribute(TEMPLATE_ENGINE_ATTR, engine);

    }

    public static TemplateEngine getTemplateEngine(ServletContext context){

        return (TemplateEngine) context.getAttribute(TEMPLATE_ENGINE_ATTR);

    }

}
