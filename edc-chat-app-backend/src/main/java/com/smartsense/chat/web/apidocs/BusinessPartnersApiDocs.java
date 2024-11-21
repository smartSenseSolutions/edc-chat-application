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
                                        "name": "bhautik",
                                        "bpn": "BPNL000000000001",
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
                                    {
                                        "response": {
                                            "bhautik": "BPNL000000000001",
                                            "dilip": "BPNL000000000002"
                                        }
                                    }
                                    """)
                    }) }) })
    public @interface GetBusinessPartners {
    }
}
