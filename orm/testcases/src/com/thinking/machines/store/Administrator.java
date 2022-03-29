package com.thinking.machines.store;
import com.orm.annotations.*;
@Table(name="administrator")
public class Administrator
{
@Column(name="username")
@NotNull
private java.lang.String username;
@Column(name="password")
@NotNull
private java.lang.String password;
public Administrator()
{
this.password=null;
this.username=null;
}
public void setPassword(java.lang.String password)
{
this.password=password;
}
public java.lang.String getPassword()
{
return this.password;
}
public void setUsername(java.lang.String username)
{
this.username=username;
}
public java.lang.String getUsername()
{
return this.username;
}
}