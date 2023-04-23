# Standalone PL/SQL & SQL Formatter

## Introduction

This Maven project produces a standalone command line executable `tvdformat.jar` for the SQLcl script [`format.js`](../sqlcl/format.js). Optionally it produces also a GraalVM [native image](https://www.graalvm.org/reference-manual/native-image/) `tvdformat`.

This project contains JUnit tests for

- the SQLDev/SQLcl formatter settings `trivadis_advanced_format.xml` and `trivadis_custom_format.arbori`
- the SQLcl script `format.js`
- the SQLcl command `tvdformat`
- the standalone exectable `tvdformat` 

The project requires a JDK 17, but it produces a Java 11 executable JAR file. A GraalVM JDK is required only if you want to build a [native image](https://www.graalvm.org/reference-manual/native-image/).

## Running the Standalone Formatter

### Executable JAR

The `tvdformat.jar` is a shaded, executable JAR that is part of a [release](https://github.com/Trivadis/plsql-formatter-settings/releases). It contains all dependent Java classes and needs a JDK 11 or higher at runtime.

To run it, open a terminal window and type

```
java -jar tvdformat.jar
```

The parameters are the same as for the [SQLcl command `tvdformat`](../sqlcl/README.md#register-script-formatjs-as-sqlcl-command-tvdformat). Except for formatting the SQLcl buffer, of course.

### Native Image

A native image is a platform specific executable. It does not require a JDK at runtime. A native image uses less resources and is faster. The following images can be produced with a GraalVM JDK 17:

OS      | amd64 (Intel))? | aarch64 (ARM)? |
------- | :-------------: | :------------: |
macOS   | yes             | yes            |
Linux   | yes             | yes            |
Windows | yes             | -              |

Currently there is no way to produce an ARM based (aarch64) native image for Windows. 

Native images are not part of a release. You have to build them yourself as described [below](#how-to-build).

To run a native image open a terminal window and type

```
./tvdformat
```

The parameters are the same as for the [executable JAR](#executable-jar).

## Important: Native Images Do not Work Anymore

Starting with SQLcl 23.1.0 it is not possible to build native images on all platforms anymore.
The reason is that the underlying parser uses AWT/Swing components (for whatever reason).
AWT/Swing components are supported on AMD/Linux platforms only by GraalVM.
As a result this build process will produce a non-working native image on most platforms.

You will see the following stacktrace similar to the following when running the resulting native image:

```
	at com.oracle.svm.core.jdk.NativeLibrarySupport.loadLibraryRelative(NativeLibrarySupport.java:120)
	at java.base@17.0.6/java.lang.ClassLoader.loadLibrary(ClassLoader.java:50)
	at java.base@17.0.6/java.lang.Runtime.loadLibrary0(Runtime.java:818)
	at java.base@17.0.6/java.lang.System.loadLibrary(System.java:1989)
	at java.desktop@17.0.6/java.awt.Toolkit$2.run(Toolkit.java:1388)
	at java.desktop@17.0.6/java.awt.Toolkit$2.run(Toolkit.java:1386)
	at java.base@17.0.6/java.security.AccessController.executePrivileged(AccessController.java:168)
	at java.base@17.0.6/java.security.AccessController.doPrivileged(AccessController.java:318)
	at java.desktop@17.0.6/java.awt.Toolkit.loadLibraries(Toolkit.java:1385)
	at java.desktop@17.0.6/java.awt.Toolkit.initStatic(Toolkit.java:1423)
	at java.desktop@17.0.6/java.awt.Toolkit.<clinit>(Toolkit.java:1397)
	at java.desktop@17.0.6/java.awt.Component.<clinit>(Component.java:624)
	at java.base@17.0.6/java.lang.Class.ensureInitialized(DynamicHub.java:528)
	at java.base@17.0.6/java.lang.Class.ensureInitialized(DynamicHub.java:528)
	at java.base@17.0.6/java.lang.Class.ensureInitialized(DynamicHub.java:528)
	at java.base@17.0.6/java.lang.Class.ensureInitialized(DynamicHub.java:528)
	at java.base@17.0.6/java.lang.Class.ensureInitialized(DynamicHub.java:528)
	at oracle.dbtools.parser.Visual.<init>(Visual.java:404)
	at oracle.dbtools.parser.Grammar.parseGrammarFile(Grammar.java:172)
	at oracle.dbtools.parser.Grammar.parseGrammarFile(Grammar.java:152)
	at oracle.dbtools.arbori.Program.getRules(Program.java:277)
	at oracle.dbtools.arbori.Program.getArboriParser(Program.java:187)
	at oracle.dbtools.arbori.Program.compile(Program.java:330)
	at oracle.dbtools.arbori.Program.compile(Program.java:319)
	at oracle.dbtools.arbori.Program.compile(Program.java:316)
	at oracle.dbtools.app.XML2Table.<init>(XML2Table.java:70)
	at oracle.dbtools.app.Persist2XML.<init>(Persist2XML.java:48)
	at oracle.dbtools.app.Persist2XML.read(Persist2XML.java:95)
	at <js>.configure(<eval>:104)
	at <js>.getConfiguredFormatter(<eval>:161)
	at <js>.run(<eval>:727)
	at <js>.:program(<eval>:803)
	at org.graalvm.sdk/org.graalvm.polyglot.Context.eval(Context.java:399)
	at org.graalvm.js.scriptengine/com.oracle.truffle.js.scriptengine.GraalJSScriptEngine.eval(GraalJSScriptEngine.java:478)
	at org.graalvm.js.scriptengine/com.oracle.truffle.js.scriptengine.GraalJSScriptEngine.eval(GraalJSScriptEngine.java:420)
	at com.trivadis.plsql.formatter.TvdFormat.run(TvdFormat.java:36)
	at com.trivadis.plsql.formatter.TvdFormat.main(TvdFormat.java:49)
```

This is really sad. However, there is nothing we can do. 
We have to wait until the SQL Developer team decides to provide a parser that does not require AWT/Swing components (as in previous versions)
or until the GraalVM team provides a native-image build process that works on all platforms with AWT/Swing.
See also [macOS: no awt in java.library.path](https://github.com/oracle/graal/issues/4124).

## How to Build

1. [Download](https://www.oracle.com/tools/downloads/sqlcl-downloads.html) and install SQLcl 23.1.0
2. [Download](https://github.com/graalvm/graalvm-ce-builds/releases) and install the GraalVM JDK 17 22.3.1
3. Go to the `bin` directory of the GraalVM JDK and run `./gu install js native-image visualvm`. For native image on Windows you need to [download](https://visualstudio.microsoft.com/downloads/) Visual Studio Community 2022 and install the C++ compiler. Use `x64 Native Tools Command Prompt for VS 2022` to get a terminal window with the correct environment.
4. [Download](https://maven.apache.org/download.cgi) and install Apache Maven 3.9.1
5. [Download](https://git-scm.com/downloads) and install a git command line client
6. Clone the plsql-formatter-settings repository. The repository uses symbolic links. On Windows you have to use `git clone -c core.symlinks=true https://github.com/Trivadis/plsql-formatter-settings.git` as Administrator to make it work. See also [Symbolic Links in Windows](https://github.com/git-for-windows/git/wiki/Symbolic-Links) for more information.
7. Open a terminal window in the plsql-formatter-settings root folder and type

    ```
    cd standalone
    ```
8. Install the required SQLcl libraries which are not available in public Maven repositories into your local Maven repository. Run the following shell script

    ```
    ./install_sqlcl_libs.sh
    ```

    The shell script expects to find the libraries such as `dbtools-common.jar` in `/usr/local/bin/sqlcl/lib`. If they are not there, pass the path to the directory as parameter to this script. For example

    ```
    ./install_sqlcl_libs.sh /usr/local/bin/sqlcl/lib
    ```

9. Run Maven build by the following command

    ```
    mvn clean package
    ```

    You can define the following optional parameters: 

    | Parameter                  | Value   | Meaning |
    | -------------------------- | ------- | ------- |
    | `skip.native`              | `true`  | Do not produce a native image (default) |
    |                            | `false` | Produce a native image |
    | `skipTests`                | `true`  | Run tests (default) |
    |                            | `false` | Do not run tests |
    | `disable.logging`          | `true`  | Disable logging message during test run (default) |
    |                            | `false` | Enable logging message during test run |

    Here's a fully qualified example to produce a native image and run all integration tests:

    ```
    mvn -Dskip.native=false -DskipTests=false -Ddisable.logging=true clean integration-test
    ```
