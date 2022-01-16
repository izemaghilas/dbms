### Description
Simple DataBase Management System, builded using JAVA. <br/>
The <a href="https://github.com/izemaghilas/dbms-ui">user interface</a>, is a react web application, that interacts with the **jakarta ee rest api**,<br/> 
to execute the user provided query.

<a href="https://izemaghilas.github.io/dbms-ui">Try it</a>. 

### Data Types
* ***int***.
* ***float***.
* ***stringL***. ***L*** is the string length.

### Query Language
* **CREATE** <br/>
  create new relation in database<br/>
  ```
  Syntax: CREATE relation_name number_of_columns data_type_of_each_column
  Ex: CREATE employee 3 string45 string45 float
  ```
* **INSERT** <br/>
  insert a record to a relation<br/>
  ```
  Syntax: INSERT relation_name value_of_each_column
  Ex: INSERT employee employee_first_name employee_last_name employee_salary
  ```
* **SELECTALL** <br/>
  select all records of a relation<br/>
  ```
  Syntax: SELECTALL relation_name
  Ex: SELECTALL employee
  ```
