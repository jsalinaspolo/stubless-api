package com.stublessapi.learning;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.apigateway.AmazonApiGateway;
import com.amazonaws.services.apigateway.AmazonApiGatewayClientBuilder;
import com.amazonaws.services.apigateway.model.AuthorizerType;
import com.amazonaws.services.apigateway.model.CreateApiKeyRequest;
import com.amazonaws.services.apigateway.model.CreateApiKeyResult;
import com.amazonaws.services.apigateway.model.CreateModelRequest;
import com.amazonaws.services.apigateway.model.CreateResourceRequest;
import com.amazonaws.services.apigateway.model.CreateResourceResult;
import com.amazonaws.services.apigateway.model.CreateRestApiRequest;
import com.amazonaws.services.apigateway.model.CreateRestApiResult;
import com.amazonaws.services.apigateway.model.DeleteRestApiRequest;
import com.amazonaws.services.apigateway.model.GatewayResponseType;
import com.amazonaws.services.apigateway.model.GetMethodRequest;
import com.amazonaws.services.apigateway.model.GetResourceRequest;
import com.amazonaws.services.apigateway.model.GetResourcesRequest;
import com.amazonaws.services.apigateway.model.GetResourcesResult;
import com.amazonaws.services.apigateway.model.PutGatewayResponseRequest;
import com.amazonaws.services.apigateway.model.PutIntegrationRequest;
import com.amazonaws.services.apigateway.model.PutIntegrationResponseRequest;
import com.amazonaws.services.apigateway.model.PutMethodRequest;
import com.amazonaws.services.apigateway.model.PutMethodResponseRequest;
import org.apache.http.entity.ContentType;

import java.util.UUID;

public class AmazonApiBuild {


  public static void main(String[] args) {

    AmazonApiGateway client = AmazonApiGatewayClientBuilder.standard()
      .withCredentials(new SystemPropertiesCredentialsProvider())
      .withRegion(Regions.EU_WEST_1).build();

    String randomAPI = UUID.randomUUID().toString().substring(0, 8);
    CreateRestApiResult restApi = client.createRestApi(new CreateRestApiRequest().withName("restTest-" + randomAPI));

    GetResourcesResult resources = client.getResources(new GetResourcesRequest().withRestApiId(restApi.getId()));

    CreateResourceResult stub = client.createResource(new CreateResourceRequest()
      .withParentId(resources.getItems().get(0).getId())
      .withPathPart("stub")
      .withRestApiId(restApi.getId())

    );

    client.putMethod(new PutMethodRequest()
      .withResourceId(stub.getId())
      .withHttpMethod("GET")
      .withAuthorizationType("NONE")
      .withRestApiId(restApi.getId()));


//    client.putMethodResponse(new PutMethodResponseRequest()
//      .withStatusCode("200")
//      .withRestApiId(restApi.getId())
//      .withHttpMethod("GET")
//      .withResourceId(stub.getId())
//      .withResponseModels()
//    );

    client.putGatewayResponse(new PutGatewayResponseRequest()
      .withResponseType(GatewayResponseType.UNAUTHORIZED)
      .withStatusCode("200")
      .withRestApiId(restApi.getId())
    );
//    client.putIntegrationResponse(new PutIntegrationResponseRequest()
//      .withRestApiId(restApi.getId()));

//    client.deleteRestApi(new DeleteRestApiRequest().withRestApiId(restApi.getId()));
    System.out.println("hi");
  }
}
