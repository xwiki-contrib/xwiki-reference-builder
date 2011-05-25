package org.xwiki.contrib.referencebuilder.internal;

import javax.inject.Inject;
import javax.inject.Named;

import org.xwiki.component.annotation.Component;
import org.xwiki.component.annotation.InstantiationStrategy;
import org.xwiki.component.descriptor.ComponentInstantiationStrategy;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.script.service.ScriptService;

@Component
@Named("referencebuilder")
@InstantiationStrategy(ComponentInstantiationStrategy.PER_LOOKUP)
public class ReferenceBuilderScriptService implements ScriptService
{
    @Inject
    private ComponentManager componentManager;

    public ReferenceBuilder getBuilder()
    {
        return new ReferenceBuilder(this.componentManager);
    }
}
