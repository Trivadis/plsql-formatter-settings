# Standalone PL/SQL & SQL Formatter

## Introduction

This Maven project produces a standalone command line executable `tvdformat.jar` for the SQLcl script [`format.js`](../sqlcl/format.js). Optionally it produces also a GraalVM [native image](https://www.graalvm.org/reference-manual/native-image/) `tvdformat`.

The startup time of standalone JAR file and the native image are similar since the image still requires a JDK to execute. However, it is faster than running `format.js` from SQLcl.

This project contains JUnit tests for

- the SQLDev/SQLcl formatter settings `trivadis_advanced_format.xml` and `trivadis_custom_format.arbori`
- the SQLcl script `format.js`
- the SQLcl command `tvdformat`
- the standalone exectable `tvdformat` 

The project requires a JDK 17, but it produces a Java 8 executable JAR file. A GraalVM JDK is required only if you want to build a [native image](https://www.graalvm.org/reference-manual/native-image/).

## Running the Standalone Formatter

### Configure Logging

Optionally, you can define the following environment variables:

Variable | Description
-------- | -----------
`TVDFORMAT_LOGGING_CONF_FILE` | Path to a [java.util.logging](https://docs.oracle.com/en/java/javase/17/core/java-logging-overview.html#GUID-B83B652C-17EA-48D9-93D2-563AE1FF8EDA) configuration file. Fully qualified or relative paths are supported. [This file](src/test/resources/logging.conf) is used for tests.
`TVDFORMAT_DEBUG` | `true` enables Arbori debug messages.
`TVDFORMAT_TIMING` |`true` enables Arbori query/callback timing messages.

### Executable JAR

The `tvdformat.jar` is a shaded, executable JAR. This means it contains all dependent Java classes. However, it still needs a JDK 8 or higher.

To run it, open a terminal window and type

```
java -jar tvdformat.jar
```

The parameters are the same as for the [SQLcl command `tvdformat`](../sqlcl/README.md#register-script-formatjs-as-sqlcl-command-tvdformat). Except for formatting the SQLcl buffer, of course.

### Native Image

A native image is a platform specific executable. The following images can be produced with a GraalVM JDK 17:

OS      | amd64)? | aarch64? | Requires JDK 8+? | No JDK required?
------- | ------- | -------- | ---------------- | ---------------- 
macOS   | yes     | no       | yes              | yes
Linux   | yes     | yes      | yes              | yes
Windows | yes     | no       | yes              | yes

Currently there is no way to produce an ARM based (aarch64) native image for macOS and Windows. This reduces the possible combinations from 12 to 8 native images. Native images are not part of a release. You have to build them yourself as described [below](#how-to-build). 

Native images produced with the `--force-fallback` option have a size of around 14 MB. They require a JDK 8+ at runtime and are considered stable. 

Native images produced with the `--no-fallback` option have a size of around 500 MB. They do not require a JDK at runtime. Due to the absence of automatic tests, these images are considered experimental.

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

8. Run Maven build by the following command

    ```
    mvn -Dsqlcl.libdir=/usr/local/bin/sqlcl/lib clean package
    ```

    Amend the parameter `sqlcl.libdir` to match the path of the lib directory of your SQLcl installation. This folder is used to reference libraries such as `dbtools-common.jar` which contains the formatter and its dependencies. These libraries are not available in public Maven repositories.
    
    You can define the following optional parameters: 

    | Parameter                  | Value   | Meaning |
    | -------------------------- | ------- | ------- |
    | `skip.native`              | `true`  | Do not produce a native image (default) |
    |                            | `false` | Produce a native image |
    | `native.image.fallback`    | `no`    | Produce a native image of about 14 MB which requires a JDK at runtime (default) |
    |                            | `force` | Produce a native image of about 500 MB that runs without JDK |
    | `skipTests`                | `true`  | Run tests (default) |
    |                            | `false` | Do not run tests |
    | `disable.logging`          | `true`  | Disable logging message during test run (default) |
    |                            | `false` | Enable logging message during test run |

    Here's a fully qualified example to produce a native image:

    ```
    mvn -Dsqlcl.libdir=/usr/local/bin/sqlcl/lib -Dskip.native=false -Dnative.image.fallback=no -DskipTests=true -Ddisable.logging=true clean package
    ```
