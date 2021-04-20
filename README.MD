## Domain Info Application

### How to build the app:

Execute the following command:
<pre><code>./gradlew build
</code></pre>

### How to start the app:

<i>Important note: application.properties file is encrypted with Jasypt</i>

#### Option 1:

1. Set JASYPT_ENCRYPTOR_PASSWORD=[password] environmental variable
2. Run <code>java -jar domaintask-0.0.1-SNAPSHOT.jar</code>

#### Option 2:

<pre><code>./gradlew bootRun --args='--jasypt.encryptor.password=[password]'</code></pre>

### How to encrypt/decrypt with Jasypt

* [How to encrypt properties with Jasypt](https://stackoverflow.com/a/37424296)
* [Encrypt/decrypt properties online](https://www.devglan.com/online-tools/jasypt-online-encryption-decryption)

### Application description

The application starts on http://localhost:8080. The UI is hosted there with simple form that requires user to input the
domain. The app uses whoisxmlapi for getting domain registrar name, expire date. The app requests domain prices from
Namecheap XML API. The request is cached.

### API description

The app's api is available at /api/getDomainInfo domainName is a mandatory parameter

#### Get exisitng domain info:

Sample request:
curl --location --request GET 'localhost:8080/api/getDomainInfo?domainName=google.com'</code></pre>
Sample response:
<pre><code>{
    "registrarName": "MarkMonitor, Inc.",
    "expiresDate": "2028-09-14"
}</code></pre>

#### Get domain price

In case the domain from the input does not exist the app requests domain price from external service

Sample request:
<pre><code>curl --location --request GET 'localhost:8080/api/getDomainInfo?domainName=runner.accountant'</code></pre>

Sample response:
<pre><code>{"price":24.88,"currency":"USD"}</code></pre>

### Possible Improvements:

* Get Client IP dynamically
* Improve exception handling
* Replace in-memory cache
* Improve content validation
