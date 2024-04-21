@echo off
SETLOCAL EnableDelayedExpansion

:: Change la page de code pour supporter les accents
chcp 1252 > nul

:: Chemin vers le dossier de votre projet backend
cd C:\Users\bbseb\Documents\programmation\ashs\backend

echo Bienvenue au script de gestion du projet backend!

:: Etape 1: Commit et push sur GitHub
echo Souhaitez-vous commit et push les modifications sur GitHub ? (y/n)
set /p doGit="R�ponse: "
if /I "!doGit!"=="y" (
    cd config-repository
    echo [1/3] Commit et push des modifications sur GitHub...
    git add .
    git commit -m "Mise � jour des services"
    git push
    cd ..
    echo - Les modifications ont �t� pouss�es sur GitHub.
) else (
    echo - �tape ignor�e.
)

echo %cd%

:: Etape 2: Build des services avec Gradle
echo Souhaitez-vous construire les services Spring Boot avec Gradle et construire les conteneur docker correspondant ? (y/n)
set /p doBuild="R�ponse: "
if /I "!doBuild!"=="y" (
    echo [2/3] Construction des services Spring Boot avec Gradle...
    for /f "delims=" %%a in (.env) do set %%a
    FOR /D %%d IN (*) DO (
        IF EXIST %%d\build.gradle (
            cd %%d
            echo - Building %%d...

            call .\gradlew build
            echo - %%d construit avec succ�s.


            :: Build Docker image with the specified version and also tag it as latest
            echo Building Docker image for %%d with version latest
            docker build --no-cache -t bbsebb/backend-%%d:latest .

            :: Push both versioned and latest tags to Docker Hub
            echo Pushing latest Docker images for %%d to Docker Hub...
            docker push bbsebb/backend-%%d:latest

            cd ..
        )
    )
) else (
    echo - �tape ignor�e.
)

:: Etape 3: Build et lancement des containers avec Docker Compose
echo Souhaitez-vous lancer les containers Docker ? (y/n)
set /p doDocker="R�ponse: "
if /I "!doDocker!"=="y" (
    for /f "delims=" %%a in (.env) do set %%a
    echo [3/4] Construction et lancement des containers Docker...
    cd logs
    echo  Lancement de Fluent Bit...
    docker-compose up -d
    cd ..
    docker-compose down
    docker-compose pull
    docker-compose up -d
    echo - Containers Docker lanc�s.
) else (
    echo - �tape ignor�e.
)
:: �tape 4: Nettoyage des ressources Docker
echo Souhaitez-vous nettoyer les ressources Docker utilis�es ? (y/n)
set /p doClean="R�ponse: "
if /I "!doClean!"=="y" (
    echo [4/4] Nettoyage des ressources Docker...
    cd logs
    echo  Fermeture de Fluent Bit...
    docker-compose down
    cd ..
    docker-compose down
    ::docker-compose down --rmi all --volumes
    ::docker image prune -f
    echo - Ressources Docker nettoy�es.
) else (
    echo - Nettoyage ignor�.
)

echo Processus termin�!

ENDLOCAL