#!/bin/bash

# Vérifier si le token est passé en argument
if [ -z "$1" ]; then
  echo "Usage: $0 <vault_token>"
  exit 1
fi

# Récupérer et exporter le token
export VAULT_TOKEN=$1

# Définir l'adresse de Vault (ici en HTTP, à adapter selon ta configuration)
export VAULT_ADDR="http://127.0.0.1:8200"

# Vérification de la connexion à Vault
echo "Vérification de la connexion à Vault..."
vault status &> /dev/null
if [ $? -ne 0 ]; then
  echo "Échec de la connexion à Vault à l'adresse $VAULT_ADDR"
  exit 1
fi

echo "Connexion réussie à Vault."

# Créer les secrets sous le chemin 'billing-service'
echo "Création des secrets dans Vault..."
vault kv put secret/billing-service user.username="example_user" user.password="example_password" user.opt="example_opt_value"

# Confirmation
echo "Les secrets ont été créés sous le chemin 'secret/billing-service'."
