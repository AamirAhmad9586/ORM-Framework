package com.orm.managers;
import java.io.*;
import com.google.gson.*;
import java.sql.*;
import java.util.*;
import com.orm.exceptions.*;
import com.orm.annotations.*;
import com.orm.pojo.*;
import com.orm.connections.*;
public class ORMPOJOTool
{
public static void main(String gg[])
{
try
{
File file=new File("conf.json");
if(!file.exists()) throw new FileNotFoundException("File \"conf.json\" does not exists.");
JsonParser jsonParser=new JsonParser();
JsonObject jsonObject=(JsonObject)jsonParser.parse(new FileReader("conf.json"));
DatabaseInformation databaseInformation=new DatabaseInformation();
if(jsonObject.has("jdbc-driver")) databaseInformation.setJdbcDriver(jsonObject.get("jdbc-driver").getAsString());
if(jsonObject.has("connection-url")) databaseInformation.setConnectionURL(jsonObject.get("connection-url").getAsString());
if(jsonObject.has("username")) databaseInformation.setUsername(jsonObject.get("username").getAsString());
if(jsonObject.has("password")) databaseInformation.setPassword(jsonObject.get("password").getAsString());
if(jsonObject.has("package-name")) databaseInformation.setPackageName(jsonObject.get("package-name").getAsString());
if(jsonObject.has("jar-file-name")) databaseInformation.setJarFileName(jsonObject.get("jar-file-name").getAsString());
System.out.println("Scanning database according to \"conf.json\",please wait...");
Connection connection=ORMConnection.getConnection(databaseInformation);

int index=0;
int found=0;

String databaseName=null;
String tableName=null;
String tableClassName=null;
String columnName=null;
String dataType=null;
String primaryKeyName=null;
String foreignKeyName=null;
String line=null;
String packageName=null;
String key=null;
String value=null;

DatabaseMetaData databaseMetaData=null;

ResultSetMetaData resultSetMetaData=null;

ResultSet tables=null;
ResultSet columns=null;
ResultSet resultSet=null;
	
java.util.List<String> primaryKeys=null;
java.util.List<ForeignKeyInformation> foreignKeys=null;

java.util.Map<String,String> fields=null;

java.util.Set<java.util.Map.Entry<String,String>> entries=null;

ForeignKeyInformation foreignKeyInformation=null;

Table tableAnnotation=null;

RandomAccessFile randomAccessFile=null;



databaseName=connection.getCatalog();
databaseMetaData=connection.getMetaData();
tables=databaseMetaData.getTables(databaseName,null,null,new String[]{"TABLE"});

packageName="";
if(databaseInformation.getPackageName()!=null && databaseInformation.getPackageName().length()!=0)
{
packageName="src"+File.separator+databaseInformation.getPackageName().replace('.',File.separator.charAt(0))+File.separator;
file=new File(packageName);
if(!file.isDirectory()) file.mkdirs();
}






while(tables.next())
{
foreignKeys=new java.util.ArrayList<>();
primaryKeys=new java.util.ArrayList<>();
tableName=tables.getString("TABLE_NAME");

tableClassName=tableName.substring(0,1).toUpperCase()+tableName.substring(1);
index=tableClassName.indexOf("_");
while(index!=-1)
{
tableClassName=tableClassName.substring(0,index)+tableClassName.substring(index+1,index+2).toUpperCase()+tableClassName.substring(index+2);
index=tableClassName.indexOf("_");
}
System.out.println("Creating - \""+packageName+tableClassName+"\" class-");
file=new File(packageName+tableClassName+".java");
if(file.exists()) file.delete();
randomAccessFile=new RandomAccessFile(file,"rw");
if(packageName.length()!=0) randomAccessFile.writeBytes("package "+databaseInformation.getPackageName()+";\r\n");
randomAccessFile.writeBytes("import com.orm.annotations.*;\r\n");
randomAccessFile.writeBytes("@Table(name=\""+tableName+"\")\r\n");
randomAccessFile.writeBytes("public class "+tableClassName+"\r\n");
randomAccessFile.writeBytes("{\r\n");

// code to get primary keys
resultSet=databaseMetaData.getPrimaryKeys(databaseName,null,tableName);
while(resultSet.next())
{
primaryKeyName=resultSet.getString("COLUMN_NAME");
primaryKeys.add(primaryKeyName);
}

// code to get foreign keys
resultSet=databaseMetaData.getImportedKeys(databaseName,null,tableName);
while(resultSet.next())
{
foreignKeyName=resultSet.getString("FKCOLUMN_NAME");
foreignKeyInformation=new ForeignKeyInformation();
foreignKeyInformation.setName(foreignKeyName);
foreignKeyInformation.setParentTableName(resultSet.getString(3));
foreignKeyInformation.setParentTableColumnName(resultSet.getString(4));
foreignKeys.add(foreignKeyInformation);
}

// code to get columns
fields=new java.util.HashMap<>();
columns=databaseMetaData.getColumns(databaseName,null,tableName,null);
while(columns.next())
{
columnName=columns.getString("COLUMN_NAME");
randomAccessFile.writeBytes("@Column(name=\""+columnName+"\")\r\n");

for(int i=0;i<primaryKeys.size();i++)
{
if(columnName.equals(primaryKeys.get(i)))
{
randomAccessFile.writeBytes("@PrimaryKey\r\n");
break;
}
}

for(int i=0;i<foreignKeys.size();i++)
{
foreignKeyInformation=foreignKeys.get(i);
if(columnName.equals(foreignKeyInformation.getName()))
{
randomAccessFile.writeBytes("@ForeignKey(parent=\""+foreignKeyInformation.getParentTableName()+"\",column=\""+foreignKeyInformation.getParentTableColumnName()+"\")\r\n");
break;
}
}

if(columns.getString("IS_NULLABLE").equals("NO")) randomAccessFile.writeBytes("@NotNull\r\n");
if(columns.getString("IS_AUTOINCREMENT").equals("YES")) randomAccessFile.writeBytes("@AutoIncrement\r\n");

index=columnName.indexOf("_");
while(index!=-1)
{
columnName=columnName.substring(0,index)+columnName.substring(index+1,index+2).toUpperCase()+columnName.substring(index+2);
index=columnName.indexOf("_");
}

dataType="";
if(columns.getString(6).equals("CHAR") || columns.getString(6).equals("char")) dataType="java.lang.String";
else if(columns.getString(6).equals("VARCHAR") || columns.getString(6).equals("varchar")) dataType="java.lang.String";
else if(columns.getString(6).equals("TEXT") || columns.getString(6).equals("text")) dataType="java.lang.String";
else if(columns.getString(6).equals("NUMERIC") || columns.getString(6).equals("numeric")) dataType="java.math.BigDecimal";
else if(columns.getString(6).equals("DECIMAL") || columns.getString(6).equals("decimal")) dataType="java.math.BigDecimal";
else if(columns.getString(6).equals("MONEY") || columns.getString(6).equals("money")) dataType="java.math.BigDecimal";
else if(columns.getString(6).equals("SMALLMONEY") || columns.getString(6).equals("smallmoney")) dataType="java.math.BigDecimal";
else if(columns.getString(6).equals("BIT") || columns.getString(6).equals("bit")) dataType="java.lang.Boolean";
else if(columns.getString(6).equals("TINYINT") || columns.getString(6).equals("tinyint")) dataType="java.lang.Byte";
else if(columns.getString(6).equals("SMALLINT") || columns.getString(6).equals("smallint")) dataType="java.lang.Short";
else if(columns.getString(6).equals("INTEGER") || columns.getString(6).equals("integer")) dataType="java.lang.Integer";
else if(columns.getString(6).equals("BIGINT") || columns.getString(6).equals("bigint")) dataType="java.lang.Long";
else if(columns.getString(6).equals("REAL") || columns.getString(6).equals("real")) dataType="java.lang.Float";
else if(columns.getString(6).equals("FLOAT") || columns.getString(6).equals("float")) dataType="java.lang.Double";
else if(columns.getString(6).equals("DOUBLE") || columns.getString(6).equals("double")) dataType="java.lang.Double";
else if(columns.getString(6).equals("BINARY") || columns.getString(6).equals("binary")) dataType="java.lang.Byte[]";
else if(columns.getString(6).equals("VARBINARY") || columns.getString(6).equals("varbinary")) dataType="java.lang.Byte[]";
else if(columns.getString(6).equals("LONG VARBINARY") || columns.getString(6).equals("long varbinary")) dataType="java.lang.Byte[]";
else if(columns.getString(6).equals("IMAGE") || columns.getString(6).equals("image")) dataType="java.lang.Byte[]";
else if(columns.getString(6).equals("DATE") || columns.getString(6).equals("date")) dataType="java.sql.Date";
else if(columns.getString(6).equals("TIME") || columns.getString(6).equals("time")) dataType="java.sql.Time";
else if(columns.getString(6).equals("TIMESTAMP") || columns.getString(6).equals("timestamp")) dataType="java.sql.Timestamp";
else if(columns.getString(6).equals("INT") || columns.getString(6).equals("int")) dataType="java.lang.Integer";
randomAccessFile.writeBytes("private "+dataType+" "+columnName+";\r\n");

fields.put(columnName,dataType);
} // columns.next() while loop ends
randomAccessFile.writeBytes("public "+tableClassName+"()\r\n");
randomAccessFile.writeBytes("{\r\n");
entries=fields.entrySet();
for(java.util.Map.Entry<String,String> entry : entries)
{
key=entry.getKey();
value=entry.getValue();
randomAccessFile.writeBytes("this."+key+"=");
if(value.equals("java.lang.Integer") || value.equals("int")) randomAccessFile.writeBytes("0");
else if(value.equals("java.lang.Character") || value.equals("char")) randomAccessFile.writeBytes("0");
else if(value.equals("java.lang.Float") || value.equals("float")) randomAccessFile.writeBytes("0");
else if(value.equals("java.lang.Double") || value.equals("double")) randomAccessFile.writeBytes("0.0");
else if(value.equals("java.lang.Short") || value.equals("short")) randomAccessFile.writeBytes("0");
else if(value.equals("java.lang.Byte") || value.equals("byte")) randomAccessFile.writeBytes("0");
else if(value.equals("java.lang.Long") || value.equals("long")) randomAccessFile.writeBytes("Long.valueOf(0)");
else if(value.equals("java.lang.Boolean") || value.equals("boolean")) randomAccessFile.writeBytes("false");
else randomAccessFile.writeBytes("null");
randomAccessFile.writeBytes(";\r\n");
}
randomAccessFile.writeBytes("}\r\n");

// code to generate setter's/getter's
for(java.util.Map.Entry<String,String> entry : entries)
{
key=entry.getKey();
value=entry.getValue();

randomAccessFile.writeBytes("public void set"+key.substring(0,1).toUpperCase()+key.substring(1)+"("+value+" "+key+")\n");
randomAccessFile.writeBytes("{\nthis."+key+"="+key+";\n}\n");

randomAccessFile.writeBytes("public "+value+" get"+key.substring(0,1).toUpperCase()+key.substring(1)+"()\n");
randomAccessFile.writeBytes("{\nreturn this."+key+";\n}\n");
}
randomAccessFile.writeBytes("}");
randomAccessFile.close();
} // tables.next() while loop ends
tables.close();
connection.close();
if(databaseInformation.getPackageName().length()==0) return;
File classesFile=new File("classes");
if(!classesFile.isDirectory()) classesFile.mkdir();
System.out.println("Creating classes files,please wait...");
Process process=Runtime.getRuntime().exec("javac -d classes -classpath ..\\dist\\ORM.jar;. src"+File.separator+databaseInformation.getPackageName().replace('.','\\')+File.separator+"*.java");
process.waitFor();
File testFile=new File("test");
if(!testFile.isDirectory()) testFile.mkdir();
if(databaseInformation.getJarFileName()==null) return;
File distFile=new File("dist");
if(!distFile.isDirectory()) distFile.mkdir();
String tmp=databaseInformation.getPackageName();
if(tmp.indexOf('.')!=-1) tmp=tmp.substring(0,tmp.indexOf('.'));
System.out.println("Creating \""+databaseInformation.getJarFileName()+"\" file,please wait...");
process=Runtime.getRuntime().exec("jar -cvf dist/"+databaseInformation.getJarFileName()+" -C classes "+tmp);
process.waitFor();
}catch(FileNotFoundException fnfe)
{
System.out.println(fnfe);
}
catch(ORMConnectionException ormce)
{
System.out.println(ormce);
}
catch(SQLException sqlException)
{
System.out.println(sqlException);
}
catch(IOException ioException)
{
System.out.println(ioException);
}
catch(InterruptedException interruptedException)
{
System.out.println(interruptedException);
}
}
}