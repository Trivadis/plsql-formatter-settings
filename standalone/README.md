# Standalone PL/SQL & SQL Formatter

## Introduction

This Maven project produces a standalone command line executable `tvdformat.jar` for the SQLcl script [`format.js`](../sqlcl/format.js). Optionally it produces also a GraalVM [native image](https://www.graalvm.org/reference-manual/native-image/) `tvdformat`.

This project contains JUnit tests for

- the SQLDev/SQLcl formatter settings `trivadis_advanced_format.xml` and `trivadis_custom_format.arbori`
- the SQLcl script `format.js`
- the SQLcl command `tvdformat`
- the standalone exectable `tvdformat` 

The project requires a JDK 17, but it produces a Java 8 executable JAR file. A GraalVM JDK is required only if you want to build a [native image](https://www.graalvm.org/reference-manual/native-image/). GraalVM 22.0.0.2 does not work in with the libraries from SQLcl 21.4.1 (throws `org.graalvm.polyglot.PolyglotException: java.lang.AssertionError`). Please use GraalVM 21.3.0 instead. 

## Running the Standalone Formatter

### Executable JAR

The `tvdformat.jar` is a shaded, executable JAR that is part of a [release](https://github.com/Trivadis/plsql-formatter-settings/releases). It contains all dependent Java classes and needs a JDK 8 or higher at runtime.

To run it, open a terminal window and type

```
java -jar tvdformat.jar
```

The parameters are the same as for the [SQLcl command `tvdformat`](../sqlcl/README.md#register-script-formatjs-as-sqlcl-command-tvdformat). Except for formatting the SQLcl buffer, of course.

### Native Image

A native image is a platform specific executable. It does not require a JDK at runtime. A native image uses less resources and is faster. The following images can be produced with a GraalVM JDK 17:

OS      | amd64 (Intel))? | aarch64 (ARM)? |
------- | :-------------: | :------------: |
macOS   | yes             | -              |
Linux   | yes             | yes            |
Windows | yes             | -              |

Currently there is no way to produce an ARM based (aarch64) native image for macOS and Windows. 

Native images are not part of a release. You have to build them yourself as described [below](#how-to-build).

To run a native image open a terminal window and type

```
./tvdformat
```

The parameters are the same as for the [executable JAR](#executable-jar).

## How to Build

1. [Download](https://www.oracle.com/tools/downloads/sqlcl-downloads.html) and install SQLcl 21.4.0
2. [Download](https://github.com/graalvm/graalvm-ce-builds/releases) and install the GraalVM JDK 17 21.3.0
3. Go to the `bin` directory of the GraalVM JDK and run `./gu install native-image`, if you want to produce a native image
4. [Download](https://maven.apache.org/download.cgi) and install Apache Maven 3.8.4
5. [Download](https://git-scm.com/downloads) and install a git command line client
6. Clone the plsql-formatter-settings repository
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
