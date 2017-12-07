package com.stublessapi.learning;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.apigateway.AmazonApiGateway;
import com.amazonaws.services.apigateway.AmazonApiGatewayClientBuilder;
import com.amazonaws.services.apigateway.model.CreateResourceRequest;
import com.amazonaws.services.apigateway.model.CreateResourceResult;
import com.amazonaws.services.apigateway.model.CreateRestApiRequest;
import com.amazonaws.services.apigateway.model.CreateRestApiResult;
import com.amazonaws.services.apigateway.model.DeleteRestApiRequest;
import com.amazonaws.services.apigateway.model.GetResourcesRequest;
import com.amazonaws.services.apigateway.model.GetRestApisRequest;
import com.amazonaws.services.apigateway.model.PutMethodRequest;
import com.amazonaws.services.apigateway.model.PutMethodResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@Ignore
public class AmazonApiBuildShould {

  private AmazonApiGateway client = AmazonApiGatewayClientBuilder.standard()
//    .withCredentials(new SystemPropertiesCredentialsProvider())
    .withCredentials(new ProfileCredentialsProvider("jsalinas"))
    .withRegion(Regions.EU_WEST_1).build();

  private CreateRestApiResult restApi;

  @Before
  public void initialise() {
    String randomAPI = UUID.randomUUID().toString().substring(0, 8);
    restApi = client.createRestApi(new CreateRestApiRequest().withName("t-restTest-" + randomAPI));
  }

  @After
  public void clean() {
    client.getRestApis(new GetRestApisRequest())
      .getItems().
      forEach(api -> client.deleteRestApi(new DeleteRestApiRequest().withRestApiId(api.getId())));
  }

  @Test
  public void createResourceFromRootParent() {
    String rootResourceId = getRootResourceId();

    CreateResourceResult stub = client.createResource(new CreateResourceRequest()
      .withParentId(rootResourceId)
      .withPathPart("stub")
      .withRestApiId(restApi.getId())
    );

    assertThat(stub.getPath()).isEqualTo("/stub");
  }

  @Test
  public void createMethodForAResource() {
    CreateResourceResult stub = client.createResource(new CreateResourceRequest()
      .withParentId(getRootResourceId())
      .withPathPart("stub")
      .withRestApiId(restApi.getId())
    );

    PutMethodResult putMethodResult = client.putMethod(new PutMethodRequest()
      .withResourceId(stub.getId())
      .withHttpMethod("GET")
      .withAuthorizationType("NONE")
      .withRestApiId(restApi.getId()));

    System.out.println(putMethodResult);
  }

  private String getRootResourceId() {
    return client.getResources(new GetResourcesRequest().withRestApiId(restApi.getId())).getItems().get(0).getId();
  }
}
