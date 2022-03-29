package com.orm.connections;
import java.sql.*;
import com.orm.exceptions.*;
import com.orm.pojo.*;
public class ORMConnection
{
private ORMConnection(){}
public static Connection getConnection(DatabaseInformation databaseInformation) throws ORMConnectionException
{
try
{
Class.forName(databaseInformation.getJdbcDriver());
Connection connection=DriverManager.getConnection(databaseInformation.getConnectionURL(),databaseInformation.getUsername(),databaseInformation.getPassword());
return connection;
}catch(SQLException sqlException)
{
throw new ORMConnectionException(sqlException.getMessage());
}
catch(ClassNotFoundException cnfe)
{
throw new ORMConnectionException(cnfe.getMessage());
}
}
}