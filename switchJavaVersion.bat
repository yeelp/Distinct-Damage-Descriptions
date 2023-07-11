@echo off

echo This will make sure the Java version is set to Java 8. To use this script, have an environment variable called JAVA_HOME_8 that points to your Java 8 installation. This script will reference that variable.
if NOT defined JAVA_HOME_8 ( 
echo JAVA_HOME_8 environment variable not set! 
) else if NOT exist "%JAVA_HOME_8%"/ (
echo JAVA_HOME_8 defined, but does not point to an existing directory!
) else if NOT "%JAVA_HOME%" == "%JAVA_HOME_8%" (
setx JAVA_HOME "%JAVA_HOME_8%" /M
echo JAVA_HOME set to Java 8 directory: "%JAVA_HOME%"
) else (
echo JAVA_HOME already set to Java 8 directory.
)