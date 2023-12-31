/**
 * Copyright 2010-2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * This file is licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License. A copy of
 * the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 */

// snippet-comment:[These are tags for the AWS doc team's sample catalog. Do not remove.]
// snippet-sourcedescription:[FindRunningInstances.java demonstrates how to use a Filter to find running instances]
// snippet-service:[ec2]
// snippet-keyword:[Java]
// snippet-keyword:[Code Sample]
// snippet-sourcetype:[full-example]
// snippet-sourcedate:[2020-01-10]
// snippet-sourceauthor:[AWS-scmacdon]

// snippet-start:[ec2.java1.running_instances.complete]
package ec2lister;

// snippet-start:[ec2.java1.running_instances.import]
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.SdkClientException;

import org.joda.time.LocalTime;
import java.util.List;

// snippet-end:[ec2.java1.running_instances.import]

public class FindRunningInstances {

  public static void wait(int ms)
  {
      try
      {
          Thread.sleep(ms);
      }
      catch(InterruptedException ex)
      {
          Thread.currentThread().interrupt();
      }
  } 
  
    public static void main(String[] args) {
        LocalTime currentTime = new LocalTime();
        System.out.println("The current local time is: " + currentTime + "\n");
        Greeter greeter = new Greeter();
        System.out.println(greeter.sayHello());   
        // snippet-start:[ec2.java1.running_instances.main]
        AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
        // https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/java-dg-region-selection.html
        // AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

        try {
            //Create the Filter to use to find running instances
            Filter filter = new Filter("instance-state-name");
            filter.withValues("running");

            //Create a DescribeInstancesRequest
            DescribeInstancesRequest request = new DescribeInstancesRequest();
            request.withFilters(filter);

            // Find the running instances
            DescribeInstancesResult response = ec2.describeInstances(request);

            for (Reservation reservation : response.getReservations()){

                for (Instance instance : reservation.getInstances()) {

                    //Print out the results
                    System.out.printf(
                            "Found reservation with id %s, " +
                                    "AMI %s, " +
                                    "type %s, " +
                                    "state %s " +
                                    "and monitoring state %s" +
                                    "\n",
                            instance.getInstanceId(),
                            instance.getImageId(),
                            instance.getInstanceType(),
                            instance.getState().getName(),
                            instance.getMonitoring().getState());
                }
            }
            System.out.print("Done" + "\n");
            
            System.out.println("Going to sleep for a while..." + "\n");
            wait(100000);            

        } catch (SdkClientException e) {
            e.getStackTrace();
        }
        // snippet-end:[ec2.java1.running_instances.main]
    }
}
// snippet-end:[ec2.java1.running_instances.complete]