# Standalone PL/SQL & SQL Formatter

## Introduction

This Maven project produces a standalone command line executable `tvdformat.jar` for the SQLcl script [`format.js`](../sqlcl/format.js). Optionally it produces also a GraalVM [native image](https://www.graalvm.org/reference-manual/native-image/) `tvdformat`.

This project contains JUnit tests for

- the SQLDev/SQLcl formatter settings `trivadis_advanced_format.xml` and `trivadis_custom_format.arbori`
- the SQLcl script `format.js`
- the SQLcl command `tvdformat`
- the standalone exectable `tvdformat` 

The project requires a JDK 17. A GraalVM JDK 21.0.2 is required if you want to build a [native image](https://www.graalvm.org/reference-manual/native-image/).

## Running the Standalone Formatter

### Executable JAR

The `tvdformat.jar` is a shaded, executable JAR that is part of a [release](https://github.com/Trivadis/plsql-formatter-settings/releases). It contains all dependent Java classes and needs a JDK 11 or higher at runtime.

To run it, open a terminal window and type

```
java -jar tvdformat.jar
```

The parameters are the same as for the [SQLcl command `tvdformat`](../sqlcl/README.md#register-script-formatjs-as-sqlcl-command-tvdformat). Except for formatting the SQLcl buffer, of course.

### Native Image

A native image is a platform specific executable. It does not require a JDK at runtime. A native image uses less resources and is faster. The following images can be produced with a GraalVM JDK 21:

| OS      | amd64 (Intel)? | aarch64 (ARM)? |
|---------|:--------------:|:--------------:|
| macOS   |      yes       |      yes       |
| Linux   |      yes       |      yes       |
| Windows |      yes       |       -        |

Currently, there is no way to produce an ARM based (aarch64) native image for Windows. 

Native images are not part of a release. You have to build them yourself as described [below](#how-to-build).

To run a native image open a terminal window and type

```
./tvdformat
```

The parameters are the same as for the [executable JAR](#executable-jar).

## How to Build

1. [Download](https://www.oracle.com/tools/downloads/sqlcl-downloads.html) and install SQLcl 23.4.0
2. [Download](https://github.com/graalvm/graalvm-ce-builds/releases/tag/jdk-17.0.9) and install the GraalVM JDK 21.0.2
3. For native image on Windows you need to [download](https://visualstudio.microsoft.com/downloads/) Visual Studio Community 2022 and install the C++ compiler. Use `x64 Native Tools Command Prompt for VS 2022` to get a terminal window with the correct environment.
4. [Download](https://maven.apache.org/download.cgi) and install Apache Maven 3.9.6
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

    | Parameter                  | Value   | Meaning                                           |
    | -------------------------- | ------- |---------------------------------------------------|
    | `skip.native`              | `true`  | Do not produce a native image (default)           |
    |                            | `false` | Produce a native image                            |
    | `skipTests`                | `true`  | Do not run tests                                  |
    |                            | `false` | Run tests (default)                               |
    | `disable.logging`          | `true`  | Disable logging message during test run (default) |
    |                            | `false` | Enable logging message during test run            |

    Here's a fully qualified example to produce a native image and run all integration tests:

    ```
    mvn -Dskip.native=false -DskipTests=false -Ddisable.logging=true clean integration-test
    ```
