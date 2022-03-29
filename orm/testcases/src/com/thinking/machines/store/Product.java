package com.thinking.machines.store;
import com.orm.annotations.*;
@Table(name="product")
public class Product
{
@Column(name="code")
@PrimaryKey
@NotNull
@AutoIncrement
private java.lang.Long code;
@Column(name="name")
@NotNull
private java.lang.String name;
@Column(name="price")
@NotNull
private java.math.BigDecimal price;
@Column(name="is_available")
@NotNull
private java.lang.Boolean isAvailable;
public Product()
{
this.isAvailable=false;
this.code=Long.valueOf(0);
this.price=null;
this.name=null;
}
public void setIsAvailable(java.lang.Boolean isAvailable)
{
this.isAvailable=isAvailable;
}
public java.lang.Boolean getIsAvailable()
{
return this.isAvailable;
}
public void setCode(java.lang.Long code)
{
this.code=code;
}
public java.lang.Long getCode()
{
return this.code;
}
public void setPrice(java.math.BigDecimal price)
{
this.price=price;
}
public java.math.BigDecimal getPrice()
{
return this.price;
}
public void setName(java.lang.String name)
{
this.name=name;
}
public java.lang.String getName()
{
return this.name;
}
}