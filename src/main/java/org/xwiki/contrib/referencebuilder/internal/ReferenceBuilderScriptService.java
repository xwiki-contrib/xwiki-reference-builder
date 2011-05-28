package org.xwiki.contrib.referencebuilder.internal;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.script.service.ScriptService;

@Component
@Named("referencebuilder")
@Singleton
public class ReferenceBuilderScriptService implements ScriptService
{
    @Inject
    private ComponentManager componentManager;

    public ReferenceBuilder getBuilder()
    {
        return new ReferenceBuilder(this.componentManager);
    }
}
