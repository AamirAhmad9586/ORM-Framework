package com.orm.managers;
import com.orm.connections.*;
import com.orm.exceptions.*;
import com.orm.annotations.*;
import com.orm.pojo.*;
import com.google.gson.*;
import java.io.*;
import java.sql.*;
import java.nio.file.*;
import java.util.stream.*;
import java.lang.reflect.*;
import java.math.*;
import java.util.*;
public class DataManager
{
public static DataManager dataManager=null;
private Connection connection;
private StringBuffer stringBuffer;
private Class targetClass;
private static java.util.Map<String,TableClassInformation> tableNameWiseMap;
static
{
try
{
dataManager=new DataManager();
tableNameWiseMap=new java.util.HashMap<>();
JsonParser jsonParser=new JsonParser();
JsonObject jsonObject=(JsonObject)jsonParser.parse(new FileReader("conf.json"));
DatabaseInformation databaseInformation=new DatabaseInformation();
if(jsonObject.has("package-name")) databaseInformation.setPackageName(jsonObject.get("package-name").getAsString());
String packageName="";
if(databaseInformation.getPackageName()==null || databaseInformation.getPackageName().length()==0) packageName="";
else packageName=databaseInformation.getPackageName().replace('.',File.separator.charAt(0));


Class preparedStatementClass=PreparedStatement.class;
Class resultSetClass=ResultSet.class;
packageName=new File(new File(".").getCanonicalPath()).getParent()+File.separator+"classes"+File.separator+packageName;
Path path=Paths.get(packageName);
Stream<Path> subPaths=Files.walk(path);
subPaths.forEach((p)->{
try
{
String classFileName=p.toFile().getName();
String pn=databaseInformation.getPackageName();
if(!classFileName.endsWith(".class")) return;
if(pn!=null && pn.length()!=0) classFileName=pn+"."+classFileName.substring(0,classFileName.length()-6);
else classFileName=classFileName.substring(0,classFileName.length()-6);



Class targetClass=Class.forName(classFileName);

TableClassInformation tableClassInformation=new TableClassInformation();

Table tableAnnotation=null;
Column columnAnnotation=null;
PrimaryKey primaryKeyAnnotation=null;
ForeignKey foreignKeyAnnotation=null;
AutoIncrement autoIncrementAnnotation=null;
NotNull notNullAnnotation=null;

int index;
int i;

String fieldName=null;
String setterName=null;
String getterName=null;
String tableName=null;
String forInsertQuery=null;
String forUpdateQuery;
String insertQuery=null;
String updateQuery=null;
String deleteQuery=null;
String primaryKeyColumnName=null;
String parentTableName=null;
String parentTableColumnName=null;
String parentTableClassName=null;
String parentTableColumnGetterName=null;
String preparedStatementSetterName=null;
String resultSetGetterName=null;

java.util.List<Method> setters=new java.util.LinkedList<>();
java.util.List<Method> getters=new java.util.LinkedList<>();
java.util.List<Method> preparedStatementSetters=new java.util.LinkedList<>();
java.util.List<Method> resultSetGetters=new java.util.LinkedList<>();
java.util.List<ForeignKeyInformation> foreignKeys=new java.util.LinkedList<>();

boolean hasPrimaryKeyAnnotation=false;
boolean hasForeignKeyAnnotation=false;
boolean hasAutoIncrementAnnotation=false;
boolean hasNotNullAnnotation=false;

ForeignKeyInformation foreignKeyInformation=null;

Method setterMethod=null;
Method getterMethod=null;
Method primaryKeyGetterMethod=null;
Method parentTableColumnGetterMethod=null;
Method preparedStatementSetterMethod=null;
Method resultSetGetterMethod=null;

Field fields[];

Class fieldDataType=null;
Class parentTableClass=null;
Class secondParameter=null;

tableAnnotation=(Table)targetClass.getAnnotation(com.orm.annotations.Table.class);
if(tableAnnotation==null)
{
System.out.println("Table Annotation not found at : "+classFileName);
System.exit(0);
}
tableName=tableAnnotation.name();
fields=targetClass.getDeclaredFields();
forInsertQuery="";
forUpdateQuery="";
for(i=0;i<fields.length;i++)
{
fieldName=fields[i].getName().substring(0,1).toUpperCase()+fields[i].getName().substring(1);
setterName="set"+fieldName;
getterName="get"+fieldName;
fieldDataType=fields[i].getType();
setterMethod=targetClass.getMethod(setterName,new Class[]{fieldDataType});
getterMethod=targetClass.getMethod(getterName,new Class[0]);
setters.add(setterMethod);

columnAnnotation=(Column)fields[i].getAnnotation(Column.class);
if(columnAnnotation==null) continue;

primaryKeyAnnotation=(PrimaryKey)fields[i].getAnnotation(PrimaryKey.class);
if(primaryKeyAnnotation!=null)
{
hasPrimaryKeyAnnotation=true;
primaryKeyColumnName=columnAnnotation.name();
primaryKeyGetterMethod=getterMethod;
}
foreignKeyAnnotation=(ForeignKey)fields[i].getAnnotation(ForeignKey.class);
if(foreignKeyAnnotation!=null)
{
hasForeignKeyAnnotation=true;
parentTableName=foreignKeyAnnotation.parent();
parentTableColumnName=foreignKeyAnnotation.column();

// code to convert parentTableColumnName (aaa_bbb_ccc) to (aaaBbbCcc)
index=parentTableColumnName.indexOf('_');
while(index!=-1)
{
parentTableColumnName=parentTableColumnName.substring(0,index)+parentTableColumnName.substring(index+1,index+2).toUpperCase()+parentTableColumnName.substring(index+2);
index=parentTableColumnName.indexOf('_');
}

// code to convert parentTableName (aaa_bbb_ccc) to (aaaBbbCcc)
index=parentTableName.indexOf('_');
while(index!=-1)
{
parentTableName=parentTableName.substring(0,index)+parentTableName.substring(index+1,index+2).toUpperCase()+parentTableName.substring(index+2);
index=parentTableName.indexOf('_');
}

// code to convert parentTableName (aaa) to (Aaa)
parentTableName=parentTableName.substring(0,1).toUpperCase()+parentTableName.substring(1);

if(pn!=null && pn.length()!=0) parentTableClassName=pn+"."+parentTableName;
else parentTableClassName=parentTableName;

parentTableClass=Class.forName(parentTableClassName);
parentTableColumnGetterName="get"+parentTableColumnName.substring(0,1).toUpperCase()+parentTableColumnName.substring(1);
parentTableColumnGetterMethod=parentTableClass.getMethod(parentTableColumnGetterName,new Class[0]);

foreignKeyInformation=new ForeignKeyInformation();
foreignKeyInformation.setParentTableName(parentTableName);
foreignKeyInformation.setParentTableColumnName(parentTableColumnName);
foreignKeyInformation.setGetterMethod(getterMethod);
foreignKeyInformation.setParentTableClass(parentTableClass);
foreignKeyInformation.setParentTableColumnGetterMethod(parentTableColumnGetterMethod);
foreignKeys.add(foreignKeyInformation);
}

notNullAnnotation=(NotNull)fields[i].getAnnotation(NotNull.class);
if(notNullAnnotation!=null) hasNotNullAnnotation=true;

autoIncrementAnnotation=(AutoIncrement)fields[i].getAnnotation(AutoIncrement.class);
if(autoIncrementAnnotation!=null) hasAutoIncrementAnnotation=true;
else forInsertQuery+=columnAnnotation.name()+",";

forUpdateQuery+=columnAnnotation.name()+"=?,";
getters.add(getterMethod);

if(fieldDataType.getName().equals("java.lang.Boolean") || fieldDataType.getName().equals("boolean"))
{
secondParameter=boolean.class;
preparedStatementSetterName="setBoolean";
resultSetGetterName="getBoolean";
}
else if(fieldDataType.getName().equals("java.lang.Byte") || fieldDataType.getName().equals("byte"))
{
secondParameter=byte.class;
preparedStatementSetterName="setByte";
resultSetGetterName="getByte";
}
else if(fieldDataType.getName().equals("java.lang.Short") || fieldDataType.getName().equals("short"))
{
secondParameter=short.class;
preparedStatementSetterName="setShort";
resultSetGetterName="getShort";
}
else if(fieldDataType.getName().equals("java.lang.Integer") || fieldDataType.getName().equals("int"))
{
secondParameter=int.class;
preparedStatementSetterName="setInt";
resultSetGetterName="getInt";
}
else if(fieldDataType.getName().equals("java.lang.Long") || fieldDataType.getName().equals("long"))
{
secondParameter=long.class;
preparedStatementSetterName="setLong";
resultSetGetterName="getLong";
}
else if(fieldDataType.getName().equals("java.lang.Float") || fieldDataType.getName().equals("float"))
{
secondParameter=float.class;
preparedStatementSetterName="setFloat";
resultSetGetterName="getFloat";
}
else if(fieldDataType.getName().equals("java.lang.Double") || fieldDataType.getName().equals("double"))
{
secondParameter=double.class;
preparedStatementSetterName="setDouble";
resultSetGetterName="getDouble";
}
else if(fieldDataType.getName().equals("java.math.BigDecimal"))
{
secondParameter=java.math.BigDecimal.class;
preparedStatementSetterName="setBigDecimal";
resultSetGetterName="getBigDecimal";
}
else if(fieldDataType.getName().equals("java.lang.String"))
{
secondParameter=java.lang.String.class;
preparedStatementSetterName="setString";
resultSetGetterName="getString";
}
else if(fieldDataType.getName().equals("java.sql.Date"))
{
secondParameter=java.sql.Date.class;
preparedStatementSetterName="setDate";
resultSetGetterName="getDate";
}
else if(fieldDataType.getName().equals("java.sql.Time"))
{
secondParameter=java.sql.Time.class;
preparedStatementSetterName="setTime";
resultSetGetterName="getTime";
}
else if(fieldDataType.getName().equals("java.sql.Timestamp"))
{
secondParameter=java.sql.Timestamp.class;
preparedStatementSetterName="setTimestamp";
resultSetGetterName="getTimestamp";
}
preparedStatementSetterMethod=preparedStatementClass.getMethod(preparedStatementSetterName,new Class[]{int.class,secondParameter});
resultSetGetterMethod=resultSetClass.getMethod(resultSetGetterName,new Class[]{int.class});
preparedStatementSetters.add(preparedStatementSetterMethod);
resultSetGetters.add(resultSetGetterMethod);
} // fields for loop ends

insertQuery="insert into "+tableName+" ("+forInsertQuery.substring(0,forInsertQuery.length()-1)+") values (";
i=0;
if(hasAutoIncrementAnnotation) i=1;
for(;i<getters.size();i++) insertQuery+="?,";
insertQuery=insertQuery.substring(0,insertQuery.length()-1)+")";
updateQuery="update "+tableName+" set "+forUpdateQuery.substring(0,forUpdateQuery.length()-1)+" where "+primaryKeyColumnName+"=?";
deleteQuery="delete from "+tableName+" where "+primaryKeyColumnName+"=?";
tableClassInformation=new TableClassInformation();
tableClassInformation.setInsertQuery(insertQuery);
tableClassInformation.setUpdateQuery(updateQuery);
tableClassInformation.setDeleteQuery(deleteQuery);
tableClassInformation.setPreparedStatementSetters(preparedStatementSetters);
tableClassInformation.setResultSetGetters(resultSetGetters);
tableClassInformation.setSetters(setters);
tableClassInformation.setGetters(getters);
if(hasAutoIncrementAnnotation) tableClassInformation.setHasAutoIncrementAnnotation(true);
if(hasPrimaryKeyAnnotation)
{
tableClassInformation.setHasPrimaryKeyAnnotation(true);
tableClassInformation.setPrimaryKeyGetterMethod(primaryKeyGetterMethod);
}
if(hasForeignKeyAnnotation)
{
tableClassInformation.setHasForeignKeyAnnotation(true);
tableClassInformation.setForeignKeys(foreignKeys);
}
tableNameWiseMap.put(tableName,tableClassInformation);
}catch(ClassNotFoundException cnfe)
{
System.out.println("ClassNotFoundException : "+cnfe.getMessage());
System.exit(0);
}
catch(NoSuchMethodException nsme)
{
System.out.println("NoSuchMethodException : "+nsme.getMessage());
System.exit(0);
}
});
}catch(ORMConnectionException ormConnectionException)
{
System.out.println("ORMConnectionException : "+ormConnectionException.getMessage());
System.exit(0);
}
catch(FileNotFoundException fnfe)
{
System.out.println("FileNotFoundException : "+fnfe.getMessage());
System.exit(0);
}
catch(IOException ioException)
{
System.out.println("IOException : "+ioException.getMessage());
System.exit(0);
}
}
private DataManager() throws ORMConnectionException
{
try
{
JsonParser jsonParser=new JsonParser();
JsonObject jsonObject=(JsonObject)jsonParser.parse(new FileReader("conf.json"));
DatabaseInformation databaseInformation=new DatabaseInformation();
if(jsonObject.has("jdbc-driver")) databaseInformation.setJdbcDriver(jsonObject.get("jdbc-driver").getAsString());
if(jsonObject.has("connection-url")) databaseInformation.setConnectionURL(jsonObject.get("connection-url").getAsString());
if(jsonObject.has("username")) databaseInformation.setUsername(jsonObject.get("username").getAsString());
if(jsonObject.has("password")) databaseInformation.setPassword(jsonObject.get("password").getAsString());
connection=ORMConnection.getConnection(databaseInformation);
this.stringBuffer=new StringBuffer();
this.targetClass=null;
}catch(FileNotFoundException fnfe)
{
throw new ORMConnectionException(fnfe.getMessage());
}
}
public static DataManager getDataManager() throws ORMConnectionException
{
if(dataManager==null) dataManager=new DataManager();
return dataManager;
}
public void begin() throws DataException
{
try
{
this.connection.setAutoCommit(false);
}catch(SQLException sqlException)
{
throw new DataException(sqlException.getMessage());
}
}
public Object save(Object object) throws DataException
{
String tableName=null;
String parentTableName=null;

PreparedStatement preparedStatement=null;

ResultSet resultSet=null;

TableClassInformation tableClassInformation=null;

ForeignKeyInformation foreignKeyInformation=null;

Method primaryKeyGetterMethod=null;
Method parentTableColumnGetterMethod=null;

Class parentTableClass=null;

Object primaryKeyGetterResult=null;
Object getterResult=null;
Object foreignKeyGetterResult=null;
Object result=null;

java.util.List<ForeignKeyInformation> foreignKeys=null;
java.util.List<Method> getters=null;
java.util.List<Method> preparedStatementSetters=null;

int i,j;



try
{
Table tableAnnotation=null;
this.targetClass=object.getClass();
this.stringBuffer=new StringBuffer();
tableAnnotation=(Table)this.targetClass.getAnnotation(Table.class);
if(tableAnnotation==null) throw new DataException("Table Annotation not found");
tableName=tableAnnotation.name();
tableClassInformation=tableNameWiseMap.get(tableName);

// Primary Key Duplicacy check
if(tableClassInformation.getHasPrimaryKeyAnnotation())
{
primaryKeyGetterMethod=tableClassInformation.getPrimaryKeyGetterMethod();
primaryKeyGetterResult=primaryKeyGetterMethod.invoke(object);
this.stringBuffer.append(new String("select * from "+tableName));
List list=this.query(this.targetClass).fire();
for(Object obj : list)
{
getterResult=primaryKeyGetterMethod.invoke(obj);
if(getterResult.equals(primaryKeyGetterResult)) throw new DataException("Duplicate Entry for primary key.");
}
}

// Foreign Key Valid state check
if(tableClassInformation.getHasForeignKeyAnnotation())
{
foreignKeys=tableClassInformation.getForeignKeys();
for(i=0;i<foreignKeys.size();i++)
{
foreignKeyInformation=foreignKeys.get(i);
foreignKeyGetterResult=foreignKeyInformation.getGetterMethod().invoke(object);
parentTableName=foreignKeyInformation.getParentTableName();
parentTableColumnGetterMethod=foreignKeyInformation.getParentTableColumnGetterMethod();
parentTableClass=foreignKeyInformation.getParentTableClass();
this.stringBuffer=new StringBuffer(new String("select * from "+parentTableName));
List fList=this.query(parentTableClass).fire();
int found=0;
for(Object obj: fList)
{
getterResult=parentTableColumnGetterMethod.invoke(obj);
if(getterResult.equals(foreignKeyGetterResult))
{
found=1;
break;
}
}
if(found==0) throw new DataException("invalid entry for foreign key.");
}
this.targetClass=object.getClass();
}

getters=tableClassInformation.getGetters();
preparedStatementSetters=tableClassInformation.getPreparedStatementSetters();
if(tableClassInformation.getHasAutoIncrementAnnotation())
{
preparedStatement=this.connection.prepareStatement(tableClassInformation.getInsertQuery(),Statement.RETURN_GENERATED_KEYS);
i=1;
}
else
{
preparedStatement=this.connection.prepareStatement(tableClassInformation.getInsertQuery());
i=0;
}
for(j=1;i<getters.size();i++,j++) preparedStatementSetters.get(i).invoke(preparedStatement,j,getters.get(i).invoke(object));
preparedStatement.executeUpdate();
if(tableClassInformation.getHasAutoIncrementAnnotation())
{
resultSet=preparedStatement.getGeneratedKeys();
resultSet.next();
result=resultSet.getInt(1);
resultSet.close();
}
preparedStatement.close();
}catch(IllegalAccessException iae)
{
throw new DataException(iae.getMessage());
}
catch(InvocationTargetException ite)
{
throw new DataException(ite.getMessage());
}
catch(SQLException sqlException)
{
throw new DataException(sqlException.getMessage());
}
this.targetClass=null;
this.stringBuffer=null;
return result;
}
public void update(Object object) throws DataException
{
Table tableAnnotation=null;

TableClassInformation tableClassInformation=null;

ForeignKeyInformation foreignKeyInformation=null;

PreparedStatement preparedStatement=null;

Class parentTableClass=null;

Method primaryKeyGetterMethod=null;
Method parentTableColumnGetterMethod=null;

Object primaryKeyGetterResult=null;
Object foreignKeyGetterResult=null;
Object getterResult=null;

java.util.List<Method> getters=null;
java.util.List<Method> preparedStatementSetters=null;
java.util.List<ForeignKeyInformation> foreignKeys=null;

String tableName=null;
String parentTableName=null;

int found;
int i;
this.targetClass=object.getClass();
this.stringBuffer=new StringBuffer();
try
{
tableAnnotation=(Table)this.targetClass.getAnnotation(Table.class);
if(tableAnnotation==null) throw new DataException("Table Annotation not found");
tableName=tableAnnotation.name();
tableClassInformation=tableNameWiseMap.get(tableName);

// primary key duplicacy checks
if(tableClassInformation.getHasPrimaryKeyAnnotation())
{
primaryKeyGetterMethod=tableClassInformation.getPrimaryKeyGetterMethod();
primaryKeyGetterResult=primaryKeyGetterMethod.invoke(object);
this.stringBuffer.append(new String("select * from "+tableName));
List list=query(this.targetClass).fire();
found=0;
for(Object obj : list)
{
getterResult=primaryKeyGetterMethod.invoke(obj);
if(getterResult.equals(primaryKeyGetterResult)) found=1;
}
if(found==0) throw new DataException("Invalid Entry for primary key.");
}

// Foreign key valid state checks
if(tableClassInformation.getHasForeignKeyAnnotation())
{
foreignKeys=tableClassInformation.getForeignKeys();
for(i=0;i<foreignKeys.size();i++)
{
foreignKeyInformation=foreignKeys.get(i);
foreignKeyGetterResult=foreignKeyInformation.getGetterMethod().invoke(object);
parentTableName=foreignKeyInformation.getParentTableName();
parentTableColumnGetterMethod=foreignKeyInformation.getParentTableColumnGetterMethod();
parentTableClass=foreignKeyInformation.getParentTableClass();
this.stringBuffer=new StringBuffer(new String("select * from "+parentTableName));
List fList=this.query(parentTableClass).fire();
found=0;
for(Object obj: fList)
{
getterResult=parentTableColumnGetterMethod.invoke(obj);
if(getterResult.equals(foreignKeyGetterResult))
{
found=1;
break;
}
}
if(found==0) throw new DataException("invalid entry for foreign key.");
}
this.targetClass=object.getClass();
}

preparedStatement=this.connection.prepareStatement(tableClassInformation.getUpdateQuery());
getters=tableClassInformation.getGetters();
preparedStatementSetters=tableClassInformation.getPreparedStatementSetters();
for(i=0;i<getters.size();i++) preparedStatementSetters.get(i).invoke(preparedStatement,(i+1),getters.get(i).invoke(object));
preparedStatement.setString((i+1),String.valueOf(tableClassInformation.getPrimaryKeyGetterMethod().invoke(object)));
preparedStatement.executeUpdate();
preparedStatement.close();
}catch(IllegalAccessException iae)
{
throw new DataException(iae.getMessage());
}
catch(SQLException sqlException)
{
throw new DataException(sqlException.getMessage());
}
catch(InvocationTargetException ite)
{
throw new DataException(ite.getMessage());
}
this.targetClass=null;
this.stringBuffer=null;
}
public void delete(Object object) throws DataException
{
Table tableAnnotation=null;

String tableName=null;

TableClassInformation tableClassInformation=null;

Method primaryKeyGetterMethod=null;

int found;

Object getterResult=null;
Object primaryKeyGetterResult=null;

PreparedStatement preparedStatement=null;

this.targetClass=object.getClass();
this.stringBuffer=new StringBuffer();
try
{
tableAnnotation=(Table)this.targetClass.getAnnotation(Table.class);
if(tableAnnotation==null) throw new DataException("Table Annotation not found.");
tableName=tableAnnotation.name();
tableClassInformation=tableNameWiseMap.get(tableName);

// primary key duplicacy checks
if(tableClassInformation.getHasPrimaryKeyAnnotation())
{
primaryKeyGetterMethod=tableClassInformation.getPrimaryKeyGetterMethod();
primaryKeyGetterResult=primaryKeyGetterMethod.invoke(object);
this.stringBuffer.append(new String("select * from "+tableName));
List list=query(this.targetClass).fire();
found=0;
for(Object obj : list)
{
getterResult=primaryKeyGetterMethod.invoke(obj);
if(getterResult.equals(primaryKeyGetterResult)) found=1;
}
if(found==0) throw new DataException("Invalid Entry for primary key.");
}
preparedStatement=this.connection.prepareStatement(tableClassInformation.getDeleteQuery());
preparedStatement.setString(1,String.valueOf(primaryKeyGetterResult));
preparedStatement.executeUpdate();
preparedStatement.close();
}catch(IllegalAccessException iae)
{
throw new DataException(iae.getMessage());
}
catch(InvocationTargetException ite)
{
throw new DataException(ite.getMessage());
}
catch(SQLException sqlException)
{
throw new DataException(sqlException.getMessage());
}
this.targetClass=null;
this.stringBuffer=null;
}
public DataManager query(Class targetClass) throws DataException
{
this.targetClass=targetClass;
this.stringBuffer=new StringBuffer();
Table tableAnnotation=(Table)targetClass.getAnnotation(Table.class);
if(tableAnnotation==null) throw new DataException("Table Annotation not found.");
String tableName=tableAnnotation.name();
this.stringBuffer.append("Select * from "+tableName);
return this.dataManager;
}
public DataManager where(Object object) throws DataException
{
if(this.stringBuffer==null) throw new DataException("Invalid use of \"where()\"");
this.stringBuffer.append(" where "+(String)object);
return this.dataManager;
}
public DataManager gt(Object object) throws DataException
{
if(this.stringBuffer==null) throw new DataException("Invalid use of \"gt()\"");
this.stringBuffer.append(">"+(java.lang.Integer)object);
return this.dataManager;
}
public DataManager eq(Object object) throws DataException
{
if(this.stringBuffer==null) throw new DataException("Invalid use of \"eq()\"");
String className=object.getClass().getName();
if(className.equals("java.lang.Integer") || className.equals("int")) this.stringBuffer.append("="+(java.lang.Integer)object);
else this.stringBuffer.append("='"+(java.lang.String)object+"'");
return this.dataManager;
}
public DataManager lt(Object object) throws DataException
{
if(this.stringBuffer==null) throw new DataException("Invalid use of \"lt()\"");
this.stringBuffer.append("<"+(java.lang.Integer)object);
return this.dataManager;
}
public DataManager ge(Object object) throws DataException
{
if(this.stringBuffer==null) throw new DataException("Invalid use of \"ge()\"");
this.stringBuffer.append(">="+(java.lang.Integer)object);
return this.dataManager;
}
public DataManager le(Object object) throws DataException
{
if(this.stringBuffer==null) throw new DataException("Invalid use of \"le()\"");
this.stringBuffer.append("<="+(java.lang.Integer)object);
return this.dataManager;
}
public DataManager ne(Object object) throws DataException
{
if(this.stringBuffer==null) throw new DataException("Invalid use of \"ne()\"");
this.stringBuffer.append("!="+(java.lang.Integer)object);
return this.dataManager;
}
public DataManager and(Object object) throws DataException
{
if(this.stringBuffer==null) throw new DataException("Invalid use of \"and()\"");
this.stringBuffer.append(" and "+(String)object);
return this.dataManager;
}
public DataManager or(Object object) throws DataException
{
if(this.stringBuffer==null) throw new DataException("Invalid use of \"or()\"");
this.stringBuffer.append(" or "+(String)object);
return this.dataManager;
}
public java.util.List fire() throws DataException
{
java.util.List list=new java.util.LinkedList();

Table tableAnnotation=null;

String tableName=null;

TableClassInformation tableClassInformation=null;

java.util.List<Method> preparedStatementSetters=null;
java.util.List<Method> setters=null;
java.util.List<Method> resultSetGetters=null;

PreparedStatement preparedStatement=null;

ResultSet resultSet=null;

ResultSetMetaData resultSetMetaData=null;

int columnCount;
int i;

Object obj=null;



try
{
if(this.targetClass==null) throw new DataException("Invalid use of \"fire()\",use \"query()\" before fire()");
tableAnnotation=(Table)this.targetClass.getAnnotation(Table.class);
tableName=tableAnnotation.name();
tableClassInformation=tableNameWiseMap.get(tableName);
preparedStatementSetters=tableClassInformation.getPreparedStatementSetters();
setters=tableClassInformation.getSetters();
resultSetGetters=tableClassInformation.getResultSetGetters();
preparedStatement=connection.prepareStatement(this.stringBuffer.toString());
resultSet=preparedStatement.executeQuery();
resultSetMetaData=resultSet.getMetaData();
columnCount=resultSetMetaData.getColumnCount();
while(resultSet.next())
{
obj=this.targetClass.newInstance();
for(i=1;i<=columnCount;i++) setters.get(i-1).invoke(obj,resultSetGetters.get(i-1).invoke(resultSet,i));
list.add(obj);
}
}catch(SQLException sqlException)
{
throw new DataException(sqlException.getMessage());
}
catch(InstantiationException ie)
{
throw new DataException(ie.getMessage());
}
catch(IllegalAccessException iae)
{
throw new DataException(iae.getMessage());
}
catch(InvocationTargetException ite)
{
throw new DataException(ite.getMessage());
}
this.stringBuffer=null;
return list;
}
public void end() throws DataException
{
try
{
this.connection.commit();
}catch(SQLException sqlException)
{
throw new DataException(sqlException.getMessage());
}
}
}