package com.smartsense.web.apidocs;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ChatResourceApiDocs {

    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid Sender OR Receiver", content = {
                    @Content(examples = {
                            @ExampleObject(name = "Invalid Sender OR Receiver", value = """
                                    {
                                        "type": "about:blank",
                                        "title": "BadDataException: Sender or Receiver is not present",
                                        "status": 400,
                                        "detail": "BadDataException: Sender or Receiver is not present",
                                        "instance": "/api/app/common/chat",
                                        "properties": {
                                            "timestamp": 1732014474190
                                        }
                                    }
                                    """)
                    }) }),
            @ApiResponse(responseCode = "200", description = "Chat Details", content = {
                    @Content(examples = {
                            @ExampleObject(name = "Chat details persist", value = """
                                    {
                                        "message": "Chat saved successfully",
                                        "body": {
                                            "id": "673c70f3fa0440590274b497",
                                            "senderId": "9c745cb5-a5cd-41be-acbf-e7f739b8bfd7",
                                            "receiverId": "0184ad20-0015-4cc3-8f14-fc635193e8e5",
                                            "message": "Hii",
                                            "messageType": "text"
                                        }
                                    }
                                    """)
                    })
            }) })
    public @interface ChatCreated {
    }
}
