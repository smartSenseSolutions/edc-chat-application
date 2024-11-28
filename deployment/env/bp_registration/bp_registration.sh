#
# Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
#

sleep 10

echo "Initiate the BP Registration..."

## Create partner data in smartSense
curl --location 'http://smartsense-backend:8080/partners' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Catena-X",
    "edcUrl": "http://catena-x-controlplane:9195/api/v1/dsp",
    "bpn": "BPNL00000CATENAX"
}'

## Create partner data in catena-x
curl --location 'http://catena-x-backend:8080/partners' \
--header 'Content-Type: application/json' \
--data '{
    "name": "smartSense",
    "edcUrl": "http://smartsense-controlplane:8195/api/v1/dsp",
      "bpn": "BPNL00SMARTSENSE"
}'

echo "Completed the BP Registration..."
