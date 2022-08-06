package org.exam;

import com.oracle.bmc.ClientConfiguration;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimplePrivateKeySupplier;
import com.oracle.bmc.secrets.SecretsClient;
import com.oracle.bmc.secrets.model.Base64SecretBundleContentDetails;
import com.oracle.bmc.secrets.model.SecretBundle;
import com.oracle.bmc.secrets.requests.GetSecretBundleRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.function.Supplier;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, InterruptedException {
        System.out.println( "Hello World!" );
        ConfigFileReader.ConfigFile config = ConfigFileReader.parse("~/.oci/config", "DEFAULT");
        Supplier<InputStream> privateKeySupplier = new SimplePrivateKeySupplier(config.get("key_file"));
        AuthenticationDetailsProvider provider
                = SimpleAuthenticationDetailsProvider.builder()
                .tenantId(config.get("tenancy"))
                .userId(config.get("user"))
                .fingerprint(config.get("fingerprint"))
                .privateKeySupplier((com.google.common.base.Supplier<InputStream>) privateKeySupplier)
                .build();

        ClientConfiguration clientConfig
                = ClientConfiguration.builder()
                .connectionTimeoutMillis(3000)
                .readTimeoutMillis(60000)
                .build();
//        ObjectStorage client = new ObjectStorageClient(provider);
//        client.setRegion(Region.AP_HYDERABAD_1);
//        GetBucketResponse response = client.getBucket(
//                GetBucketRequest.builder().namespaceName("axqhmrmiebxj").bucketName("myBucket").build());
//        String requestId = response.getOpcRequestId();
//        Bucket bucket = response.getBucket();
//        System.out.println(requestId);
//        System.out.println(bucket.getName());

        SecretsClient secretsClient = new SecretsClient(provider);
        secretsClient.setRegion(Region.AP_HYDERABAD_1);
        GetSecretBundleRequest getSecretBundleRequest = GetSecretBundleRequest.builder()
                .secretId("ocid1.vaultsecret.oc1.ap-hyderabad-1.amaaaaaacnbrdcaackcfnatnyjrgdzxgo2n6erirslardcosd2pfov42zpxq")
                .stage(GetSecretBundleRequest.Stage.Latest).build();
        System.out.println("Invoking getSecretBundleRequest: {}  \n "+ getSecretBundleRequest.toString());
        SecretBundle secretBundle = secretsClient.getSecretBundle(getSecretBundleRequest).getSecretBundle();
        System.out.println("Invoking secretBundle: {}  \n "+ secretBundle.toString());
        String secretContent = ((Base64SecretBundleContentDetails) secretBundle.getSecretBundleContent()).getContent();
        String secret =new String(Base64.getDecoder().decode(secretContent));
        System.out.println("Invoking getSecretBundle: {}  \n "+ secret);

    }
}
