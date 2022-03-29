package com.thinking.machines.store;
import com.orm.annotations.*;
@Table(name="purchase_order_item")
public class PurchaseOrderItem
{
@Column(name="order_id")
@PrimaryKey
@ForeignKey(parent="purchase_order",column="id")
@NotNull
private java.lang.Long orderId;
@Column(name="product_code")
@PrimaryKey
@ForeignKey(parent="product",column="code")
@NotNull
private java.lang.Long productCode;
@Column(name="quantity")
@NotNull
private java.lang.Integer quantity;
@Column(name="price")
@NotNull
private java.math.BigDecimal price;
public PurchaseOrderItem()
{
this.productCode=Long.valueOf(0);
this.quantity=0;
this.orderId=Long.valueOf(0);
this.price=null;
}
public void setProductCode(java.lang.Long productCode)
{
this.productCode=productCode;
}
public java.lang.Long getProductCode()
{
return this.productCode;
}
public void setQuantity(java.lang.Integer quantity)
{
this.quantity=quantity;
}
public java.lang.Integer getQuantity()
{
return this.quantity;
}
public void setOrderId(java.lang.Long orderId)
{
this.orderId=orderId;
}
public java.lang.Long getOrderId()
{
return this.orderId;
}
public void setPrice(java.math.BigDecimal price)
{
this.price=price;
}
public java.math.BigDecimal getPrice()
{
return this.price;
}
}