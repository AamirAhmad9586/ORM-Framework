package com.orm.pojo;
import java.lang.reflect.*;
public class TableClassInformation
{
private String insertQuery;
private String updateQuery;
private String deleteQuery;
private java.util.List<Method> setters;
private java.util.List<Method> getters;
private java.util.List<Method> preparedStatementSetters;
private java.util.List<Method> resultSetGetters;
private java.util.List<ForeignKeyInformation> foreignKeys;
private boolean hasAutoIncrementAnnotation;
private boolean hasPrimaryKeyAnnotation;
private boolean hasNotNullAnnotation;
private boolean hasForeignKeyAnnotation;
private Method primaryKeyGetterMethod;
public TableClassInformation()
{
this.insertQuery="";
this.updateQuery="";
this.deleteQuery="";
this.preparedStatementSetters=null;
this.setters=null;
this.getters=null;
this.resultSetGetters=null;
this.foreignKeys=null;
this.hasAutoIncrementAnnotation=false;
this.hasNotNullAnnotation=false;
this.hasPrimaryKeyAnnotation=false;
this.hasForeignKeyAnnotation=false;
this.primaryKeyGetterMethod=null;
}
public void setInsertQuery(java.lang.String insertQuery)
{
this.insertQuery=insertQuery;
}
public java.lang.String getInsertQuery()
{
return this.insertQuery;
}
public void setUpdateQuery(java.lang.String updateQuery)
{
this.updateQuery=updateQuery;
}
public java.lang.String getUpdateQuery()
{
return this.updateQuery;
}
public void setDeleteQuery(java.lang.String deleteQuery)
{
this.deleteQuery=deleteQuery;
}
public java.lang.String getDeleteQuery()
{
return this.deleteQuery;
}
public void setPreparedStatementSetters(java.util.List preparedStatementSetters)
{
this.preparedStatementSetters=preparedStatementSetters;
}
public java.util.List getPreparedStatementSetters()
{
return this.preparedStatementSetters;
}
public void setResultSetGetters(java.util.List resultSetGetters)
{
this.resultSetGetters=resultSetGetters;
}
public java.util.List getResultSetGetters()
{
return this.resultSetGetters;
}
public void setSetters(java.util.List setters)
{
this.setters=setters;
}
public java.util.List getSetters()
{
return this.setters;
}
public void setGetters(java.util.List getters)
{
this.getters=getters;
}
public java.util.List getGetters()
{
return this.getters;
}
public void setForeignKeys(java.util.List foreignKeys)
{
this.foreignKeys=foreignKeys;
}
public java.util.List getForeignKeys()
{
return this.foreignKeys;
}
public void setHasAutoIncrementAnnotation(boolean hasAutoIncrementAnnotation)
{
this.hasAutoIncrementAnnotation=hasAutoIncrementAnnotation;
}
public boolean getHasAutoIncrementAnnotation()
{
return this.hasAutoIncrementAnnotation;
}
public void setNotNullAnnotation(boolean hasNotNullAnnotation)
{
this.hasNotNullAnnotation=hasNotNullAnnotation;
}
public boolean getHasNotNullAnnotation()
{
return this.hasNotNullAnnotation;
}
public void setHasPrimaryKeyAnnotation(boolean hasPrimaryKeyAnnotation)
{
this.hasPrimaryKeyAnnotation=hasPrimaryKeyAnnotation;
}
public boolean getHasPrimaryKeyAnnotation()
{
return this.hasPrimaryKeyAnnotation;
}
public void setHasForeignKeyAnnotation(boolean hasForeignKeyAnnotation)
{
this.hasForeignKeyAnnotation=hasForeignKeyAnnotation;
}
public boolean getHasForeignKeyAnnotation()
{
return this.hasForeignKeyAnnotation;
}
public void setPrimaryKeyGetterMethod(Method primaryKeyGetterMethod)
{
this.primaryKeyGetterMethod=primaryKeyGetterMethod;
}
public Method getPrimaryKeyGetterMethod()
{
return this.primaryKeyGetterMethod;
}
}