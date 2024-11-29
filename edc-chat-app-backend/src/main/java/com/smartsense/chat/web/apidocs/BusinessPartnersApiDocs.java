/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.web.apidocs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class BusinessPartnersApiDocs {


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(description = "Register new Business Partner", summary = "Register new Business Partner by providing bpn and edcUrl.")
    @RequestBody(content = {
            @Content(examples = {
                    @ExampleObject(value = """
                                   {
                                        "name": "smartSense",
                                        "bpn": "BPNL00SMARTSENSE",
                                        "edcUrl": "http://localhost:8090"
                                    }
                            """, description = "Register new business partner", name = "Register new business partner")
            })
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Business Partner created", content = {
                    @Content(examples = {
                            @ExampleObject(name = "Business Partner created", value = """
                                    {
                                        "id": "89221248-4c98-4d8e-a909-d31f3ea0fa8f",
                                        "name": "smartSense",
                                        "bpn": "BPNL00SMARTSENSE",
                                        "edcUrl": "http://localhost:8090"
                                    }
                                    """)
                    })
            }),
            @ApiResponse(responseCode = "409", description = "Duplicate BPN", content = {
                    @Content(examples = {
                            @ExampleObject(name = "Duplicate BPN", value = """
                                    {
                                       "type": "about:blank",
                                       "title": "BusinessPartner with BPN 'string' already exists.",
                                       "status": 409,
                                       "detail": "BusinessPartner with BPN 'string' already exists.",
                                       "instance": "/partners",
                                       "timestamp": 1732267124105
                                     }
                                    """)
                    })
            })})
    public @interface CreateBusinessPartner {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(description = "Get Business Partner", summary = "Get Business Partner details by name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Business Partner details by name.", content = {
                    @Content(examples = {
                            @ExampleObject(name = "Get Business Partner details", value = """
                                    [
                                        {
                                          "bpn": "BPNL00SMARTSENSE",
                                          "name": "smartSense"
                                        }
                                      ]
                                    """)
                    })})})
    public @interface GetBusinessPartners {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(description = "Get EDC configuration", summary = "Get EDC configuration.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get EDC configuration.", content = {
                    @Content(examples = {
                            @ExampleObject(name = "Get EDC configuration", value = """
                                    {
                                         "bpn": "BPNL000000000001",
                                         "assetUrl": "http://localhost:9192",
                                         "edc": {
                                           "edcUrl": "http://localhost:9192",
                                           "authCode": "password",
                                           "assetId": "edc-chat-app"
                                         }
                                       }
                                    """)
                    })})})
    public @interface GetConfiguration {
    }
}
