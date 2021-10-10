# Unit Tests for PL/SQL & SQL Formatter Settings

## Introduction

This is a Maven project that does not produce an artifact. It's designed to execute tests only. The idea is to unit test the settings for the SQL Developer's formatter and to test the SQLcl script `format.js`.

## How to Run Tests

1. [Download](https://www.oracle.com/tools/downloads/sqlcl-downloads.html) and install SQLcl 21.3.0
2. Download and install a JDK 17, e.g. from
   - https://www.oracle.com/java/technologies/downloads/#JDK17
   - https://jdk.java.net/17/
3. [Download](https://maven.apache.org/download.cgi) and install Apache Maven 3.8.3
4. [Download](https://git-scm.com/downloads) and install a git command line client
5. Clone the plsql-formatter-settings repository
6. Open a terminal window in the plsql-formatter-settings root folder and type

		cd tests

6. Run maven build by the following command

		mvn -Dsqlcl.libdir=/usr/local/bin/sqlcl/lib clean test

	Amend the parameter `sqlcl.libdir` to match the path of the lib directory of you SQLcl installation. This folder is used to reference the `dbtools-common.jar` library (containing the formatter and its dependencies) and the `dbtools-sqlcl.jar` (containing SQLcl related features) which are not available in public Maven repositories.

## Excerpt of `mvn clean test` Output

```
[INFO] Scanning for projects...
[INFO] 
[INFO] ---------------< com.trivadis:plsql.formatter.settings >----------------
[INFO] Building com.trivadis.org.plsql.formatter.settings 21.3.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]  
...
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
...
[INFO] 
[INFO] Results:
[INFO] 
[WARNING] Tests run: 520, Failures: 0, Errors: 0, Skipped: 2
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  55.466 s
[INFO] Finished at: 2021-10-10T19:00:13+02:00
[INFO] ------------------------------------------------------------------------
```
