package org.xwiki.contrib.referencebuilder.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xwiki.component.manager.ComponentManager;
import org.xwiki.model.EntityType;
import org.xwiki.model.reference.AttachmentReference;
import org.xwiki.model.reference.AttachmentReferenceResolver;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.DocumentReferenceResolver;
import org.xwiki.model.reference.EntityReference;
import org.xwiki.model.reference.EntityReferenceResolver;

public class ReferenceBuilder
{
    private Map<EntityType, List<EntityType>> nextAllowedEntityTypes = new HashMap<EntityType, List<EntityType>>()
    {
        {
            put(EntityType.WIKI, Arrays.asList(EntityType.SPACE));
            put(EntityType.SPACE, Arrays.asList(EntityType.DOCUMENT, EntityType.SPACE));
            put(EntityType.DOCUMENT, Arrays.asList(EntityType.ATTACHMENT, EntityType.OBJECT));
            put(EntityType.OBJECT, Arrays.asList(EntityType.OBJECT_PROPERTY));
        }
    };

    private ComponentManager componentManager;

    private EntityReference reference;

    public ReferenceBuilder(ComponentManager componentManager)
    {
        this.componentManager = componentManager;
    }

    public ReferenceBuilder get(String name, EntityType entityType)
    {
        this.reference = new EntityReference(name, entityType, this.reference);

        return this;
    }

    public ReferenceBuilder get(String name, String entityTypeName)
    {
        try {
            EntityType type = EntityType.valueOf(entityTypeName.toUpperCase());

            return get(name, type);
        } catch (Exception e) {
            return this;
        }
    }

    public ReferenceBuilder get(String name)
    {
        EntityType nextEntity;
        if (this.reference == null) {
            nextEntity = EntityType.WIKI;
        } else {
            List<EntityType> entities =
                this.reference != null ? this.nextAllowedEntityTypes.get(this.reference.getType()) : null;
            if (entities == null || entities.isEmpty()) {
                return this;
            }
            nextEntity = entities.get(0);
        }

        return get(name, nextEntity);
    }

    // final

    public EntityReference getReference()
    {
        return this.reference;
    }

    public EntityReference getReference(String resolverId, String entityTypeName, Object... parameters)
    {
        try {
            EntityType entityType = EntityType.valueOf(entityTypeName);

            EntityReferenceResolver<EntityReference> resolver =
                this.componentManager.lookup(EntityReferenceResolver.class, resolverId + "/entity");

            return resolver.resolve(this.reference, entityType, parameters);
        } catch (Exception e) {
            return null;
        }
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

    public AttachmentReference getAttachmentReference()
    {
        return getAttachmentReference("current");
    }

    public AttachmentReference getAttachmentReference(String resolverId, Object... parameters)
    {
        if (this.reference == null) {
            return null;
        }

        try {
            AttachmentReferenceResolver<EntityReference> resolver =
                this.componentManager.lookup(AttachmentReferenceResolver.class, resolverId + "/entity");

            return resolver.resolve(this.reference, parameters);
        } catch (Exception e) {
            return null;
        }
    }
}
