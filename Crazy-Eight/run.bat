@echo off
cls
SETLOCAL ENABLEDELAYEDEXPANSION

REM === Set environment variables using relative paths ===
SET "JAVA_FX_PATH=javafx-sdk-21.0.2\lib"
SET "MODULES=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web"
SET "BIN_DIR=bin\classes"

ECHO Java version:
java -version
ECHO Javac version:
javac -version

ECHO "[LABS SCRIPT ---------------------]"
ECHO "0. Cleaning previous build files..."
IF EXIST "bin" RD /S /Q "bin"
IF EXIST "doc" RD /S /Q "doc"
IF EXIST "*.err" DEL /F /Q "*.err"
IF EXIST "*.out" DEL /F /Q "*.out"
IF EXIST "*.txt" DEL /F /Q "*.txt"

ECHO "1. Creating new directories..."
mkdir "%BIN_DIR%"

REM === Generate list of Java source files ===
ECHO "2. Generating Java source file list..."
(
    for /R "src\main\java" %%F in (*.java) do (
        echo %%F
    )
) > files_to_compile.txt
echo "[files_to_compile.txt] has been created."

REM === Compile Java source files ===
ECHO "3. Compiling source code..."
ECHO. > "labs-javac.err"
javac -encoding UTF-8 ^
      --module-path "%JAVA_FX_PATH%" ^
      --add-modules %MODULES% ^
      -d "%BIN_DIR%" @files_to_compile.txt 2>> "labs-javac.err"
IF NOT EXIST "%BIN_DIR%\app\Generator.class" (
    echo "Compilation failed: Please check labs-javac.err."
    type labs-jar.err
    GOTO END_BATCH
) ELSE (
    echo "Compilation succeeded!"
)

REM === Copy resources to the class directory ===
ECHO "4. Copying resources..."
xcopy /E /I /Y "src\main\resources\*" "%BIN_DIR%\" >NUL 2>&1
IF ERRORLEVEL 1 (
    echo "Resource copying failed."
) ELSE (
    echo "Resource copying succeeded!"
)

REM === Create MANIFEST and generate the JAR file ===
ECHO "5. Creating JAR file..."
REM MANIFEST file must include a blank line at the end.
(
    echo Main-Class: app.Generator
    echo.
) > "%BIN_DIR%\MANIFEST.MF"
jar cvfe Crazy-Eights.jar app.Generator -C "%BIN_DIR%" . > labs-jar.out 2> labs-jar.err
IF NOT EXIST Crazy-Eights.jar (
    echo "JAR creation failed: Please check labs-jar.err."
    type labs-jar.err
    GOTO END_BATCH
) ELSE (
    echo "JAR creation succeeded!"
)

REM === Generate Javadoc ===
ECHO "6. Generating Javadoc..."
javadoc --module-path "%JAVA_FX_PATH%" --add-modules %MODULES% -d "doc" -sourcepath "src\main\java" -subpackages "app" 2> "labs-javadoc.err"
IF ERRORLEVEL 1 (
    echo "Javadoc generation failed: Please check labs-javadoc.err."
    type labs-javadoc.err
) ELSE (
    echo "Javadoc generation succeeded!"
)

REM === Run the JAR file ===
ECHO "7. Running JAR file..."
java -Dprism.d3d=false -Dfile.encoding=UTF-8 ^
     --module-path "%JAVA_FX_PATH%" ^
     --add-modules %MODULES% ^
     -jar Crazy-Eights.jar
IF ERRORLEVEL 1 (
    echo "JAR execution failed. Please check if app.Generator is correctly configured."
    pause
    GOTO END_BATCH
) ELSE (
    echo "JAR executed successfully!"
)

:END_BATCH
ECHO "✅ Process completed successfully."
pause
ENDLOCAL
