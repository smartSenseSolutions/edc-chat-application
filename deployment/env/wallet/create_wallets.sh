#!/bin/bash

# Comma-separated string of BPNs
bpn_string=$BPN_LIST

# Convert the string into an array
IFS=',' read -r -a bpns <<< "$bpn_string"

# Base URL of the API
base_url="http://wallet-stub"

# Loop through each BPN and call the API
for bpn in "${bpns[@]}"; do
  echo "Calling API for ${bpn}..."
  response=$(curl -s "${base_url}/${bpn}/did.json")
  echo "Response for ${bpn}:"
  echo "$response"
  echo "----------------------"
done