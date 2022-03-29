package com.thinking.machines.store;
import com.orm.annotations.*;
@Table(name="customer")
public class Customer
{
@Column(name="code")
@PrimaryKey
@NotNull
@AutoIncrement
private java.lang.Long code;
@Column(name="name")
@NotNull
private java.lang.String name;
@Column(name="email_id")
@NotNull
private java.lang.String emailId;
@Column(name="password")
@NotNull
private java.lang.String password;
@Column(name="password_key")
@NotNull
private java.lang.String passwordKey;
@Column(name="date_of_registration")
@NotNull
private java.sql.Date dateOfRegistration;
public Customer()
{
this.password=null;
this.code=Long.valueOf(0);
this.name=null;
this.dateOfRegistration=null;
this.emailId=null;
this.passwordKey=null;
}
public void setPassword(java.lang.String password)
{
this.password=password;
}
public java.lang.String getPassword()
{
return this.password;
}
public void setCode(java.lang.Long code)
{
this.code=code;
}
public java.lang.Long getCode()
{
return this.code;
}
public void setName(java.lang.String name)
{
this.name=name;
}
public java.lang.String getName()
{
return this.name;
}
public void setDateOfRegistration(java.sql.Date dateOfRegistration)
{
this.dateOfRegistration=dateOfRegistration;
}
public java.sql.Date getDateOfRegistration()
{
return this.dateOfRegistration;
}
public void setEmailId(java.lang.String emailId)
{
this.emailId=emailId;
}
public java.lang.String getEmailId()
{
return this.emailId;
}
public void setPasswordKey(java.lang.String passwordKey)
{
this.passwordKey=passwordKey;
}
public java.lang.String getPasswordKey()
{
return this.passwordKey;
}
}