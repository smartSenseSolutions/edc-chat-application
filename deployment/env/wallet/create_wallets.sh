#!/bin/bash

# Check if BPN_LIST is set
if [[ -z "$BPN_LIST" ]]; then
  echo "Error: BPN_LIST environment variable is not set."
  exit 1
fi

# Comma-separated string of BPNs
bpn_string="$BPN_LIST"

# Base URL of the API
base_url="http://wallet-stub"

# Convert the string into space-separated values and loop through them
for bpn in $(echo "$bpn_string" | tr ',' ' '); do
  echo "Calling API for ${bpn}..."
  response=$(curl -s "${base_url}/${bpn}/did.json")
  echo "Response for ${bpn}:"
  echo "$response"
  echo "----------------------"
done
