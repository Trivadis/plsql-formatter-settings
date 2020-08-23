# Formatting Code with SQLcl

## Introduction

You can apply the formatter settings in SQL Developer. Another option is to apply them with SQLcl using the [`format.js`](format.js) script or the custom SQLcl command `tvdformat`.

## Why not Use `FORMAT FILE`?

Yes, you can use the built-in `FORMAT FILE` command. Here's the help output from SQLcl 20.2.0:

```
FORMAT
---------

FORMAT BUFFER - formats the script in the SQLcl Buffer
FORMAT RULES <filename> - Loads SQLDeveloper Formatter rules file to formatter.
FORMAT FILE <input_file> <output_file> 

Format used is default or for SQLcl can be chosen by setting an environmental variable
pointing to a SQLDeveloper export (.xml) of formatter options.
The variable is called SQLFORMATPATH
In SQLDeveloper the format options are the default chosen in the preferences.
```

You can use a directory for `<input_file>` and `output_file`. It can be even the same. Only files that are parsed without errors are formatted. This is good. However, you cannot configure an `.arbori` file as input. `FORMAT RULES` can only load the `.xml` file. 

So, if you want to have full control over the formatting result you have to use the script or the custom command.

## Using `format.js`

We recommend to download, clone or fork this repository when you plan to use [`format.js`](format.js). It’s easier because the SQL Developer settings - [`trivadis_advanced_format.xml`](../settings/sql_developer/trivadis_advanced_format.xml) and [`trivadis_custom_format.arbori`](../settings/sql_developer/trivadis_custom_format.arbori) are used by default. Therefore you do not need to pass them as command line arguments.

However, [`format.js`](format.js) works also as a standalone script. Here's the usage:

```
usage: script format.js <rootPath> [options]

mandatory arguments:
  <rootPath>      path to directory containing files to format (content will be replaced!)

options:
  --register, -r  register SQLcl command tvdformat, without processing, no <rootPath> required
  ext=<ext>       comma separated list of file extensions to process, e.g. ext=sql,pks,pkb
  xml=<file>      path to the file containing the xml file for advanced format settings
                  xml=default uses default advanced settings included in sqlcl
                  xml=embedded uses advanced settings defined in format.js
  arbori=<file>   path to the file containing the Arbori program for custom format settings
                  arbori=default uses default Arbori program included in sqlcl
```

## Register Script `format.js` as SQLcl Command `tvdformat`

Add the following to your login.sql to permanently add `tvdformat` as a SQLcl command:

```
script (...)/plsql-formatter-settings/sqlcl/format.js --register
```

Afterwards you can type `tvdformat` to get this usage help:

```
usage: tvdformat <rootPath> [options]

mandatory arguments:
  <rootPath>      path to directory containing files to format (content will be replaced!)

options:
  ext=<ext>       comma separated list of file extensions to process, e.g. ext=sql,pks,pkb
  xml=<file>      path to the file containing the xml file for advanced format settings
                  xml=default uses default advanced settings included in sqlcl
                  xml=embedded uses advanced settings defined in format.js
  arbori=<file>   path to the file containing the Arbori program for custom format settings
                  arbori=default uses default Arbori program included in sqlcl
```

It's very similar to `script format.js`. The advantage is, that you do not need to know where [`format.js`](format.js) is stored. You may pass relative paths for `rootPath` and `file`. The SQLcl `CD` command is honored.
