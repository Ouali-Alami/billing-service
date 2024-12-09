#!/bin/bash

# Démarrer l'agent Consul

# Valeurs extraites de la classe Java MyConsulConfig
ACCESS_TOKEN_TIMEOUT=3600  # Exemple : 3600 secondes = 1 heure
REFRESH_TOKEN_TIMEOUT=7200  # Exemple : 7200 secondes = 2 heures


# Mettre à jour les KV sous le dossier token dans Consul
echo "Création des KV dans Consul sous 'token/'..."

# Utilisation de la commande consul kv pour ajouter les valeurs
consul kv put config/billing-service/token.accessTokenTimeout $ACCESS_TOKEN_TIMEOUT
consul kv put config/billing-service/token.refreshTokenTimeout $REFRESH_TOKEN_TIMEOUT

# Confirmation
echo "Les secrets ont été créés sous le chemin 'token/' dans Consul."
