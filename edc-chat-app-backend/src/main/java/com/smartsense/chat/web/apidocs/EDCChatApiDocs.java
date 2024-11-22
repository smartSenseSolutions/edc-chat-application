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

public class EDCChatApiDocs {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(description = "Receive Chat message", summary = "Receive Chat message.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receive Chat message", content = {
                    @Content(examples = {
                            @ExampleObject(name = "Business Partner created", value = """
                                     {
                                        "message": "User Created successfully",
                                        "body": {
                                          "id": "248b97f0-6f3a-4f56-af04-c0da600125b1",
                                          "name": "Name Surname",
                                          "age": 19,
                                          "city": "Some City",
                                          "country": "Some Country"
                                        }
                                      }
                                    """)
                    })
            }) })
    public @interface EDCChatReceive {
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(description = "Sent Chat message", summary = "Sent Chat message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sent Chat message", content = {
                    @Content(examples = {
                            @ExampleObject(name = "Business Partner created", value = """
                                     {
                                        "message": "User Created successfully",
                                        "body": {
                                          "id": "248b97f0-6f3a-4f56-af04-c0da600125b1",
                                          "name": "Name Surname",
                                          "age": 19,
                                          "city": "Some City",
                                          "country": "Some Country"
                                        }
                                      }
                                    """)
                    })
            }) })
    public @interface EDCChatSent {
    }
}
