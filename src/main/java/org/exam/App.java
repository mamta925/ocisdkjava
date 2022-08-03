package org.exam;


import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.identity.Identity;
import com.oracle.bmc.identity.IdentityClient;
import com.oracle.bmc.identity.model.Compartment;
import com.oracle.bmc.identity.requests.ListCompartmentsRequest;
import com.oracle.bmc.identity.responses.ListCompartmentsResponse;
import shared.ExampleCompartmentHelper;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, InterruptedException {
        System.out.println( "Hello World!" );
        String configurationFilePath = "~/.oci/config";
        String profile = "DEFAULT";
        final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parseDefault();

        final AuthenticationDetailsProvider provider =
                new ConfigFileAuthenticationDetailsProvider(configFile);

        String compartmentId = provider.getTenantId();
        final String tenantId = provider.getTenantId();
        Identity identityClient = new IdentityClient(provider);
        identityClient.setRegion(Region.AP_HYDERABAD_1);
        Compartment cp1 = null;
        Compartment cp2 = null;
        Compartment cp3 = null;
        Compartment cp21 = null;
        Compartment cp31 = null;
        Compartment cp211 = null;
        String nextPageToken = null;
        // Setup the first level compartments (CP-1, CP-2, CP-3)
        cp1 = ExampleCompartmentHelper.createCompartment(identityClient, tenantId, "CP-1");
        cp2 = ExampleCompartmentHelper.createCompartment(identityClient, tenantId, "CP-2");
        cp3 = ExampleCompartmentHelper.createCompartment(identityClient, tenantId, "CP-3");

        // If we create/update and then try to use compartments straight away, sometimes we can get a 404. To try and avoid this, the script
        // adds a short delay between the compartment management operations
        Thread.sleep(10000);

        // Setup the second level compartments (CP-21, CP-31)
        cp21 = ExampleCompartmentHelper.createCompartment(identityClient, cp2.getId(), "CP-21");
        cp31 = ExampleCompartmentHelper.createCompartment(identityClient, cp3.getId(), "CP-31");

        Thread.sleep(10000);

        // Setup the third level compartments (CP-211)
        cp211 =
                ExampleCompartmentHelper.createCompartment(
                        identityClient, cp2.getId(), "CP-211");

        // List all compartments within tenancy with Accessible compartment filter

        System.out.println(
                "ListCompartments: with compartmentIdInSubtree == true and AccessLevel==Accessible");
        do {
            ListCompartmentsResponse response =
                    identityClient.listCompartments(
                            ListCompartmentsRequest.builder()
                                    .limit(3)
                                    .compartmentId(compartmentId)
                                    .accessLevel(ListCompartmentsRequest.AccessLevel.Accessible)
                                    .compartmentIdInSubtree(Boolean.TRUE)
                                    .page(nextPageToken)
                                    .build());

            for (Compartment compartment : response.getItems()) {
                System.out.println(compartment);
            }
            nextPageToken = response.getOpcNextPage();
        } while (nextPageToken != null);
    }
}
