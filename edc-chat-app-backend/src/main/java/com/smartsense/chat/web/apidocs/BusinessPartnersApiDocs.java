package com.smartsense.chat.web.apidocs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class BusinessPartnersApiDocs {


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(description = "Create Business Partner", summary = "Create Business Partner by providing bpn and edcUrl.")
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
            }) })
    public @interface CreateBusinessPartner {
    }

    @Target({ ElementType.TYPE, ElementType.METHOD })
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
                    }) }) })
    public @interface GetBusinessPartners {
    }

    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(description = "Get Chat history", summary = "Get Chat history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Chat history", content = {
                    @Content(examples = {
                            @ExampleObject(name = "Get Chat history", value = """
                                    [
                                         {
                                             "receiver": "BPNL000000000001",
                                             "sender": "BPNL000000000TATA",
                                             "content": "Hello!",
                                             "timestamp": 1700654400
                                         },
                                         {
                                             "receiver": "BPNL000000000TATA",
                                             "sender": "BPNL000000000001",
                                             "content": "Hello! How can I help you?",
                                             "timestamp": 1700654400
                                         }
                                     ]
                                    """)
                    }) }) })
    public @interface GetChatHistory {
    }
}
