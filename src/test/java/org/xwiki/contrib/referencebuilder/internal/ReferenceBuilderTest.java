package org.xwiki.contrib.referencebuilder.internal;

import org.junit.Assert;
import org.junit.Test;
import org.xwiki.model.reference.AttachmentReference;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.SpaceReference;
import org.xwiki.model.reference.WikiReference;
import org.xwiki.script.service.ScriptService;
import org.xwiki.test.AbstractComponentTestCase;

public class ReferenceBuilderTest extends AbstractComponentTestCase
{
    private ReferenceBuilder builder;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        this.builder =
            ((ReferenceBuilderScriptService) getComponentManager().lookup(ScriptService.class, "referencebuilder"))
                .getBuilder();
    }

    @Test
    public void testGetNext()
    {
        Assert.assertEquals(new WikiReference("wiki"), this.builder.get("wiki").getReference());
        Assert.assertEquals(new SpaceReference("space", new WikiReference("wiki")), this.builder.get("space")
            .getReference());
        Assert.assertEquals(new DocumentReference("wiki", "space", "document"), this.builder.get("document")
            .getReference());
        Assert.assertEquals(new AttachmentReference("attachment.ext",
            new DocumentReference("wiki", "space", "document")), this.builder.get("attachment.ext").getReference());
    }
}
