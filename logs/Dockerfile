# Utiliser l'image officielle Fluent Bit
FROM fluent/fluent-bit:latest

# Définir le répertoire de travail pour copier les fichiers de configuration
WORKDIR /fluent-bit/etc

# Copier le fichier de configuration Fluent Bit local dans l'image Docker
COPY fluent-bit.conf /fluent-bit/etc/fluent-bit.conf

# Exposer les ports TCP et UDP sur lesquels Fluent Bit écoute
EXPOSE 24224/tcp
EXPOSE 24224/udp

# Commande par défaut pour démarrer Fluent Bit
CMD ["fluent-bit", "-c", "/fluent-bit/etc/fluent-bit.conf"]