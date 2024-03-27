@echo off
SETLOCAL EnableDelayedExpansion

:: Chemin vers le dossier de votre projet backend
cd C:\Users\bbseb\Documents\programmation\ashs\backend

:: Etape 1: Commit et push sur GitHub
cd config-repository
echo Commit et push des modifications sur GitHub...
git add .
git commit -m "Mise à jour des services"
git push
cd ..
echo %cd%

:: Etape 2: Build des services avec Gradle
echo Construction des services Spring Boot avec Gradle...
FOR /D %%d IN (*) DO (
    IF EXIST %%d\build.gradle (
        cd %%d
        echo Building %%d...
        call .\gradlew build
        cd ..
    )
)


:: Etape 3: Build et lancement des containers avec Docker Compose
echo Construction et lancement des containers Docker...
docker-compose down
docker-compose up --build -d

echo Processus termine!
pause
ENDLOCAL