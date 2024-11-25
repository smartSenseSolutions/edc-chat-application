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


    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(description = "Send Chat message to selected partner", summary = "Send Chat message to selected partner")
    @RequestBody(content = {
            @Content(examples = {
                    @ExampleObject(value = """
                                                        {
                                                            "message": "Hi there! How are you?",
                                                            "receiverBpn": "BPNL000000000TATA"
                                                        }
                            """, description = "Send message to selected partner", name = "send message to selected partner")
            })
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat response", content = {
                    @Content(examples = {
                            @ExampleObject(name = "Chat Response", value = """
                                    {
                                              "receiver": "BPNL000000000001",
                                              "sender": "BPNL000000000TATA",
                                              "content": "Hello!",
                                              "timestamp": 1700654400
                                          }
                                    """)
                    }) }) })
    public @interface Chat {
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
                                            "id": 1,
                                            "receiver": "BPNL000000000012",
                                            "sender": "BPNL000000000001",
                                            "content": "Hello!",
                                            "status": "sent",
                                            "timestamp": 1732514671370
                                        },
                                        {
                                            "id": 2,
                                            "receiver": "BPNL000000000001",
                                            "sender": "BPNL000000000012",
                                            "content": "Hello! How can I help you?",
                                            "status": "sent",
                                            "timestamp": 1732514675065
                                        }
                                    ]
                                    """)
                    }) }) })
    public @interface GetChatHistory {
    }
}
