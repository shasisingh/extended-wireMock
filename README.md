# Extended WireMock

### Idea
idea behind this code base is...
* If you have existing server which you used for some other util purposes?
* You need to mock some api call/proxy?
* You can leverage ServletRegistrationBean to register you WireMockHandlerDispatchingServlet bean
* Mocking can be found and utilize under /stub/mapping.
* Not required to start standalone wiremock server
* Docker is not required.
* Same server can be used of multipurpose.

## Url exposed.

| method  | url path                    | description                        |
| --------| ----------------------------| -----------------------------------|
| GET     | /stub/admin/mappings        | retrieve all mapping               |
| POST    | /stub/admin/mappings        | create new mapping                 |
| GET     | /stub/admin/requests        | retrieve all request/responses     |
| DELETE  | /stub/admin/requests        | delete stored requests/responses   |
| DELETE  | /stub/admin/mappings/{UUID} | delete specific request             |
| POST    | /stub/admin/mappings/reset  | resent mappings                    |

### Defaults
* you can set defaults mappings.
* store your mappings inside resources /container/wiremock/mappings
* store your responses files inside resources /container/wiremock/__files

### Note

* some samples are exists inside /container/wiremock.
