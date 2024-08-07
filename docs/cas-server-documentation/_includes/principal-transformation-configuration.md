<!-- fragment:keep -->

<p/>

Authentication handlers that generally deal with username-password credentials can be configured to 
transform the user id prior to executing the authentication sequence. Each authentication 
strategy in CAS provides settings to properly transform the principal. Refer to the 
relevant settings for the authentication strategy at hand to learn more.

Authentication handlers as part of principal transformation may also be provided a path to a 
Groovy script to transform the provided username. The outline of the script may take on the following form:

```groovy
String run(final Object... args) {
    def (providedUsername,logger) = args
    return providedUsername.concat("SomethingElse")
}
```

To prepare CAS to support and integrate with Apache Groovy, please [review this guide]({{ baseUrl }}/integration/Apache-Groovy-Scripting.html).
