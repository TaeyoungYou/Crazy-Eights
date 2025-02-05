@echo off
CLS

ECHO "Cleaning build directories and log files..."

:: 삭제할 디렉토리 목록
SET BINDIR=bin
SET DOCDIR=doc

:: 삭제할 로그 파일 목록
SET BINERR=labs-javac.err
SET JAROUT=labs-jar.out
SET JARERR=labs-jar.err
SET DOCERR=labs-javadoc.err

:: bin 디렉토리 삭제
IF EXIST "%BINDIR%" (
    RD /S /Q "%BINDIR%"
    ECHO "Deleted: %BINDIR%"
) ELSE (
    ECHO "Directory not found: %BINDIR%"
)

:: doc 디렉토리 삭제
IF EXIST "%DOCDIR%" (
    RD /S /Q "%DOCDIR%"
    ECHO "Deleted: %DOCDIR%"
) ELSE (
    ECHO "Directory not found: %DOCDIR%"
)

:: 로그 파일 삭제
FOR %%F IN (%BINERR% %JAROUT% %JARERR% %DOCERR%) DO (
    IF EXIST "%%F" (
        DEL "%%F"
        ECHO "Deleted: %%F"
    ) ELSE (
        ECHO "File not found: %%F"
    )
)

ECHO "Cleanup completed."
