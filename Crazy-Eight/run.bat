@echo off
CLS
setlocal EnableDelayedExpansion

REM JavaFX 관련 환경 변수 설정
SET MODULE_PATH=javafx-sdk-21.0.2\lib
SET MODULES=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web
SET OUT_DIR=bin\main\java

java -version
javac -version

ECHO "[LABS SCRIPT ---------------------]"
ECHO "0. Cleaning previous build files..."

IF EXIST "bin" RD /S /Q "bin"
IF EXIST "doc" RD /S /Q "doc"
IF EXIST "labs-javac.err" DEL "labs-javac.err"
IF EXIST "labs-jar.out" DEL "labs-jar.out"
IF EXIST "labs-jar.err" DEL "labs-jar.err"
IF EXIST "labs-javadoc.err" DEL "labs-javadoc.err"

ECHO "1. Creating necessary directories..."
mkdir "bin\main\java"
mkdir "doc"

ECHO "2. Accumulating Java source files..."
set "FILES="
for /R "src\main\java\app" %%F in (*.java) do (
    set "FILES=!FILES! %%F"
)
echo Files to compile: !FILES!

ECHO "3. Compiling Java source files..."
javac --module-path "%MODULE_PATH%" ^
      --add-modules %MODULES% ^
      -encoding UTF-8 ^
      -sourcepath "src\main\java" ^
      -d "%OUT_DIR%" !FILES! 2>> "labs-javac.err"

ECHO "4. Creating META-INF/MANIFEST.MF..."
mkdir "bin\META-INF"
ECHO Main-Class: app.Generator > "bin\META-INF\MANIFEST.MF"

ECHO "5. Copying Resources..."
xcopy /E /I /Y "src\main\resources" "bin\resources"

ECHO "6. Creating JAR..."
cd bin
jar cvfe "Crazy-Eights.jar" "app.Generator" -C "main\java" . -C "resources" . > ../labs-jar.out 2> ../labs-jar.err
cd ..

ECHO "6. Creating Javadoc..."
javadoc --module-path "%MODULE_PATH%" --add-modules %MODULES% -d "doc" -sourcepath "src/main/java" -subpackages "app" 2> "labs-javadoc.err"

ECHO "8. Running JAR..."
cd bin
java --module-path "..\%MODULE_PATH%" --add-modules %MODULES% -jar "Crazy-Eights.jar"
cd ..

ECHO "JAR execution finished."
ECHO "[END OF SCRIPT -------------------]"
endlocal
pause
