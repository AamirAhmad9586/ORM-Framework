package com.thinking.machines.store;
import com.orm.annotations.*;
@Table(name="feed_back")
public class FeedBack
{
@Column(name="id")
@PrimaryKey
@NotNull
@AutoIncrement
private java.lang.Long id;
@Column(name="given_on")
@NotNull
private java.sql.Date givenOn;
@Column(name="product_code")
@ForeignKey(parent="product",column="code")
@NotNull
private java.lang.Long productCode;
@Column(name="customer_code")
@ForeignKey(parent="customer",column="code")
@NotNull
private java.lang.Long customerCode;
@Column(name="feed_back")
@NotNull
private java.lang.String feedBack;
public FeedBack()
{
this.givenOn=null;
this.feedBack=null;
this.productCode=Long.valueOf(0);
this.customerCode=Long.valueOf(0);
this.id=Long.valueOf(0);
}
public void setGivenOn(java.sql.Date givenOn)
{
this.givenOn=givenOn;
}
public java.sql.Date getGivenOn()
{
return this.givenOn;
}
public void setFeedBack(java.lang.String feedBack)
{
this.feedBack=feedBack;
}
public java.lang.String getFeedBack()
{
return this.feedBack;
}
public void setProductCode(java.lang.Long productCode)
{
this.productCode=productCode;
}
public java.lang.Long getProductCode()
{
return this.productCode;
}
public void setCustomerCode(java.lang.Long customerCode)
{
this.customerCode=customerCode;
}
public java.lang.Long getCustomerCode()
{
return this.customerCode;
}
public void setId(java.lang.Long id)
{
this.id=id;
}
public java.lang.Long getId()
{
return this.id;
}
}