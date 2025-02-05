@echo off
CLS

:: 환경 변수 설정
SET JDK=jdk-1.8
SET JAVA_HOME=%CD%\%JDK%
SET "OLD_PATH=%PATH%"
SET PATH=%JAVA_HOME%\bin;%OLD_PATH%

ECHO "Using JDK from: %JAVA_HOME%"
java -version
javac -version

ECHO "[LABS SCRIPT ---------------------]"
ECHO "0. Cleaning previous build files..."

:: 기존 빌드 폴더 및 파일 삭제
IF EXIST "bin" RD /S /Q "bin"
IF EXIST "doc" RD /S /Q "doc"
IF EXIST "labs-javac.err" DEL "labs-javac.err"
IF EXIST "labs-jar.out" DEL "labs-jar.out"
IF EXIST "labs-jar.err" DEL "labs-jar.err"
IF EXIST "labs-javadoc.err" DEL "labs-javadoc.err"

ECHO "1. Creating necessary directories..."
mkdir "bin\main\java"
mkdir "doc"

ECHO "2. Compiling Java source files..."
FOR /R "src/main/java" %%F IN (*.java) DO (
    ECHO Compiling: %%F
    javac -cp "%JAVA_HOME%/jre/lib/ext/jfxrt.jar;src/main/java" -d "bin/main/java" "%%F" 2>> "labs-javac.err"
)

ECHO "3. Creating META-INF/MANIFEST.MF inside resources..."
mkdir "src/main/resources/META-INF"
ECHO Main-Class: app.Generator > "src/main/resources/META-INF/MANIFEST.MF"

ECHO "4. Copying Resources..."
xcopy /E /I /Y "src/main/resources" "bin/resources"

ECHO "5. Creating Jar ..."
cd bin
jar cvfe "Crazy-Eights.jar" "app.Generator" -C "main/java" . -C "resources" . > ../labs-jar.out 2> ../labs-jar.err
cd ..

ECHO "6. Creating Javadoc..."
javadoc -d "doc" -sourcepath "src/main/java" -subpackages "app" 2> "labs-javadoc.err"

ECHO "7. Running Jar..."
cd bin
java -jar "Crazy-Eights.jar"
cd ..

:: 원래 PATH 복구
SET PATH=%OLD_PATH%

ECHO "JAR execution finished."
ECHO "[END OF SCRIPT -------------------]"
@echo on
pause
