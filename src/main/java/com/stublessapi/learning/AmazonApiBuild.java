package com.stublessapi.learning;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.apigateway.AmazonApiGateway;
import com.amazonaws.services.apigateway.AmazonApiGatewayClientBuilder;
import com.amazonaws.services.apigateway.model.CreateApiKeyRequest;
import com.amazonaws.services.apigateway.model.CreateApiKeyResult;
import com.amazonaws.services.apigateway.model.CreateModelRequest;
import com.amazonaws.services.apigateway.model.CreateResourceRequest;
import com.amazonaws.services.apigateway.model.CreateRestApiRequest;
import com.amazonaws.services.apigateway.model.CreateRestApiResult;
import org.apache.http.entity.ContentType;

public class AmazonApiBuild {


    public static void main(String[] args) {

        AmazonApiGateway client = AmazonApiGatewayClientBuilder.standard().withCredentials(new AWSCredentialsProvider() {
            @Override
            public AWSCredentials getCredentials() {
                return new AWSCredentials() {
                    @Override
                    public String getAWSAccessKeyId() {
                        return "key";
                    }

                    @Override
                    public String getAWSSecretKey() {
                        return "secret";
                    }
                };
            }

            @Override
            public void refresh() {

            }
        }).withRegion(Regions.EU_WEST_1).build();

//        CreateApiKeyResult apiKey = client.createApiKey(new CreateApiKeyRequest().withName("test")
//                .withValue("12345678901234567890"));
        CreateRestApiResult restApi = client.createRestApi(new CreateRestApiRequest().withName("restTest"));
        client.createResource(new CreateResourceRequest()
                .withParentId("/some")
                .withPathPart("test-endpoint")
                .withRestApiId(restApi.getId())
        );

        System.out.println("hi");
    }
}
