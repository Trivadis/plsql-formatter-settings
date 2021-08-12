# Formatting Code with SQLcl

## Introduction

You can apply the formatter settings in SQL Developer. Another option is to apply them with SQLcl using the [`format.js`](format.js) script or the custom SQLcl command `tvdformat`.

## Why not Use `FORMAT FILE`?

Yes, you can use the built-in `FORMAT FILE` command. Here's the help output from SQLcl 21.2.2:

```
FORMAT
---------

FORMAT BUFFER - formats the script in the SQLcl Buffer
FORMAT RULES <filename> - Loads formatter preferences file from SQL Developer export.
FORMAT FILE <input_file> <output_file>
```

You can use a directory for `<input_file>` and `output_file`. It can be even the same. Only files that are parsed without errors are formatted. This is good. However, you cannot configure an `.arbori` file as input. `FORMAT RULES` can only load the `.xml` file. 

So, if you want to have full control over the formatting result you have to use the script or the custom command, especially if you want to format SQL code blocks within Markdown files.

## Using `format.js`

We recommend you download, clone, or fork this repository when you plan to use [`format.js`](format.js). It’s easier because the SQL Developer settings [`trivadis_advanced_format.xml`](../settings/sql_developer/trivadis_advanced_format.xml) and [`trivadis_custom_format.arbori`](../settings/sql_developer/trivadis_custom_format.arbori) are used by default. Therefore you do not need to pass them as command line arguments.

However, [`format.js`](format.js) also works as a standalone script. Here's the usage:

```
usage: script format.js <rootPath> [options]

mandatory argument (one of the following):
  <rootPath>      file or path to directory containing files to format (content will be replaced!)
  <config.json>   configuration file in JSON format (must end with .json)
  *               use * to format the SQLcl buffer

options:
  --register, -r  register SQLcl command tvdformat, without processing, no <rootPath> required
  ext=<ext>       comma separated list of file extensions to process, e.g. ext=sql,pks,pkb
  mext=<ext>      comma separated list of markdown file extensions to process, e.g. ext=md,mdown
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

mandatory argument (one of the following):
  <rootPath>      file or path to directory containing files to format (content will be replaced!)
  <config.json>   configuration file in JSON format (must end with .json)
  *               use * to format the SQLcl buffer

options:
  ext=<ext>       comma separated list of file extensions to process, e.g. ext=sql,pks,pkb
  mext=<ext>      comma separated list of markdown file extensions to process, e.g. ext=md,mdown
  xml=<file>      path to the file containing the xml file for advanced format settings
                  xml=default uses default advanced settings included in sqlcl
                  xml=embedded uses advanced settings defined in format.js
  arbori=<file>   path to the file containing the Arbori program for custom format settings
                  arbori=default uses default Arbori program included in sqlcl
```

It's very similar to `script format.js`. The advantage is, that you do not need to know where [`format.js`](format.js) is stored. You may pass relative paths for `rootPath` and `file`. The SQLcl `CD` command is honored.

## Using a configuration file

In addition to `rootPath` and `*`, `format.js` can accept a JSON configuration file as the mandatory argument. Here's an example of using a configuration file:

```
script format.js config.json
```

While the name of the file doesn't matter, it must end with .json. If the content is a JSON array, then the array should contain a list of files to be formatted. If the file is an object, then the object should contain a `files` array attribute with a list of files to be formatted.

If using the object format, additional properties with names that match any of the options (e.g. `xml`, `arbori`, etc.) may be provided as an alternative means of providing option values. If an option value is provided in the configuration file and as an argument, then the argument will override the value in the file.

Here's an example configuration file:

```
{
  "arbori": "path/to/arbori",
  "files": [
    "relative/path/to/file1.sql",
    "/full/path/to/file2.sql"
  ]
}
```