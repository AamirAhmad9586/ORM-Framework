package com.orm.pojo;
public class DatabaseInformation
{
private String jdbcDriver;
private String connectionURL;
private String username;
private String password;
private String packageName;
private String jarFileName;
public DatabaseInformation()
{
this.jdbcDriver=null;
this.connectionURL=null;
this.username=null;
this.password=null;
this.packageName=null;
this.jarFileName=null;
}
public void setJdbcDriver(java.lang.String jdbcDriver)
{
this.jdbcDriver=jdbcDriver;
}
public java.lang.String getJdbcDriver()
{
return this.jdbcDriver;
}
public void setConnectionURL(java.lang.String connectionURL)
{
this.connectionURL=connectionURL;
}
public java.lang.String getConnectionURL()
{
return this.connectionURL;
}
public void setUsername(java.lang.String username)
{
this.username=username;
}
public java.lang.String getUsername()
{
return this.username;
}
public void setPassword(java.lang.String password)
{
this.password=password;
}
public java.lang.String getPassword()
{
return this.password;
}
public void setPackageName(java.lang.String packageName)
{
this.packageName=packageName;
}
public java.lang.String getPackageName()
{
return this.packageName;
}
public void setJarFileName(java.lang.String jarFileName)
{
this.jarFileName=jarFileName;
}
public java.lang.String getJarFileName()
{
return this.jarFileName;
}
}