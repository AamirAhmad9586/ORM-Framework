package com.orm.pojo;
import java.lang.reflect.*;
public class ForeignKeyInformation
{
private String name;
private String parentTableName;
private String parentTableColumnName;
private Method getterMethod;
private Class parentTableClass;
private Method parentTableColumnGetterMethod;
public ForeignKeyInformation()
{
this.name=null;
this.parentTableName=null;
this.parentTableColumnName=null;
this.getterMethod=null;
this.parentTableClass=null;
this.parentTableColumnGetterMethod=null;
}
public void setName(java.lang.String name)
{
this.name=name;
}
public java.lang.String getName()
{
return this.name;
}
public void setParentTableName(java.lang.String parentTableName)
{
this.parentTableName=parentTableName;
}
public java.lang.String getParentTableName()
{
return this.parentTableName;
}
public void setParentTableColumnName(java.lang.String parentTableColumnName)
{
this.parentTableColumnName=parentTableColumnName;
}
public java.lang.String getParentTableColumnName()
{
return this.parentTableColumnName;
}
public void setGetterMethod(java.lang.reflect.Method getterMethod)
{
this.getterMethod=getterMethod;
}
public java.lang.reflect.Method getGetterMethod()
{
return this.getterMethod;
}
public void setParentTableClass(java.lang.Class parentTableClass)
{
this.parentTableClass=parentTableClass;
}
public java.lang.Class getParentTableClass()
{
return this.parentTableClass;
}
public void setParentTableColumnGetterMethod(java.lang.reflect.Method parentTableColumnGetterMethod)
{
this.parentTableColumnGetterMethod=parentTableColumnGetterMethod;
}
public java.lang.reflect.Method getParentTableColumnGetterMethod()
{
return this.parentTableColumnGetterMethod;
}
}