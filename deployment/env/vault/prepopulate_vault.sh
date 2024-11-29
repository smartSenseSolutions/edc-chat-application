#
# Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
#

sleep 10

## Create secret engine
curl --header "X-Vault-Token: root-token" \
     --request POST \
     --data '{"type": "kv", "options": {"version": "2"}}' \
     http://vault:8200/v1/sys/mounts/edc


## Create secrets for provider
curl --location 'http://vault:8200/v1/edc/data/edc_provider' \
--header 'X-Vault-Token: root-token' \
--header 'Content-Type: application/json' \
--data '{
    "data": {"content":"UJC48hDEvjQNMgEgv6yKoFsRSNCl5XDU"}
}'

curl --location 'http://vault:8200/v1/edc/data/edc_stub' \
--header 'X-Vault-Token: root-token' \
--header 'Content-Type: application/json' \
--data '{
    "data": {"content":"kAzbFOnq9buQc6iHwjZq1MSrNM4LRoqr"}
}'


curl --location 'http://vault:8200/v1/edc/data/private' \
--header 'X-Vault-Token: root-token' \
--header 'Content-Type: application/json' \
--data '{
    "data": {
        "content": "-----BEGIN RSA PRIVATE KEY-----\nMIIEpAIBAAKCAQEA6+Cfc8GRmamLZ3Uqc/QU6m5CY+P3OE47B65J+AL4cVcXib7qnT62dQTtSJ5CAe+3300j8KWi+qrOnMiuAjxinXu8NDy3DD2v3o68kUzaW83RiaxRAhhaIq4V9t1EdXZYvOmZXxhc8nyXwzmdx2LMGmLwb4efnzA64Rd+cT3rpoimHbNx1gAm0YeaEa1x9/dW0FjI5NetNrMNbZhws6zo3oZBqVceoRwAByPZcCQM0iIfvJ3pjyZUqHGWojtKj68Rda0jW5y41GSFFyzv5hOeQj6upwIfrYROV9x3VSdy1g0CTbppAuc9DsUXMehYcyhjqxWKZvhhWZvSSzg7A+KHzQIDAQABAoIBAG4ZFJQ1X763sogB2mTxIuohuP9jIru3WDpHkeMfj9NncWcsI9ptsUCBojfpFqpMuCiUmxfyWFZkb7giu4XzpZwYa0p+pHokOPjK6x1vH+UF9q4qLk2qDDhvxXTK5u9k5BqZdqs6dfgBS90mAY1Vgis2zz9fsJPfMTxreOtmmZ3K+UcjYLtU2lRXU3UQ6eBrxHoJo2TjeLlG+DzjaoEpZB4acyeyboKJT7X9liYvYeA0FT88A22/0NUx3uhAtCQmMSg968dNN3dkjm1Ku3pL3pzgoanqUQiVYamIxXv7r+GJf4ABVb6u0ODugoT/ZVNnXiipSkhauj3sUOAZauJAE0kCgYEA90Y4I/crj9AyHSJKH0Nun+gcVU3aJofHo02kdlgbG457xsWTJPuEQ2lpu8V0sOvZIG9BuERc+ZtzspA6kfS/3Iuf5YKxb5+di8wBPjZwc76GBPTP4zN2fl/8I0yiRvpaY+hYrRvi9dTHcYtBHV5d+I304/Vrjow8VLVwhwL6LI8CgYEA9DNy3pxAjK9W/v+4fgcE91TYRAAtogatRbSI4TrZPmDw6U5FCUnOSD8D8Z2VXXnxlPl+pkXBP/YFo7avsQyps6FVw9ZmbxyKiUVWGbe+aDgssww6NksI7EfLUG2r/JkWcv/2WSrzE2SCWLZ81P4Wvrvrt+MSc1wONOQCOdbfK+MCgYEAu+H63jj8PHWw3jojn8WY1J+Eh7xzuTsKEab5A4LGdhW5LrsIpt0FDkOWNzo9BADwJZjjkqM/Edlv+4ljN2uELItanwTC1VGuX35Oo3qxzwBAzJtHKhb8PeGorxmXY97tt2PfnQjHVoqqUDfC7hG5zRXvsRXU96bOYH7MVYmJlTkCgYBm5N67yZ6xKJhBbcJw58hB5rARRvnzUVjlDAK41DEBRZt80ovoM/8FFfg1cC6lamJOYSnqKW4Z4zHRH4K72RgQJiSbFdWnb6E34c07nN5Pz4uWDcrhYZBq7n1OcEeNcXuyDQPh4mT1gN4jPxEuNbmjuE7D9pupm9ookwIVGOKD/QKBgQDmmb9AhZumbm9Vm8KbI1943U23DnPWonAoDywM//34CQWuJvGFp4cgkCuJFXDHFAJ8K7wtkSOIb0S13tn+a7Dicf+Gey0Li7jmPsvPotpRVybhJgcsKSQln2mcAU5k//Hw8FGMFLTqEhQ/MX9NAdllTo225GLvpDvl39N9qiYg+A==\n-----END RSA PRIVATE KEY-----"
    }
}'




curl --location 'http://vault:8200/v1/edc/data/public' \
--header 'X-Vault-Token: root-token' \
--header 'Content-Type: application/json' \
--data '{
    "data": {
        "content": "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6+Cfc8GRmamLZ3Uqc/QU\n6m5CY+P3OE47B65J+AL4cVcXib7qnT62dQTtSJ5CAe+3300j8KWi+qrOnMiuAjxi\nnXu8NDy3DD2v3o68kUzaW83RiaxRAhhaIq4V9t1EdXZYvOmZXxhc8nyXwzmdx2LM\nGmLwb4efnzA64Rd+cT3rpoimHbNx1gAm0YeaEa1x9/dW0FjI5NetNrMNbZhws6zo\n3oZBqVceoRwAByPZcCQM0iIfvJ3pjyZUqHGWojtKj68Rda0jW5y41GSFFyzv5hOe\nQj6upwIfrYROV9x3VSdy1g0CTbppAuc9DsUXMehYcyhjqxWKZvhhWZvSSzg7A+KH\nzQIDAQAB\n-----END PUBLIC KEY-----"
    }
}'
