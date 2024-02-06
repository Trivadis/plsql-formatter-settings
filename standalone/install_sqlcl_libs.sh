#!/bin/sh

# set the directory where SQLcl libraries are stored
if [[ "$1" = "" ]]; then
    SQLCL_LIBDIR="/usr/local/bin/sqlcl/lib"
else
    SQLCL_LIBDIR="$1"
fi

# check SQLcl library directory
if ! test -f "${SQLCL_LIBDIR}/dbtools-common.jar"; then
    echo "Error: ${SQLCL_LIBDIR} is not a valid path to the lib directory of SQLcl."
    echo "       Please pass a valid path to this script."
    exit 1
fi

# define common Maven properties
SQLCL_VERSION="23.4.0"

# install JAR files in local Maven repository, these libs are not available in public Maven repositories
mvn install:install-file -Dfile=$SQLCL_LIBDIR/dbtools-common.jar -DgeneratePom=true \
        -DgroupId=oracle.dbtools -DartifactId=dbtools-common -Dversion=$SQLCL_VERSION -Dpackaging=jar
mvn install:install-file -Dfile=$SQLCL_LIBDIR/dbtools-sqlcl.jar -DgeneratePom=true \
        -DgroupId=oracle.dbtools -DartifactId=dbtools-sqlcl -Dversion=$SQLCL_VERSION -Dpackaging=jar
mvn install:install-file -Dfile=$SQLCL_LIBDIR/xmlparserv2_sans_jaxp_services.jar -DgeneratePom=true \
        -DgroupId=oracle.xml -DartifactId=xmlparserv2-sans-jaxp-services -Dversion=$SQLCL_VERSION -Dpackaging=jar
mvn install:install-file -Dfile=$SQLCL_LIBDIR/orai18n.jar -DgeneratePom=true \
        -DgroupId=oracle.i18n -DartifactId=orai18n -Dversion=$SQLCL_VERSION -Dpackaging=jar
mvn install:install-file -Dfile=$SQLCL_LIBDIR/orai18n-mapping.jar -DgeneratePom=true \
        -DgroupId=oracle.i18n -DartifactId=orai18n-mapping -Dversion=$SQLCL_VERSION -Dpackaging=jar
mvn install:install-file -Dfile=$SQLCL_LIBDIR/orajsoda.jar -DgeneratePom=true \
        -DgroupId=oracle.soda -DartifactId=orajsoda -Dversion=$SQLCL_VERSION -Dpackaging=jar
