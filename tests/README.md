# Unit Tests for PL/SQL & SQL Formatter Settings

## Introduction

This is a Maven project that does not produce an artifact. It's designed to execute tests only. The idea is to unit test the settings for the SQL Developer's formatter and to test the SQLcl script `format.js`.

We've started with a few simple test cases and will add new tests based on the reported issues.

## How to Run Tests

1. [Download](https://www.oracle.com/tools/downloads/sqlcl-downloads.html) and install SQLcl 20.2.0
2. Download and install a JDK8, e.g. from
   - https://www.java.com/en/download/
   - https://github.com/graalvm/graalvm-ce-builds/releases
   - https://openjdk.java.net/install/
3. [Download](https://maven.apache.org/download.cgi) and install Apache Maven 3.6.3
4. [Download](https://git-scm.com/downloads) and install a git command line client
5. Clone the plsql-formatter-settings repository
6. Open a terminal window in the plsql-formatter-settings root folder and type

		cd tests

6. Run maven build by the following command

		mvn -Dsqlcl.libdir=/usr/local/bin/sqlcl/lib clean test

	Amend the parameter `sqlcl.libdir` to match the path of the lib directory of you SQLcl installation. This folder is used to reference the `dbtools-common.jar` library (containing the formatter and its dependencies) and the `dbtools-sqlcl.jar` (containing SQLcl related features) which are not available in public Maven repositories.

## Eclipse IDE

We use the [Eclipse IDE for Java and DSL Developers](https://www.eclipse.org/downloads/packages/release/2020-03/r/eclipse-ide-java-and-dsl-developers) because we use Xtend to handle multiline strings (unformatted and formatted code) in a convientient way. You may use any IDE that can import Maven projects. However, Xtend support is poor outside of the Eclipse ecosystem. You've been warned.

## Excerpt of `mvn clean test` Output

```
[INFO] Scanning for projects...
[INFO] 
[INFO] ---------------< com.trivadis:plsql.formatter.settings >----------------
[INFO] Building com.trivadis.org.plsql.formatter.settings 20.3.0-SNAPSHOT
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
[[INFO] Tests run: 82, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  32.764 s
[INFO] Finished at: 2020-11-02T05:24:13+01:00
[INFO] ------------------------------------------------------------------------
```
