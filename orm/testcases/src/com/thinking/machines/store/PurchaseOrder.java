package com.thinking.machines.store;
import com.orm.annotations.*;
@Table(name="purchase_order")
public class PurchaseOrder
{
@Column(name="id")
@PrimaryKey
@NotNull
@AutoIncrement
private java.lang.Long id;
@Column(name="order_date")
@NotNull
private java.sql.Date orderDate;
@Column(name="customer_code")
@ForeignKey(parent="customer",column="code")
@NotNull
private java.lang.Long customerCode;
@Column(name="total_amount")
@NotNull
private java.math.BigDecimal totalAmount;
public PurchaseOrder()
{
this.totalAmount=null;
this.customerCode=Long.valueOf(0);
this.id=Long.valueOf(0);
this.orderDate=null;
}
public void setTotalAmount(java.math.BigDecimal totalAmount)
{
this.totalAmount=totalAmount;
}
public java.math.BigDecimal getTotalAmount()
{
return this.totalAmount;
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
public void setOrderDate(java.sql.Date orderDate)
{
this.orderDate=orderDate;
}
public java.sql.Date getOrderDate()
{
return this.orderDate;
}
}