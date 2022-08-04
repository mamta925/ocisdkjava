package org.exam;


import com.oracle.bmc.ClientConfiguration;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimplePrivateKeySupplier;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.model.Bucket;
import com.oracle.bmc.objectstorage.requests.GetBucketRequest;
import com.oracle.bmc.objectstorage.responses.GetBucketResponse;

import java.io.IOException;
import java.io.InputStream;
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
        ObjectStorage client = new ObjectStorageClient(provider);
        client.setRegion(Region.AP_HYDERABAD_1);
        GetBucketResponse response = client.getBucket(
                GetBucketRequest.builder().namespaceName("axqhmrmiebxj").bucketName("myBucket").build());
        String requestId = response.getOpcRequestId();
        Bucket bucket = response.getBucket();
        System.out.println(requestId);
        System.out.println(bucket.getName());

//        final SecretsClient secretsClient = SecretsClient.builder()
//                .requestSignerFactory(NoOpRequestSignerFactory.getRequestSignerFactory())
//                .build(provider);
//        GetSecretBundleByNameRequest getSecretBundleByNameRequest = GetSecretBundleByNameRequest.builder()
//                .secretName("SecretKey")
//                .vaultId("ocid1.vault.oc1.ap-hyderabad-1.dzrow3hcaagw6.abuhsljrttcarmotzatyg3jq4q3crvosi4vh4uw4s32eael77cdkff5w5tza")
//                .build();
//        GetSecretBundleByNameResponse secretBundleByName = secretsClient.getSecretBundleByName(
//                getSecretBundleByNameRequest);
//        System.out.println("Invoking getSecretBundle: {}"+ getSecretBundleByNameRequest);
//        System.out.println("Invoking getSecretBundle: {}"+secretBundleByName.getSecretBundle());

    }
}
