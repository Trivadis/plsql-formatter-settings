# Standalone PL/SQL & SQL Formatter

## Introduction

This Maven project produces a standalone command line executable `tvdformat.jar` for the SQLcl script [`format.js`](../sqlcl/format.js). Optionally it produces also a GraalVM native image `tvdformat`.

The startup time of standalone JAR file and the native image are identical since the image still requires a JDK to execute. However, it is faster than running `format.js` from SQLcl.

## How to Build

1. [Download](https://www.oracle.com/tools/downloads/sqlcl-downloads.html) and install SQLcl 21.2.2
2. [Download](https://github.com/graalvm/graalvm-ce-builds/releases) and install the GraalVM JDK 11 21.2.0
3. Go to the bin directory of the GraalVM JDK and run `gu install native-image`, if you want to produce a native image
4. [Download](https://maven.apache.org/download.cgi) and install Apache Maven 3.8.1
5. [Download](https://git-scm.com/downloads) and install a git command line client
6. Clone the plsql-formatter-settings repository
7. Open a terminal window in the plsql-formatter-settings root folder and type

		cd standalone

6. Run maven build by the following command

		mvn -Dsqlcl.libdir=/usr/local/bin/sqlcl/lib -Dskip.native=false clean package

	Amend the parameter `sqlcl.libdir` to match the path of the lib directory of you SQLcl installation. This folder is used to reference the `dbtools-common.jar` library (containing the formatter and its dependencies) which is not available in public Maven repositories. 
	
	When you specifiy the parameter `-Dskip.native=false` a native image for your platform is created. When you pass the value `true` (default) then no native image is produced.
