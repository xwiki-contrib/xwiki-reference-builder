package org.xwiki.contrib.referencebuilder.internal;

import org.xwiki.component.manager.ComponentManager;
import org.xwiki.model.EntityType;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.DocumentReferenceResolver;
import org.xwiki.model.reference.EntityReference;

public class ReferenceBuilder
{
    private ComponentManager componentManager;

    private EntityReference reference;

    public ReferenceBuilder(ComponentManager componentManager)
    {
        this.componentManager = componentManager;
    }

    public ReferenceBuilder get(String name, EntityType type)
    {
        this.reference = new EntityReference(name, type, this.reference);

        return this;
    }

    public ReferenceBuilder get(String name, String typeName)
    {
        EntityType type = EntityType.valueOf(typeName.toUpperCase());

        return get(name, type);
    }

    public ReferenceBuilder get(String name)
    {
        int index = this.reference == null ? 0 : this.reference.getType().ordinal();

        EntityType nextEntity = EntityType.values()[index + 1];

        return get(name, nextEntity);
    }

    // final

    public EntityReference getReference()
    {
        return this.reference;
    }

    public DocumentReference getDocumentReference()
    {
        return getDocumentReference("current");
    }

    public DocumentReference getDocumentReference(String resolverId, Object... parameters)
    {
        if (this.reference == null) {
            return null;
        }

        try {
            DocumentReferenceResolver<EntityReference> resolver =
                this.componentManager.lookup(DocumentReferenceResolver.class, resolverId + "/entity");

            return resolver.resolve(this.reference, parameters);
        } catch (Exception e) {
            return null;
        }
    }
}
