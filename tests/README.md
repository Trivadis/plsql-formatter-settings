# Unit Tests for PL/SQL & SQL Formatter Settings

## Introduction

This is a Maven project that does not produce an artifact. It's designed to execute tests only. The idea is to unit test the settings for the SQL Developer's formatter. 

We've started with a few simple test cases and will add new tests based on the reported issues.

## How to Run Tests

1. [Download](http://www.oracle.com/technetwork/developer-tools/sql-developer/downloads/index.html) and install SQL Developer 19.4.0
2. Download and install a JDK8, e.g. from
   - https://www.java.com/en/download/
   - https://github.com/graalvm/graalvm-ce-builds/releases
   - https://openjdk.java.net/install/
3. [Download](https://maven.apache.org/download.cgi) and install Apache Maven 3.6.3
4. [Download](https://git-scm.com/downloads) and install a git command line client
5. Clone the plsql-formatter-settings repository
6. Open a terminal window in the plsql-formatter-settings root folder and type

		cd sqldev

6. Run maven build by the following command

		mvn -Dsqldev.basedir=/Applications/SQLDeveloper19.4.0.app/Contents/Resources/sqldeveloper clean test

	Amend the parameter `sqldev.basedir` to match the path of your SQL Developer installation. This folder is used to reference the `dbtools-common.jar` library (containing the formatter and its dependencies) which is not available in public Maven repositories

## Eclipse IDE

We use the [Eclipse IDE for Java and DSL Developers](https://www.eclipse.org/downloads/packages/release/2020-03/r/eclipse-ide-java-and-dsl-developers) because we use Xtend to handle multiline strings (unformatted and formatted code) in a convientient way. You may use any IDE that can import Maven projects. However, Xtend support is poor outside of the Eclipse ecosystem. You've been warned.

## Example `mvn clean test` Output

```
[INFO] Scanning for projects...
[INFO] 
[INFO] ---------------< com.trivadis:plsql.formatter.settings >----------------
[INFO] Building com.trivadis.org.plsql.formatter.settings 19.4.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:3.1.0:clean (default-clean) @ plsql.formatter.settings ---
[INFO] Deleting /Users/phs/github/trivadis/plsql-formatter-settings/sqldev/target
[INFO] Deleting /Users/phs/github/trivadis/plsql-formatter-settings/sqldev/src/test/xtend-gen (includes = [**/*], excludes = [])
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ plsql.formatter.settings ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory /Users/phs/github/trivadis/plsql-formatter-settings/sqldev/src/main/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.8.1:compile (default-compile) @ plsql.formatter.settings ---
[INFO] No sources to compile
[INFO] 
[INFO] --- xtend-maven-plugin:2.21.0:testCompile (test) @ plsql.formatter.settings ---
[INFO] 
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ plsql.formatter.settings ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 4 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.8.1:testCompile (default-testCompile) @ plsql.formatter.settings ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 8 source files to /Users/phs/github/trivadis/plsql-formatter-settings/sqldev/target/test-classes
[INFO] 
[INFO] --- maven-surefire-plugin:2.22.2:test (default-test) @ plsql.formatter.settings ---
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.trivadis.plsql.formatter.settings.tests.Paren_expr_list
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.23 s - in com.trivadis.plsql.formatter.settings.tests.Paren_expr_list
[INFO] Running com.trivadis.plsql.formatter.settings.tests.Select_list
2020-04-19 11:29:53.313 FINE: --- indentCaseExpression ---
2020-04-19 11:29:53.321 FINE: tuple: {node=[29,45)   case_expression  expr  expr#, node^-1=[28,29)   ',', node^=[29,46)   "aliased_expr"  select_term}
2020-04-19 11:29:53.324 FINE: length of addIndent: 5
2020-04-19 11:29:53.347 FINEST: addIndent on pos 30 for token WHEN
2020-04-19 11:29:53.350 FINEST: addIndent on pos 35 for token 'one'
2020-04-19 11:29:53.352 FINEST: addIndent on pos 36 for token WHEN
2020-04-19 11:29:53.353 FINEST: addIndent on pos 41 for token 'two'
2020-04-19 11:29:53.355 FINEST: addIndent on pos 42 for token ELSE
2020-04-19 11:29:53.356 FINEST: addIndent on pos 43 for token 'else'
2020-04-19 11:29:53.357 FINEST: addIndent on pos 44 for token END
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.881 s - in com.trivadis.plsql.formatter.settings.tests.Select_list
[INFO] Running com.trivadis.plsql.formatter.settings.tests.Issue_1
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.78 s - in com.trivadis.plsql.formatter.settings.tests.Issue_1
[INFO] Running com.trivadis.plsql.formatter.settings.tests.Issue_2
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.075 s - in com.trivadis.plsql.formatter.settings.tests.Issue_2
[INFO] Running com.trivadis.plsql.formatter.settings.examples.AllroundAutomations
2020-04-19 11:29:54.464 FINE: --- indentInExistsTable ---
2020-04-19 11:29:54.467 FINE: tuple: {node=[85,92)   "expr_list"  expression_list  in_condition[26,31), node-1=[84,85)   '(', node^=[84,93)   in_condition[24,35)}
2020-04-19 11:29:54.469 FINE: length of addIndent: 7
2020-04-19 11:29:54.475 FINEST: addIndent on pos 85 for token 10
2020-04-19 11:29:54.477 FINEST: addIndent on pos 87 for token 20
2020-04-19 11:29:54.478 FINEST: addIndent on pos 89 for token 30
2020-04-19 11:29:54.479 FINEST: addIndent on pos 91 for token 40
2020-04-19 11:29:54.481 FINE: --- indentInExistsTable ---
2020-04-19 11:29:54.483 FINE: tuple: {node=[92,93)   ')', node-1=[85,92)   "expr_list"  expression_list  in_condition[26,31), node^=[84,93)   in_condition[24,35)}
2020-04-19 11:29:54.484 FINE: length of addIndent: 7
2020-04-19 11:29:54.485 FINEST: addIndent on pos 92 for token )
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.295 s - in com.trivadis.plsql.formatter.settings.examples.AllroundAutomations
[INFO] Running com.trivadis.plsql.formatter.settings.examples.Trivadis
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.071 s - in com.trivadis.plsql.formatter.settings.examples.Trivadis
[INFO] Running com.trivadis.plsql.formatter.settings.examples.Oracle
2020-04-19 11:29:54.743 FINE: --- indentInExistsTable ---
2020-04-19 11:29:54.743 FINE: tuple: {node=[170,201)   query_block  simple_set_expr  subquery, node-1=[169,170)   '(', node^=[168,202)   condition  exists_condition}
2020-04-19 11:29:54.744 FINE: length of addIndent: 7
2020-04-19 11:29:54.745 FINEST: addIndent on pos 170 for token SELECT
2020-04-19 11:29:54.746 FINEST: addIndent on pos 173 for token 2
2020-04-19 11:29:54.747 FINEST: addIndent on pos 175 for token 3
2020-04-19 11:29:54.748 FINEST: addIndent on pos 176 for token FROM
2020-04-19 11:29:54.749 FINEST: addIndent on pos 179 for token JOIN
2020-04-19 11:29:54.750 FINEST: addIndent on pos 182 for token ON
2020-04-19 11:29:54.751 FINEST: addIndent on pos 190 for token JOIN
2020-04-19 11:29:54.752 FINEST: addIndent on pos 193 for token ON
2020-04-19 11:29:54.754 FINE: --- indentInExistsTable ---
2020-04-19 11:29:54.754 FINE: tuple: {node=[201,202)   ')', node-1=[170,201)   query_block  simple_set_expr  subquery, node^=[168,202)   condition  exists_condition}
2020-04-19 11:29:54.755 FINE: length of addIndent: 7
2020-04-19 11:29:54.756 FINEST: addIndent on pos 201 for token )
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.155 s - in com.trivadis.plsql.formatter.settings.examples.Oracle
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  13.518 s
[INFO] Finished at: 2020-04-19T11:29:55+02:00
[INFO] ------------------------------------------------------------------------
```
