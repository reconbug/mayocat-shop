package org.mayocat.shop.store.rdbms.dbi.dao.jointype;

import java.util.Locale;
import java.util.Map;

public class EntityFullJoinRow
{
    private Map<String, Object> entityData;

    private Locale locale;

    private String text;

    private String field;

    private Long translationId;

    public String getField()
    {
        return field;
    }

    public Locale getLocale()
    {
        return locale;
    }

    public String getText()
    {
        return text;
    }

    public void setField(String field)
    {
        this.field = field;
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Long getTranslationId()
    {
        return translationId;
    }

    public void setTranslationId(Long translationId)
    {
        this.translationId = translationId;
    }
    
    public Map<String, Object> getEntityData()
    {
        return entityData;
    }
    
    public void setEntityData(Map<String, Object> entityData)
    {
        this.entityData = entityData;
    }
}
