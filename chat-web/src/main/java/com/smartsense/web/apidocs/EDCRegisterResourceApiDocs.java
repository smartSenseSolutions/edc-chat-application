package com.smartsense.web.apidocs;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class EDCRegisterResourceApiDocs {

    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Issue while creating the EDC", content = {
                    @Content(examples = {
                            @ExampleObject(name = "EDC url already exist", value = """
                                    {
                                        "type": "about:blank",
                                        "title": "BadDataException: validate.edc.create.url.exist",
                                        "status": 400,
                                        "detail": "BadDataException: validate.edc.create.url.exist",
                                        "instance": "/api/app/common/edc/create",
                                        "properties": {
                                            "timestamp": 1732008491139
                                        }
                                    }
                                    """),
                            @ExampleObject(name = "User not exist", value = """
                                    {
                                        "type": "about:blank",
                                        "title": "BadDataException: User not present for given userId: 3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        "status": 400,
                                        "detail": "BadDataException: User not present for given userId: 3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        "instance": "/api/app/common/edc/create",
                                        "properties": {
                                            "timestamp": 1732012098646
                                        }
                                    }
                                    """)
                    }) }),
            @ApiResponse(responseCode = "200", description = "EDC Registered", content = {
                    @Content(examples = {
                            @ExampleObject(name = "EDC Registered", value = """
                                    {
                                        "message": "User Created successfully",
                                        "body": {
                                            "id": "9c745cb5-a5cd-41be-acbf-e7f739b8bfd7",
                                            "firstName": "Bhautik",
                                            "lastName": "Sakhiya",
                                            "email": "bhautik@gmail.com"
                                        }
                                    }
                                    """)
                    })
            }) })
    public @interface RegisterEDC {
    }

    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "EDC Registered with USer", content = {
                    @Content(examples = {
                            @ExampleObject(name = "EDC Registered with User", value = """
                                    {
                                        "userId":"9c745cb5-a5cd-41be-acbf-e7f739b8bfd7",
                                        "edcUrl": [
                                            "http://localhost:8080/api/app/ui/swagger-ui/index.html#/edc-register-resource/createEDC",
                                            "http://localhost:8080/api/app/ui/swagger-ui/index.html#/edc-register-resource/createEDC1",
                                            "http://localhost:8080/api/app/ui/swagger-ui/index.html#/edc-register-resource/createEDC2",
                                            "http://localhost:8080/api/app/ui/swagger-ui/index.html#/edc-register-resource/createEDC3",
                                            "http://localhost:8080/api/app/ui/swagger-ui/index.html#/edc-register-resource/createEDC4",
                                            "http://localhost:8080/api/app/ui/swagger-ui/index.html#/edc-register-resource/createEDC5",
                                            "http://localhost:8080/api/app/ui/swagger-ui/index.html#/edc-register-resource/createEDC6"
                                        ]
                                    }
                                    """),
                            @ExampleObject(name = "No EDC register with USer", value = """
                                    {
                                        "userId": "9c745cb5-a5cd-41be-acbf-e7f739b8bfd3",
                                        "edcUrl": []
                                    }
                                    """)
                    })
            }) })
    public @interface GetEDCByUserId {
    }

    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "EDC Details not found", content = {
                    @Content(examples = {
                            @ExampleObject(name = "EDC Details not found", value = """
                                    {
                                        "type": "about:blank",
                                        "title": "BadDataException: No EDC registered with EDC Id: 3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        "status": 400,
                                        "detail": "BadDataException: No EDC registered with EDC Id: 3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        "instance": "/api/app/common/edc/3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        "properties": {
                                            "timestamp": 1732012976729
                                        }
                                    }
                                    """)
                    }) }),
            @ApiResponse(responseCode = "200", description = "Get EDC Details", content = {
                    @Content(examples = {
                            @ExampleObject(name = "Get EDC Details", value = """
                                    {
                                        "id": "cd0d266c-6f42-4b37-b5ca-6ff14e8c1c06",
                                        "edcUrl": "http://localhost:8080/api/app/ui/swagger-ui/index.html#/edc-register-resource/createUser_1",
                                        "userId": {
                                            "id": "9c745cb5-a5cd-41be-acbf-e7f739b8bfd7",
                                            "firstName": "Bhautik",
                                            "lastName": "Sakhiya",
                                            "email": "bhautik@gmail.com"
                                        }
                                    }
                                    """)
                    })
            }) })
    public @interface GetEDCById {
    }

    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Not able to Update the EDC", content = {
                    @Content(examples = {
                            @ExampleObject(name = "Invalid User", value = """
                                    {
                                        "type": "about:blank",
                                        "title": "BadDataException: User not present for given userId: 3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        "status": 400,
                                        "detail": "BadDataException: User not present for given userId: 3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        "instance": "/api/app/common/edc/update",
                                        "properties": {
                                            "timestamp": 1732013440419
                                        }
                                    }
                                    """),
                            @ExampleObject(name = "EDC not registered with given EDC Id", value = """
                                    {
                                        "type":"about:blank",
                                        "title":"BadDataException: No EDC registered with EDC Id: 3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        "status":400,
                                        "detail":"BadDataException: No EDC registered with EDC Id: 3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        "instance":"/api/app/common/edc/update",
                                        "properties":{
                                            timestamp":1732013532531
                                        }
                                    }"""
                            )
                    }) }),
            @ApiResponse(responseCode = "200", description = "EDC Updated", content = {
                    @Content(examples = {
                            @ExampleObject(name = "EDC Updated", value = """
                                    {
                                        "message": "EDC Created successfully",
                                        "body": {
                                            "id": "f949a7a9-9423-498c-99cc-543697398df0",
                                            "edcUrl": "google.com",
                                            "userId": {
                                                "id": "9c745cb5-a5cd-41be-acbf-e7f739b8bfd7",
                                                "firstName": "Bhautik",
                                                "lastName": "Sakhiya",
                                                "email": "bhautik@gmail.com"
                                            }
                                        }
                                    }    
                                    """)
                    })
            }) })
    public @interface UpdateEDC {

    }
}
